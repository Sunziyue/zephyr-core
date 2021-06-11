package com.jjy.ip.hiboos.order.server.dao;

import com.jjy.ip.hiboos.order.server.model.TBpSaleposH;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

/**
 * @author Sunziyue
 */
@Repository
public class TBpSalePosHDao extends MyBatisDao<TBpSaleposH> {
    /**
     * 插入一条
     * @param record
     * @return
     */
    public int insert(TBpSaleposH record){
        return this.sqlSession.insert(this.sqlId("insert"), record);
    }
}
