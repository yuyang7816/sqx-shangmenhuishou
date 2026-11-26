package com.sqx.app.messages.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.messages.domain.bo.MessagesInfoBo;
import com.sqx.app.messages.domain.bo.MessagesPageBo;
import com.sqx.app.messages.domain.bo.MessagesSaveBo;
import com.sqx.app.messages.domain.bo.MessagesUpdateBo;
import com.sqx.app.messages.domain.vo.MessageDataVo;
import com.sqx.app.messages.domain.vo.MessagesVo;
import com.sqx.app.messages.service.MessagesService;
import com.sqx.common.core.domain.Result;
import com.sqx.common.shiro.app.annotation.Login;
import com.sqx.common.shiro.utils.UserContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息信息表 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-15 16:28:04
 */
@Tag(name = "用户端-消息信息表", description = "用户端-消息信息表")
@RestController
@RequestMapping("/app/messages")
public class AppMessagesController {

    @Resource
    private MessagesService messagesService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated MessagesSaveBo messagesSaveBo) {
        messagesService.saveInfo(messagesSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Login
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = MessagesVo.class)))
    @PostMapping("page")
    public Result<IPage<MessagesVo>> page(@RequestBody @Validated MessagesPageBo messagesPageBo) {
        messagesPageBo.setUserId(UserContextHolder.getAppUserId());
        IPage<MessagesVo> voIPage = messagesService.pageInfo(messagesPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = MessagesVo.class)))
    @PostMapping("info")
    public Result<MessagesVo> info(@RequestBody @Validated MessagesInfoBo messagesInfoBo) {
        MessagesVo vo = messagesService.info(messagesInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated MessagesUpdateBo messagesUpdateBo) {
        messagesService.updateInfo(messagesUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated MessagesInfoBo messagesInfoBo) {
        messagesService.delete(messagesInfoBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "消息数据")
    @GetMapping("data")
    public Result<List<MessageDataVo>> data() {
        List<MessageDataVo> voList = messagesService.data(UserContextHolder.getAppUserId());
        return Result.success(voList);
    }
}
