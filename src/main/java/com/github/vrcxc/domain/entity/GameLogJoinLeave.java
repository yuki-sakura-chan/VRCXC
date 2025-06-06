package com.github.vrcxc.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 游戏的加入和离开日志
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "game_log_join_leave")
public class GameLogJoinLeave extends VRCXCBaseEntity {
    @TableField(value = "created_at")
    @JsonProperty(value = "created_at")
    private String createdAt;

    @TableField(value = "`type`")
    private String type;

    @TableField(value = "display_name")
    private String displayName;

    @TableField(value = "`location`")
    private String location;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "`time`")
    private Integer time;
}