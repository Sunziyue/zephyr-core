package com.jjy.ip.hiboos.order.server.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Sunziyue
 */
@Data
public class PosPrice implements Serializable {

    private static final long serialVersionUID = -2726213591182562622L;

    // 营销节点
    private String drpid;

    // 商场编号
    private String mktid;

    // 商品编码
    private String shopid;

    // 促销单号
    private String bill_dept;

    // 商品规格
    private String standards;

    // 单位
    private String unit_sale;

    // 商品计量
    private String flag_wp;

    // 柜组编码
    private String countid;

    // 部门编码
    private String deptid;

    // 商品简称
    private String name_short;

    // 条形码
    private String txm;

    // 销售价格
    private BigDecimal price_sale;

    // 正常销售价格
    private BigDecimal price_must;

    // VIP价格
    private BigDecimal price_vip;

    // 允许退货
    private String canrtn;

    // 新商品
    private String newshop;

    // 促销分类
    private String promotion;

    // 公司定价
    private BigDecimal price_com;
}
