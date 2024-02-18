package com.khun.balance.config;

import com.khun.balance.domain.entity.Role;
import com.khun.balance.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    private final DataSource dataSource;

    @Bean
    SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        "/signin",
                                        "/signup"
                                )
                                .permitAll()
                                .requestMatchers("/user/**").hasAnyAuthority(Role.Admin.name(),Role.Member.name())
                                .requestMatchers("/admin/**").hasAuthority(Role.Admin.name())
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(
                        (formLogin) -> formLogin
                                .loginPage("/signin")
                                .loginProcessingUrl("/process-login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                )
                .logout(
                        (logout) -> logout
                                .logoutUrl("/logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .clearAuthentication(true)
                                .logoutSuccessHandler((request, response, authentication) ->
                                    response.sendRedirect("/signin")
                                )
                                .permitAll()
                )
                .exceptionHandling(
                        (exceptionHandling) -> exceptionHandling
                                .accessDeniedPage("/access-denied"))
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")
                        .rememberMeServices(rememberMeServices())
                        .tokenValiditySeconds(60 * 60 * 24)//1 day
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)//1 user 1 session
                        .maxSessionsPreventsLogin(false)//if user already login, then user can't log in again
                        .expiredUrl("/signin")//if user already login, then user can't log in again
                        .sessionRegistry(sessionRegistry())//session registry
                )
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByLoginIdOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        if (tokenRepository.getJdbcTemplate() == null) {
            tokenRepository.setJdbcTemplate(new JdbcTemplate(dataSource));
        }
        tokenRepository.getJdbcTemplate().execute("CREATE TABLE IF NOT EXISTS persistent_logins (" +
                "username VARCHAR(64) NOT NULL," +
                "series VARCHAR(64) PRIMARY KEY," +
                "token VARCHAR(64) NOT NULL," +
                "last_used TIMESTAMP NOT NULL" +
                ")");

        return tokenRepository;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        String MY_KEY = "akatsuki-abcdEFGH1234-5678IJKLmnopqrstuvwxYZ";
        return new PersistentTokenBasedRememberMeServices(
                MY_KEY, userDetailsService(), persistentTokenRepository()
        );
    }

    @Bean
    AuthenticationEventPublisher authenticationEventPublisher(){
        return new DefaultAuthenticationEventPublisher();
    }

    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }
}