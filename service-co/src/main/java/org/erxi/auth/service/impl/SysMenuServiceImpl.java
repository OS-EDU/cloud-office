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
import org.erxi.vo.system.MetaVo;
import org.erxi.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;

        // 默认 userId == 1 为管理员，可根据实际生产环境自定义
        if (userId.longValue() == 1) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = sysMenuMapper.selectList(wrapper);
        } else {
            sysMenuList = sysMenuMapper.findMenuListByUserId(userId);
        }

        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        // 构建成框架要求的路由结构
        List<RouterVo> routerList = this.buildRouter(sysMenuTreeList);
        return routerList;
    }

    private List<RouterVo> buildRouter(List<SysMenu> menus) {

        // 创建 list 集合，存储最终数据
        List<RouterVo> routers = new ArrayList<>();
        // menus 遍历
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            // 下一层数据部分
            List<SysMenu> children = menu.getChildren();
            if (menu.getType().intValue() == 1) {
                // 加载出来下面隐藏路由
                List<SysMenu> hiddenMenuList = children.stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    // true 隐藏路由
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));

                    routers.add(hiddenRouter);
                }

            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    //递归
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    private String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    @Override
    public List<String> findUserPermsByUserId(Long userId) {

        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            sysMenuList = baseMapper.selectList(wrapper);
        } else {
            // 如果不是管理员，根据 userId 查询可以操作按钮列表
            // 多表关联查询：用户角色关系表 、 角色菜单关系表、 菜单表
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }

        // 从查询出来的数据里面，获取可以操作按钮值的 list 集合，返回
        List<String> permsList = sysMenuList.stream()
                .filter(item -> item.getType() == 2)
                .map(item -> item.getPerms())
                .collect(Collectors.toList());

        return permsList;
    }


}
