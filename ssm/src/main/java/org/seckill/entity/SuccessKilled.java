package org.seckill.entity;

import java.util.Date;

/**
 * 秒杀成功明细实体类
 */
public class SuccessKilled {
    private long seckilled;     //秒杀商品ID
    private long userPhone;     //用户手机号
    private short state;        //状态标示:-1无效 0成功 1已付款
    private Date createTime;    //创建时间

    //多对一
    private Seckill seckill;

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    public long getSeckilled() {
        return seckilled;
    }

    public void setSeckilled(long seckilled) {
        this.seckilled = seckilled;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckilled=" + seckilled +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
