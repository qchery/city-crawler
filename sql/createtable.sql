drop table if exists sys_area;

create table sys_area (
id int comment 'ID',
name varchar(20) not null comment '名称',
code varchar(15) comment '行政代码',
`level` tinyint comment '行政级别 1：省、2：市、3：区',
parent_id int comment '父级地区ID',
parent_name varchar(20) comment '父级地区名称',
primary key(id)
) comment '行政区划表';

create index idx_sa_name on sys_area(name);
create index idx_sa_parent_id on sys_area(parent_id);