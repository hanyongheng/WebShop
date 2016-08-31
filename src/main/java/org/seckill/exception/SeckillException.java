package org.seckill.exception;


/**
 * Created by hanyh on 16/8/31.
 * 秒杀异常 其实是个规范 spring中DAO有个DAO的通用异常
 * 这里也做一个秒杀通用异常
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
