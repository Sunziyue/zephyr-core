package com.jjy.ip.hiboos.order.api.service;

/**
 * @author Sunziyue
 * @date 2020/09/14 18:03
 * @apiNote 处理订单总入口
 */
public interface ProcessOrderService {

    /**
     * 处理订单总入口
     * @param subnetId 数据库节点
     * @return 数据库节点 处理情况
     */
    void processOrderBySubnetId(String subnetId);
}
