package com.myproject.seuPedidoAqui.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/index.html",
                                "/login.html",
                                "/carrinho.html",
                                "/pedido.html",
                                "/cadastro.html",
                                "/api/usuarios/login",
                                "/api/usuarios/cadastro",
                                "/api/pedidos/cadastro",
                                "/api/pedidos/listarPedidoId/**",
                                "/api/pedidos/cancelar/**",
                                "/api/pedidos/listarPedidos",
                                "api/pedidos/atualizarPedido",
                                "/css/**",
                                "/js/**",
                                "/img/**"
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}