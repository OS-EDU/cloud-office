package org.erxi.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.erxi.model.system.SysUser;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
