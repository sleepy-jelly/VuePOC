package common.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.jelly.pb.vuepoc.user.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/**", "/register/**").permitAll()
                .requestMatchers("/dash-board/**", "/api/dash-board/**").hasAnyAuthority(UserRole.USER,UserRole.MEMBERSHIP_USER,UserRole.ADMIN)
				.requestMatchers("/bbs/**", "/api/bbs/**").hasAnyAuthority(UserRole.USER,UserRole.MEMBERSHIP_USER,UserRole.ADMIN)
				.requestMatchers("/admins/**", "/api/admins/**").hasAnyAuthority(UserRole.ADMIN)
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(formLogin -> formLogin
            	.loginPage("http://localhost:5173/login/login-page")
                .loginProcessingUrl("/login/loginProcess")
                .usernameParameter("userId")
                .passwordParameter("userPw") 
				.defaultSuccessUrl("/login/loginSuccessful", true)
                .successHandler(authenticationSuccessHandler())
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Authentication failed");
                })
                .permitAll()
            )
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String username = authentication.getName();
            
            log.info(" authenticationSuccessHandler username  -> ,  {}",username);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("ReturnMessage", "Successful");
            resultMap.put("Redirect", "/dash-board");
            
            response.setContentType("application/json");
        };
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}