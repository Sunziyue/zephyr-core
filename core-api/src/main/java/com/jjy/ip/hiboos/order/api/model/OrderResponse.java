package com.jjy.ip.hiboos.order.api.model;

import com.jjy.ip.hiboos.order.api.enums.OrderResponseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Sunziyue
 * @date 2020-09-16 15:17
 * @apiNote 订单核销接口返回值
 */
@Data
@Builder
@ApiModel("订单核销接口返回值")
public class OrderResponse implements Serializable {

    private static final long serialVersionUID = -7027302047111040191L;

    @ApiModelProperty("状态码")
    private String code;

    @ApiModelProperty("状态说明")
    private String msg;

    public static OrderResponse OK() {
        return OrderResponse.builder()
                .code(OrderResponseEnum.SUCCESS.code())
                .msg(OrderResponseEnum.SUCCESS.msg())
                .build();
    }

    public static OrderResponse NG() {
        return OrderResponse.builder()
                .code(OrderResponseEnum.FAILURE.code())
                .msg(OrderResponseEnum.FAILURE.msg())
                .build();
    }

    public static OrderResponse NG(String msg) {
        return OrderResponse.builder()
                .code(OrderResponseEnum.FAILURE.code())
                .msg(msg)
                .build();
    }
}
