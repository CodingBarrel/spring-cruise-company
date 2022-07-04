package ua.cruise.springcruise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/admin-panel**", "/admin-cruise**", "/admin-liner/**", "/admin-route/**", "/admin-routepoint/**", "admin-ticket/**", "/admin-user/**").hasAnyRole("OWNER", "ADMIN")
                .antMatchers("/ticket/**").hasAnyRole("OWNER", "ADMIN", "USER")
                .antMatchers("/", "/about", "/auth/**", "/cruise/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/signIn")
                .usernameParameter("login")
                .defaultSuccessUrl("/", false)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/auth/signOut")
                .invalidateHttpSession(false)
                //.deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .permitAll();
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
