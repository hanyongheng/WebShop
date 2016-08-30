package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
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
    public int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

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
     * @Param 这个注解是mybatis提供的注解类 因为java没有保证形参的运行轨迹
     * 所以在映射到mybatis中 mybatis不是到哪个参数对应哪个参数
     * @return
     */
    public List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
