package lt.vladimiras.blog.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lt.vladimiras.blog.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder,
                                                  UserDetailsService userDetailsService) throws Exception {

      var authProvider = new DaoAuthenticationProvider(passwordEncoder);
      authProvider.setUserDetailsService(userDetailsService);

      return http.authenticationProvider(authProvider)
            .addFilter(new BasicAuthenticationFilter(authProvider::authenticate))
//                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests

                  .requestMatchers(HttpMethod.POST, "/users").hasRole("role_admin")
                  .requestMatchers(HttpMethod.GET, "/users").hasRole("role_admin")

                  .requestMatchers(HttpMethod.POST, "/blog-posts").hasRole("role_admin")
                  .requestMatchers(HttpMethod.DELETE, "/blog-posts").hasRole("role_admin")
                  .requestMatchers(HttpMethod.GET, "/blog-posts").permitAll()

                  .requestMatchers(HttpMethod.POST, "/blog-posts/*/comment").hasRole("role_user")

                  .requestMatchers(HttpMethod.POST, "/login", "/signup").permitAll()
                  .requestMatchers(HttpMethod.POST, "/error").permitAll()
                  .anyRequest().authenticated())
            .build();
   }

   @Bean
   public UserDetailsService userDetailsService(UserRepository userRepository) {
      return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
