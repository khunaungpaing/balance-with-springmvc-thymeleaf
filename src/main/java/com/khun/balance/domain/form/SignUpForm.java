package com.khun.balance.domain.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpForm {
    @NotBlank(message = "Enter member name")
    private String name;
    @NotBlank(message = "Enter login ID")
    private String loginId;
    @NotBlank(message = "Enter password")
    private String password;
}
