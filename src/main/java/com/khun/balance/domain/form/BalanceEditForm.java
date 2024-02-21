package com.khun.balance.domain.form;

import com.khun.balance.domain.entity.Balance;
import com.khun.balance.domain.entity.BalanceItem;
import com.khun.balance.domain.entity.Type;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BalanceEditForm implements Serializable {
    private BalanceSummaryForm header;
    private List<BalanceItemForm> items;

    public BalanceEditForm() {
        this.header = new BalanceSummaryForm();
        this.items = new ArrayList<>();
    }

    public BalanceEditForm type(Type type) {
        this.header.setType(type);
        return this;
    }

    public BalanceEditForm(Balance balance) {
        header = new BalanceSummaryForm();
        header.setId(balance.getId());
        header.setCategory(balance.getCategory());
        header.setDate(balance.getDate());
        header.setType(balance.getType());

        items = new ArrayList<>(balance.getItems().stream().map(a -> {
            var item = new BalanceItemForm();
            item.setId(a.getId());
            item.setItem(a.getItem());
            item.setQuantity(a.getQuantity());
            item.setUnitPrice(a.getUnitPrice());
            return item;
        }).toList());
    }

    public boolean isShow() {
        return !items.isEmpty();
    }

    public int getTotal() {
        return items.stream()
                .filter(a -> !a.isDeleted())
                .mapToInt(value -> value.getQuantity() * value.getUnitPrice()).sum();
    }

    public int getTotalCount() {
        return items.stream().filter(a -> !a.isDeleted()).mapToInt(BalanceItemForm::getQuantity).sum();
    }

    public List<BalanceItemForm> getValidItems() {
        return items.stream().filter(a -> !a.isDeleted()).toList();
    }

    public String queryParam() {
        return header.getId() == null ? "type=%s".formatted(header.getType()) : "id=%s".formatted(header.getId());
    }

    public void clear() {
        header = new BalanceSummaryForm();
        items.clear();
    }
}
