package com.sqx.app.base.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.message.domain.bo.MessageInfoInfoBo;
import com.sqx.app.base.message.domain.bo.MessageInfoPageBo;
import com.sqx.app.base.message.domain.bo.MessageInfoSaveBo;
import com.sqx.app.base.message.domain.bo.MessageInfoUpdateBo;
import com.sqx.app.base.message.domain.vo.MessageInfoVo;
import com.sqx.app.base.message.service.MessageInfoService;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.exception.SqxException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.domain.db.entity.MessageInfo;
import com.sqx.domain.db.service.IMessageInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author chengming
 * @since 2025/5/19
 */
@Service
public class MessageInfoServiceImpl implements MessageInfoService {

    @Resource
    private IMessageInfoService iMessageInfoService;

    @Override
    public Result<Void> saveInfo(MessageInfoSaveBo messageInfoSaveBo) {
        MessageInfo messageInfo = new MessageInfo();
        BeanUtils.copyProperties(messageInfoSaveBo, messageInfo);

        iMessageInfoService.save(messageInfo);
        return Result.success();
    }

    @Override
    public Result<IPage<MessageInfoVo>> pageInfo(MessageInfoPageBo messageInfoPageBo) {
        Page<MessageInfo> initPage = PageUtil.initPage(messageInfoPageBo.getPage(), messageInfoPageBo.getLimit());

        QueryWrapper<MessageInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(messageInfoPageBo.getType()), MessageInfo::getType, messageInfoPageBo.getType())
                .orderByDesc(MessageInfo::getCreateTime);

        IPage<MessageInfo> iPage = iMessageInfoService.page(initPage, queryWrapper);
        List<MessageInfoVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (MessageInfo record : iPage.getRecords()) {
                MessageInfoVo vo = new MessageInfoVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<MessageInfoVo> voIPage = PageUtil.copyPage(iPage, voList);
        return Result.success(voIPage);
    }

    @Override
    public Result<MessageInfoVo> info(MessageInfoInfoBo messageInfoInfoBo) {
        MessageInfo messageInfo = iMessageInfoService.getById(messageInfoInfoBo.getId());
        if (Objects.isNull(messageInfo)) {
            throw new SqxException("信息不存在");
        }

        MessageInfoVo vo = new MessageInfoVo();
        BeanUtils.copyProperties(messageInfo, vo);
        return Result.success(vo);
    }

    @Override
    public Result<Void> updateInfo(MessageInfoUpdateBo messageInfoUpdateBo) {
        MessageInfo messageInfo = new MessageInfo();
        BeanUtils.copyProperties(messageInfoUpdateBo, messageInfo);

        iMessageInfoService.updateById(messageInfo);
        return Result.success();
    }

    @Override
    public Result<Void> delete(MessageInfoInfoBo messageInfoInfoBo) {
        iMessageInfoService.removeById(messageInfoInfoBo.getId());
        return Result.success();
    }
}
