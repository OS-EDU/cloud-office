package org.erxi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.erxi.auth.mapper.SysMenuMapper;
import org.erxi.auth.mapper.SysRoleMenuMapper;
import org.erxi.auth.service.SysMenuService;
import org.erxi.auth.utils.MenuHelper;
import org.erxi.common.config.exception.CustomException;
import org.erxi.model.system.SysMenu;
import org.erxi.model.system.SysRoleMenu;
import org.erxi.vo.system.AssignMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {

        List<SysMenu> sysMenuList = sysMenuMapper.selectList(null);

        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);

        return resultList;
    }

    @Override
    public void removeMenuById(Long id) {
        // 判断当前菜单是否有下一层
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer count = sysMenuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CustomException(201, "This can not delete...");
        }
        sysMenuMapper.deleteById(id);

    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        // 全部权限列表
        List<SysMenu> allSysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));

        // 根据角色 id 获取角色所属权限
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        // 转换给角色 id 与角色权限对应 Map 对象
        List<Long> menuIdList = sysRoleMenuList.stream()
                .map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        allSysMenuList.forEach(permission -> {
            permission.setSelect(menuIdList.contains(permission.getId()));
        });

        List<SysMenu> sysMenuList = MenuHelper.buildTree(allSysMenuList);

        return sysMenuList;
    }

    @Transactional
    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId());
        sysRoleMenuMapper.delete(wrapper);

        for (Long menuId : assignMenuVo.getMenuIdList()) {
            if (StringUtils.isEmpty(menuId)) {
                continue;
            }

            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(assignMenuVo.getRoleId());
            roleMenu.setMenuId(menuId);
            sysRoleMenuMapper.insert(roleMenu);

        }
    }


}
