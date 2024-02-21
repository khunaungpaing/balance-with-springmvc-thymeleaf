package com.khun.balance.service;

import com.khun.balance.domain.entity.Balance;
import com.khun.balance.domain.entity.BalanceItem;
import com.khun.balance.domain.entity.Type;
import com.khun.balance.domain.form.BalanceEditForm;
import com.khun.balance.domain.vo.BalanceReportVo;
import com.khun.balance.repo.BalanceItemRepository;
import com.khun.balance.repo.BalanceRepository;
import com.khun.balance.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceItemRepository itemRepository;
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    @PreAuthorize("isAuthenticated()")
    public Page<BalanceItem> searchItems(
            Type type, LocalDate dateFrom,
            LocalDate dateTo, String keyword,
            Optional<Integer> page, Optional<Integer> size
    ) {
        var pageInfo = PageRequest.of(page.orElse(0),size.orElse(10));
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        Specification<BalanceItem> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("balance").get("user").get("loginId"),username);
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("balance").get("type"),type));

        if (null!=dateFrom){
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("balance").get("date"),dateFrom));
        }
        if (null!=dateTo){
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("balance").get("date"),dateTo));
        }
        if (StringUtils.hasLength(keyword)){
            Specification<BalanceItem> category = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("balance").get("category")),"%%%s%%".formatted(keyword.toLowerCase()));
            Specification<BalanceItem> item = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("item")),"%%%s%%".formatted(keyword.toLowerCase()));

            spec = spec.and(item.or(category));
        }

        return itemRepository.findAll(spec,pageInfo);
    }

    public BalanceEditForm findById(Long id) {
        return balanceRepository.findById(id).map(BalanceEditForm::new).orElseThrow();
    }

    @Transactional
    public Long save(BalanceEditForm editForm) {
        var id = editForm.getHeader().getId();
        var balance = id == null ? new Balance() : balanceRepository.findById(id).orElseThrow();
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByLoginId(username).orElseThrow();
        balance.setUser(user);
        balance.setCategory(editForm.getHeader().getCategory());
        balance.setDate(editForm.getHeader().getDate());
        balance.setType(editForm.getHeader().getType());

        balance = balanceRepository.save(balance);
        for (var formItem : editForm.getItems()){
            var item = formItem.getId()==null? new BalanceItem(): itemRepository.findById(formItem.getId()).orElseThrow();
            if(formItem.isDeleted()){
                itemRepository.delete(item);
                continue;
            }
            item.setItem(formItem.getItem());
            item.setQuantity(formItem.getQuantity());
            item.setUnitPrice(formItem.getUnitPrice());
            item.setBalance(balance);

            itemRepository.save(item);
        }
        return balance.getId();
    }

    public void deleteById(Long id) {
        balanceRepository.deleteById(id);
    }

    public Page<BalanceReportVo> searchReports(Type type, LocalDate dateFrom, LocalDate dateTo, Optional<Integer> page, Optional<Integer> size) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        var pageInfo = PageRequest.of(page.orElse(0), size.orElse(10));

        Specification<Balance> spec = (root, query, cb) -> cb.equal(root.get("user").get("loginId"),
                username);

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }

        if (dateFrom != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
        }

        if (dateTo != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), dateTo));
        }

        var result = balanceRepository.findAll(spec, pageInfo).map(BalanceReportVo::new);

        // calculate net amount
        if (!result.getContent().isEmpty()) {
            var firstId = result.getContent().get(0).getId();

            // get balance before first search result
            var lastIncome = itemRepository.getLastBalance(firstId, Type.Income).map(Number::intValue).orElse(0);
            var lastExpense = itemRepository.getLastBalance(firstId, Type.Expense).map(Number::intValue).orElse(0);

            // calculate total of before first search result
            var totalBalance = lastIncome - lastExpense;

            // calculate with search results
            for (var vo : result.getContent()) {
                if (vo.getType() == Type.Income) {
                    totalBalance += vo.getAmount();
                } else {
                    totalBalance -= vo.getAmount();
                }
                vo.setBalance(totalBalance);
            }
        }

        return result;
    }
}
