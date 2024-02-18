package com.khun.balance.domain.vo;

import com.khun.balance.domain.entity.Type;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BalanceSummaryVo {
   private Long id;
   private LocalDate date;
   private Type type;
   private String category;
}
