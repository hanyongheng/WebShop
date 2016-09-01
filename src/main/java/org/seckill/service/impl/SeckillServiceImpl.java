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
import org.springframework.transaction.annotation.Transactional;
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
            System.out.println("秒杀产品名字   "+seckill.getSeckillId());
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            System.out.println("秒杀产品时间  nowTime "+nowTime.getTime());
            System.out.println("秒杀产品时间  startTime "+startTime.getTime());
            System.out.println("秒杀产品时间  endTime "+endTime.getTime());
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //转换特殊字符串过程不可逆
        String md5 = getMd5(seckillId);
        System.out.println("秒杀产品md5   "+md5);
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId) {

        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    //使用注解进行标注事务方法的好处
    // 1:开发团队达成一致的约定 明确标注事务方法的编程风格 不需要xml的配置方式一次配置永久失效 不好维护
    // 2:保证事务方法的执行时间尽可能的短 不要穿插其他的网络操作RPC/HTTP 如果需要可以剥离这样的网络操作到另外一个方法中
    // 3:不是所有的方法都需要标注成事务的 如只需要一条修改操作 不需要多个修改操作 只读操作 不需要事务
    @Transactional
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
