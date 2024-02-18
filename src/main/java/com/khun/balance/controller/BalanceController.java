package com.khun.balance.controller;

import com.khun.balance.domain.entity.Type;
import com.khun.balance.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/user/balance")
@Slf4j
@RequiredArgsConstructor
public class BalanceController {
    private final UserRepository userRepository;
    @GetMapping("report")
    String report(){
        return "balance-report";
    }
    @GetMapping("/{type}")
    String income(ModelMap model, @PathVariable String type){
        model.put("title","incomes".equals(type)?"Income Management":"Expense Management");
        model.put("type", type);
        log.info("users : {}", userRepository.findAll());
        return "balance-list";
    }

    @GetMapping("/add/{type}")
    public String addNew(@PathVariable String type, ModelMap model) {
        model.put("title","incomes".equals(type)?"Add New Income":"Add New Expense");
        model.put("type",type);
        return "balance-edit";
    }

    @GetMapping("/edit/{id:\\d+}")
    public String edit(@PathVariable int id, ModelMap model) {
        model.put("title", "Edit income");
        model.put("type", "incomes");
        return "balance-edit";
    }

    @PostMapping
    public String save() {
        return "redirect:/user/balance/1";
    }

    @GetMapping("{id:\\d+}")
    public String findById(@PathVariable int id) {
        return "balance-details";
    }

    public String search(Type type, String category, LocalDate from, LocalDate to) {
        return "";
    }

    @GetMapping("delete/{id:\\d+}")
    public String delete(@PathVariable int id) {
        return "redirect:/";
    }
}
