package org.erxi.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.erxi.auth.service.SysMenuService;
import org.erxi.auth.service.SysUserService;
import org.erxi.common.config.exception.CustomException;
import org.erxi.common.jwt.JwtHelper;
import org.erxi.common.result.Result;
import org.erxi.model.system.SysUser;
import org.erxi.vo.system.LoginVo;
import org.erxi.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台登录退出
 */
@Api(tags = "后台管理系统")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation(value = "login")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {

        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(wrapper);

        if (null == sysUser) {
            throw new CustomException(201, "用户不存在");
        }

        String password_db = sysUser.getPassword(); // 当前获取的密码为数据库中的密码

        String password_input = loginVo.getPassword();

        if (!password_input.equals(password_db)) {
            throw new CustomException(201, "密码错误");
        }

        // 判断用户状态 1：可用 0：禁用
        if (sysUser.getStatus() == 0) {
            throw new CustomException(201, "该用户已被禁用");
        }

        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);

        return Result.ok(map);
    }

    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");

        Long userId = JwtHelper.getUserId(token);
        SysUser sysUser = sysUserService.getById(userId);

        List<RouterVo> routerVoList = sysMenuService.findUserMenuListByUserId(userId);
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://avatars.githubusercontent.com/u/45645138?v=4");
        map.put("routers", routerVoList);
        map.put("buttons", permsList);
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
