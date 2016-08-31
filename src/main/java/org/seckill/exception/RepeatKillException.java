package org.seckill.exception;

/**
 * Created by hanyh on 16/8/31.
 * 重复秒杀异常
 * 需要继承运行时异常 因为spring只接受声明时事务只接受运行时异常回滚
 */
public class RepeatKillException extends SeckillException{


    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
