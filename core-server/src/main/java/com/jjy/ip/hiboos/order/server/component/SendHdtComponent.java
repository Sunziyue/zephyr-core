package com.jjy.ip.hiboos.order.server.component;

import com.jjy.ip.hiboos.order.server.dao.TBpSalePosDDao;
import com.jjy.ip.hiboos.order.server.dao.TBpSalePosHDao;
import com.jjy.ip.hiboos.order.server.dao.TBpSalePosPromDao;
import com.jjy.ip.hiboos.order.server.dao.TBpSalePosTDao;
import com.jjy.ip.hiboos.order.server.model.CacheOrder;
import com.jjy.ip.hiboos.order.server.model.TBpSalepos;
import com.jjy.ip.hiboos.order.server.model.TBpSaleposD;
import com.jjy.ip.hiboos.order.server.model.TBpSaleposProm;
import io.jjy.platform.common.datasource.DynamicDataSourceContext;
import io.terminus.common.utils.Arguments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

/**
 * @author Sunziyue
 * @date 2020/09/18 16:16
 * @apiNote 发送订单到DRP中间表
 */
@Slf4j
@Component
public class SendHdtComponent {

    private final TBpSalePosHDao tBpSalePosHDao;
    private final TBpSalePosDDao tBpSalePosDDao;
    private final TBpSalePosTDao tBpSalePosTDao;
    private final TBpSalePosPromDao tBpSalePosPromDao;
    private final DynamicDataSourceContext dynamicDataSourceContext;
    private final ProcedureHdtComponent procedureHdtComponent;
    private final DataSourceTransactionManager txManager;
    private final TransactionDefinition transactionDefinition;

    @Autowired
    public SendHdtComponent(TBpSalePosHDao tBpSalePosHDao,
                            TBpSalePosDDao tBpSalePosDDao,
                            TBpSalePosTDao tBpSalePosTDao,
                            TBpSalePosPromDao tBpSalePosPromDao,
                            DynamicDataSourceContext dynamicDataSourceContext,
                            ProcedureHdtComponent procedureHdtComponent,
                            DataSourceTransactionManager txManager,
                            TransactionDefinition transactionDefinition) {
        this.tBpSalePosHDao = tBpSalePosHDao;
        this.tBpSalePosDDao = tBpSalePosDDao;
        this.tBpSalePosTDao = tBpSalePosTDao;
        this.tBpSalePosPromDao = tBpSalePosPromDao;
        this.dynamicDataSourceContext = dynamicDataSourceContext;
        this.procedureHdtComponent = procedureHdtComponent;
        this.txManager = txManager;
        this.transactionDefinition = transactionDefinition;
    }

    /**
     * 发送订单到DRP中间表
     *
     * @param tBpSalePosList HDT 数据集合
     */
    public void send(List<TBpSalepos> tBpSalePosList, List<CacheOrder> cacheOrderList, String subnetId) throws Exception {
        if (Arguments.notEmpty(tBpSalePosList)) {
            for (TBpSalepos salePos : tBpSalePosList) {
                try {
                    this.dynamicDataSourceContext.setDataSource(subnetId);
                    try {
                        this.save(salePos);
                    } catch (Exception e) {
                        // 发送失败就清除这个订单在这次任务分片生成的bill等等信息, 等待下次重新生成发送
                        String bill = salePos.getTBpSaleposH().getBill();
                        cacheOrderList.stream()
                                .filter(cacheOrder -> bill.equals(cacheOrder.getJjyBillId()))
                                .forEach(cache -> cache.dataNG("HDT发送DRP失败, 等待下次分片重新发送"));
                    }
                } catch (Exception e) {
                    throw new Exception("保存HDT数据到DRP数据库动态数据源切换失败 : " + e.getMessage(), e);
                } finally {
                    this.dynamicDataSourceContext.clear();
                }
            }
            this.procedureHdtComponent.doProcedureHdt(subnetId);
        }
    }

    public void save(TBpSalepos item) throws Exception {
        TransactionStatus txStatus = txManager.getTransaction(transactionDefinition);
        try {
            tBpSalePosHDao.insert(item.getTBpSaleposH());
            for (TBpSaleposD dItem : item.getTBpSaleposDList()) {
                this.tBpSalePosDDao.insert(dItem);
            }
            for (TBpSaleposProm prom : item.getTBpSaleposPromList()) {
                this.tBpSalePosPromDao.insert(prom);
            }
            this.tBpSalePosTDao.insert(item.getTBpSaleposT());
            txManager.commit(txStatus);
        } catch (DuplicateKeyException e) {
            // 主键冲突说明已经入库了(网络原因)
            log.warn("订单bill:[{}], 在DRP流水表中已存在", item.getTBpSaleposH().getBill());
            txManager.rollback(txStatus);
        } catch (Exception e) {
            txManager.rollback(txStatus);
            throw new Exception("bill:[" + item.getTBpSaleposH().getBill() + "] 包持久化失败", e);
        }
    }
}
