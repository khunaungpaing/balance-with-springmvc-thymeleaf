package com.khun.balance.domain.vo;

import com.khun.balance.domain.entity.Role;
import com.khun.balance.domain.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVo {
    private Long id;
    private String name;
    private String loginId;
    private String phone;
    private String email;
    private boolean status;
    public UserVo(User entity){
        this.id = entity.getId();
        this.loginId = entity.getLoginId();
        this.name = entity.getName();
        this.phone = entity.getPhone();
        this.email = entity.getEmail();
        this.status = entity.isActive();
    }
}
