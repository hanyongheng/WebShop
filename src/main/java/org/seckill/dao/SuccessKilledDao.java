package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by hanyh on 16/8/26.
 *  秒杀的购买明细DAO
 */
public interface SuccessKilledDao {

    /**
     *  插入购买明细 可以过滤重复(联合主键 过滤防止重复秒杀)
     * @param seckillId
     * @param userPhone
     * @return
     */
    public int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     *  根据秒杀商品ID查询秒杀购买明细
     *  并且携带出该秒杀商品(因为一个秒杀明细可能对应对个秒杀产品 多对一的关系)
     * @param seckillId
     * @return
     */
    public SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
