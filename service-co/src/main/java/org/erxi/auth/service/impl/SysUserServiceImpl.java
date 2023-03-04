package org.erxi.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.erxi.auth.mapper.SysUserMapper;
import org.erxi.auth.service.SysUserService;
import org.erxi.model.system.SysUser;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private SysUserMapper sysUserMapper;
}
