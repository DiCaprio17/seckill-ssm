---
layout:     post
title:      SSM框架高并发和商品秒杀项目（IDEA）
subtitle:   （一）Java高并发秒杀API之业务分析与DAO层
date:       2019-08-19
author:     DiCaprio
header-img: img/post-bg-alibaba.jpg
catalog: true
tags:
    - SSM
    - Java高并发
---
# seckill

一个整合SSM框架的高并发和商品秒杀项目,学习目前较流行的Java框架组合实现高并发秒杀API

## 项目的来源

项目的来源于国内IT公开课平台,质量没的说,很适合学习一些技术的基础,这个项目是由四个系列的课程组成的,流程分为几个流程,很基础地教你接触到一个相对有技术含量的项目

- Java高并发秒杀API之业务分析与DAO层
- Java高并发秒杀API之web层
- Java高并发秒杀API之Service层
- Java高并发秒杀API之高并发优化

其实这几个流程也就是开发的流程,首先从DAO层开始开发,从后往前开发,开始Coding吧！

## 项目环境的搭建

- **操作系统** : Ubuntu 16.04 
- **IDE** ：IntelliJ IDEA 2019.2.5 x64 用Eclipse也一样的,工具时靠人用的
- **JDK** : JDK1.8 建议使用JDK1.7以上版本,有许多语法糖用着挺舒服的
- **Web容器** ： Tomcat 8.5
- **数据库** ：Mysql-5.6.17-WinX64    实验性的项目用Mysql就足够啦
- **依赖管理工具** : Maven  管理jar包真的很方便  

这里列出的环境不是必须的,你喜欢用什么就用什么,这里只是给出参考,不过不同的版本可能会引起各种不同的问题就需要我们自己去发现以及排查,在这里使用Maven的话时方便我们管理JAR包,我们不用跑去各种开源框架的官网去下载一个又一个的JAR包,配置好了Maven后添加pom文件坐标就会从中央仓库下载JAR包,如果哪天替换版本也很方便

------

## 项目效果图

- 秒杀商品列表 

![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-result_1.jpg)
- 秒杀结束提示界面 

![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-result_2.jpg)

- 开始秒杀提示界面 

![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-result_3.jpg)
- 重复秒杀提示界面 

![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-result_4.jpg)

- 秒杀秒杀成功提示界面 

![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-result_5.jpg)

------

## 项目的运行

### 下载  

`Download Zip`或者 `git clone`

```shell
	git clone https://github.com/DiCaprio17/seckill.git
```

### 导入到IDE  

这里因为是使用`IDEA`创建的项目,所以使用`IDEA`直接打开是很方便的,提前是你要配置好`maven`的相关配置,以及项目`JDK`版本,
`JDK`版本必须在`1.8`以上,因为在项目中使用了`Java8`的`LocalDateTime`以及`LocalDate`,所以低于这个版本编译会失败的

- IDEA  
  直接在主界面选择`Open`,然后找到项目所在路径,点击`pom.xml`打开就可以了
- Eclipse
  这个项目是基于`IDEA`创建,我这里把项目转成了`Eclipse`的项目,如果你使用Eclipse的话也可以直接导入,只是步骤更繁琐一点,[Eclipse导入步骤](/note/EclipseImport.md)

### (一)Java高并发秒杀APi之业务分析与DAO层代码编写

#### 构建项目的基本骨架

- 首先我们要搭建出一个符合Maven约定的目录来,这里大致有两种方式,第一种:

1. 第一种使用命令行手动构建一个maven结构的目录,当然我基本不会这样构建

   ```
   mvn archetype:generate -DgroupId=com.hnz.seckill -DartifactId=seckill -Dpackage=com.hnz.seckill -Dversion=1.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-webapp
   ```

   这里要注意的是使用`archetype:generate`进行创建,在Maven老版本中是使用`archetype:create`,现在这种方法已经被弃用了,所以使用命令行创建的话注意了,稍微解释下这段语句的意思,就是构建一个一个`maven-archetype-webapp`骨架的Webapp项目,然后`groupId`为`com.hnz.seckill `,`artifactId`为`seckill`,这里是Maven相关知识,可以按照自己的情况进行修改  

2. 第二种直接在IDE中进行创建,这里以IDEA为例

   - 点击左上角`File>New>Project>Maven`
   - 然后在里面勾选`Create from archetype`,然后再往下拉找到`org.apache.cocoon:cocoon-22-archetype-webapp`,选中它,注意要先勾选那个选项,否则选择不了,然后点击`Next`继续  
     ![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-001.png)
     - 然后就填写你的Maven的那几个重要的坐标了,自己看着填吧  
       ![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-002.png)
     - 再就配置你的Maven的相关信息,默认应该是配置好的  
       ![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-003.png)
      - 之后就是点`Finsh`,到此不出意外的话就应该创建成功了

#### 构建pom文件

  项目基本的骨架我们就创建出来了,接下来我们要添加一些基本的JAR包的依赖,也就是在`pom.xml`中添加各种开源组件的三坐标了    

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.hnz.ssm</groupId>
    <artifactId>ssm</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>ssm Maven Webapp</name>
    <!-- FIXME change it to the project's website -->
    <url>http://www.example.com</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <!--4.11使用注解方式-->
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
        <!--补全项目依赖-->
        <!--1：日志 java日志：slf4j（规范/接口），log4j、logback、common-logging（日志实现）-->
        <!--配置日志相关,日志门面使用slf4j,日志的具体实现由logback实现-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.12</version>
        </dependency>
        <!--实现slf4j接口并整合-->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.1.7</version>
        </dependency>
        <!--2：数据库相关依赖-->
        <!--首先导入连接Mysql数据连接-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.32</version>
        </dependency>
        <!--导入数据库连接池-->
        <dependency>
            <groupId>c3p0</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.1.2</version>
        </dependency>
        <!--导入mybatis依赖-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.5</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.3.1</version>
        </dependency>
        <!--导入Servlet web相关的依赖-->
        <!--jsp标签-->
        <dependency>
            <groupId>taglibs</groupId>
            <artifactId>standard</artifactId>
            <version>1.1.2</version>
        </dependency>
        <dependency>
            <groupId>jstl</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>
        <!--spring默认的json转换-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.6.7</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
        </dependency>

        <!--导入spring相关依赖-->
        <!--spring核心依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>4.3.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>4.3.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.6.RELEASE</version>
        </dependency>
        <!--spring dao层依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>4.3.6.RELEASE</version>
        </dependency>
        <!--spring web依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>4.3.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <!--导入springTest-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>4.2.7.RELEASE</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>ssm</finalName>
        <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
            <plugins>
                <plugin>
                    <artifactId>maven-clean-plugin</artifactId>
                    <version>3.1.0</version>
                </plugin>
                <!-- see http://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_war_packaging -->
                <plugin>
                    <artifactId>maven-resources-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.8.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>2.22.1</version>
                </plugin>
                <plugin>
                    <artifactId>maven-war-plugin</artifactId>
                    <version>3.2.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-install-plugin</artifactId>
                    <version>2.5.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-deploy-plugin</artifactId>
                    <version>2.8.2</version>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```

#### 建立数据库

在根目录下有一个[sql](sql)文件夹里面有一个[sql数据库脚本](sql/seckill.sql),如果你不想自己手写的话就直接导入到你的数据库里面去吧,不过还是建议自己手写一遍加深印象

```sql
-- 整个项目的数据库脚本
-- 开始创建一个数据库
CREATE DATABASE seckill;
-- 使用数据库
USE seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
  `seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
  `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
  `number` INT NOT NULL COMMENT '库存数量',
  `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '秒杀开启的时间',
  `end_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '秒杀结束的时间',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建的时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)ENGINE =INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 插入初始化数据

INSERT INTO
  seckill(NAME,number,start_time,end_time)
VALUES
  ('1000元秒杀iPhoneX',100,'2019-5-22 00:00:00','2019-5-23 00:00:00'),
  ('500元秒杀iPad2',200,'2019-5-22 00:00:00','2019-5-23 00:00:00'),
  ('300元秒杀小米7',300,'2019-5-22 00:00:00','2019-5-23 00:00:00'),
  ('200元秒杀红米note',400,'2019-5-22 00:00:00','2019-5-23 00:00:00');

-- 秒杀成功明细表
-- 用户登录相关信息
CREATE TABLE success_killed(
  `seckill_id` BIGINT NOT NULL COMMENT '秒杀商品ID',
  `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标示:-1无效 0成功 1已付款',
  `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone), /*联合主键*/
  KEY idx_create_time(create_time)
)ENGINE =INNODB DEFAULT CHARSET =utf8 COMMENT ='秒杀成功明细表'

```

- 在建立数据库的,如果按照我这里的数据库脚本建立的话应该是没问题的,但是我按照视频里面的数据库脚本建表的话发生了一个错误  
  ![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-sqlError.png)
   这个报错看起来比较的诡异,我仔细检查`sql`也没有错误,它总提示我`end_time`要有一个默认的值,可我记得我以前就不会这样,然后视频里面也没有执行错误,然后我感觉可能时`MySQL`版本的差异,我查看了下我数据库版本,在登录`Mysql`控制台后输入指令,在控制台的我暂时知道的有两种方式:

```sql
select version();  
select @@version;
```

我的输出结果如下:
![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-mysqlVersion.png)
其实登录进控制台就已经可以看到版本了,我的Mysql是`5.7`的,以前我用的时`5.6`的,然后去`Google`上搜索了下,找到了几个答案,参考链接：  

- [Invalid default value for 'create_date' timestamp field
  
  ](https://stackoverflow.com/questions/9192027/invalid-default-value-for-create-date-timestamp-field)  
- [mysql官方的解释](https://dev.mysql.com/doc/refman/5.7/en/sql-mode.html#sqlmode_no_zero_date)  
- [MySQL Community 5.7 - Invalid default value (datetime field type)
  
  ](https://stackoverflow.com/questions/34570611/mysql-community-5-7-invalid-default-value-datetime-field-type)  
  总结出来一句话就是:

> mysql 5.7中,默认使用的是严格模式,这里的日期必须要有时间,所以一定要给出默认值,要么就修改数据库设置  

然后网友评论里总结出来的几种解决办法,未经测试！：  

- 下次有问题一定要先看一下评论！！！create不了的同学,可以这样写：

```sql
    `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
    `end_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
```

- 关于timestamp的问题,需要先运行 set explicit_defaults_for_timestamp = 1,否则会报invalid default value错误
- 还需要注意的是SQL版本的问题会导致视频中seckill表创建会出错。只要将create_time放在start_time和end_time之前是方便的解决方法。  

 对比下我修改过后的跟视频里面的`sql`片段:
![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-sqlCompare.png)
 我们可以看到在这三个字段有一个小差别,那就是给`start_time`,`end_time`,`create_time`三个字段都添加一个默认值,然后执行数据库语句就没问题了

------

#### 这里我们需要修改下`web.xml`中的servlet版本为`3.0`

打开`WEB-INF`下的`web.xml`,修改为以下代码:

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                      http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0"
         metadata-complete="true">
    <!--用maven创建的web-app需要修改servlet的版本为3.0-->
```

修改的原因有以下几点:  

- 高版本的Servlet支持更多的特性,更方便我们的Coding,特别是支持注解这一特性
- 在`Servlet2.3`中新加入了`Listener`接口的实现,,我们可以使用`Listener`引入`Spring`的`ContextLoaderListener`  

举个栗子:  

- 在`Servlet2.3`以前我们这样配置`ContextLoaderListener`:

```xml
<servlet>
 <servlet-name>context</servlet-name>
 <servlet-class>org.springframework.context.ContextLoaderServlet</servlet-class>
 <load-on-startup>1</load-on-startup>
</servlet>
```

- 在`Servlet2.3`以后可以使用`Listener`配置,也就是我们项目中使用的方法

```xml
<listener>
 <listener-class>org.springframework.context.ContextLoaderListener</listener-class>
</listener>

```

两种方法的效果都是一样的,主要不要同时使用,否则会报错的  

#### 建立实体类

- 首先建立`SuccessKilled`  秒杀状态表

```java
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


```

- 再建立`Seckill` 秒杀商品信息

```java
package org.seckill.entity;

import java.util.Date;

/**
 * 秒杀库存实体类
 */
public class Seckill {
    private long seckillId; //商品库存ID
    private String name;    //商品名称
    private int number;     //库存数量
    private Date startTime; //秒杀开启的时间
    private Date endTime;   //秒杀结束的时间
    private Date createTime;//创建的时间

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Seckill{" +
                "seckillId=" + seckillId +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}


```

#### 对实体类创建对应的`mapper`接口,也就是`dao`接口类

- 首先创建`SeckillDao`,在我这里位于`org.seckill.dao`包下

```java
package org.seckill.dao;

import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * 秒杀库存dao
 */
public interface SeckillDao {
    /**
     * 根据传过来的<code>seckillId</code>去减少商品的库存.
     *
     * @param seckillId 秒杀商品ID
     * @param killTime  秒杀的精确时间
     * @return 如果秒杀成功就返回1, 否则就返回0
     */
    int reduceNumber(long seckillId, Date killTime);

    /**
     * 根据传过来的<code>seckillId</code>去查询秒杀商品的详情.
     *
     * @param seckillId 秒杀商品ID
     * @return 对应商品ID的的数据
     */
    Seckill queryById(long seckillId);

    /**
     * 根据一个偏移量去查询秒杀的商品列表.
     *
     * @param offset 偏移量
     * @param limit  限制查询的数据个数
     * @return 符合偏移量查出来的数据个数
     */
    List<Seckill> queryAll(int offset, int limit);
}


```

#### 接下来书写`xml`配置文件

##### 建立对应的`mapper xml`  

首先在`src/main/resources`建立`org.seckill.dao`这个包,也就是对应`mapper`接口文件包一样的包名,这样符合Maven的约定,就是资源放置在`Resource`包下,`Java`包下则是放置`java`类文件,编译后最后还是会在同一个目录下.  
![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-004.png)

- 首先建立`SeckillDao.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.seckill.dao.SeckillDao">
    <!--目的：为DAO接口方法提供sql语句配置-->
    <!--这里的<=需要使用进行忽略,所以是要进行忽略,使用CDATA 区段中的文本会被解析器忽略 -->
    <!--不允许出现<=符号，与关键字有冲突，所有要通过xml格式的CDATA这个<=不是xml语法-->
    <update id="reduceNumber">
        UPDATE seckill
        SET number = number - 1
        WHERE seckill_id = #{seckillId}
              AND start_time <![CDATA[ <= ]]> #{killTime}
              AND end_time >= #{killTime}
              AND number > 0
    </update>

    <select id="queryById" resultType="Seckill" parameterType="long">
        SELECT *
        FROM seckill AS s
        WHERE s.seckill_id = #{seckillId}
    </select>

    <select id="queryAll" resultType="Seckill">
        SELECT *
        FROM seckill AS s
        ORDER BY create_time DESC
        LIMIT #{offset}, #{limit}
    </select>
</mapper>

```

- 建立`SuccessKilledDao.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.seckill.dao.SuccessKilledDao">
    <!--添加主键冲突时忽略错误返回0-->
    <!--主键冲突忽略，sql错误还是会报错-->
    <insert id="insertSuccessKilled">
        INSERT IGNORE INTO success_killed (seckill_id, user_phone, state)
        VALUES (#{seckillId}, #{userPhone}, 0)
    </insert>
    <!--根据seckillId查询SuccessKilled对象,并携带Seckill对象,告诉mybatis把映射结果映射到SuccessKill属性同时映射到Seckill属性-->
    <!--使用mybatis可以自由控制sql-->
    <select id="queryByIdWithSeckill" resultType="SuccessKilled">
        SELECT
            sk.seckill_id,
            sk.user_phone,
            sk.create_time,
            sk.state,
            s.seckill_id  "seckill.seckill_id",
            s.name "seckill.name",
            s.number "seckill",
            s.start_time  "seckill.start_time",
            s.end_time  "seckill.end_time",
            s.create_time "seckill.create_time"
        FROM success_killed sk
            INNER JOIN seckill s ON sk.seckill_id = s.seckill_id
        WHERE sk.seckill_id = #{seckillId}
    </select>

</mapper>
  

```

- 建立`Mybatis`的配置文件`mybatis-config.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC
        "-//mybatis.org//DTD MyBatis Generator Configuration 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd" >
<configuration>
    <!--首先配置全局属性-->
    <settings>
        <!--开启自动填充主键功能,原理时通过jdbc的一个方法getGeneratekeys获取自增主键值-->
        <setting name="useGeneratedKeys" value="true"/>
        <!--使用别名替换列名,默认就是开启的-->
        <!--select name as title from table-->
        <setting name="useColumnLabel" value="true"/>
        <!--开启驼峰命名的转换-->
        <!--Table(create_time) -> Entity(createTime)-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>

```

- 然后建立连接数据库的配置文件`jdbc.properties`,这里的属性要根据自己的需要去进行修改,切勿直接复制使用
![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-005.png)

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.user=root
jdbc.password=123456
jdbc.url=jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf-8
```

- 建立`Spring`的`dao`的配置文件,在`resources`包下创建`spring/spring-dao.xml`
![在这里插入图片描述](https://raw.githubusercontent.com/DiCaprio17/DiCaprio17.github.io/master/img/2019-08-19-SSM框架高并发和商品秒杀项目（IDEA）-006.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--suppress SpringFacetInspection -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd">
    <!--配置整合mybatis过程-->
    <!--1：配置数据库相关参数-->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!--2：配置数据库连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <!--配置基本的数据库连接-->
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.user}"/>
        <property name="password" value="${jdbc.password}"/>
        <!--c3p0私有属性-->
        <property name="maxPoolSize" value="30"/>
        <property name="minPoolSize" value="10"/>
        <!--关闭连接后不自动commit-->
        <property name="autoCommitOnClose" value="false"/>
        <!--获取连接超时时间-->
        <property name="checkoutTimeout" value="1000"/>
        <!--当获取连接失败时的重试次数-->
        <property name="acquireRetryAttempts" value="2"/>
    </bean>

    <!--3：配置sqlSessionFactory对象-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--注入数据库连接池-->
        <property name="dataSource" ref="dataSource"/>
        <!--配置mybatis全局配置文件-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <!--配置entity包,也就是实体类包,自动扫描,用于别名配置 org.seckill.entity.Seckill -> Seckill-->
        <property name="typeAliasesPackage" value="org.seckill.entity"/>
        <!--配置需要扫描sql配置文件：mapper需要的xml文件-->
        <property name="mapperLocations" value="classpath*:mapper/*.xml"/>
    </bean>

    <!--4：配置mapper接口包,动态实现mapper接口,注入到Spring容器-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--注入sqlSessionFactory,请注意不要使用sqlSessionFactoryBean,否则会出现注入异常，
        sqlSessionFactoryBeanName后处理，当用到mybatis的时候才回去找sqlSessionFactory，
        防止提前初始化sqlSessionFactory，如jdbc.properties未加载-->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!--给出要扫描的mapper接口-->
        <property name="basePackage" value="org.seckill.dao"/>
    </bean>
</beans>


```

- 基础的部分我们搭建完成了,然后要开始测试了
  在`IDEA`里面有一个快速建立测试的快捷键`Ctrl+Shift+T`,在某个要测试的类里面按下这个快捷键就会出现`Create new Test`,然后选择你要测试的方法跟测试的工具就可以了,这里我们使用Junit作为测试
  - 建立`SeckillDaoTest`文件,代码如下

```java
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

```

测试中可能会出现`Mybatis`参数绑定失败的错误,在`mapper`接口中的方法里面添加`@Param`的注解,显示的告诉`Mybatis`参数的名称是什么,例如

```java
 List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

```

修改后的`SeckillDao`，queryByIdWithSeckill

```java
package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * 秒杀库存dao
 */
public interface SeckillDao {
    /**
     * 根据传过来的<code>seckillId</code>去减少商品的库存.
     *
     * @param seckillId 秒杀商品ID
     * @param killTime  秒杀的精确时间
     * @return 如果秒杀成功就返回1, 否则就返回0
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据传过来的<code>seckillId</code>去查询秒杀商品的详情.
     *
     * @param seckillId 秒杀商品ID
     * @return 对应商品ID的的数据
     */
    Seckill queryById(long seckillId);

    /**
     * 根据一个偏移量去查询秒杀的商品列表.
     *
     * @param offset 偏移量
     * @param limit  限制查询的数据个数
     * @return 符合偏移量查出来的数据个数
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}

```

修改后的`SuccessKilledDao`，这里`queryByIdWithSeckill`方法需要两个参数才能查询到唯一结果，因此也进行修改

```java
package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * 秒杀成功明细实体类
 */
public interface SuccessKilledDao {
    /**
     * 插入一条详细的购买信息.
     *
     * @param seckillId 秒杀商品的ID
     * @param userPhone 购买用户的手机号码
     * @return 成功插入就返回1, 否则就返回0
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据秒杀商品的ID查询<code>SuccessKilled</code>的明细信息.
     *
     * @param seckillId 秒杀商品的ID
     * @param seckillId 用户手机号
     * @return 秒杀商品的明细信息
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}


```

同时`SuccessKilledDao.xml`的sql语句也要修改这部分

```
WHERE sk.seckill_id = #{seckillId} AND sk.user_phone= #{userPhone}
```

`SuccessKilledDaoTest `测试类

```java
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

```

### 总结

- DAO层工作演变为:接口设计+SQL编写
- 代码和SQL的分离,方便Review
- DAO拼接等逻辑在service层完成