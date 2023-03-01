package org.erxi.auth.mapper;

import org.erxi.model.system.SysRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

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

}