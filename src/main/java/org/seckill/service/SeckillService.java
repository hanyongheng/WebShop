package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by hanyh on 16/8/31.
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 根据Id去查询某一个秒杀
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 秒杀开启时暴露输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
      * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param phone
     * @param md5 在秒杀暴露接口中有个md5需要此方法中来教研
     */
    SeckillExecution executeSeckill(long seckillId, long phone, String md5) throws SeckillException,
            RepeatKillException,SeckillCloseException;

}
