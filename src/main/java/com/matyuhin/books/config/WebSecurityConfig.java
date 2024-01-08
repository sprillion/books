package com.matyuhin.books.config;

import com.matyuhin.books.models.Roles;
import com.matyuhin.books.service.DirectoryUserDetailsService;
import com.matyuhin.books.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request -> {
            request.requestMatchers(
                    "/register/**",
                    "/index",
                    "/actuator/**",
                    "/about").permitAll();
            request.requestMatchers(
                    "/users",
                    "/listLogs",
                    "/changeRole"
            ).hasRole(Roles.ADMIN.toString());
            request.requestMatchers(
                    "/add**",
                    "/delete**",
                    "/show**"
            ).hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString());
            request.anyRequest().authenticated();
        });
        http.exceptionHandling(h -> h.accessDeniedPage("/accessDenied"));
        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index")
                .permitAll()
        );

        http.logout(logOut -> logOut.logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login")
        );
        return http.build();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new DirectoryUserDetailsService(userService)).passwordEncoder(passwordEncoder());
    }

}
