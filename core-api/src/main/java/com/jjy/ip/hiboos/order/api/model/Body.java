package com.jjy.ip.hiboos.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Sunziyue
 * @date 2020-09-16 15:17
 * @apiNote 订单核销接口入参业务数据
 */
@Data
@Builder
@ApiModel("订单核销接口入参业务数据")
public class Body implements Serializable {

    private static final long serialVersionUID = -7242905338760828293L;

    @ApiModelProperty("调用方流水号")
    private String transactionId;

    @ApiModelProperty("渠道订单号")
    private String channelOrderId;

    @ApiModelProperty("商家自有门店编码")
    private String outStationNo;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("是否是退货")
    private Boolean refund;

    @ApiModelProperty("会员卡号")
    private String vipCode;

    @ApiModelProperty("用于区分调用渠道")
    private String channel;

    @ApiModelProperty("商品明细")
    private List<Product> productList;
}
