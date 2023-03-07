package org.erxi.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.erxi.auth.service.SysMenuService;
import org.erxi.common.Result;
import org.erxi.model.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("获取菜单")
    @GetMapping("findNodes")
    public Result findNode() {
        List<SysMenu> menuList = sysMenuService.findNodes();
        return Result.ok(menuList);
    }

    @ApiOperation("新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu menu) {
        boolean is_success = sysMenuService.save(menu);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu menu) {
        boolean is_success = sysMenuService.updateById(menu);
        if (is_success)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }
}
