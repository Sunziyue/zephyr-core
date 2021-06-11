package com.jjy.ip.hiboos.order.server.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Sunziyue
 */
@Data
public class TBpSaleposT implements Serializable {
    private static final long serialVersionUID = -1141294303048656277L;

    //	营销节点
    private String drpid;

    //	门店编码
    private String mktid;

    //	年月日
    private Integer ymd;

    //	流水号
    private String bill;

    //	记录号
    private Integer id;

    //	付款方式
    private String payid;

    //	币种编号
    private String coinid;

    //	有价券号
    private String no_card;

    //	信用卡交易号
    private String no_bank;

    //	付款量
    private BigDecimal num_pay;

    //	折合汇率
    private BigDecimal rate_bk;

    //	付款额
    private BigDecimal value_pay;

    //	交易时间
    private Date trade_time;

    //	商户号
    private String dealerno;

    //	活动主题
    private String coupon_bill;

    //	券种ID
    private String cpid;

    //	终端号
    private String termino;

    //	运营方编码
    private String payid_port;

    //	支付归属
    private String parentcode;

    //	银行名称
    private String bankcode;

    //	原记录号
    private Integer id_old;

    //	原付款方式
    private String payid_old;

    //	联机退货
    private String online_return;
}