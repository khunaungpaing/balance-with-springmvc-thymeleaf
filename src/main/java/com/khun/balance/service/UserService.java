package com.khun.balance.service;

import com.khun.balance.domain.entity.Role;
import com.khun.balance.domain.entity.User;
import com.khun.balance.domain.form.ChangePasswordForm;
import com.khun.balance.domain.form.SignUpForm;
import com.khun.balance.domain.vo.UserVo;
import com.khun.balance.exception.BalanceAppException;
import com.khun.balance.exception.WrongPasswordException;
import com.khun.balance.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpForm form) {
        userRepository.save(User.builder().name(form.getName()).loginId(form.getLoginId()).password(passwordEncoder.encode(form.getPassword())).role(Role.Member).active(true).build());
    }

    public UserVo findByLoginId(String username) {
        return userRepository.findByLoginId(username).map(UserVo::new).orElseThrow();
    }

    @Transactional
    public void updateContact(String username, String phone, String email) {
        userRepository.findByLoginId(username).ifPresent(user -> {
            user.setPhone(phone);
            user.setEmail(email);
        });
    }

    public List<UserVo> search(Boolean status, String name, String phone) {
        var spec = isMember();
        if(null != status){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"),status));
        }
        if(StringUtils.hasLength(name)){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),name.toLowerCase().concat("%")));
        }
        if(StringUtils.hasLength(phone)){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("phone"),phone.concat("%")));
        }
        return userRepository.findAll(spec).stream().map(UserVo::new).toList();
    }

    private Specification<User> isMember(){
         return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"),Role.Member);
    }

    @Transactional
    public void changeStatus(Long id, boolean status) {
        userRepository.findById(id).ifPresent(user -> user.setActive(status));
    }

    @Transactional
    public void changePassword(ChangePasswordForm form) {
        if(!StringUtils.hasLength(form.getOldPassword())){
            System.out.println("hellll");
            throw new BalanceAppException("Please enter old password");
        }
        if(!StringUtils.hasLength(form.getNewPassword())){
            System.out.println("heee");
            throw new BalanceAppException("Please enter new password");
        }
        if(form.getNewPassword().equals(form.getOldPassword())){
            System.out.println("wefwef");
            throw new BalanceAppException("old and new password must not be same");
        }
        var user = userRepository.findByLoginId(form.getLoginId()).orElseThrow();
        if(!passwordEncoder.matches(form.getOldPassword(),user.getPassword())){
            throw new BalanceAppException("Please check your old password");
        }
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
    }
}
