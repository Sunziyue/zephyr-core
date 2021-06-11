package com.jjy.ip.hiboos.order.server.component;

import com.google.common.base.Throwables;
import com.jjy.ip.hiboos.order.server.dao.ProcedureHdtDao;
import io.jjy.platform.common.datasource.DynamicDataSourceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcedureHdtComponent {

    private final DynamicDataSourceContext dynamicDataSourceContext;
    private final ProcedureHdtDao procedureHdtDao;

    @Autowired
    public ProcedureHdtComponent(DynamicDataSourceContext dynamicDataSourceContext,
                                 ProcedureHdtDao procedureHdtDao) {
        this.dynamicDataSourceContext = dynamicDataSourceContext;
        this.procedureHdtDao = procedureHdtDao;
    }

    public void doProcedureHdt(String subnetId) {
        log.info("调度DRP数据库节点[{}]存储过程，从美团接口表向HDT流水表写入数据", subnetId);
        try {
            this.dynamicDataSourceContext.setDataSource(subnetId);
            procedureHdtDao.doProcedureHdt();
            log.info("调度DRP数据库节点[{}]存储过程成功", subnetId);
        } catch (Exception e) {
            log.error("调度DRP数据库节点[{}]存储过程异常, ERROR:\n{}", subnetId, Throwables.getStackTraceAsString(e));
        } finally {
            this.dynamicDataSourceContext.clear();
        }
    }
}
