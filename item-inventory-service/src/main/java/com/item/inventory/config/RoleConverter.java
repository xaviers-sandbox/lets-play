package com.item.inventory.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import reactor.core.publisher.Mono;


public class RoleConverter implements Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> {

	@Override
	public final Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
		Collection<GrantedAuthority> authoritiesCollection = this.getAuthorities(jwt);
		
		return Mono.just(new JwtAuthenticationToken(jwt, authoritiesCollection));
	}

	@SuppressWarnings("unchecked")
	protected Collection<GrantedAuthority> getAuthorities(Jwt jwt) {
		Map<String, Object> claimsMap = jwt.getClaim("realm_access");
		
		if (ObjectUtils.isEmpty(claimsMap))
			return Collections.emptyList();

		List<String> rolesList = (List<String>) claimsMap.get("roles");
		
		return rolesList.parallelStream()
				.map(name -> "ROLE_".concat(name))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
}
