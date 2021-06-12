package xyz.szy.zephyr.core.api.exception;

public class ChannelUnKnowException extends RuntimeException{
    public ChannelUnKnowException() {
        super();
    }

    public ChannelUnKnowException(String message) {
        super(message);
    }
}
