package com.jjy.ip.hiboos.order.server.dao;

import com.jjy.ip.hiboos.order.server.model.TBpSaleposD;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

/**
 * @author Sunziyue
 */
@Repository
public class TBpSalePosDDao extends MyBatisDao<TBpSaleposD> {
    /**
     * 插入一条
     * @param record
     * @return
     */
    public int insert(TBpSaleposD record){
        return this.sqlSession.insert(this.sqlId("insert"), record);
    }
}
