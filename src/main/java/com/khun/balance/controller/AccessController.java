package com.khun.balance.controller;

import com.khun.balance.domain.entity.UserAccessLog.Type;
import com.khun.balance.service.UserAccessLogService;
import com.khun.balance.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AccessController {
    private final UserAccessLogService accessLogService;

    @GetMapping("access-logs")
    String search(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            ModelMap model
    ) {
        var result = accessLogService.search(type, username, date, page, size);
        model.put("result", result.getContent());

        var params = new HashMap<String, String>();
        params.put("type", null == type ? "" : type.name());
        params.put("username", StringUtils.hasLength(username) ? username : "");
        params.put("date", null == date ? "" : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        size.ifPresent(a -> params.put("size", a.toString()));

        model.put("pagination", Pagination.builder("/admin/access-logs")
                .size(List.of(5, 10, 25))
                .sizeChangeFormId("accessLogSearchForm")
                .page(result)
                .params(params)
                .build());
        return "access-logs";
    }

    @ModelAttribute("types")
    Type[] types() {
        return Type.values();
    }
}
