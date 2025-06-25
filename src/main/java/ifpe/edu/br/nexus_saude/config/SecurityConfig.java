package ifpe.edu.br.nexus_saude.config;

import ifpe.edu.br.nexus_saude.security.JwtAuthenticationFilter;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .cors()
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Endpoints Públicos
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admin/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/paciente/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/medico/registrar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/dashboard-stats").permitAll()

                        // 2. Informações do usuário logado
                        .requestMatchers(HttpMethod.GET, "/usuarios/logado").authenticated()

                        // 3. Acesso do paciente autenticado
                        .requestMatchers("/paciente/**").authenticated()

                        // 4. Regras por papel
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/medico/listar").hasAnyRole("MEDICO", "ADMIN", "PACIENTE")
                        .requestMatchers("/medico/**").hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/DiasAtendimento/listar/**")
                        .hasAnyRole("ADMIN", "PACIENTE", "MEDICO")
                        .requestMatchers(HttpMethod.GET, "/agendamento/medico/**")
                        .hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/agendamento/inserir")
                        .hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                        .requestMatchers("/agendamento/**").hasAnyRole("MEDICO", "ADMIN") // mantém o restante restrito
                        .requestMatchers("/DiasAtendimento/**").hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers("/consultas/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                        .requestMatchers("/consulta-historico/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                        .requestMatchers("/situacao/**").hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers("/logs/**").hasRole("ADMIN")

                        // 5. Demais endpoints exigem autenticação
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
