package com.app.expd.config;

import com.app.expd.exceptions.CustomAccessDeniedHandler;
import com.app.expd.models.Roles;
import com.app.expd.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/auth/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/subtalk/**")
         .hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
         .antMatchers(HttpMethod.GET, "/api/comments/**")
         .hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
         .antMatchers(HttpMethod.GET, "/api/posts/**")
         .hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
         .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**")
         .permitAll()
                .antMatchers(HttpMethod.POST, "/api/comments").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
                .antMatchers(HttpMethod.POST, "/api/subtalk").hasRole(Roles.ADMIN.toString())
                .antMatchers(HttpMethod.POST, "/api/posts").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
                .antMatchers(HttpMethod.POST, "/api/changePassowrd").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
                .antMatchers(HttpMethod.POST, "/api/vote").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, "/api/subtalk/**").hasRole(Roles.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, "/api/posts/**").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
                .antMatchers(HttpMethod.DELETE, "/api/comments/**").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .accessDeniedHandler(accessDeniedHandler());

        httpSecurity.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobalAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return  new CustomAccessDeniedHandler();
    }
}
