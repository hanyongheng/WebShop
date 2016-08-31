package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by hanyh on 16/8/31.
 * 业务实现类
 */

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入service的依赖 dao已经由mapper那个时候有spring容器创造了
    // 所以这里就用Autowired注解还有@resource和@inject等注解等同于Autowired
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    /**
     * 盐值字符串用于加密MD5
     */
    private final String salt = "@@E2rafqwrqwsenddw3wpljyp[luyk[@)($)$)(Qqwa;;s;s;s;sao'asa'asok";

    public List<Seckill> getSeckillList() {

        return seckillDao.queryAll(0, 4);
    }

    public Seckill getSeckillById(long seckillId) {

        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {

        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {

            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {

            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //转换特殊字符串过程不可逆
        String md5 = getMd5(seckillId);

        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId) {

        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public SeckillExecution executeSeckill(long seckillId, long phone, String md5) throws SeckillException,
            RepeatKillException, SeckillCloseException {

        if (md5 == null || !getMd5(seckillId).equals(md5)) {

            throw new SeckillException("seckill data rewrite");
        }
        try {
            //执行秒杀 1.减库存 2插入购买明细
            Date nowTime = new Date();
            int state = seckillDao.reduceNumber(seckillId, nowTime);
            //减库存
            if (state <= 0) {
                //没有更新到记录 秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, phone);
                if (insertCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException("seckilled repeated");
                } else {
                    //秒杀成功 在数据库中可以查询的到
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, phone);

                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e1) {

            throw e1;
        } catch (RepeatKillException e2) {

            throw e2;
        } catch (Exception e) {
            //所有编译期异常 转换成运行期异常 方便spring进行rollback
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }
}
