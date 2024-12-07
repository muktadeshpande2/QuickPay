package com.major.user_service.configs;

import com.major.user_service.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Authentication
     *
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }


    /**
     * Authorisation Bean
     * @param httpSecurity
     * @return
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                  .requestMatchers("/**").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    /**
     * Password Encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }
}
