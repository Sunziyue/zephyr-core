package xyz.szy.zephyr.core.api.exception;

public class DataMissException extends RuntimeException{
    public DataMissException() {
        super();
    }

    public DataMissException(String message) {
        super(message);
    }
}
