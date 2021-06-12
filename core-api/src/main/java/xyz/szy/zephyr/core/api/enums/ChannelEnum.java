package xyz.szy.zephyr.core.api.enums;

import xyz.sunziyue.common.utils.Arguments;
import xyz.szy.zephyr.core.api.exception.ChannelUnKnowException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.MessageFormat;

/**
 * @author SunZiYue
 * @date 2021年04月13日10点38分
 * @apiNote 订单状态
 */
@ApiModel("海博订单渠道信息枚举")
public enum ChannelEnum {

    @ApiModelProperty("京东")
    JD_CHANNEL("1", "24", "992", "JD", "25", "102", "京东"),

    @ApiModelProperty("饿了么")
    ELE_CHANNEL("3", "20", "995", "EL", "161", "102", "饿了么"),

    @ApiModelProperty("美团")
    MT_CHANNEL("2", "10", "997", "MT", "67", "102", "美团");

    private final String hibChannel;
    private final String jjyChannel;
    private final String posId;
    private final String cash;
    private final String payId;
    private final String parentCode;
    private final String explain;

    ChannelEnum(String hibChannel, String jjyChannel, String posId, String cash, String payId, String parentCode, String explain) {
        this.hibChannel = hibChannel;
        this.jjyChannel = jjyChannel;
        this.posId = posId;
        this.cash = cash;
        this.payId = payId;
        this.parentCode = parentCode;
        this.explain = explain;
    }

    public String hibChannel() {
        return hibChannel;
    }

    public String jjyChannel() {
        return jjyChannel;
    }

    public String posId() {
        return posId;
    }

    public String cash() {
        return cash;
    }

    public String payId() {
        return payId;
    }

    public String parentCode() {
        return parentCode;
    }

    public String explain() {
        return explain;
    }

    public static ChannelEnum getByHibChannel(String hibChannel) throws ChannelUnKnowException {
        ChannelEnum result = null;
        if (Arguments.isEmpty(hibChannel)) {
            throw new ChannelUnKnowException(MessageFormat.format("海博渠道号为:[{0}]", hibChannel));
        }
        for (ChannelEnum anEnum : ChannelEnum.values()) {
            if (anEnum.hibChannel.equals(hibChannel)) {
                result = anEnum;
            }
        }
        if (Arguments.notNull(result)) {
            return result;
        } else {
            throw new ChannelUnKnowException(MessageFormat.format("海博渠道号[{0}],不在DRP支持范围内", hibChannel));
        }
    }
}
