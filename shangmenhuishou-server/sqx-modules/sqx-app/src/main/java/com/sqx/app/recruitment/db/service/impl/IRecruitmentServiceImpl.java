package com.sqx.app.recruitment.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqx.app.recruitment.domain.entity.Recruitment;
import com.sqx.app.recruitment.db.mapper.RecruitmentMapper;
import com.sqx.app.recruitment.db.service.IRecruitmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 招募 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@Service
public class IRecruitmentServiceImpl extends ServiceImpl<RecruitmentMapper, Recruitment> implements IRecruitmentService {

    @Override
    public Recruitment getByUserId(Long userId) {
        LambdaQueryWrapper<Recruitment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Recruitment::getUserId, userId);

        return this.getOne(queryWrapper);
    }
}
