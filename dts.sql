
drop database if exists `dts`;

create database `dts`;

use dts;

--厂区设置
create table area (
	id int(10) not null auto_increment comment "自增id",
	name varchar(50) not null comment "分区名字",
	level tinyint(3) not null default 0 comment "第几层：0-厂区，1-缆沟，2-分区",
	`index` tinyint(3) not null default 0 comment "报警区域",
	parent int(10) not null default 0 comment "父级id",
	image varchar(1024) comment "自定义背景图片",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	index apid (parent)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table level (
	id int(10) not null auto_increment comment "自增id",
	level tinyint(3) not null default 0 comment "第几层：0-厂区，1-电缆沟，2-桥架，3-配电室，4-区段，5-电缆夹层，6-开关柜",
	name varchar(50) not null comment "分区名字：0-厂区，1-电缆沟，2-桥架，3-配电室，4-区段，5-电缆夹层，6-开关柜",
	image varchar(1024) comment "自定义背景图片",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table area_hardware_config (
	id int(10) not null auto_increment comment "自增id",
	area_id int(10) not null comment "分区id",
	light varchar(10) not null comment "灯号",
	relay varchar(10) not null comment "继电器号",
	voice varchar(10) not null comment "声音地址",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	unique index ahcaid (area_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table area_channel (
	id int(10) not null auto_increment  comment "自增id",
	name varchar(50) not null comment "通道报警显示名字",
	area_id int(10) not null comment "分区id",
	channel_id int(10) not null default 0 comment "下属通道数据库唯一id",
	start int(10) not null default 0 comment "下属通道开始距离",
	end int(10) not null default 0 comment "下属通道结束距离",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	index acname (name),
	index acaid (area_id),
	index accid (channel_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table area_temp_config (
	id int(10) not null auto_increment  comment "自增id",
	area_id int(10) not null comment "分区id",
	temperature_low int(10) not null default 99999 comment "定温1",
	temperature_high int(10) not null default 99999 comment "定温2",
	exotherm int(10) not null default 99999 comment "温升",
	temperature_diff int(10) not null default 99999 comment "差温",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	unique index atcaid (area_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--通道设置
create table channel (
	id int(10) not null auto_increment comment "自增id",
	machine_id int(10) not null comment "机器数据库唯一id",
	name varchar(50) not null comment "通道名字",
	length int(10) not null default 0 comment "通道总长度",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	index cmid (machine_id),
	index cname (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--机器设置
create table machine (
	id int(10) not null auto_increment comment "自增id",
	name varchar(50) not null comment "机器名字",
	serial_port varchar(50) not null default 0 comment "串口号",
	baud_rate varchar(50) not null default 0 comment "波特率",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	index mname (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--软件使用权限设置
create table license (
	id int(10) not null auto_increment comment "自增id",
	mac varchar(20) not null comment "mac",
	use_time bigint(20) not null default 0 comment "已经使用期限，以秒为单位",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--系统配置
create table config (
	id int(10) not null auto_increment comment "自增id",
	type tinyint(3) not null comment "配置类型",
	value bigint(20) not null comment "配置值",
	intr varchar(20) comment "配置信息",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid int(10) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	index ctype (type)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--lifetime
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(1, -1, "lifetime", now(), 1, 0);
--保存stock数据：0-不保存，1-保存
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(2, 0, "保存stock数据", now(), 1, 0);
--保存refertem数据：0-不保存，1-保存
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(3, 0, "保存refertem数据", now(), 1, 0);
--备份数据间隔：以分钟为单位
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(4, 1, "备份数据间隔", now(), 1, 0);
--前后端更新频率：以秒为单位
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(5, 10, "前后端更新频率", now(), 1, 0);

--日志：type：1-web系统启动，2-web系统关闭，3-用户登录，4-用户登出，5-数据采集启动，6-数据采集关闭
create table log (
	id bigint(20) not null auto_increment comment "自增id",
	type int(10) not null comment "类型",
	value varchar(50) not null comment "值",
	source varchar(50) not null comment "来源",
	lastmod_time datetime not null comment "上次修改时间",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	index logtime (lastmod_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- temp table
CREATE TABLE `temperature` (
	`channel` int(10) NOT NULL,
	`tem` MEDIUMTEXT NOT NULL,
	`stock` MEDIUMTEXT DEFAULT NULL,
	`unstock` MEDIUMTEXT DEFAULT NULL,
	`refer_tem` double DEFAULT NULL,
	`date` datetime NOT NULL,
	`status` tinyint(3) not null default 0 comment "0: new, 1: alarm checked",
  KEY `tm_key` (`channel`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
CREATE TABLE `temperature_tmp` (
	`channel` int(10) NOT NULL,
	`tem` MEDIUMTEXT NOT NULL,
	`stock` MEDIUMTEXT DEFAULT NULL,
	`unstock` MEDIUMTEXT DEFAULT NULL,
	`refer_tem` double DEFAULT NULL,
	`date` datetime NOT NULL,
  KEY `tm_key` (`channel`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
CREATE TABLE `temperature_event` (
	`channel` int(10) NOT NULL,
	`tem` MEDIUMTEXT NOT NULL,
	`stock` MEDIUMTEXT DEFAULT NULL,
	`unstock` MEDIUMTEXT DEFAULT NULL,
	`refer_tem` double DEFAULT NULL,
	`date` datetime NOT NULL,
  KEY `tm_key` (`channel`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
CREATE TABLE `temperature_log` (
	`channel` int(10) NOT NULL,
	`tem` MEDIUMTEXT NOT NULL,
	`stock` MEDIUMTEXT DEFAULT NULL,
	`unstock` MEDIUMTEXT DEFAULT NULL,
	`refer_tem` double DEFAULT NULL,
	`date` datetime NOT NULL,
  KEY `tm_key` (`channel`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

--用户
create table user (
	id bigint(20) not null auto_increment comment "自增id",
	name varchar(50) not null comment "名字&登录名",
	password varchar(50) not null comment "密码",
	role int(10) not null comment "角色：1 super admin, 2 system admin, 3 super user, 4 normal user",
	area_ids varchar(255) not null comment "查询厂区id",
	lastmod_time datetime not null comment "上次修改时间",
	lastmod_userid bigint(20) not null comment "上次修改人",
	isdel tinyint(3) not null default 0 comment "是否删除",
	primary key(id),
	index uname (name, password)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;