package com.sqx.app.base.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.constants.BaseConstant;
import com.sqx.app.base.user.domain.bo.ListUserBO;
import com.sqx.app.base.user.domain.bo.UpdateUserBO;
import com.sqx.app.base.user.domain.bo.UpdateUserDepositBO;
import com.sqx.app.base.user.domain.vo.UserVo;
import com.sqx.app.base.user.service.UserService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.db.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author chengming
 * @since 2025/4/24
 */
@Tag(name = "管理端-用户", description = "管理端-用户")
@RestController
@RequestMapping(BaseConstant.BASE_ADMIN_PREFIX + "/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户列表")
    @PostMapping("list")
    public Result<Page<UserEntity>> list(@RequestBody @Validated ListUserBO listUserBO) {
        Page<UserEntity> userEntityList = userService.list(listUserBO);
        return Result.success(userEntityList);
    }

    @Operation(summary = "用户信息")
    @GetMapping("info")
    public Result<UserVo> info(@NotNull(message = "用户id不能为空") Long userId) {
        return userService.info(userId);
    }

    @Operation(summary = "修改用户信息")
    @PostMapping("update")
    public Result<Page<UserEntity>> update(@RequestBody @Validated UpdateUserBO updateUserBO) {
        userService.update(updateUserBO);
        return Result.success();
    }

    @Operation(summary = "修改用户保证金")
    @PostMapping("updateDeposit")
    public Result<Page<UserEntity>> updateDeposit(@RequestBody @Validated UpdateUserDepositBO updateUserDepositBO) {
        userService.updateDeposit(updateUserDepositBO);
        return Result.success();
    }

    @Operation(summary = "注销账号")
    @GetMapping("delete")
    public Result<Void> delete(Long userId) {
        return userService.delete(userId);
    }
}
