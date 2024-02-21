package com.khun.balance.utils;

import com.khun.balance.exception.BalanceAppException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;

@ControllerAdvice
public class BalanceAppExceptionHandler {
    @ExceptionHandler(value = BalanceAppException.class)
    String handleBalanceAppException(BalanceAppException e, HttpServletRequest req) {
        RequestContextUtils.getOutputFlashMap(req).put("message", e.getMessage());
        return "redirect:/user/home";
    }
}
