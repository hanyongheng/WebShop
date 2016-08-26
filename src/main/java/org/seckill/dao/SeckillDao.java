package org.seckill.dao;

import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by hanyh on 16/8/26.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    public int reduceNumber(long seckillId, Date killTime);

    /**
     * 根据秒杀商品ID 查询该秒杀产品
     * @param seckillId
     * @return
     */
    public Seckill queryById(long seckillId);

    /**
     * 查询秒杀产品的列表(分页)
     * @param offset
     * @param limit
     * @return
     */
    public List<Seckill> queryAll(int offset,int limit);
}
