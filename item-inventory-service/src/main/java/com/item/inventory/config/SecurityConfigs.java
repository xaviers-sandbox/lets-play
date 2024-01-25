package com.item.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
public class SecurityConfigs  {
	
	
	
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
		
		//disable all security
			return httpSecurity
					.authorizeExchange((authorize) -> authorize.anyExchange().permitAll())
					//.authorizeExchange((authorize) -> authorize.pathMatchers("/**").permitAll())
					.csrf(csrf -> csrf.disable())
					.cors(cors -> cors.disable())
					.build();
		
// enable security
//		return httpSecurity
//				.authorizeExchange((authorize) -> authorize.pathMatchers("/v1/item-inventories/unsecure-world")
//						.permitAll()
//						.anyExchange()
//						.authenticated())
//				
//				.httpBasic(Customizer.withDefaults())
//				.formLogin(Customizer.withDefaults())
//				.csrf(csrf -> csrf.disable())
//				.cors(cors -> cors.disable())
//				//.oauth2ResourceServer(OAuth2ResourceServerSpec::jwt)
//				.build();
	}

    @Bean
     MapReactiveUserDetailsService userDetailsService() {
         UserDetails user = User.builder().username("user")
                 .password(passwordEncoder().encode("user"))
                 .roles("USER")
                 .build();
         
         return new MapReactiveUserDetailsService(user);
    }
    

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
//	@Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                )
//                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .build();
//    }
	
//	@Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        contentSecurityConfig(http);
//        AntPathRequestMatcher[] requestMatchers = getAntPathRequestMatchers();
//        http.authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(requestMatchers).permitAll()
//                        .anyRequest().authenticated())
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                ).csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(withDefaults());
//        return http.build();
//    }
	
}
