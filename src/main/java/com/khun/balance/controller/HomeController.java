package com.khun.balance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class HomeController {

    @GetMapping("/incomes")
    String income(ModelMap model){
        model.put("title","Income Management");
        return "balance-list";
    }
    @GetMapping("/expenses")
    String expense(ModelMap model){
        model.put("title","Expense Management");
        return "balance-list";
    }
}
