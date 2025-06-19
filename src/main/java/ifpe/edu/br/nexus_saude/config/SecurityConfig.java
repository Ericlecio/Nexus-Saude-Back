package ifpe.edu.br.nexus_saude.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ifpe.edu.br.nexus_saude.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // Permitido sem autenticação
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/admin/registrar").permitAll()
                .requestMatchers(HttpMethod.POST, "/paciente/registrar").permitAll()
                .requestMatchers(HttpMethod.POST, "/medico/registrar").permitAll()
                .requestMatchers(HttpMethod.GET, "/admin/dashboard-stats").permitAll()

                // Endpoints de Administrador
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Endpoints de Médico
                .requestMatchers("/medico/listar", "/medico/{id}").hasAnyRole("MEDICO", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/medico/update/{id}").hasAnyRole("MEDICO", "ADMIN")
                .requestMatchers("/DiasAtendimento/**").hasAnyRole("MEDICO", "ADMIN")

                // Endpoints de Paciente
                .requestMatchers("/paciente/listar", "/paciente/{id}").hasAnyRole("PACIENTE", "ADMIN", "MEDICO")
                .requestMatchers(HttpMethod.PUT, "/paciente/update/{id}").hasAnyRole("PACIENTE", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/paciente/redefinir-senha/{id}").hasAnyRole("PACIENTE", "ADMIN")

                // Endpoints Compartilhados
                .requestMatchers("/agendamento/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                .requestMatchers("/consultas/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                .requestMatchers("/consulta-historico/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                .requestMatchers("/situacao/**").hasAnyRole("MEDICO", "ADMIN")
                .requestMatchers("/logs/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
