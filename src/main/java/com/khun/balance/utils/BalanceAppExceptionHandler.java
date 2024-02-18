package com.khun.balance.utils;

import com.khun.balance.exception.BalanceAppException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;

@ControllerAdvice
public class BalanceAppExceptionHandler {
    @ExceptionHandler(value = BalanceAppException.class)
    String handle(BalanceAppException e, HttpServletRequest request){
        System.out.println("heelleoel");
        RequestContextUtils.getOutputFlashMap(request)
                .put("message",e.getMessage());
        return "redirect:/";
    }
}
