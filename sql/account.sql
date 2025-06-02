CREATE TABLE `account`
(
    `id`              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `username`        VARCHAR(16)  NOT NULL UNIQUE COMMENT '用户名',
    `password`        VARCHAR(128) NOT NULL COMMENT '密码',
    `nickname`        VARCHAR(16)  NOT NULL COMMENT '昵称',
    `avatar`          VARCHAR(255) NOT NULL COMMENT '头像',
    `gender`          TINYINT(1)   NOT NULL COMMENT '性别',
    `age`             TINYINT(3) COMMENT '年龄',
    `email`           VARCHAR(32) COMMENT '邮箱',
    `phone`           VARCHAR(32) COMMENT '手机号',
    `status`          TINYINT(1) COMMENT '状态',
    `register_ip`     VARCHAR(16)  NOT NULL COMMENT '注册ip',
    `register_time`   DATETIME     NOT NULL COMMENT '注册时间',
    `last_login_ip`   VARCHAR(16)  NOT NULL COMMENT '最后登录ip',
    `last_login_time` DATETIME     NOT NULL COMMENT '最后登录时间',
    `create_time`     DATETIME     NOT NULL COMMENT '创建时间',
    `creator`         BIGINT COMMENT '创建人',
    `update_time`     DATETIME     NOT NULL COMMENT '更新时间',
    `updater`         BIGINT COMMENT '更新人',
    `deleted`         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';
INSERT INTO `account` (`username`, `password`, `nickname`, `avatar`, `gender`, `register_ip`, `register_time`,
                       `last_login_ip`, `last_login_time`, `create_time`, `update_time`)
VALUES ('admin', '{noop}123', '管理员', 'https://avatars0.g|ithubusercontent.com/u/21042571?s=460&v=4', 1, '127.0.0.1',
        NOW(), '127.0.0.1', NOW(), NOW(), NOW());

alter table `account`
    add index `idx_account_deleted` (`deleted`);
