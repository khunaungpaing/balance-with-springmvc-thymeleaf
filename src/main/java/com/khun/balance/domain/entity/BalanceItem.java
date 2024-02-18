package com.khun.balance.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class BalanceItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String item;
    private int unitPrice;
    private int quantity;

    @ManyToOne(optional = false)
    private Balance balance;
}
