package com.jjy.ip.hiboos.order.api.exception;

public class DataMissException extends RuntimeException{
    public DataMissException() {
        super();
    }

    public DataMissException(String message) {
        super(message);
    }
}
