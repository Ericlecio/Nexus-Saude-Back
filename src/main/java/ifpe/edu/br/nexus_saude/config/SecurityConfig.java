package ifpe.edu.br.nexus_saude.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(auth -> auth
	                .anyRequest().authenticated()
	            )
	            .csrf(csrf -> csrf.disable())
	            .httpBasic(httpBasic -> {});  

	        return http.build();

}}
