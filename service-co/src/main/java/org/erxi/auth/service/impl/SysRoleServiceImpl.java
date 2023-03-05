package org.erxi.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.erxi.auth.mapper.SysRoleMapper;
import org.erxi.auth.mapper.SysUserRoleMapper;
import org.erxi.auth.service.SysRoleService;
import org.erxi.model.system.SysRole;
import org.erxi.model.system.SysUserRole;
import org.erxi.vo.system.AssignRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public Map<String, Object> findRoleByAdminId(Long userId) {
        // 查询所有的角色
        List<SysRole> allRolesList = this.list();

        // 拥有的角色 id
        List<SysUserRole> existUserRoleList = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId).select(SysUserRole::getRoleId));
        List<Long> existRoleIdList = existUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        // 对角色进行分类
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole role : allRolesList) {
            // 已分配
            if (existRoleIdList.contains(role.getId())) {
                assignRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", assignRoleList);
        roleMap.put("allRolesList", allRolesList);

        return roleMap;
    }

    @Transactional
    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, assignRoleVo.getUserId()));

        for (Long roleId : assignRoleVo.getRoleIdList()) {
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assignRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleMapper.insert(sysUserRole);
        }
    }
}
