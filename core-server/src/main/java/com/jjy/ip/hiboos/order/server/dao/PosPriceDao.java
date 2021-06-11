package com.jjy.ip.hiboos.order.server.dao;

import com.jjy.ip.hiboos.order.server.model.PosPrice;
import io.terminus.common.mysql.dao.MyBatisDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sunziyue
 */
@Slf4j
@Repository
public class PosPriceDao extends MyBatisDao<PosPrice> {

    /**
     * @return
     */
    public Map<String, PosPrice> findByShopIds(Map<String, Object> criteria) {
        List<PosPrice> posPriceList = this.sqlSession.selectList(this.sqlId("findByShopIds"), criteria);
        return posPriceList.stream().collect(Collectors.toMap(PosPrice::getShopid, posPrice -> posPrice));
    }

    /**
     * 犯了低级错误，心情低迷，很迷
     * @param criteria
     * @return
     */
    public Map<String, PosPrice> findByOuterProduct(Map<String, Object> criteria) {
        List<PosPrice> posPriceList = this.sqlSession.selectList(this.sqlId("findByOuterProduct"), criteria);
        return posPriceList.stream().collect(Collectors.toMap(posPrice -> posPrice.getMktid() + posPrice.getShopid(), posPrice -> posPrice));
    }

}
