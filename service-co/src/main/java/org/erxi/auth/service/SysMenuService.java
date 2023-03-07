package org.erxi.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.erxi.model.system.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    // 菜单树形数据
    List<SysMenu> findNodes();

    // 删除菜单
    void removeMenuById(Long id);

}
