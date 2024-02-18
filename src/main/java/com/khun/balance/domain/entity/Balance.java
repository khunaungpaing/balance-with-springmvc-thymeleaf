package com.khun.balance.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
public class Balance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private String category;
    private Type type;
    @ManyToOne(optional = false,cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private User user;

}
