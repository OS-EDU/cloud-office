package org.erxi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.errorprone.annotations.Var;
import org.erxi.auth.service.SysMenuService;
import org.erxi.auth.service.SysUserService;
import org.erxi.model.system.SysUser;
import org.erxi.security.custom.CustomUser;
import org.erxi.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByUsername(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if (sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用！");
        }

        // 根据 userId 查询用户操作权限数据
        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());
        // 创建 List 集合封装最终权限数据
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String perm : userPermsList) {
            authList.add(new SimpleGrantedAuthority(perm.trim()));
        }
        return new CustomUser(sysUser, authList);
    }

}
