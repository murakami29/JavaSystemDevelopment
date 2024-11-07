package com.example.demo.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	
	private final com.example.demo.entity.User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(com.example.demo.entity.User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    
    // UserDetailsのメソッドをオーバーライド
    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // ここはアカウントが期限切れかどうかをチェックする
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // アカウントがロックされているかどうか
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 資格情報が期限切れかどうか
    }

    @Override
    public boolean isEnabled() {
        return true;  // ユーザーが有効かどうか
    }
    
    // カスタムメソッド
    public Long getId() {
        return user.getId();
    }
        
    public Long getStoreId() {
        return user.getStore() != null ? user.getStore().getId() : null;
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }
    
}
