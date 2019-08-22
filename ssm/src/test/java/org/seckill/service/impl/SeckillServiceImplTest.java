package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}", seckillList);
        System.out.println(seckillList.toString());
        /**
         * 2019-08-20 17:23:11.216 [main] INFO  org.seckill.service.impl.SeckillServiceImplTest - list=[Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=100, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}, Seckill{seckillId=1001, name='500元秒杀iPad2', number=200, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}, Seckill{seckillId=1002, name='300元秒杀小米7', number=300, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}, Seckill{seckillId=1003, name='200元秒杀红米note', number=400, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}]
         * [Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=100, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}, Seckill{seckillId=1001, name='500元秒杀iPad2', number=200, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}, Seckill{seckillId=1002, name='300元秒杀小米7', number=300, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}, Seckill{seckillId=1003, name='200元秒杀红米note', number=400, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}]
         */
    }

    @Test
    public void getById() throws Exception {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}", seckill);
        /**
         * 2019-08-20 17:24:44.717 [main] INFO  org.seckill.service.impl.SeckillServiceImplTest - seckill=Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=100, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}
         */
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}", exposer);
        /**
         * 2019-08-20 17:26:04.390 [main] INFO  org.seckill.service.impl.SeckillServiceImplTest - exposer=Exposer{秒杀状态=false, md5加密值='null', 秒杀ID=1000, 当前时间=1566293164389, 开始时间=1558454400000, 结束=1558540800000}
         * 秒杀状态=false, md5加密值='null'是因为createTime没有在startTime-endTime之间
         * 修改之后
         * 2019-08-20 17:39:34.947 [main] INFO  org.seckill.service.impl.SeckillServiceImplTest - exposer=Exposer{秒杀状态=true, md5加密值='3781463499435ea6016e4163568492c2', 秒杀ID=1000, 当前时间=0, 开始时间=0, 结束=0}
         */
    }

    @Test
    public void executeSeckill() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            long userPhone = 12222222222L;
            String md5 = "3781463499435ea6016e4163568492c2";//使用上面的md5
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("result={}", seckillExecution);
            } catch (SeckillCloseException | RepeatKillException e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("秒杀未开启");
        }
        /**
         * 2019-08-20 17:41:57.712 [main] INFO  org.seckill.service.impl.SeckillServiceImplTest - result=SeckillExecution{秒杀的商品ID=1000, 秒杀状态=1, 秒杀状态信息='秒杀成功', 秒杀的商品=SuccessKilled{seckilled=0, userPhone=12222222222, state=0, createTime=Tue Aug 20 17:41:57 CST 2019}}
         */
    }

    @Test
    public void executeSeckillProcedureTest() {
        long seckillId = 1001;
        long phone = 1368011101;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            System.out.println(execution.getStateInfo());
            //秒杀成功
        }
    }

    @Test
    public void executeSeckillProducedureTest() {
        long seckillId = 1001;
        long phone = 1368011101;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProducedure(seckillId, phone, md5);
            System.out.println(execution.getStateInfo());
            //重复秒杀
        }
    }
}