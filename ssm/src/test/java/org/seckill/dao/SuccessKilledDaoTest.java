package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1000L;
        long phone = 13568745965L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("insertCount=" + insertCount);
        /**
         * 第一次：insertCount=1
         * 第二次：insertCount=0 不允许重复秒杀
         * 成功插入就返回1, 否则就返回0
         * 因为设置了PRIMARY KEY (seckill_id,user_phone), 联合主键，唯一，不允许重复
         * 而且在在SuccessKilledDao.xml设置了INSERT IGNORE INTO success_killed (seckill_id, user_phone, state)VALUES (#{seckillId}, #{userPhone}, 0)
         * 因此主键冲突忽略了，没有报错，而是返回0或1
         */

    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        //因为之前这用户进行秒杀过，所以查询这个用户
        long id = 1000L;
        long phone = 13568745965L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
        /**
         * SuccessKilled{seckilled=0, userPhone=13568745965, state=0, createTime=Mon Aug 19 12:03:10 CST 2019}
         * Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=0, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}
         */
    }
}