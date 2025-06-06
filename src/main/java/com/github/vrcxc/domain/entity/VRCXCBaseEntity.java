package com.github.vrcxc.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VRCXCBaseEntity extends BaseEntity {

    private String deviceId;

}
