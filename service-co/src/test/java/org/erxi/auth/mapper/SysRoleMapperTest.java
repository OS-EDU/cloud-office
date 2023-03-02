package org.erxi.auth.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.erxi.model.system.SysRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * insert one role
     *
     * @return SysRole
     */
    private SysRole insertOne() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("this is a role manager");
        sysRoleMapper.insert(sysRole);
        return sysRole;
    }

    @Test
    public void testUpdate() {
        // insertOne
        SysRole role = insertOne();

        // update
        role.setRoleName("角色管理员 1.0");
        role.setRoleCode("roleManager");
        role.setDescription("this is a role manager~~");
        int update = sysRoleMapper.updateById(role);
        Assertions.assertEquals(update, 1);
    }

    @Test
    public void testDelete() {
        // insertOne
        SysRole role = insertOne();
        int delete = sysRoleMapper.deleteById(role.getId());
        Assertions.assertEquals(delete, 1);
    }

    @Test
    public void testSelectByIds() {
        // insertOne
        SysRole role = insertOne();
        ArrayList<Long> roleIds = new ArrayList<>();
        roleIds.add(role.getId());
        Assertions.assertFalse(roleIds.isEmpty());
    }

    @Test
    public void testQueryWrapper() {
        SysRole sysRole = insertOne();
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", "role");
        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        Assertions.assertFalse(sysRoles.isEmpty());
    }

    @Test
    public void testLambdaQueryWrapper() {
        SysRole sysRole = insertOne();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleCode, "role");
        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        Assertions.assertFalse(sysRoles.isEmpty());
    }

}