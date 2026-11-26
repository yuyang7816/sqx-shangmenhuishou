package com.sqx.app.messages.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.messages.domain.bo.MessagesInfoBo;
import com.sqx.app.messages.domain.bo.MessagesPageBo;
import com.sqx.app.messages.domain.bo.MessagesSaveBo;
import com.sqx.app.messages.domain.bo.MessagesUpdateBo;
import com.sqx.app.messages.domain.vo.MessageDataVo;
import com.sqx.app.messages.domain.vo.MessagesVo;

import java.util.List;

/**
 * 消息信息表 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-15 16:28:04
 */
public interface MessagesService {

    void saveInfo(MessagesSaveBo messagesSaveBo);

    IPage<MessagesVo> pageInfo(MessagesPageBo messagesPageBo);

    MessagesVo info(MessagesInfoBo messagesInfoBo);

    void updateInfo(MessagesUpdateBo messagesUpdateBo);

    void delete(MessagesInfoBo messagesInfoBo);

    List<MessageDataVo> data(Long userId);
}
