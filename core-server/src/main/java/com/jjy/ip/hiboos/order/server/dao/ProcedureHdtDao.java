package com.jjy.ip.hiboos.order.server.dao;

import io.terminus.common.mysql.dao.MyBatisDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Repository;

/**
 * 触发DRP存储过程
 * @author Sunziyue
 */
@Slf4j
@Repository
public class ProcedureHdtDao extends MyBatisDao<Null> {
    /**
     * 触发DRP存储过程
     */
    public void doProcedureHdt(){
        this.sqlSession.selectOne("procedureHdtMapper.doProcedureHdt");
    }
}
