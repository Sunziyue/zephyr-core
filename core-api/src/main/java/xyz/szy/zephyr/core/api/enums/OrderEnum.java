package xyz.szy.zephyr.core.api.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author SunZiYue
 * @date 2020年09月17日10点18分
 * @apiNote 订单状态
 */
@ApiModel("海博订单状态枚举")
public enum OrderEnum {

    @ApiModelProperty("销售单")
    SUBMIT_ORDER(0, "销售单"),

    @ApiModelProperty("退款单")
    RETURN_ORDER(1, "退款单"),

    @ApiModelProperty("未处理")
    UNTREATED(0, "未处理"),

    @ApiModelProperty("处理成功")
    SUCCESS(1, "处理成功"),

    @ApiModelProperty("处理失败")
    FAILED(2, "处理失败"),

    @ApiModelProperty("系统异常处理失败")
    SYS_ERROR(3, "系统异常处理失败");

    private final Integer statue;

    private final String explain;

    OrderEnum(Integer statue, String explain) {
        this.statue = statue;
        this.explain = explain;
    }

    public Integer statue(){
        return statue;
    }

    public String explain(){
        return explain;
    }
}
