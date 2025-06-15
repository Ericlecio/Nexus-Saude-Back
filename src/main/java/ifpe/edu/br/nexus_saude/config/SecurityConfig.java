package ifpe.edu.br.nexus_saude.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService)
			throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.cors(cors -> {
				})
				.authorizeHttpRequests(authorize -> authorize
						// Permitido sem autenticação
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/admin/registrar").permitAll()
						.requestMatchers(HttpMethod.POST, "/paciente/registrar").permitAll()
						.requestMatchers(HttpMethod.POST, "/medico/registrar").permitAll()

						// Endpoints de Administrador
						.requestMatchers(HttpMethod.GET, "/admin/dashboard-stats").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")

						// Endpoints de Médico
						.requestMatchers("/medico/listar", "/medico/{id}").hasAnyRole("MEDICO", "ADMIN")
						.requestMatchers(HttpMethod.PUT, "/medico/update/{id}").hasAnyRole("MEDICO", "ADMIN")
						.requestMatchers("/DiasAtendimento/**").hasAnyRole("MEDICO", "ADMIN")

						// Endpoints de Paciente
						.requestMatchers("/paciente/listar", "/paciente/{id}").hasAnyRole("PACIENTE", "ADMIN")
						.requestMatchers(HttpMethod.PUT, "/paciente/update/{id}").hasAnyRole("PACIENTE", "ADMIN")
						.requestMatchers(HttpMethod.PUT, "/paciente/redefinir-senha/{id}")
						.hasAnyRole("PACIENTE", "ADMIN")

						// Endpoints Compartilhados
						.requestMatchers("/agendamentos/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
						.requestMatchers("/consultas/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
						.requestMatchers("/consulta-historico/**").hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
						.requestMatchers("/situacoes/**").hasAnyRole("MEDICO", "ADMIN")
						.requestMatchers("/logs/**").hasRole("ADMIN")

						.anyRequest().authenticated())

				// 🔥 COMENTADO: REMOVER esta parte se você for usar autenticação via API (como
				// JWT)
				/*
				 * .formLogin(formLogin -> formLogin
				 * .loginPage("/login")
				 * .loginProcessingUrl("/perform_login")
				 * .defaultSuccessUrl("/", true)
				 * .failureUrl("/login?error=true")
				 * .usernameParameter("email")
				 * .passwordParameter("senha")
				 * .permitAll())
				 * .logout(logout -> logout
				 * .logoutUrl("/perform_logout")
				 * .logoutSuccessUrl("/login?logout")
				 * .deleteCookies("JSESSIONID")
				 * .permitAll())
				 */

				.userDetailsService(userDetailsService)
				.httpBasic(httpBasic -> httpBasic.disable());

		return http.build();
	}
}
