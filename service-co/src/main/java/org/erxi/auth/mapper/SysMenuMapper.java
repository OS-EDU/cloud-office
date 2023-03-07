package org.erxi.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.erxi.model.system.SysMenu;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
}
