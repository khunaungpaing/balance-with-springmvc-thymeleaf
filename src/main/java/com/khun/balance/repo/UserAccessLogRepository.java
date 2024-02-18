package com.khun.balance.repo;

import com.khun.balance.domain.entity.UserAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessLogRepository extends JpaRepository<UserAccessLog,Long> {

}
