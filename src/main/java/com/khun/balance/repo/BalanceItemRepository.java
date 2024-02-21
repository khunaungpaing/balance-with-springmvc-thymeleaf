package com.khun.balance.repo;

import com.khun.balance.domain.entity.BalanceItem;
import com.khun.balance.domain.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BalanceItemRepository extends JpaRepository<BalanceItem,Long>, JpaSpecificationExecutor<BalanceItem> {
    @Query("SELECT SUM(bi.unitPrice * bi.quantity) FROM BalanceItem bi WHERE bi.balance.id < :id AND bi.balance.type = :type")
    Optional<Number> getLastBalance(Long id, Type type);

    @Query("SELECT SUM(bi.unitPrice * bi.quantity) FROM BalanceItem bi WHERE bi.balance.id = :id AND bi.balance.type = :type")
    Optional<Number> getTotalBalance(Long id, Type type);

    @Query("SELECT SUM(bi.unitPrice * bi.quantity) FROM BalanceItem bi WHERE bi.balance.date = :date AND bi.balance.type = :type")
    Optional<Number> getTotalBalanceByDate(LocalDate date, Type type);
}
