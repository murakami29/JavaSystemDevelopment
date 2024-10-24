package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.UserService;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/resources/**", "/store/details").permitAll()
                // 管理者権限が必要なURLを指定
                .requestMatchers("/admin/edit/**", "/admin/delete/**", "/admin/create/**")
                	.hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> 
	            formLogin
	                .loginPage("/login") // カスタムのログインページを指定
	                .loginProcessingUrl("/login") // フォームのアクションに指定したURL
	                .usernameParameter("email") // emailフィールドをユーザ名として使用
	                .passwordParameter("password") // パスワードフィールド
	                .defaultSuccessUrl("/home", true) // ログイン成功後のデフォルトURL
	                .failureUrl("/login?error=true") // ログイン失敗時にエラーを表示
	                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
