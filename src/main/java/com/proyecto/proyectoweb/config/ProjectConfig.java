package com.proyecto.proyectoweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/images/", "classpath:/static/images/");
    }



    public static final String[] PUBLIC_URLS = {
            "/", "/index", "/home",
            "/login", "/register",
            "/productos", "/productos/**",
            "/producto/**",
            "/buscar",
            "/contact",
            "/css/**", "/images/**", "/js/**", "/webjars/**"
    };

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/css/**", "/images/**").permitAll()
                .requestMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
        ).formLogin(form -> form // Configuración de formulario de login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email") // usamos el campo 'email' del formulario como username
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error") // query param sin valor para mostrar mensaje genérico
                .permitAll()
        ).logout(logout -> logout // Configuración de logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions // Manejo de excepciones
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session // Configuración de sesiones
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

     @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
