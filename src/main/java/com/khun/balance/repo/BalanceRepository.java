package com.khun.balance.repo;

import com.khun.balance.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance,Long>, JpaSpecificationExecutor<Balance> {
}
