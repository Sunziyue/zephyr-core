package com.jjy.ip.hiboos.order.server.component;

import cn.hutool.core.math.Money;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jjy.ip.hiboos.order.api.enums.OrderEnum;
import com.jjy.ip.hiboos.order.api.model.Body;
import com.jjy.ip.hiboos.order.api.model.Product;
import com.jjy.ip.hiboos.order.api.utils.date.DateUtils;
import com.jjy.ip.hiboos.order.api.utils.sale.SaleUtils;
import com.jjy.ip.hiboos.order.server.dao.PosPriceDao;
import com.jjy.ip.hiboos.order.server.model.*;
import io.jjy.platform.common.datasource.DynamicDataSourceContext;
import io.terminus.common.utils.Arguments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CreateSubmitOrderHdtComponent {

    @Value("${prom.ACTIVITYCODE}")
    private String activitycode;
    @Value("${prom.COUPONTYPE}")
    private String coupontype;
    @Value("${prom.COUPONID}")
    private String couponid;
    @Value("${prom.COUPONPPNO}")
    private String couponppno;

    private final SendHdtComponent sendHdtComponent;
    private final PosPriceDao posPriceDao;
    private final DynamicDataSourceContext dynamicDataSourceContext;

    @Autowired
    public CreateSubmitOrderHdtComponent(PosPriceDao posPriceDao,
                                         SendHdtComponent sendHdtComponent,
                                         DynamicDataSourceContext dynamicDataSourceContext) {
        this.sendHdtComponent = sendHdtComponent;
        this.posPriceDao = posPriceDao;
        this.dynamicDataSourceContext = dynamicDataSourceContext;
    }

    //          商品编码　价格信息
    private Map<String, PosPrice> posPriceMap = Maps.newHashMap();

    /**
     * 处理销售单缓存 生成HDT 发送 DRP
     *
     * @param cacheOrderList 订单缓存列表
     * @param subnetId       数据库节点
     */
    public void create(List<CacheOrder> cacheOrderList, String subnetId) {
        if (Arguments.notEmpty(cacheOrderList) && Arguments.notEmpty(subnetId)) {
            try {
                this.init(cacheOrderList, subnetId);
            } catch (Exception e) {
                // init() 失败之后不允许继续处理当前包了, 因为DRP库连不上了.
                log.error("subnetId : [{}], ERROR : \n{}", subnetId, Throwables.getStackTraceAsString(e));
                // 直接操作cacheOrderList, 改变的就是外层ProcessOrderService::processJson传过来的cacheOrderList;
                // 清除所有订单缓存, 系统失败不是订单的错, 不改变当前订单的状态. 这里clear()是因为外层会全量更新, 清空了就不会更新了
                cacheOrderList.clear();
            }

            List<TBpSalepos> tBpSalePosList = Lists.newArrayList();

            // 直接操作cacheOrderList, 改变的就是外层ProcessOrderService::processJson传过来的cacheOrderList;
            cacheOrderList.forEach(cache -> {
                // 这里只处理正常生成过 jjy_bill_id 的 销售单
                if (Arguments.notEmpty(cache.getJjyBillId()) && OrderEnum.SUBMIT_ORDER.statue().equals(cache.getOrderType())) {
                    try {
                        TBpSalepos tBpSalepos = this.createPackage(cache);
                        tBpSalePosList.add(tBpSalepos);
                        cache.OK();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        cache.NG(e.getMessage());
                    }
                }
            });
            // 处理好的 HDT 传入 DRP中间表;
            // 这里判断是因为上面 init() 外层的 catch 会清空 cacheOrderList 那么 tBpSaleposList 就不会值
            if (Arguments.notEmpty(tBpSalePosList)) {
                try {
                    this.sendHdtComponent.send(tBpSalePosList, cacheOrderList, subnetId);
                } catch (Exception e) {
                    log.error("subnetId : [{}], ERROR : \n{}", subnetId, Throwables.getStackTraceAsString(e));
                    // 直接操作cacheOrderList, 改变的就是外层ProcessOrderService::processJson传过来的cacheOrderList;
                    // 清除所有订单缓存, 系统失败不是订单的错, 不改变当前订单的状态. 这里clear()是因为外层会全量更新, 清空了就不会更新了
                    cacheOrderList.clear();
                }
            }
        }
    }

    /**
     * 生成 HDT 包
     *
     * @param cache 订单缓存信息
     * @return HDT 包
     * @throws Exception 异常信息，用来传输错误原因
     */
    private TBpSalepos createPackage(CacheOrder cache) throws Exception {

        TBpSalepos tBpSalepos = this.createDAndProm(cache);

        List<TBpSaleposD> tBpSaleposDList = tBpSalepos.getTBpSaleposDList();
        TBpSaleposH tBpSaleposH = this.createH(cache, tBpSaleposDList);
        TBpSaleposT tBpSaleposT = this.createT(cache, tBpSaleposH);

        tBpSalepos.setTBpSaleposT(tBpSaleposT);
        tBpSalepos.setTBpSaleposH(tBpSaleposH);

        BigDecimal h = tBpSaleposH.getValue_afterdisc();
        BigDecimal d = SaleUtils.ZERO_DECIMAL;
        for (TBpSaleposD item : tBpSaleposDList) {
            d = d.add(item.getValue_afterdisc());
        }
        BigDecimal t = tBpSaleposT.getValue_pay();
        //hdt三张表账不平
        if (h.compareTo(d) != 0 || h.compareTo(t) != 0) {
            String str = "h:[" + h.toString() + "], d:[" + d.toString() + "], t:[" + t.toString() + "]";
            throw new Exception("HDT三张表账不平,账单信息: " + str);
        }

        return tBpSalepos;
    }

    /**
     * 生成 H 表
     *
     * @param cache    订单缓存信息
     * @param dataList D 表
     * @return H 表
     */
    private TBpSaleposH createH(CacheOrder cache, List<TBpSaleposD> dataList) throws Exception {
        BigDecimal value = SaleUtils.ZERO_DECIMAL;
        BigDecimal valueAfterDisc = SaleUtils.ZERO_DECIMAL;
        BigDecimal valueDisc = SaleUtils.ZERO_DECIMAL;

        for (TBpSaleposD data : dataList) {
            value = value.add(data.getValue());
            valueAfterDisc = valueAfterDisc.add(data.getValue_afterdisc());
            valueDisc = valueDisc.add(data.getValue_disc());
        }

        TBpSaleposH header = new TBpSaleposH();
        header.setDrpid(cache.getDrpId());
        header.setMktid(cache.getMktId());
        header.setYmd(DateUtils.saleYmd());
        header.setBill(cache.getJjyBillId());
        header.setPosid(cache.getPosId());
        header.setCash_by(cache.getCash() + cache.getMktId());
        header.setTime_sale(cache.getCreateTime());
        header.setNo_vip(SaleUtils.EMPTY_STR);
        header.setDegree(SaleUtils.ZERO);
        header.setValue(value);
        header.setValue_afterdisc(valueAfterDisc);
        header.setValue_disc(valueDisc);
        header.setValue_rtn(SaleUtils.ZERO_DECIMAL);
        header.setValue_rtnafterdisc(SaleUtils.ZERO_DECIMAL);
        header.setValue_rtndisc(SaleUtils.ZERO_DECIMAL);
        header.setTime_send(DateUtils.date());
        header.setFlag_count(SaleUtils.ZERO_STR);
        header.setFlag_bk(SaleUtils.ZERO_STR);
        header.setBill_old(SaleUtils.EMPTY_STR);
        header.setBill_tax(SaleUtils.EMPTY_STR);
        header.setStampbill(SaleUtils.EMPTY_STR);
        header.setStampqty(SaleUtils.ZERO);
        header.setStamptype(SaleUtils.SHORT_ZERO);
        header.setFlag_taxbill(SaleUtils.EMPTY_STR);
        header.setEinvoiceCode(SaleUtils.createEinvoiceCode(cache.getTransactionId()));
        header.setBiz_order_id(cache.getTransactionId());
        header.setOrder_channel(cache.getChannel());
        header.setTb_biz_order_id(SaleUtils.EMPTY_STR);
        return header;
    }

    /**
     * 生成 D 表
     *
     * @param cache 订单缓存信息
     * @return D 表
     * @throws Exception 异常信息，用来传输错误原因
     */
    private TBpSalepos createDAndProm(CacheOrder cache) throws Exception {
        TBpSalepos tBpSalepos = new TBpSalepos();
        List<TBpSaleposD> tBpSaleposDList = Lists.newArrayList();
        List<TBpSaleposProm> tBpSaleposPromList = Lists.newArrayList();
        // 获取正向订单对象
        Body body = JSON.parseObject(cache.getJsonCache(), Body.class);
        List<Product> productList = body.getProductList();
        int id = 0;
        int row = 0;
        for (Product product : productList) {
            id++;
            // 找到当前商品的价格信息, init() 中有查询过.
            PosPrice posPrice = this.posPriceMap.get(cache.getMktId() + product.getOutSkuId());
            if (Arguments.isNull(posPrice)) {
                throw new Exception("订单号: [ " + cache.getTransactionId() +
                        " ] -> 商品: [ " + product.getOutSkuId() + " ] PosPrice表中无此商品信息");
            }
            // 算钱神器
            Money money = new Money();
            TBpSaleposD data = new TBpSaleposD();
            data.setDrpid(cache.getDrpId());
            data.setMktid(cache.getMktId());
            data.setYmd(DateUtils.saleYmd());
            data.setBill(cache.getJjyBillId());
            data.setId(id);
            data.setBill_dept(posPrice.getBill_dept() == null ? SaleUtils.EMPTY_STR : posPrice.getBill_dept());
            data.setSale_by(SaleUtils.EMPTY_STR);
            data.setCountid(posPrice.getCountid());
            data.setDeptid(posPrice.getDeptid());
            data.setShopid(product.getOutSkuId());
            data.setStandards(posPrice.getStandards());
            data.setUnit_sale(posPrice.getUnit_sale());
            data.setFlag_wp(posPrice.getFlag_wp());
            data.setTxm(posPrice.getTxm());
            data.setName_short(posPrice.getName_short());
            data.setNewshop(posPrice.getNewshop());
            BigDecimal num_sale = BigDecimal.valueOf(product.getSkuCount());
            data.setNum_sale(num_sale);
            BigDecimal price_sale = posPrice.getPrice_sale();
            data.setPrice_sale(price_sale);
            data.setPrice_must(posPrice.getPrice_must());
            data.setPrice_vip(posPrice.getPrice_vip());
            // 修改后价格 payValue/skuCount  注意hdt表金额单位为元，海博金额为分
            BigDecimal payValue = BigDecimal.valueOf(product.getPayValue());
            BigDecimal skuCount = BigDecimal.valueOf(product.getSkuCount());
            BigDecimal price_salechg = payValue.divide(skuCount, 4, RoundingMode.UP);
            money.setCent(price_salechg.toBigInteger().longValue());
            data.setPrice_salechg(money.getAmount());
            data.setDisc(SaleUtils.ONE_DECIMAL);
            // 销售总额  price_sale*num_sale
            data.setValue(price_sale.multiply(num_sale).setScale(2, RoundingMode.UP));
            // 折后销售额 payValue   注意hdt表金额单位为元，海博金额为分
            money.setCent(product.getPayValue() + product.getDiscountValue());
            data.setValue_afterdisc(money.getAmount());
            // 分摊销售折让 merchantSingleProductSubsidy + bussinessSubsidy  注意hdt表金额单位为元，海博金额为分
            // money.setCent(product.getMerchantSingleProductSubsidy() + product.getBussinessSubsidy());
            // data.setValue_disc(money.getAmount());
            data.setValue_disc(SaleUtils.ZERO_DECIMAL);
            data.setCanrtn(posPrice.getCanrtn());
            data.setIsfree(SaleUtils.ONE);
            data.setIsrtn(SaleUtils.ONE);
            data.setPromotion(posPrice.getPromotion());
            data.setBill_old(SaleUtils.EMPTY_STR);
            data.setUnit_piece(SaleUtils.EMPTY_STR);
            data.setPiece(SaleUtils.ZERO);
            data.setFlag_split(SaleUtils.ZERO_STR);
            data.setFlag_count(SaleUtils.ZERO_STR);
            data.setFlag_pack(SaleUtils.ZERO_STR);
            data.setNo_pack(SaleUtils.EMPTY_STR);
            data.setNum_pack(SaleUtils.ONE);
            data.setPro_class(SaleUtils.ZERO_STR);
            data.setStampqty(SaleUtils.ZERO);
            data.setPrice_com(posPrice.getPrice_com());
            data.setStampexcept(SaleUtils.SHORT_ZERO);
            data.setCoupon_bill(SaleUtils.EMPTY_STR);
            data.setCoupon_value(SaleUtils.ZERO_DECIMAL);
            data.setCoupon_value_disc(SaleUtils.ZERO_DECIMAL);
            data.setCoupon_value_use(SaleUtils.ZERO_DECIMAL);
            data.setScancode(SaleUtils.EMPTY_STR);
            // 添加到结果List
            tBpSaleposDList.add(data);
            // 优惠券
            if (product.getDiscountValue() > 0) {
                row++;
                TBpSaleposProm prom = new TBpSaleposProm();
                prom.setDrpid(cache.getDrpId());
                prom.setMktid(cache.getMktId());
                prom.setYmd(DateUtils.saleYmd());
                prom.setBill(cache.getJjyBillId());
                prom.setRowno(row);
                prom.setId(id);
                prom.setShopid(product.getOutSkuId());
                prom.setProm_type("plat_coupon");
                prom.setActivitycode(activitycode); // 取配置文件中的值
                prom.setCoupontype(coupontype); // 取配置文件中的值
                prom.setCouponid(couponid); // 取配置文件中的值
                prom.setCouponppno(couponppno); // 取配置文件中的值
                money.setCent(product.getDiscountValue());
                prom.setDiscount(money.getAmount());
                money.setCent(product.getMerchantSingleProductSubsidy());
                prom.setMt_rate(money.getAmount());
                money.setCent(product.getPlatformSingleProductSubsidy());
                prom.setShop_rate(money.getAmount());
                prom.setCouponclass(SaleUtils.ZERO_STR);
                prom.setAgent_rate(SaleUtils.ZERO_DECIMAL);
                prom.setLogistics_rate(SaleUtils.ZERO_DECIMAL);
                prom.setFlag(SaleUtils.ZERO_STR);
                tBpSaleposPromList.add(prom);
            }
        }
        tBpSalepos.setTBpSaleposDList(tBpSaleposDList);
        tBpSalepos.setTBpSaleposPromList(tBpSaleposPromList);
        return tBpSalepos;
    }

    /**
     * 生成 T 表
     *
     * @param cache       订单缓存信息
     * @param tBpSaleposH H 表
     * @return 生成 T 表
     */
    private TBpSaleposT createT(CacheOrder cache, TBpSaleposH tBpSaleposH) {
        // 获取正向订单对象
        TBpSaleposT tBpSaleposT = new TBpSaleposT();
        tBpSaleposT.setDrpid(cache.getDrpId());
        tBpSaleposT.setMktid(cache.getMktId());
        tBpSaleposT.setYmd(DateUtils.saleYmd());
        tBpSaleposT.setBill(cache.getJjyBillId());
        tBpSaleposT.setId(SaleUtils.ONE);
        tBpSaleposT.setPayid(cache.getPayId());
        tBpSaleposT.setCoinid(SaleUtils.ONE_STR);
        tBpSaleposT.setNo_card(cache.getPayId());
        tBpSaleposT.setNo_bank(SaleUtils.EMPTY_STR);
        tBpSaleposT.setNum_pay(tBpSaleposH.getValue_afterdisc());
        tBpSaleposT.setRate_bk(SaleUtils.ONE_DECIMAL);
        tBpSaleposT.setValue_pay(tBpSaleposH.getValue_afterdisc());
        tBpSaleposT.setTrade_time(cache.getCreateTime());
        tBpSaleposT.setDealerno(SaleUtils.EMPTY_STR);
        tBpSaleposT.setCoupon_bill(SaleUtils.EMPTY_STR);
        tBpSaleposT.setCpid(SaleUtils.EMPTY_STR);
        tBpSaleposT.setTermino(SaleUtils.EMPTY_STR);
        tBpSaleposT.setPayid_port(SaleUtils.EMPTY_STR);
        tBpSaleposT.setParentcode(cache.getParentCode());
        tBpSaleposT.setBankcode(SaleUtils.EMPTY_STR);
        tBpSaleposT.setId_old(SaleUtils.ZERO);
        tBpSaleposT.setPayid_old(SaleUtils.EMPTY_STR);
        tBpSaleposT.setOnline_return(SaleUtils.EMPTY_STR);
        return tBpSaleposT;
    }

    /**
     * 信息初始化
     *
     * @param cacheOrderList 订单信息
     * @param subnetId       数据库节点
     * @throws Exception 异常信息，用来传输错误原因
     */
    public void init(List<CacheOrder> cacheOrderList, String subnetId) throws Exception {
        if (Arguments.notEmpty(cacheOrderList)) {
            List<Product> productList = Lists.newArrayList();
            for (CacheOrder cache : cacheOrderList) {
                if (Arguments.notEmpty(cache.getJjyBillId()) && OrderEnum.SUBMIT_ORDER.statue().equals(cache.getOrderType())) {
                    Body body = JSON.parseObject(cache.getJsonCache(), Body.class);
                    List<Product> products = body.getProductList().stream().peek(product -> {
                        product.setDrpid(cache.getDrpId());
                        product.setMktid(cache.getMktId());
                    }).collect(Collectors.toList());
                    productList.addAll(products);
                }
            }
            // 如果本次分片都是退款单（上级 ProcessOrderService::processJson 传过来的 cacheOrderList 中 无销售单）
            // this.posPriceDao.findByShopIds(criteria); 会报错,因为 shopIdList 为空,生成SQL时 会出错
            if (Arguments.notEmpty(productList)) {
                try {
                    this.dynamicDataSourceContext.setDataSource(subnetId);
                    Map<String, Object> criteria = Maps.newHashMap();
                    List<List<Product>> lists = Lists.partition(productList, 500);
                    for (List<Product> list : lists) {
                        criteria.put("productList", list);
                        this.posPriceMap.putAll(this.posPriceDao.findByOuterProduct(criteria));
                    }
                } catch (Exception e) {
                    throw new Exception("subnetId: [" + subnetId + "]销售单::init::PosPriceMap创建异常, DRP连接失败", e);
                } finally {
                    this.dynamicDataSourceContext.clear();
                }
            }
        }
    }
}
