package org.erxi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.erxi.auth.mapper.SysMenuMapper;
import org.erxi.auth.service.SysMenuService;
import org.erxi.auth.utils.MenuHelper;
import org.erxi.common.config.exception.CustomException;
import org.erxi.model.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

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


}
