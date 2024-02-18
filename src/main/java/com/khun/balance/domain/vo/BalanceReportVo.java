package com.khun.balance.domain.vo;

import com.khun.balance.domain.entity.Type;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BalanceReportVo {
    private Long id;
    private LocalDate date;
    private Type type;
    private String category;
    private int amount;
    private int balance;

}
