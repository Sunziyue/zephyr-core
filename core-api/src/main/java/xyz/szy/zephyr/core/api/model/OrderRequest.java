package xyz.szy.zephyr.core.api.model;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Sunziyue
 * @date 2020-09-16 15:17
 * @apiNote 订单核销接口入参
 */
@Data
@Builder
@ApiModel("订单核销接口入参")
public class OrderRequest implements Serializable {

    private static final long serialVersionUID = -6518919396108643430L;

    @ApiModelProperty("应用标识")
    private String appId;

    @ApiModelProperty(value = "时间戳（毫秒）",example = "123456798")
    private Long timestamp;

    @ApiModelProperty("MD5 签名数据")
    private String sign;

    @ApiModelProperty("json 格式的业务参数")
    private String body;

    /**
     * @return 订单核销业务数据
     * @apiNote 转换String类型的json业务数据
     * @date 2020-09-16 15:17
     */
    public Body body() {
        return JSON.parseObject(body, Body.class);
    }
}
