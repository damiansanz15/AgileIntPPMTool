package com.damianIntelligence.ppmtool.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.damianIntelligence.ppmtool.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	//generate the token
	
	public String generateToken(Authentication authentication) {
		User user=(User)authentication.getPrincipal();
		Date now=new Date(System.currentTimeMillis());
		Date expiryDate=new Date(now.getTime()+SecurityConstants.EXPIRATION_TIME);
		String userId=Long.toString(user.getId());
		
		Map<String,Object> claims=new HashMap<>();
		claims.put("id",(Long.toString(user.getId())));
		claims.put("username", user.getUsername());
		claims.put("fullName", user.getFullName());
		
		return Jwts.builder()
				.setSubject(userId)
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
				.compact();
		
	}
	//validate the token
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			System.out.println("Invalid JWT signature");
		}catch(MalformedJwtException ex) {
			System.out.println("Invalid JTW token");	
		}catch(ExpiredJwtException ex) {
			System.out.println("Expired JWT token");
		}catch(UnsupportedJwtException ex) {
			System.out.println("unsupported expection");
		}catch (IllegalArgumentException ex) {
			System.out.println("JWT claims string is empty");
		}
		return false;
	}
	public Long getUserIdFromJWT(String token) {
		Claims claims=Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
		String id=(String) claims.get("id");
		
		return Long.parseLong(id);
	}
	
	
	
	
	
	//get the user id from the token
	
	
	
	
}
