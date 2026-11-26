package com.sqx.domain.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.VerifyCode;
import com.sqx.domain.db.mapper.VerifyCodeMapper;
import com.sqx.domain.db.service.IVerifyCodeService;
import org.springframework.stereotype.Service;

/**
 * 验证码 服务实现类
 *
 * @author sqx
 * @since 2025-04-22 15:31:50
 */
@Service
public class IVerifyCodeServiceImpl extends ServiceImpl<VerifyCodeMapper, VerifyCode> implements IVerifyCodeService {

}
