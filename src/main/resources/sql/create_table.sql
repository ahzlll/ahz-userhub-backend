# 数据库初始化
# @author ahz
# @version 1.1

-- 创建库
create database if not exists ahz_userhub;

-- 切换库
use ahz_userhub;

# 用户表
create table user
(
    user_id      bigint auto_increment comment '用户id' primary key,
    username     varchar(256)                       null comment '用户昵称',
    user_account varchar(256)                       null comment '账号',
    avatar_url   varchar(1024)                      null comment '用户头像',
    gender       enum('male', 'female', 'unknown')  default 'unknown' comment '性别',
    password_hash varchar(60)                       not null comment '密码哈希(bcrypt)',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    status       enum('active', 'inactive', 'banned') default 'active' comment '状态',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_delete    tinyint  default 0                 not null comment '是否删除',
    user_role    enum('user', 'admin')              default 'user' comment '用户角色',
    index idx_email (email),
    index idx_phone (phone)
)
    comment '用户';
