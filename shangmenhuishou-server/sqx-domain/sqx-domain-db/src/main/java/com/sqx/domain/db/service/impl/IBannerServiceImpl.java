package com.sqx.domain.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.exception.SqxException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.domain.db.entity.Banner;
import com.sqx.domain.db.mapper.BannerMapper;
import com.sqx.domain.db.service.IBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

/**
 * banner 服务实现类
 *
 * @author sqx
 * @since 2025-05-16 13:49:04
 */
@Service
public class IBannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {
}
