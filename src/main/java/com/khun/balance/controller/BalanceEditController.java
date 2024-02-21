package com.khun.balance.controller;

import com.khun.balance.domain.entity.Type;
import com.khun.balance.domain.form.BalanceEditForm;
import com.khun.balance.domain.form.BalanceItemForm;
import com.khun.balance.domain.form.BalanceSummaryForm;
import com.khun.balance.exception.BalanceAppException;
import com.khun.balance.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("user/balance-edit")
@RequiredArgsConstructor
@SessionAttributes("balanceEditForm")
public class BalanceEditController {

    private final BalanceService service;
    @GetMapping
    public String edit(
            @ModelAttribute("balanceEditForm") BalanceEditForm editForm,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Type type
    ) {
        if(null != id && !Objects.equals(editForm.getHeader().getId(), id)){
           var result = service.findById(id);
           editForm.setHeader(result.getHeader());
           editForm.setItems(result.getItems());
        }

        if(null!=type && !Objects.equals(type,editForm.getHeader().getType())){
            editForm.setHeader(new BalanceSummaryForm());
            editForm.getHeader().setType(type);
            editForm.getItems().clear();
        }
        return "balance-edit";
    }

    @PostMapping("item")
    public String addItem(
            @ModelAttribute("balanceEditForm") BalanceEditForm editForm,
            @ModelAttribute("balanceItemForm")@Valid BalanceItemForm itemForm,
            BindingResult result
    ){
        if(result.hasErrors()){
            return "balance-edit";
        }
        editForm.getItems().add(itemForm);
        return "redirect:/user/balance-edit?%s".formatted(editForm.queryParam());
    }

    @GetMapping("item")
    public String deleteItem(
            @ModelAttribute("balanceEditForm") BalanceEditForm editForm,
            @RequestParam int index
    ){
        var item = editForm.getItems().get(index);
        if(item.getId()==null){
            editForm.getItems().remove(item);
        }else {
            item.setDeleted(true);
        }

        return "redirect:/user/balance-edit?%s".formatted(editForm.queryParam());
    }

    @GetMapping("/confirm")
    public String confirm(){
        return "balance-edit-confirm";
    }


    @PostMapping
    public String save(
            @ModelAttribute("balanceEditForm") BalanceEditForm editForm,
            @ModelAttribute("summaryForm") @Valid BalanceSummaryForm summaryForm,
            BindingResult result
    ) {
        if(result.hasErrors()){
            return "balance-edit-confirm";
        }
        editForm.getHeader().setCategory(summaryForm.getCategory());
        editForm.getHeader().setDate(summaryForm.getDate());

        Long id = service.save(editForm);
        editForm.clear();
        return "redirect:/user/balance/details/%d".formatted(id);
    }

    @ModelAttribute("summaryForm")
    public BalanceSummaryForm summaryForm(@ModelAttribute("balanceEditForm") BalanceEditForm editForm){
        return editForm.getHeader();
    }
    @ModelAttribute("balanceItemForm")
    public BalanceItemForm itemForm(){
        return new BalanceItemForm();
    }
    @ModelAttribute("balanceEditForm")
    public BalanceEditForm form(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Type type
    ){
        if(null != id){
            return service.findById(id);
        }

        if(null==type){
            throw new BalanceAppException("Please set type for balance");
        }

        return new BalanceEditForm().type(type);
    }

}
