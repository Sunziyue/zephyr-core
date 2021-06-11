package com.jjy.ip.hiboos.order.api.exception;

public class ChannelUnKnowException extends RuntimeException{
    public ChannelUnKnowException() {
        super();
    }

    public ChannelUnKnowException(String message) {
        super(message);
    }
}
