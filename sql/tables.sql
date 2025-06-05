CREATE TABLE IF NOT EXISTS `memos`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键',
    `user_id`     VARCHAR(255) UNIQUE,
    `edited_at`   TEXT,
    `memo`        TEXT,
    `create_time` DATETIME   NOT NULL COMMENT '创建时间',
    `creator`     BIGINT COMMENT '创建人',
    `update_time` DATETIME   NOT NULL COMMENT '更新时间',
    `updater`     BIGINT COMMENT '更新人',
    `deleted`     TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='memos';