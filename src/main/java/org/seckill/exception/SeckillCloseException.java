package org.seckill.exception;

/**
 * Created by hanyh on 16/8/31.
 */
public class SeckillCloseException extends SeckillException{


    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
