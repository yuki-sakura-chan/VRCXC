package com.github.vrcxc.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountDto {

    @NotEmpty(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度为4-16")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 8, max = 32, message = "密码长度为8-32")
    private String password;

}
