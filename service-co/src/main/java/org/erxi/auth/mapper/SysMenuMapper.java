package org.erxi.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.erxi.model.system.SysMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户 id 查询菜单列表
     * 三张表关联查询：用户角色关系表、角色菜单关系表、菜单表
     * @param userId 用户 id
     * @return 菜单列表
     */
    List<SysMenu> findMenuListByUserId(@Param("userId") Long userId);

}
