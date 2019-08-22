package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * 1000元秒杀iPhoneX
         * Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=100,
         * startTime=Wed May 22 00:00:00 CST 2019,
         * endTime=Thu May 23 00:00:00 CST 2019,
         * createTime=Sun Aug 18 13:38:11 CST 2019}
         */
    }

    @Test
    public void queryAll() throws Exception {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException:
         * Parameter 'offset' not found. Available parameters are [arg1, arg0, param1, param2]
         */
        /**
         * List<Seckill> queryAll(int offset, int limit);
         * java没有保存形参的记录：queryAll(int offset, int limit) -> queryAll(arg0, arg1)
         * 一个参数的时候没问题，但是有多个参数的时候需要告诉mybatis每个位置的参数是什么，
         * 这样后面#{offset}提取参数的时候，mybatis才能帮我们找到这个参数所代表的具体的值
         * 如何解决：利用@Param注解
         * List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
         */
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
        /**
         * Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=100, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}
         * Seckill{seckillId=1001, name='500元秒杀iPad2', number=200, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}
         * Seckill{seckillId=1002, name='300元秒杀小米7', number=300, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}
         * Seckill{seckillId=1003, name='200元秒杀红米note', number=400, startTime=Wed May 22 00:00:00 CST 2019, endTime=Thu May 23 00:00:00 CST 2019, createTime=Sun Aug 18 13:38:11 CST 2019}
         */
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount=" + updateCount);
        /**
         * updateCount=0
         */
    }
}