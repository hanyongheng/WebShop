package org.seckill.dto;

/**
 * Created by hanyh on 16/9/2.
 *  封装秒杀结果的ajax的json数据
 */
public class SeckillResult<T> {

    private boolean success;

    private T data;

    private String error;

    /**
     *  如果成功就会有数据
     * @param success
     * @param data
     */
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     *  如果失败就会有错误信息
     * @param success
     * @param error
     */
    public SeckillResult(boolean success,String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "SeckillResult{" +
                "success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
