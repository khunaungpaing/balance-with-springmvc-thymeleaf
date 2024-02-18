package com.khun.balance.repo;

import com.khun.balance.domain.entity.User;
import com.khun.balance.domain.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByLoginId(String loginId);
    Optional<User> findByLoginIdOrEmail(String username, String email);

}
