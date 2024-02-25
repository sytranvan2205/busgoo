package com.iuh.busgoo.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.iuh.busgoo.secirity.CustomUserDetail;
import com.iuh.busgoo.secirity.UserDetailServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	private final String JWT_SECRET = "xys2205";
	private final long JWT_EXPIRATION  = 604800000L;
	
//	@Autowired
//	private UserService userService;
	
	@Autowired
	private UserDetailServiceImpl userDetailService;
	
	public String generateToken(CustomUserDetail customUserDetail) {
		try {
			Date now = new Date();
			Date expriryDate = new Date(now.getTime()+ JWT_EXPIRATION);
			return Jwts.builder().setSubject(customUserDetail.getAccount().getEmail())
					.setIssuedAt(now)
					.setExpiration(expriryDate)
					.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
					.compact();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean validateToken (String authToken) throws Exception {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (Exception e) {
			throw new Exception("Can not parse Token");
		}
	}
	
	public UserDetails getUserDetailFromJwt(String token) {
		Claims claims = Jwts.parser()
						.setSigningKey(JWT_SECRET)
						.parseClaimsJws(token)
						.getBody();
		String email = claims.getSubject().toString();
		return userDetailService.loadUserByUsername(email);
	}
}
