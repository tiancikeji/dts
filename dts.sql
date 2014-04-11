
drop database if exists `dts`;

create database `dts`;

use dts

--��������
create table area (
	id int(10) not null auto_increment comment "����id",
	name varchar(50) not null comment "��������",
	level tinyint(3) not null default 0 comment "�ڼ��㣺0-����1-�¹���2-����",
	`index` tinyint(3) not null default 0 comment "��������",
	parent int(10) not null default 0 comment "����id",
	image varchar(1024) comment "�Զ��屳��ͼƬ",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index apid (parent)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table level (
	id int(10) not null auto_increment comment "����id",
	name varchar(50) not null comment "�������֣�0-����1-���¹���2-�żܣ�3-����ң�4-��Σ�5-���¼в㣬6-���ع�",
	image varchar(1024) comment "�Զ��屳��ͼƬ",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table area_hardware_config (
	id int(10) not null auto_increment comment "����id",
	area_id int(10) not null comment "����id",
	relay1 varchar(10) not null comment "Ԥ���̵�����",
	light varchar(10) not null comment "�ƺ�",
	relay varchar(10) not null comment "�̵�����",
	voice varchar(10) comment "������ַ",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index ahcaid (area_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table area_channel (
	id int(10) not null auto_increment  comment "����id",
	name varchar(50) not null comment "ͨ��������ʾ����",
	area_id int(10) not null comment "����id",
	channel_id int(10) not null default 0 comment "����ͨ����ݿ�Ψһid",
	start int(10) not null default 0 comment "����ͨ����ʼ����",
	end int(10) not null default 0 comment "����ͨ���������",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index acname (name),
	index acaid (area_id),
	index accid (channel_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table area_temp_config (
	id int(10) not null auto_increment  comment "����id",
	area_id int(10) not null comment "����id",
	temperature_low int(10) not null default 99999 comment "����1",
	temperature_high int(10) not null default 99999 comment "����2",
	exotherm int(10) not null default 99999 comment "����",
	temperature_diff int(10) not null default 15 comment "����",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index atcaid (area_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--ͨ������
create table channel (
	id int(10) not null auto_increment comment "����id",
	machine_id int(10) not null comment "������ݿ�Ψһid",
	name varchar(50) not null comment "ͨ������",
	length int(10) not null default 0 comment "ͨ���ܳ���",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index cmid (machine_id),
	index cname (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--��������
create table machine (
	id int(10) not null auto_increment comment "����id",
	name varchar(50) not null comment "��������",
	serial_port varchar(50) not null default 0 comment "���ں�",
	baud_rate varchar(50) not null default 0 comment "������",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index mname (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--���ʹ��Ȩ������
create table license (
	id int(10) not null auto_increment comment "����id",
	mac varchar(20) not null comment "mac",
	use_time bigint(20) not null default 0 comment "�Ѿ�ʹ�����ޣ�����Ϊ��λ",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--ϵͳ����
create table config (
	id int(10) not null auto_increment comment "����id",
	type tinyint(3) not null comment "��������",
	value bigint(20) not null comment "����ֵ",
	intr varchar(20) comment "������Ϣ",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid int(10) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index ctype (type)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--lifetime
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(1, -1, "lifetime", now(), 1, 0);
--����stock��ݣ�0-�����棬1-����
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(2, 0, "����stock���", now(), 1, 0);
--����refertem��ݣ�0-�����棬1-����
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(3, 0, "����refertem���", now(), 1, 0);
--������ݼ�����Է���Ϊ��λ
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(4, 1, "������ݼ��", now(), 1, 0);
--ǰ��˸���Ƶ�ʣ�����Ϊ��λ
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(5, 10, "ˢ��ʱ��", now(), 1, 0);
--˹�п�˹����ֵ
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(6, 0, "˹�п�˹����ֵ", now(), 1, 0);
--��˹�п�˹����ֵ
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(7, 0, "��˹�п�˹����ֵ", now(), 1, 0);
--���¹���ֵ
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(8, 120, "���¹���ֵ", now(), 1, 0);
--���¹���ֵ
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(9, -40, "���¹���ֵ", now(), 1, 0);
--Ӳ��ʣ�������ٷֱ�
insert into config(type, value, intr, lastmod_time, lastmod_userid, isdel) values(10, 10, "Ӳ��ʣ�������ٷֱ�", now(), 1, 0);

--��־��type��1-webϵͳ������2-webϵͳ�رգ�3-�û���¼��4-�û��ǳ���5-��ݲɼ�������6-��ݲɼ��ر�
create table log (
	id bigint(20) not null auto_increment comment "����id",
	type int(10) not null comment "����",
	value varchar(50) not null comment "ֵ",
	source varchar(50) not null comment "��Դ",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
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

--�û�
create table user (
	id bigint(20) not null auto_increment comment "����id",
	name varchar(50) not null comment "����&��¼��",
	password_login varchar(50) not null comment "��¼����",
	password_reset varchar(50) not null comment "��λ����",
	password_logout varchar(50) not null comment "�˳�����",
	role int(10) not null comment "��ɫ��1 super admin, 2 system admin, 3 super user, 4 normal user",
	area_ids varchar(255) not null comment "��ѯ����id",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid bigint(20) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index uname (name, password_login)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- alarm
create table alarm (
	id bigint(20) not null auto_increment comment "����id",
	type tinyint(3) not null comment "�������ͣ�1Ԥ����2�𾯣�3���±�����4�������ʱ�����5���¹��ϣ�6���¹��ϣ�7˹�п�˹���ϣ�8��˹�п�˹���ϣ�9��ݴ洢���",
	machine_id int(10) default 0 comment "����Ψһid",
	machine_name varchar(50) default 0 comment "�������",
	channel_id int(10) default 0 comment "ͨ����ݿ�Ψһid",
	channel_name varchar(50) default 0 comment "ͨ�����",
	length int(10) comment "��������",
	area_id int(10) default 0 comment "����Ψһid",
	area_name varchar(50) default 0 comment "�������",
	alarm_name varchar(50) default 0 comment "�����������",
	light varchar(10) comment "�ƺ�",
	relay varchar(10) comment "�̵�����",
	relay1 varchar(10) comment "�̵�����",
	voice varchar(10) comment "������ַ",
	temperature double(10,2) comment "Ŀǰ�¶�",
	temperature_pre double(10,2) comment "�����¶�",
	temperature_max double(10,2) comment "����¶�",
	status tinyint(3) not null default 0 comment '״̬��0������1������2ȷ�ϣ�3������4������5��λ��6��λ��',
	add_time datetime not null comment "���ʱ��",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid bigint(20) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index acid (channel_id),
	index amid (machine_id),
	index aaid (area_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table alarm_history (
	alarm_id bigint(20) not null default 0 comment "alarmΨһid",
	operation tinyint(3) not null default 0 comment '2ȷ�ϣ�3������5��λ',
	add_time datetime not null comment "�޸�ʱ��",
	add_userid bigint(20) not null comment "�޸���",
	index ahaid (alarm_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `check` (
	id bigint(20) not null auto_increment comment "����id",
	machine_id int(10) default 0 comment "����Ψһid",
	channel_id int(10) default 0 comment "ͨ����ݿ�Ψһid",
	area_id int(10) default 0 comment "����Ψһid",
	light varchar(10) comment "�ƺ�",
	relay varchar(10) comment "�̵�����",
	relay1 varchar(10) comment "�̵�����",
	voice varchar(10) comment "������ַ",
	status tinyint(3) not null default 0 comment '״̬��0������1�Լ�',
	add_time datetime not null comment "���ʱ��",
	lastmod_time datetime not null comment "�ϴ��޸�ʱ��",
	lastmod_userid bigint(20) not null comment "�ϴ��޸���",
	isdel tinyint(3) not null default 0 comment "�Ƿ�ɾ��",
	primary key(id),
	index ccid (channel_id),
	index cmid (machine_id),
	index caid (area_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;