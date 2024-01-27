package com.item.review.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

//@EnableWebFlux
//@EnableWebFluxSecurity
@Configuration
public class SecurityConfigs {

	private static final String[] WHITE_LIST = { "/webjars", "/webjars/**",
			"/actuator", "/actuator/**", "/api-docs", "/api-docs/**", "/swagger-ui.html", "/swagger-ui.html/**",
			"/swagger-ui/**" };

	private static final String[] BLACK_LIST = { "/v1/item-reviews/app", "/v1/item-reviews/app",
			"/v1/item-reviews/app/**", "/v1/item-reviews/dbs", "/v1/item-reviews/dbs/**",
			"/v1/item-reviews/caches", "/v1/item-reviews/caches/**" };

	private static final String[] ROLES_LIST = { "USER", "ADMIN" };

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
		// disable api security
//			return httpSecurity
//					.authorizeExchange((authorize) -> authorize.anyExchange().permitAll())
//					//.authorizeExchange((authorize) -> authorize.pathMatchers("/**").permitAll())
//					.csrf(csrf -> csrf.disable())
//					.cors(cors -> cors.disable())
//					.build();

// enable security
		return httpSecurity
				.authorizeExchange((authorize) -> authorize.pathMatchers(WHITE_LIST)
						.permitAll()
						.pathMatchers(BLACK_LIST)
						.hasAnyRole(ROLES_LIST)
						.anyExchange()
						.authenticated())
				.csrf(csrf -> csrf.disable())
				//.csrf(csrf -> Customizer.withDefaults())
				//.cors(cors -> Customizer.withDefaults())
				.oauth2ResourceServer(oauth2ResourceServerCustomizer -> {
					oauth2ResourceServerCustomizer.jwt(jwtCustomizer -> {
						jwtCustomizer.jwtAuthenticationConverter(new RoleConverter());
					});
				})
				.build();
	}
}
