package com.sqx.app.recruitment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.recruitment.db.service.IRecruitmentService;
import com.sqx.app.recruitment.domain.bo.*;
import com.sqx.app.recruitment.domain.entity.Recruitment;
import com.sqx.app.recruitment.domain.vo.RecruitmentVo;
import com.sqx.app.recruitment.service.RecruitmentService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.common.shiro.utils.UserContextHolder;
import com.sqx.domain.db.entity.UserEntity;
import com.sqx.domain.db.service.IUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 招募 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
@Service
public class RecruitmentServiceImpl implements RecruitmentService {

    @Resource
    private IRecruitmentService iRecruitmentService;
    @Resource
    private IUserService iUserService;

    @Override
    public void saveInfo(RecruitmentSaveBo recruitmentSaveBo) {
        Recruitment recruitment = new Recruitment();
        BeanUtils.copyProperties(recruitmentSaveBo, recruitment);

        if (checkExistsByUserId(recruitmentSaveBo.getUserId())) {
            throw new BizException("只能发布一条招募信息");
        }

        recruitment.setStatus(0);
        iRecruitmentService.save(recruitment);
    }

    private boolean checkExistsByUserId(Long userId) {
        LambdaQueryWrapper<Recruitment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Recruitment::getUserId, userId);

        return iRecruitmentService.exists(queryWrapper);
    }

    @Override
    public IPage<RecruitmentVo> pageInfo(RecruitmentPageBo recruitmentPageBo) {
        Page<Recruitment> initPage = PageUtil.initPage(recruitmentPageBo.getPage(), recruitmentPageBo.getLimit());

        QueryWrapper<Recruitment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(recruitmentPageBo.getUserId()), Recruitment::getUserId, recruitmentPageBo.getUserId())
                .like(StringUtils.isNotBlank(recruitmentPageBo.getCity()), Recruitment::getCity, recruitmentPageBo.getCity())
                .like(StringUtils.isNotBlank(recruitmentPageBo.getName()), Recruitment::getName, recruitmentPageBo.getName())
                .like(StringUtils.isNotBlank(recruitmentPageBo.getPhone()), Recruitment::getPhone, recruitmentPageBo.getPhone())
                .eq(Objects.nonNull(recruitmentPageBo.getStatus()), Recruitment::getStatus, recruitmentPageBo.getStatus())
                .orderByDesc(Recruitment::getCreateTime);

        IPage<Recruitment> iPage = iRecruitmentService.page(initPage, queryWrapper);
        List<RecruitmentVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (Recruitment record : iPage.getRecords()) {
                RecruitmentVo vo = new RecruitmentVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<RecruitmentVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public RecruitmentVo info(RecruitmentInfoBo recruitmentInfoBo) {
        Recruitment recruitment = iRecruitmentService.getById(recruitmentInfoBo.getId());
        if (Objects.isNull(recruitment)) {
            throw new BizException("信息不存在");
        }

        RecruitmentVo vo = new RecruitmentVo();
        BeanUtils.copyProperties(recruitment, vo);
        return vo;
    }

    @Override
    public void updateInfo(RecruitmentUpdateBo recruitmentUpdateBo) {
        Recruitment recruitment = new Recruitment();
        BeanUtils.copyProperties(recruitmentUpdateBo, recruitment);

        iRecruitmentService.updateById(recruitment);
    }

    @Override
    public void delete(RecruitmentInfoBo recruitmentInfoBo) {
        iRecruitmentService.removeById(recruitmentInfoBo.getId());
    }

    @Override
    public void audit(RecruitmentAuditBo recruitmentAuditBo) {
        Recruitment recruitment = iRecruitmentService.getById(recruitmentAuditBo.getId());
        if (Objects.isNull(recruitment)) {
            throw new BizException("招募信息不存在");
        }

        if (1 == recruitmentAuditBo.getStatus()) {
            iUserService.update(new UserEntity(), new LambdaUpdateWrapper<UserEntity>()
                    .eq(UserEntity::getUserId, recruitment.getUserId())
                    .set(UserEntity::getUserType, 2)
            );
        }

        LambdaUpdateWrapper<Recruitment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Recruitment::getId, recruitmentAuditBo.getId())
                .set(Recruitment::getStatus, recruitmentAuditBo.getStatus())
                .set(Recruitment::getReason, recruitmentAuditBo.getReason());

        iRecruitmentService.update(new Recruitment(), updateWrapper);
    }

    @Override
    public RecruitmentVo myInfo() {
        LambdaQueryWrapper<Recruitment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Recruitment::getUserId, UserContextHolder.getAppUserId());

        Recruitment recruitment = iRecruitmentService.getOne(queryWrapper);
        if (Objects.isNull(recruitment)) {
            return null;
        }

        RecruitmentVo vo = new RecruitmentVo();
        BeanUtils.copyProperties(recruitment, vo);

        return vo;
    }
}
