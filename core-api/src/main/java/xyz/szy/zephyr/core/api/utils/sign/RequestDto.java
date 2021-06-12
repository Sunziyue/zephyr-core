package xyz.szy.zephyr.core.api.utils.sign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("签名参数")
public class RequestDto {

    @ApiModelProperty("海博系统分配的应用标识")
    private String appId;

    @ApiModelProperty("时间戳（毫秒）请实时获取，允许误差在 15 分钟以内")
    private long timestamp;

    @ApiModelProperty("MD5签名数据")
    private String sign;

    @ApiModelProperty("json 格式的业务参数")
    private String body;

}
