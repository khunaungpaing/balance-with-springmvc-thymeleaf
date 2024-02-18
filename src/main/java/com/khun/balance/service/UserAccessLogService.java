package com.khun.balance.service;

import com.khun.balance.domain.entity.UserAccessLog;
import com.khun.balance.domain.entity.UserAccessLog.Type;
import com.khun.balance.repo.UserAccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccessLogService {
    private final UserAccessLogRepository userAccessLogRepository;

    public Page<UserAccessLog> search(String username, Optional<Integer> page, Optional<Integer> size) {
        var pageInfo = PageRequest.of(page.orElse(0), size.orElse(5))
                .withSort(Sort.by("accessTime").descending());
        Specification<UserAccessLog> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
        return userAccessLogRepository.findAll(spec, pageInfo);
    }

    @PreAuthorize("hasAuthority('Admin')")
    public Page<UserAccessLog> search(Type type, String username, LocalDate date, Optional<Integer> page, Optional<Integer> size) {
        var pageInfo = PageRequest.of(page.orElse(0), size.orElse(5))
                .withSort(Sort.by("accessTime").descending());
        Specification<UserAccessLog> spec = Specification.where(null);
        if (null != type) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type));
        }
        if (StringUtils.hasLength(username)) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    username.toLowerCase().concat("%")));
        }
        if(null != date){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                    root.get("accessTime"),date.atStartOfDay())
            );
        }

        return userAccessLogRepository.findAll(spec,pageInfo);
    }
}
