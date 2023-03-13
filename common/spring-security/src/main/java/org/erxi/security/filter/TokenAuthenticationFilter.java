package org.erxi.security.filter;

import com.alibaba.fastjson.JSON;
import org.erxi.common.jwt.JwtHelper;
import org.erxi.common.result.ResponseUtil;
import org.erxi.common.result.Result;
import org.erxi.common.result.ResultCodeEnum;
import org.erxi.security.custom.LoginUserInfoHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 认证解析 token 过滤器
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // 如果是登录接口，直接放行
        if ("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 请求头是否有 token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)) {
                // 当前用户信息放到 ThreadLocal 里面
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
                LoginUserInfoHelper.setUsername(username);

                // 通过 username 从 redis 获取权限数据
                String authString = (String) redisTemplate.opsForValue().get(username);
                // 把 redis 获取字符串权限数据转换要求集合类型 List<SimpleGrantedAuthority>
                if (!StringUtils.isEmpty(authString)) {
                    List<Map> mapList = JSON.parseArray(authString, Map.class);
                    System.out.println(mapList);
                    List<SimpleGrantedAuthority> authList = new ArrayList<>();
                    for (Map map : mapList) {
                        String authority = (String) map.get("authority");
                        authList.add(new SimpleGrantedAuthority(authority));
                    }
                    return new UsernamePasswordAuthenticationToken(username, null, authList);
                } else {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }
            }
        }
        return null;
    }
}
