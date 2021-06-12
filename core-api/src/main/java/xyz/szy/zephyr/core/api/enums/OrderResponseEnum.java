package xyz.szy.zephyr.core.api.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("海博订单接收状态")
public enum OrderResponseEnum {

    @ApiModelProperty("接收成功")
    SUCCESS("0", "success"),

    @ApiModelProperty("失败")
    FAILURE("1", "failure");

    private String code;
    private String msg;

    OrderResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
