package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by hanyh on 16/9/1.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})

public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    private static final Logger logger= LoggerFactory.getLogger(SeckillServiceTest.class);
    @Test
    public void testGetSeckillList() throws Exception {

        List<Seckill> list=seckillService.getSeckillList();
//        for (Seckill seckill:list){
//            System.out.println(seckill.getName());
//        }
//        logger.trace("======trace");
//        logger.debug("======debug");
        logger.info("list={}",list);
//        logger.warn("======warn");
//        logger.error("======error");
    }

    @Test
    public void testGetSeckillById() throws Exception {

        long id=1000L;
        Seckill seckill=seckillService.getSeckillById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testExportSeckillUrl() throws Exception {

        long id=1000;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
    }

    @Test
    public void testExecuteSeckill() throws Exception {

    }
}