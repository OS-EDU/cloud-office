package org.erxi.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.erxi.model.system.SysRole;
import org.erxi.vo.system.AssignRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户获取角色数据
     * @param userId 用户 ID
     * @return 角色信息
     */
    Map<String, Object> findRoleByAdminId(Long userId);

    /**
     * 分配角色
     * @param assignRoleVo
     */
    void doAssign(AssignRoleVo assignRoleVo);
}
