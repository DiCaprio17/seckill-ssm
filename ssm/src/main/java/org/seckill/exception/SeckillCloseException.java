package org.seckill.exception;

/**
 * 秒杀已经关闭异常(运行时异常),当秒杀结束就会出现这个异常
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}