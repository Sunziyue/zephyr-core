package com.jjy.ip.hiboos.order.server.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Sunziyue
 */
@Data
public class TBpSaleposD implements Serializable {
    private static final long serialVersionUID = -5765371847127975039L;

    //	营销节点
    private String drpid;

    //	商场编号
    private String mktid;

    //	销售传回
    private Integer ymd;

    //	销售票据
    private String bill;

    //	记录号
    private Integer id;

    //	促销单号
    private String bill_dept;

    //	销售员
    private String sale_by;

    //	柜组编码
    private String countid;

    //	部门编码
    private String deptid;

    //	商品编号
    private String shopid;

    //	商品规格
    private String standards;

    //	单位
    private String unit_sale;

    //	商品计量
    private String flag_wp;

    //	条形码
    private String txm;

    //	商品简称
    private String name_short;

    //	新商品
    private String newshop;

    //	销售量
    private BigDecimal num_sale;

    //	销售价格
    private BigDecimal price_sale;

    //	正常销售价格
    private BigDecimal price_must;

    //	VIP价格
    private BigDecimal price_vip;

    //	修改后价格
    private BigDecimal price_salechg;

    //	销售折扣率
    private BigDecimal disc;

    //	销售总额
    private BigDecimal value;

    //	折后销售额
    private BigDecimal value_afterdisc;

    //	分摊销售折让
    private BigDecimal value_disc;

    //	允许退货
    private String canrtn;

    //	赠送
    private Integer isfree;

    //	退货
    private Integer isrtn;

    //	促销分类
    private String promotion;

    //	原销售票据号
    private String bill_old;

    //	记数单位
    private String unit_piece;

    //	销售个数
    private Integer piece;

    //	拆零销售
    private String flag_split;

    //	实况统计
    private String flag_count;

    //	打包销售
    private String flag_pack;

    //	店长打折/退货原因
    private String no_pack;

    //	份数
    private Integer num_pack;

    //	促销分类
    private String pro_class;

    //	加码派送印花数
    private Integer stampqty;

    //	公司定价
    private BigDecimal price_com;

    //	印花除外标志
    private Short stampexcept;

    //	活动主题
    private String coupon_bill;

    //	主题送券金额
    private BigDecimal coupon_value;

    //	单品送券金额
    private BigDecimal coupon_value_disc;

    //	单品用券金额
    private BigDecimal coupon_value_use;

    //	原始扫描码
    private String scancode;

}