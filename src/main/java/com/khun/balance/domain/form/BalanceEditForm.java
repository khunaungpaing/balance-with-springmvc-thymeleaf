package com.khun.balance.domain.form;

import lombok.Data;

import java.util.List;

@Data
public class BalanceEditForm {
    private BalanceSummaryForm header;
    private List<BalanceItemForm> items;
    public int getTotal(){
        return 0;
    }
}
