package org.erxi.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.erxi.model.system.SysMenu;
import org.erxi.vo.system.AssignMenuVo;
import org.erxi.vo.system.RouterVo;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    // 菜单树形数据
    List<SysMenu> findNodes();

    // 删除菜单
    void removeMenuById(Long id);

    List<SysMenu> findSysMenuByRoleId(Long roleId);

    void doAssign(AssignMenuVo assignMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);

    List<String> findUserPermsByUserId(Long userId);
}
