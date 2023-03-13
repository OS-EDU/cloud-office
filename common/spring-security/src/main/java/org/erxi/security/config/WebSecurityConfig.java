package org.erxi.security.config;

import org.erxi.security.custom.CustomMd5PasswordEncoder;
import org.erxi.security.filter.TokenAuthenticationFilter;
import org.erxi.security.filter.TokenLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomMd5PasswordEncoder customMd5PasswordEncoder;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 关闭 csrf 跨站请求伪造
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                //.antMatchers("/admin/system/index/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilter(new TokenLoginFilter(authenticationManager(), redisTemplate));

        // 禁用 session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定 UserDetailService 和加密器
        auth.userDetailsService(userDetailsService).passwordEncoder(customMd5PasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * 排除 swagger 相关请求
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/admin/modeler/**", "/diagram-viewer/**", "/editor-app/**", "/*.html",
                "/admin/processImage/**",
                "/admin/wechat/authorize", "/admin/wechat/userInfo", "/admin/wechat/bindPhone",
                "/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html");
    }
}
