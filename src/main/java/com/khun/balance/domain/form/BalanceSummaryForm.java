package com.khun.balance.domain.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BalanceSummaryForm {
    private Long id;
    private LocalDate date;
    private String category;

}
