package org.erxi.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.erxi.auth.service.SysUserService;
import org.erxi.common.Result;
import org.erxi.model.system.SysUser;
import org.erxi.vo.system.SysUserQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    // 用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryUser(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysUserQueryVo sysUserQueryVo) {
        Page<SysUser> pageParam = new Page<>(page, limit);

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        // 获取条件值
        String keyword = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        // like 模糊查询
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like(SysUser::getUsername, keyword);
        }
        // ge 大于等于
        if (!StringUtils.isEmpty(createTimeBegin)) {
            queryWrapper.ge(SysUser::getCreateTime, createTimeBegin);
        }
        // le 小于等于
        if (!StringUtils.isEmpty(createTimeEnd)) {
            queryWrapper.le(SysUser::getCreateTime, createTimeEnd);
        }

        Page<SysUser> pageModel = sysUserService.page(pageParam, queryWrapper);

        return Result.ok(pageModel);
    }

    @ApiOperation("添加用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        boolean is_success = sysUserService.save(user);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据 id 查询用户")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return Result.ok(sysUser);
    }

    @ApiOperation("更新用户")
    @PutMapping("update")
    public Result update(@RequestBody SysUser user) {
        boolean is_success = sysUserService.updateById(user);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据 id 删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_success = sysUserService.removeById(id);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }
}
