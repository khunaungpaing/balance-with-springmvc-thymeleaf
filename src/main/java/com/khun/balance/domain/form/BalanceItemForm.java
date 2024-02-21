package com.khun.balance.domain.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BalanceItemForm {
    private Long id;
    @NotBlank(message = "Enter Item Name")
    private String item;
    @Min(value = 1, message = "Enter Price")
    private int unitPrice;
    @Min(value = 1, message = "Enter Quantity")
    private int quantity;
    private boolean deleted;
}
