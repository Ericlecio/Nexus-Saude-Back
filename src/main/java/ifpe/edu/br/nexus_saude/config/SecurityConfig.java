package ifpe.edu.br.nexus_saude.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository; // Para interações CSRF com frontend

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Para @PreAuthorize funcionar
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
            
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
               
            )
            
            .authorizeHttpRequests(authorize -> authorize
                // Endpoints Públicos (login, registro de usuários, recursos estáticos)
                .requestMatchers("/auth/**", "/login", "/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/admin/registrar").permitAll() // Endpoint de registro para Admin
                .requestMatchers(HttpMethod.POST, "/medico/registrar").permitAll() // Endpoint de registro para Medico
                .requestMatchers(HttpMethod.POST, "/paciente/registrar").permitAll() // Endpoint de registro para Paciente

                // Endpoints de Administrador
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Endpoints de Médico
                .requestMatchers("/medico/listar", "/medico/{id}").hasAnyRole("MEDICO", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/medico/update/{id}").hasAnyRole("MEDICO", "ADMIN") // Médico atualiza seus dados, Admin também
                .requestMatchers("/DiasAtendimento/**").hasAnyRole("MEDICO", "ADMIN") // Médico gerencia seus dias, Admin também

                // Endpoints de Paciente
                .requestMatchers("/paciente/listar", "/paciente/{id}").hasAnyRole("PACIENTE", "ADMIN") // Paciente vê seus dados, Admin também
                .requestMatchers(HttpMethod.PUT, "/paciente/update/{id}").hasAnyRole("PACIENTE", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/paciente/redefinir-senha/{id}").hasRole("PACIENTE") // Só o próprio paciente


                // Endpoints Compartilhados (Agendamentos, Consultas, etc.) - Ajuste fino aqui!
                .requestMatchers("/agendamentos/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                .requestMatchers("/consultas/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                .requestMatchers("/consulta-historico/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                .requestMatchers("/situacoes/**").hasAnyRole("MEDICO", "ADMIN") // Exemplo: só médico/admin gerenciam situações
                .requestMatchers("/logs/**").hasRole("ADMIN") // Logs apenas para Admin


                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login") // Sua página/endpoint de login (GET)
                .loginProcessingUrl("/perform_login") // Spring Security processa o POST para esta URL
                .defaultSuccessUrl("/", true) // Redireciona para a home após login
                .failureUrl("/login?error=true") // Em caso de falha
                .usernameParameter("email") // Nome do parâmetro para o email/username no form
                .passwordParameter("senha") // Nome do parâmetro para a senha no form
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID") // Importante para invalidar a sessão
                .permitAll()
            )
            .userDetailsService(userDetailsService) // Nosso serviço customizado
            .httpBasic(httpBasic -> httpBasic.disable()); // Desabilitar Basic Auth se usar formLogin

        return http.build();
    }
}