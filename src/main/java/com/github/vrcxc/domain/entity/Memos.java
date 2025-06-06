package com.github.vrcxc.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * memos
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "memos")
public class Memos extends VRCXCBaseEntity {
    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "edited_at")
    private String editedAt;

    @TableField(value = "memo")
    private String memo;
}