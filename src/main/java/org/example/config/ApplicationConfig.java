package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with this email."));
    }
}
