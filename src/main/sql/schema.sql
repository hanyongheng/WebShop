-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;

-- 使用数据库
use seckill;

-- 创建秒杀库存表 MYSQL支持事务的引擎是 InnoDB 要显示的指定
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR (120) NOT NULL COMMENT '商品名称',
`number` INT NOT NULL COMMENT '库存数量',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`start_time` DATE NOT NULL COMMENT '秒杀开始时间',
`end_time` DATE NOT NULL  COMMENT '秒杀结束时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--初始化数据
INSERT INTO
    seckill(name,number,start_time,end_time)
VALUES
   ('1000元秒杀iphone6',100,'2016-10-10 00:00:00','2016-08-25 00:00:00'),
   ('500元秒杀ipad2',200,'2016-10-10 00:00:00','2016-08-25 00:00:00'),
   ('300元秒杀小米4',300,'2016-10-10 00:00:00','2016-08-25 00:00:00'),
   ('200元秒杀红米note',400,'2016-10-10 00:00:00','2016-08-25 00:00:00');

UPDATE seckill
  SET end_time=CASE seckill_id
      WHEN 1000 THEN '2016-10-11 00:00:00'
      WHEN 1001 THEN '2016-10-11 00:00:00'
      WHEN 1002 THEN '2016-10-11 00:00:00'
      WHEN 1003 THEN '2016-10-11 00:00:00'
      END
  WHERE seckill_id IN (1000,1001,1002,1003);
-- 秒杀成功明细表
-- 用户登录认证相关信息
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品ID',
`user_phone` bigint NOT NULL COMMENT '用户手机号码',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识 -1: 无效 0:成功 1:已付款',
`create_time` TIMESTAMP NOT NULL COMMENT '秒杀成功创建时间',
PRIMARY KEY (seckill_id,user_phone),/* 联合主键 seckill_id 和user_phone组合成一个联合主键 防止重复秒杀的过滤*/
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';