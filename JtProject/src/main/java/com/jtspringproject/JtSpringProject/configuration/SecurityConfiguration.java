package com.jtspringproject.JtSpringProject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jtspringproject.JtSpringProject.models.User;
import com.jtspringproject.JtSpringProject.services.userService;

@Configuration
public class SecurityConfiguration {

	userService UserService;

	public SecurityConfiguration(userService UserService) {
		this.UserService = UserService;
	}

	@Configuration
	@Order(1)
	public static class AdminConfigurationAdapter {

		@Bean
		SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
			http.antMatcher("/admin/**")
					.authorizeHttpRequests(requests -> requests
							.antMatchers("/admin/login").permitAll()
							.antMatchers("/admin/**").hasRole("ADMIN")
					)
					.formLogin(login -> login
							.loginPage("/admin/login")
							.loginProcessingUrl("/admin/loginvalidate")
							.successHandler((request, response, authentication) -> {
								response.sendRedirect("/admin/");
							})
							.failureHandler((request, response, exception) -> {
								response.sendRedirect("/admin/login?error=true");
							}))
					.logout(logout -> logout
							.logoutUrl("/admin/logout")
							.logoutSuccessUrl("/admin/login")
							.deleteCookies("JSESSIONID"))
					.exceptionHandling(exception -> exception
							.accessDeniedPage("/403"));

			http.csrf(csrf -> csrf.disable());
			return http.build();
		}
	}

	@Configuration
	@Order(2)
	public static class UserConfigurationAdapter {

		@Bean
		SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
			http.authorizeHttpRequests(requests -> requests
							.antMatchers("/login", "/register", "/newuserregister", "/test", "/test2").permitAll()

							.antMatchers("/client/**").hasRole("CLIENT")

							.antMatchers("/teacher/**").hasRole("TEACHER")

							.antMatchers("/**").authenticated()
					)
					.formLogin(login -> login
							.loginPage("/login")
							.loginProcessingUrl("/userloginvalidate")
							.successHandler((request, response, authentication) -> {
								String role = authentication.getAuthorities().toString();
								if (role.contains("TEACHER")) {
									response.sendRedirect("/tutor_inform");
								} else {
									response.sendRedirect("/");
								}
							})
							.failureHandler((request, response, exception) -> {
								response.sendRedirect("/login?error=true");
							}))
					.logout(logout -> logout
							.logoutUrl("/logout")
							.logoutSuccessUrl("/login")
							.deleteCookies("JSESSIONID"))
					.exceptionHandling(exception -> exception
							.accessDeniedPage("/403"));

			http.csrf(csrf -> csrf.disable());
			http.sessionManagement(session -> session
					.maximumSessions(1)
					.expiredUrl("/login?expired=true")
			.maxSessionsPreventsLogin(true));
			http.sessionManagement(session -> session
					.sessionFixation().newSession());
			return http.build();
		}
	}

	@Bean
	UserDetailsService userDetailsService() {
		return username -> {
			User user = UserService.getUserByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("User with username " + username + " not found.");
			}

			// Определяем роль пользователя
			String role;
			switch (user.getRole()) {
				case ADMIN:
					role = "ADMIN";
					break;
				case CLIENT:
					role = "CLIENT";
					break;
				case TEACHER:
					role = "TEACHER";
					break;
				default:
					role = "CLIENT";
					break;
			}

			return org.springframework.security.core.userdetails.User
					.withUsername(username)
					.password(user.getPassword())
					.roles(role)
					.build();
		};
	}
}
