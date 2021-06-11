package com.jjy.ip.hiboos.order.server.model;

import com.jjy.ip.hiboos.order.api.enums.OrderEnum;
import com.jjy.ip.hiboos.order.api.utils.date.DateUtils;
import io.terminus.common.model.Criteria;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author SunZiYue
 * 订单缓存DTO
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CacheOrder extends Criteria implements Serializable {

    private static final long serialVersionUID = -4011261026076557678L;
    // 用于区分调用渠道
    private String channel;

    // pos号
    private String posId;

    // 用于生成 cash_by
    private String cash;

    // 支付方式编号
    private String payId;

    // 父级支付方式编号
    private String parentCode;

    // 调用方流水号，JDDJ+渠道单号(逆向单为中台逆 向单号)_channel 主键
    private String transactionId;

    // 渠道单号 同一个订单的{正向、逆向}此字段一致, 可以用作关联关系
    private String channelOrderId;

    // 订单类型：0：正向单；1：全部退款单；2部分退款单
    private Integer orderType;

    // 线下门店标识
    private String mktId;

    // 营销节点
    private String drpId;

    // DRP单号
    private String jjyBillId;

    // 每个门店当天下的订单流水号 [00001] 开始 第二天重新从 [00001] 排序
    private Integer serialNo;

    // 订单数据
    private String jsonCache;

    // 订单创建时间本条记录的存储时间
    private Date createTime;

    // 最后更新时间
    private Date updateTime;

    // 在当天时间处理处理的订单
    private String processDate;

    // 处理处理的订单的结果； 0：未处理；1：已处理 2：处理失败
    private Integer processStatus;

    // 失败原因
    private String failureReason;

    public void NG(String msg) {
//        this.jjyBillId = null;
//        this.serialNo = 10000;
        this.processStatus = OrderEnum.FAILED.statue();
        this.failureReason = msg;
        this.updateTime = DateUtils.date();
    }

    public void dataNG(String msg) {
//        this.jjyBillId = null;
//        this.serialNo = 10000;
        this.processStatus = OrderEnum.UNTREATED.statue();
        this.failureReason = msg;
        this.updateTime = DateUtils.date();
    }

    public void OK() {
        this.processStatus = OrderEnum.SUCCESS.statue();
        this.updateTime = DateUtils.date();
        this.failureReason = null;
    }
}
