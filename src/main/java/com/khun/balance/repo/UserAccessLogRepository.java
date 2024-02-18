package com.khun.balance.repo;

import com.khun.balance.domain.entity.UserAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAccessLogRepository extends JpaRepository<UserAccessLog,Long>, JpaSpecificationExecutor<UserAccessLog> {

}
