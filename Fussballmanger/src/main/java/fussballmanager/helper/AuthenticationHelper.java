package fussballmanager.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthenticationHelper {

	public static boolean isAdmin(Authentication auth) {
		return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}
