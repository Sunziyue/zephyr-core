package com.jjy.ip.hiboos.order.server.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TBpSalepos implements Serializable {

    private static final long serialVersionUID = 8676079144451059698L;

    private TBpSaleposH tBpSaleposH;
    private List<TBpSaleposD> tBpSaleposDList;
    private TBpSaleposT tBpSaleposT;
    private List<TBpSaleposProm> tBpSaleposPromList;
}
