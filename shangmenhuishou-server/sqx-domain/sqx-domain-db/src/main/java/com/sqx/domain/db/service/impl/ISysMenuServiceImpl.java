package com.sqx.domain.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.domain.db.entity.SysMenuEntity;
import com.sqx.domain.db.mapper.SysMenuDao;
import com.sqx.domain.db.service.ISysMenuService;
import org.springframework.stereotype.Service;


@Service
public class ISysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements ISysMenuService {

}
