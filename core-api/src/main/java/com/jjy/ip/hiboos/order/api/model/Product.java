package com.jjy.ip.hiboos.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Sunziyue
 * @date 2020-09-16 15:17
 * @apiNote 订单核销接口入参业务数据商品详情
 */
@Data
@Builder
@ApiModel("订单核销接口入参业务数据商品详情")
public class Product implements Serializable {

    private static final long serialVersionUID = 1578756518092793459L;

    @ApiModelProperty("商家 skuId")
    private String outSkuId;

    @ApiModelProperty("商品 UPC 编码")
    private String skuUpc;

    @ApiModelProperty("商品数量（非标品按 kg, 标品按个数）")
    private double skuCount;

    @ApiModelProperty("商品单价; 单位分")
    private long skuPrice;

    @ApiModelProperty("商品金额小计（正、逆向核销金额均为正数）; 单位分")
    private long payValue;

    @ApiModelProperty("优惠金额，默认为 0")
    private long discountValue;

    @ApiModelProperty("商家单品补贴")
    private long merchantSingleProductSubsidy;

    @ApiModelProperty("平台单品补贴")
    private long platformSingleProductSubsidy;

    @ApiModelProperty("商家整单补贴")
    private long bussinessSubsidy;

    @ApiModelProperty("平台整单补贴")
    private long platformSubsidy;

    @ApiModelProperty("营销节点") // 此对象里不传
    private String drpid;

    @ApiModelProperty("商场编号") // 此对象里不传
    private String mktid;
}
