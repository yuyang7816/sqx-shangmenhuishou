package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysDictEntity;
import com.sqx.domain.db.mapper.SysDictDao;
import com.sqx.domain.db.service.ISysDictService;
import org.springframework.stereotype.Service;


@Service
public class ISysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements ISysDictService {

}
