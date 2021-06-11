package com.jjy.ip.hiboos.order.server.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Sunziyue
 */
@Data
public class TBpSaleposH implements Serializable {
    private static final long serialVersionUID = -8404781634175066070L;

    //	营销节点
    private String drpid;

    //	商场编号
    private String mktid;

    //	销售传回
    private Integer ymd;

    //	销售票据
    private String bill;

    //	收银机
    private String posid;

    //	收银员
    private String cash_by;

    //	销售时间
    private Date time_sale;

    //	VIP号码
    private String no_vip;

    //	会员卡折扣等级
    private Integer degree;

    //	销售总额
    private BigDecimal value;

    //	折后总额
    private BigDecimal value_afterdisc;

    //	销售折让
    private BigDecimal value_disc;

    //	退货总额
    private BigDecimal value_rtn;

    //	折后退额
    private BigDecimal value_rtnafterdisc;

    //	退货折让
    private BigDecimal value_rtndisc;

    //	异常传回
    private Date time_send;

    //
    private String flag_count;

    //	已经结账
    private String flag_bk;

    //	原打印票据
    private String bill_old;

    //
    private String bill_tax;

    //	印花主题
    private String stampbill;

    //	派送印花类型
    private Integer stampqty;

    //	顾客印花类型
    private Short stamptype;

    //
    private String flag_taxbill;

    // 电子发票的验证码
    private String einvoiceCode;

    //	外部渠道订单号
    private String biz_order_id;

    //	销售渠道
    private String order_channel;

    //	对账订单号
    private String tb_biz_order_id;
}