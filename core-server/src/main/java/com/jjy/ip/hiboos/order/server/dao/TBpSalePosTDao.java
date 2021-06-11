package com.jjy.ip.hiboos.order.server.dao;

import com.jjy.ip.hiboos.order.server.model.TBpSaleposT;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

/**
 * @author Sunziyue
 */
@Repository
public class TBpSalePosTDao extends MyBatisDao<TBpSaleposT> {
    /**
     * 插入一条
     * @param record
     * @return
     */
    public int insert(TBpSaleposT record){
        return this.sqlSession.insert(this.sqlId("insert"), record);
    }
}
