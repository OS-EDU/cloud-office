package org.erxi.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.erxi.auth.mapper.SysUserMapper;
import org.erxi.auth.service.SysUserService;
import org.erxi.model.system.SysUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private SysUserMapper sysUserMapper;

    @Transactional
    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = this.getById(id);
        if (status.intValue() == 1) {
            sysUser.setStatus(status);
        } else {
            sysUser.setStatus(0);
        }
        this.updateById(sysUser);
    }
}
