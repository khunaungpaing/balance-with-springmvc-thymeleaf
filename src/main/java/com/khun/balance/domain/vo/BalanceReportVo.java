package com.khun.balance.domain.vo;

import com.khun.balance.domain.entity.Balance;
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

    public BalanceReportVo(Balance entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.type = entity.getType();
        this.category = entity.getCategory();
        this.amount = entity.getItems().stream().mapToInt(a -> a.getQuantity() * a.getUnitPrice()).sum();
    }


    public int getIncome() {
        return type == Type.Income ? amount : 0;
    }

    public int getExpense() {
        return type == Type.Expense ? amount : 0;
    }
}
