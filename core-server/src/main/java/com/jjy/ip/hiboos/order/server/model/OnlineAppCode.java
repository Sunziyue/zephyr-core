package com.jjy.ip.hiboos.order.server.model;

import io.swagger.annotations.ApiModel;
import io.terminus.common.model.Criteria;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author SunZiYue
 * @date 2020/09/14 16:18
 * @apiNote 各系统门店编号码表
 */
@Data
@Builder
@ApiModel("各系统门店编号码表")
@EqualsAndHashCode(callSuper = true)
public class OnlineAppCode extends Criteria implements Serializable {

    private static final long serialVersionUID = 6740922527852372932L;
    // 线上渠道
    private String channel;

    // Sap门店编码
    private String sap_id;

    // 营销节点
    private String drpid;

    // Drp门店代码
    private String drp_id;

    // Drp门店名称
    private String drp_name;

    // 线上分配给APP方的id
    private String app_id;

    // 线上门店id
    private String baidu_shop_id;

    // 购物袋编码
    private String bag_code;

    // Drp数据节点
    private String subnetid;

    // 密钥
    private String secret;

    // 开发者source
    private String source;

    // 状态
    private String status;

    // 同步状态
    private String sync;
}
