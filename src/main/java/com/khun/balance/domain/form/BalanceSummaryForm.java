package com.khun.balance.domain.form;

import com.khun.balance.domain.entity.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class BalanceSummaryForm implements Serializable {
    private Long id;
    @NotNull(message = "Enter date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotBlank(message = "Enter category")
    private String category;
    private Type type;
}

