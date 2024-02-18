package com.khun.balance.domain.form;

import lombok.Data;

@Data
public class ChangePasswordForm {
    private String loginId;
    private String oldPassword;
    private String newPassword;
}
