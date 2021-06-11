package com.jjy.ip.hiboos.order.server.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TBpSaleposProm {
    // 营销节点
    private String drpid;

    // 门店编号
    private String mktid;

    // 销售日期
    private Integer ymd;

    // 订单号
    private String bill;

    // 促销行号
    private Integer rowno;

    // 商品行号
    private Integer id;

    // 商品编码
    private String shopid;

    // 活动类型
    private String prom_type;

    // 活动ID
    private String activitycode;

    // 券种ID
    private String coupontype;

    // 券号
    private String couponid;

    // 券类型
    private String couponclass;

    // 档期编号
    private String couponppno;

    // 总优惠金额
    private BigDecimal discount;

    // 平台承担费用
    private BigDecimal mt_rate;

    // 商户承担费用
    private BigDecimal shop_rate;

    // 代理商承担费用
    private BigDecimal agent_rate;

    // 物流承担费用
    private BigDecimal logistics_rate;

    // 处理标记
    private String flag;
}
