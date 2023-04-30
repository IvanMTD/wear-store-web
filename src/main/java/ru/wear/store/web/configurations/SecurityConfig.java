package ru.wear.store.web.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;
import ru.wear.store.web.services.ClientDetailsService;

import java.net.URI;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager=true)
public class SecurityConfig {

    private final ClientDetailsService clientDetailsService;

    public SecurityConfig(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Bean
    public SecurityWebFilterChain mainSecurityConfigure(ServerHttpSecurity http) {

        ServerCsrfTokenRequestAttributeHandler requestHandler = new ServerCsrfTokenRequestAttributeHandler();
        requestHandler.setTokenFromMultipartDataEnabled(true);

        return http
                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler))
                .authorizeExchange()
                .pathMatchers("/registration/login")
                .permitAll()
                .anyExchange()
                .permitAll()
                .and()
                .httpBasic(withDefaults())
                .formLogin()
                .loginPage("/registration/login")
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .build();
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler(){
        RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
        handler.setLogoutSuccessUrl(URI.create("/"));
        return handler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
