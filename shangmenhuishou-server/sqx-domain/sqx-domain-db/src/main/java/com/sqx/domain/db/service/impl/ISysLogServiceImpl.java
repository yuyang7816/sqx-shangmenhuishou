package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysLogEntity;
import com.sqx.domain.db.mapper.SysLogDao;
import com.sqx.domain.db.service.ISysLogService;
import org.springframework.stereotype.Service;


@Service
public class ISysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements ISysLogService {

}
