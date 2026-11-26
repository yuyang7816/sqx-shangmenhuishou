package com.sqx.app.messages.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.messages.db.service.IMessagesService;
import com.sqx.app.messages.domain.bo.MessagesInfoBo;
import com.sqx.app.messages.domain.bo.MessagesPageBo;
import com.sqx.app.messages.domain.bo.MessagesSaveBo;
import com.sqx.app.messages.domain.bo.MessagesUpdateBo;
import com.sqx.app.messages.domain.entity.Messages;
import com.sqx.app.messages.domain.vo.MessageDataVo;
import com.sqx.app.messages.domain.vo.MessagesVo;
import com.sqx.app.messages.enums.MessageTypeEnum;
import com.sqx.app.messages.service.MessagesService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 消息信息表 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-15 16:28:04
 */
@Service
public class MessagesServiceImpl implements MessagesService {

    @Resource
    private IMessagesService iMessagesService;

    @Override
    public void saveInfo(MessagesSaveBo messagesSaveBo) {
        Messages messages = new Messages();
        BeanUtils.copyProperties(messagesSaveBo, messages);

        iMessagesService.save(messages);
    }

    @Override
    public IPage<MessagesVo> pageInfo(MessagesPageBo messagesPageBo) {
        Page<Messages> initPage = PageUtil.initPage(messagesPageBo.getPage(), messagesPageBo.getLimit());

        QueryWrapper<Messages> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(messagesPageBo.getUserId()), Messages::getUserId, messagesPageBo.getUserId())
                .eq(Objects.nonNull(messagesPageBo.getType()), Messages::getType, messagesPageBo.getType())
                .orderByDesc(Messages::getCreateTime);

        IPage<Messages> iPage = iMessagesService.page(initPage, queryWrapper);
        List<MessagesVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (Messages record : iPage.getRecords()) {
                MessagesVo vo = new MessagesVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<MessagesVo> voIPage = PageUtil.copyPage(iPage, voList);

        // 改成已读
        iMessagesService.updateStatus(messagesPageBo.getUserId(), 1);
        return voIPage;
    }

    @Override
    public MessagesVo info(MessagesInfoBo messagesInfoBo) {
        Messages messages = iMessagesService.getById(messagesInfoBo.getId());
        if (Objects.isNull(messages)) {
            throw new BizException("信息不存在");
        }

        // 标记为已读
        messages.setIsRead(1);
        iMessagesService.updateById(messages);

        MessagesVo vo = new MessagesVo();
        BeanUtils.copyProperties(messages, vo);
        return vo;
    }

    @Override
    public void updateInfo(MessagesUpdateBo messagesUpdateBo) {
        Messages messages = new Messages();
        BeanUtils.copyProperties(messagesUpdateBo, messages);

        iMessagesService.updateById(messages);
    }

    @Override
    public void delete(MessagesInfoBo messagesInfoBo) {
        iMessagesService.removeById(messagesInfoBo.getId());
    }

    @Override
    public List<MessageDataVo> data(Long userId) {
        LambdaQueryWrapper<Messages> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Messages::getUserId, userId)
                .eq(Messages::getIsRead, 0);

        List<Messages> messagesList = iMessagesService.list(queryWrapper);

        List<MessageDataVo> voList = new ArrayList<>();
        for (MessageTypeEnum messageType : MessageTypeEnum.values()) {
            MessageDataVo vo = new MessageDataVo();
            vo.setType(messageType.getCode());
            vo.setUnreadCount(0L);

            voList.add(vo);
        }

        if (CollectionUtils.isEmpty(messagesList)) {
            return voList;
        }

        Map<Integer, Long> messagesMap = messagesList.stream()
                .collect(Collectors.groupingBy(Messages::getType, Collectors.counting()));

        for (MessageDataVo vo : voList) {
            if (messagesMap.containsKey(vo.getType())) {
                vo.setUnreadCount(messagesMap.get(vo.getType()));
            }
        }

        return voList;
    }
}
