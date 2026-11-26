package com.sqx.domain.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.MessageInfo;
import com.sqx.domain.db.mapper.MessageInfoMapper;
import com.sqx.domain.db.service.IMessageInfoService;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author sqx
 * @since 2025-05-19 15:35:07
 */
@Service
public class IMessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements IMessageInfoService {

}
