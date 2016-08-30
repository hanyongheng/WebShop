package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by hanyh on 16/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testInsertSuccessKilled() throws Exception {

        long id=1000L;
        long phone=18117502911L;
        int result=successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("result   "+result);
    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {

        long id=1000L;
        long phone=18117502911L;
        SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println("successKilled   "+successKilled);
        System.out.println("Seckill   "+successKilled.getSeckill());

    }
}