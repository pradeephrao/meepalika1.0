package com.meepalika.security;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.meepalika.localization.Translator;
import com.meepalika.security.UserAccessDetailsService.UserRoleAccess;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	/**
	 * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key
	 * here. Ideally, in a microservices environment, this key would be kept on a
	 * config-server.
	 */
	@Value("${security.jwt.token.secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds;

	@Autowired
	private UserAccessDetailsService userAccessDetailsService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(Long userId, String username, boolean anonymoous, Collection<String> roles) {
		if (roles == null || roles.size() == 0) {
			roles = Arrays.asList(UserRoleAccess.ROLES.ROLE_ANONNYMOUS.toString());
		}

		Claims claims = Jwts.claims().setSubject(userId + "");
		claims.put("username", username);
		claims.put("anonymous", anonymoous);
		claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s)).filter(Objects::nonNull)
				.collect(Collectors.toList()));

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();

	}

	public Authentication getAuthentication(String token) {
		String username = token != null ? getUserId(token) : null;
		UserDetails userDetails = userAccessDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUserId(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			// seperate word bearer from token and send only the actual content
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token, HttpServletRequest httpServletRequest) {
		Map<String, String> errorOutputs = new HashMap<>();
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
			errorOutputs.put("invalid_jwt_signature", Translator.toLocale("invalid_jwt_signature"));
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			errorOutputs.put("invalid_jwt_token", Translator.toLocale("invalid_jwt_token"));
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
			errorOutputs.put("jwt_token_expired", Translator.toLocale("jwt_token_expired"));
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
			errorOutputs.put("jwt_token_unsupported", Translator.toLocale("jwt_token_unsupported"));
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
			errorOutputs.put("illegal_jwt_token", Translator.toLocale("illegal_jwt_token"));
		}
		httpServletRequest.setAttribute("errorOutputs", errorOutputs);

		return false;
	}
}
