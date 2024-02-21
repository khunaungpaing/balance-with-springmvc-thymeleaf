package com.khun.balance.domain.entity;

import com.khun.balance.domain.form.BalanceSummaryForm;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Balance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private String category;
    private Type type;
    @ManyToOne(optional = false,cascade = {CascadeType.PERSIST,CascadeType.MERGE })
    private User user;
    @OneToMany(mappedBy = "balance",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true)
    private List<BalanceItem> items;

    public Balance(){
        items = new ArrayList<>();
    }

    public void addItem(BalanceItem item){
        item.setBalance(this);
        items.add(item);
    }
}
