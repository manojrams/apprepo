package com.example.h5api.config;

import com.example.h5api.filter.CorsFilterExample;
import com.example.h5api.jwt.JwtAuthenticationEntryPoint;
import com.example.h5api.filter.JwtRequestFilter;
import com.example.h5api.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());

    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPointBean() throws Exception {
        return new JwtAuthenticationEntryPoint();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();

    }

    @Bean
    CorsFilterExample corsFilterExample() {
        CorsFilterExample filter = new CorsFilterExample();
        return filter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();
        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .antMatchers(HttpMethod.GET, "/user/{\\d+}").hasRole("ADMIN") // need to modify
                .antMatchers(HttpMethod.GET, "/user/list").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/user/list/api*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/user*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/value/list/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/value/list/api*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/nomination").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/nomination/list/").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/nomination/list/api").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/nomination/summary/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/nomination/drawWinners").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/nomination/drawWinners/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/campaign/").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/campaign/enable/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/campaign/list/api").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/campaign/get/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/winner").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/winner/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/winner/hasRepeat").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/winner/hasRepeat/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/winner/findByCampaignId/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/winner/list/").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/winner/list/all").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/winner/list/api").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/user/list").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(corsFilterExample(), SessionManagementFilter.class);

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

}
