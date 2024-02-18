package com.khun.balance.domain.vo;

import com.khun.balance.domain.form.BalanceItemForm;
import lombok.Data;

import java.util.List;

@Data
public class BalanceVo {
    private BalanceSummaryVo header;
    private List<BalanceItemForm> items;
    public int getTotal(){
        return 0;
    }
}
