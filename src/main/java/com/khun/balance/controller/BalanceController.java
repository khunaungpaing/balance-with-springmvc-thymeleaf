package com.khun.balance.controller;

import com.khun.balance.domain.entity.Type;
import com.khun.balance.service.BalanceService;
import com.khun.balance.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user/balance")
@Slf4j
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("report")
    String report(
            ModelMap model,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {
        var result = balanceService.searchReports(type, dateFrom, dateTo, page, size);
        model.put("list", result.getContent());

        var pagination = Pagination.builder("/user/balance/%s".formatted(type))
                .params(Map.of(
                        "type", null == type ? "" : type.name(),
                        "dateFrom", null == dateFrom ? "" : dateFrom.format(dateTimeFormatter),
                        "dateTo", null == dateTo ? "" : dateTo.format(dateTimeFormatter)
                ))
                .page(result)
                .size(List.of(5, 10, 25))
                .build();

        model.put("pagination", pagination);

        return "balance-report";
    }

    @GetMapping("/{type:[A-Za-z]+}")
    String searchBalanceItems(
            ModelMap model,
            @PathVariable Type type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
            @RequestParam(required = false) String keyword,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {
        model.put("title", "%s Management".formatted(type));
        model.put("type", type);

        var result = balanceService.searchItems(type, dateFrom, dateTo, keyword, page, size);

        model.put("list", result.getContent());

        var params = new HashMap<String, String>();
        params.put("type", type.name());
        params.put("keyword", "%s".formatted(keyword));
        params.put("dateFrom", null == dateFrom ? "" : dateFrom.format(dateTimeFormatter));
        params.put("dateTo", null == dateTo ? "" : dateTo.format(dateTimeFormatter));


        var pagination = Pagination.builder("/user/balance/%s".formatted(type))
                .params(params)
                .page(result)
                .size(List.of(5, 10, 25))
                .build();
        model.put("pagination", pagination);

        return "balance-list";
    }

    @GetMapping("details/{id:\\d+}")
    public String findById(@PathVariable Long id, ModelMap model) {
        model.put("vo", balanceService.findById(id));
        return "balance-details";
    }

    public String search(Type type, String category, LocalDate from, LocalDate to) {
        return "";
    }

    @GetMapping("delete/{id:\\d+}")
    public String delete(@PathVariable Long id) {
        balanceService.deleteById(id);
        return "redirect:/";
    }

    @ModelAttribute("balanceTypes")
    Type[] types() {
        return Type.values();
    }
}
