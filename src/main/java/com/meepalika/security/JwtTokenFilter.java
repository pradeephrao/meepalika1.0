package com.meepalika.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtTokenProvider.resolveToken(request);
		Authentication auth = null;
		/*
		 * this if loop is for an anonymous user.
		 */
		if (token == null && HttpMethod.GET.matches(request.getMethod())) {
			// allow user with out login - this is name - anonymous@hms.com - create a new
			// token
			String anonymousUser = UserAccessDetailsService.ANONYMOUS_EMAIL;
			Long anonymousId = UserAccessDetailsService.ANONYMOUS_ID;
			Collection<String> anonymousRoles = Arrays
					.asList(UserAccessDetailsService.UserRoleAccess.ROLES.ROLE_ANONNYMOUS.toString());

			token = jwtTokenProvider.createToken(anonymousId, anonymousUser, true, anonymousRoles);

		}
		if (token != null && jwtTokenProvider.validateToken(token, request)) {
			auth = jwtTokenProvider.getAuthentication(token);
		}
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

}
