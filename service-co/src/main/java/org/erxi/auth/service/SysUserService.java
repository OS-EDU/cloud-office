package org.erxi.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.erxi.model.system.SysUser;

public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    SysUser getUserByUsername(String username);

}
