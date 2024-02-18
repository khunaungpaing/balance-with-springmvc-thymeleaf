package com.khun.balance.domain.form;

import lombok.Data;

@Data
public class BalanceItemForm {
    private Long id;
    private String item;
    private int unitPrice;
    private int quantity;
}
