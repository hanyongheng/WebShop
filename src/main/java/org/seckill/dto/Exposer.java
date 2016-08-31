package org.seckill.dto;

/**
 * Created by hanyh on 16/8/31.
 * 暴露秒杀地址Dto
 */
public class Exposer {

    /**
     * 秒杀是否开始
     */
    private boolean isExposed;
    /**
     * 加密的Md5
     */
    private String md5;
    /**
     * 秒杀的Id
     */
    private long seckillId;
    /**
     * 系统当前时间毫秒
     */
    private long now;
    /**
     * 秒杀开启时间
     */
    private long start;
    /**
     * 秒杀结束时间
     */
    private long end;

    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Exposer(boolean isExposed, String md5, long seckillId) {
        this.isExposed = isExposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean isExposed,long seckillId,long now, long start, long end) {
        this.isExposed = isExposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean isExposed, long seckillId) {
        this.isExposed = isExposed;
        this.seckillId = seckillId;
    }

}
