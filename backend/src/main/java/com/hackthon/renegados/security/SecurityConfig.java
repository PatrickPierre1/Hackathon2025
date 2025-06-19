package com.hackthon.renegados.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/images/**",
                                "/css/**",
                                "/error/**"
                        ).permitAll()

                        .requestMatchers(
                                "/login"
                        ).permitAll()

                        .requestMatchers(
                                "/banco/**",
                                "usuario/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/api/**"
                        ).permitAll()


                        .anyRequest().authenticated()
                ).formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/",true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"))
                .build();
    }



//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails admin =
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("admin")
//                        .roles("ADMIN")
//                        .build();
//
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password(new BCryptPasswordEncoder().encode("user"))
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
