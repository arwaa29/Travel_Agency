package org.example.travelagency.userManagement;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class securityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/**")) // Disable CSRF for /api/** routes
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Allow register and login without authentication
                        .requestMatchers("/hotels/**").permitAll() // Allow unauthenticated access to /hotels endpoint
                        .requestMatchers("/rooms/**").permitAll()  // Allow unauthenticated access to /rooms endpoints
                        .requestMatchers("/events/**").permitAll()
                        .requestMatchers("/notifications/**").permitAll()
                        .requestMatchers("/bookings/**").permitAll()// Allow unauthenticated access to /bookings endpoints
                        .anyRequest().authenticated() // Secure other requests
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                );
        return http.build();
    }

    // Custom firewall configuration
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);  // Allow '/' encoded as %2F
        firewall.setAllowUrlEncodedPeriod(true); // Allow '.' encoded as %2E
        firewall.setAllowUrlEncodedPercent(true); // Allow '%' encoded as %25
        firewall.setAllowBackSlash(true);        // Allow '\' encoded as %5C
        return firewall;
    }
}


