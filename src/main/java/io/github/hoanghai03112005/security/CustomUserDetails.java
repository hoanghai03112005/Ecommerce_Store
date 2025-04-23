package io.github.hoanghai03112005.security;



import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



import io.github.hoanghai03112005.models.User;

public class CustomUserDetails implements UserDetails{
	private final User user;
	private final Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);
	public CustomUserDetails(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		System.out.println("🔍 Role assigned customUserService: " + user.getRole().name());
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}
	
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public String getFullName() {
        return user.getName();
    }
}

