package com.jjy.ip.hiboos.order.server.dao;

import com.jjy.ip.hiboos.order.server.model.TBpSaleposProm;
import io.terminus.common.mysql.dao.MyBatisDao;
import org.springframework.stereotype.Repository;

/**
 * @author Sunziyue
 */
@Repository
public class TBpSalePosPromDao extends MyBatisDao<TBpSaleposProm> {
    /**
     * 插入一条
     * @param record
     * @return
     */
    public int insert(TBpSaleposProm record){
        return this.sqlSession.insert(this.sqlId("insert"), record);
    }
}
