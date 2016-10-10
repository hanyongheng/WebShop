package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by hanyh on 16/10/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit加载Spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    @Autowired
    RedisDao redisDao;
    @Autowired
    SeckillDao seckillDao;
    private long id = 1001;

    @Test
    public void testGetSeckill() throws Exception {
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if (seckill != null) {
                String result = redisDao.putSeckill(seckill);
                System.out.print(result);
                Seckill seckill1=redisDao.getSeckill(id);
                System.out.print(seckill1);
            }
        }
    }

    @Test
    public void testPutSeckill() throws Exception {

    }
}