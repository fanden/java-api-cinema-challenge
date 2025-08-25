package com.booleanuk.cinema.security;


import com.booleanuk.cinema.security.jwt.AuthEntryPointJwt;
import com.booleanuk.cinema.security.jwt.AuthTokenFilter;
import com.booleanuk.cinema.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/customers", "/customers/**").hasAnyRole("ADMIN","MANAGER")
                        .requestMatchers(HttpMethod.GET,"/movies").hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.GET,"/movies").hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST,"/movies").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/movies").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/movies").hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.GET,"movies/{id}/screenings").hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST,"movies/{id}/screenings").hasAnyRole("ADMIN","MANAGER")

                        .requestMatchers("/tickets", "/tickets/**").hasAnyRole("ADMIN", "MANAGER")
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
