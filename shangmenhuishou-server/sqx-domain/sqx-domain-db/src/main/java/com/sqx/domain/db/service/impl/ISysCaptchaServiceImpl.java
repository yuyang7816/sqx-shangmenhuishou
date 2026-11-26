package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysCaptchaEntity;
import com.sqx.domain.db.mapper.SysCaptchaDao;
import com.sqx.domain.db.service.ISysCaptchaService;
import org.springframework.stereotype.Service;

/**
 * 验证码
 *
 */
@Service
public class ISysCaptchaServiceImpl extends ServiceImpl<SysCaptchaDao, SysCaptchaEntity> implements ISysCaptchaService {

}
