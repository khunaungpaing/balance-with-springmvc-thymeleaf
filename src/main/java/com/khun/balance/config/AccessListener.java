package com.khun.balance.config;

import com.khun.balance.domain.entity.UserAccessLog;
import com.khun.balance.domain.entity.UserAccessLog.Type;
import com.khun.balance.repo.UserAccessLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccessListener {
    private final UserAccessLogRepository userAccessLogRepository;
    @EventListener
    @Transactional
    void OnSuccess(AuthenticationSuccessEvent event){
        var username = event.getAuthentication().getName();
        log.info("{} is sing in",username);
        userAccessLogRepository.save(
                UserAccessLog.builder()
                        .username(username)
                        .type(Type.SignIn)
                        .accessTime(LocalDateTime.now())
                        .build()
        );
    }
    @EventListener
    @Transactional
    void OnFailure(AbstractAuthenticationFailureEvent event){
        var username = event.getAuthentication().getName();
        log.info("{} is fail to sign in beacuse of {}",username,event.getException().getMessage());
        userAccessLogRepository.save(
                UserAccessLog.builder()
                        .username(username)
                        .type(Type.Error)
                        .accessTime(LocalDateTime.now())
                        .errorMessage(event.getException().getMessage())
                        .build()
        );
    }

    @EventListener
    @Transactional
    void OnSessionDenied(HttpSessionDestroyedEvent event){
        event.getSecurityContexts().stream().findAny()
                .ifPresent(auth->{
                     var username = auth.getAuthentication().getName();
                     log.info("{} sign out", username);
                    userAccessLogRepository.save(
                            UserAccessLog.builder()
                                    .username(username)
                                    .type(Type.SignOut)
                                    .accessTime(LocalDateTime.now())
                                    .build()
                    );
                });

    }
}
