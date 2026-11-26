package com.sqx.app.authentication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.authentication.db.service.IUserAuthenticationService;
import com.sqx.app.authentication.domain.bo.*;
import com.sqx.app.authentication.domain.entity.UserAuthentication;
import com.sqx.app.authentication.domain.vo.UserAuthenticationVo;
import com.sqx.app.authentication.service.UserAuthenticationService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.common.shiro.utils.UserContextHolder;
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
 * 用户实名认证 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Resource
    private IUserAuthenticationService iUserAuthenticationService;
    @Resource
    private IUserService iUserService;

    @Override
    public void saveInfo(UserAuthenticationSaveBo userAuthenticationSaveBo) {
        if (checkExistsByUserId(UserContextHolder.getAppUserId())) {
            throw new BizException("您已认证过， 无须再次认证");
        }

        UserAuthentication userAuthentication = new UserAuthentication();
        BeanUtils.copyProperties(userAuthenticationSaveBo, userAuthentication);
        userAuthentication.setStatus(1);
        userAuthentication.setUserId(UserContextHolder.getAppUserId());

        iUserAuthenticationService.save(userAuthentication);
    }

    private boolean checkExistsByUserId(Long appUserId) {
        LambdaQueryWrapper<UserAuthentication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAuthentication::getUserId, appUserId);

        return iUserAuthenticationService.exists(queryWrapper);
    }

    @Override
    public IPage<UserAuthenticationVo> pageInfo(UserAuthenticationPageBo userAuthenticationPageBo) {
        Page<UserAuthentication> initPage = PageUtil.initPage(userAuthenticationPageBo.getPage(), userAuthenticationPageBo.getLimit());

        QueryWrapper<UserAuthentication> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotBlank(userAuthenticationPageBo.getName()), UserAuthentication::getName, userAuthenticationPageBo.getName())
                .like(StringUtils.isNotBlank(userAuthenticationPageBo.getIdCard()), UserAuthentication::getIdCard, userAuthenticationPageBo.getIdCard())
                .eq(Objects.nonNull(userAuthenticationPageBo.getStatus()), UserAuthentication::getStatus, userAuthenticationPageBo.getStatus())
                .eq(Objects.nonNull(userAuthenticationPageBo.getGender()), UserAuthentication::getGender, userAuthenticationPageBo.getGender())
                .like(StringUtils.isNotBlank(userAuthenticationPageBo.getContact()), UserAuthentication::getContact, userAuthenticationPageBo.getContact())
                .orderByDesc(UserAuthentication::getCreateTime);

        IPage<UserAuthentication> iPage = iUserAuthenticationService.page(initPage, queryWrapper);
        List<UserAuthenticationVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (UserAuthentication record : iPage.getRecords()) {
                UserAuthenticationVo vo = new UserAuthenticationVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<UserAuthenticationVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public UserAuthenticationVo info(UserAuthenticationInfoBo userAuthenticationInfoBo) {
        UserAuthentication userAuthentication = iUserAuthenticationService.getById(userAuthenticationInfoBo.getId());
        if (Objects.isNull(userAuthentication)) {
            throw new BizException("信息不存在");
        }

        UserAuthenticationVo vo = new UserAuthenticationVo();
        BeanUtils.copyProperties(userAuthentication, vo);
        return vo;
    }

    @Override
    public void updateInfo(UserAuthenticationUpdateBo userAuthenticationUpdateBo) {
        UserAuthentication userAuthentication = new UserAuthentication();
        BeanUtils.copyProperties(userAuthenticationUpdateBo, userAuthentication);

        userAuthentication.setStatus(1);
        userAuthentication.setReason("");
        iUserAuthenticationService.updateById(userAuthentication);
    }

    @Override
    public void delete(UserAuthenticationInfoBo userAuthenticationInfoBo) {
        iUserAuthenticationService.removeById(userAuthenticationInfoBo.getId());
    }

    @Override
    public void audit(UserAuthenticationAuditBo userAuthenticationAuditBo) {
        UserAuthentication authentication = iUserAuthenticationService.getById(userAuthenticationAuditBo.getId());
        if (Objects.isNull(authentication)) {
            throw new BizException("认证信息不存在");
        }

        iUserAuthenticationService.update(new LambdaUpdateWrapper<UserAuthentication>()
                .eq(UserAuthentication::getId, userAuthenticationAuditBo.getId())
                .set(UserAuthentication::getStatus, userAuthenticationAuditBo.getStatus())
                .set(UserAuthentication::getReason, userAuthenticationAuditBo.getReason())
        );

        if (Objects.equals(userAuthenticationAuditBo.getStatus(), 2)) {
            iUserService.updateUserType(authentication.getUserId(), 2);
        }
    }

    @Override
    public UserAuthenticationVo owner() {
        QueryWrapper<UserAuthentication> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserAuthentication::getUserId, UserContextHolder.getAppUserId());

        UserAuthentication userAuthentication = iUserAuthenticationService.getOne(queryWrapper);
        if (Objects.isNull(userAuthentication)) {
            return null;
        }

        UserAuthenticationVo vo = new UserAuthenticationVo();
        BeanUtils.copyProperties(userAuthentication, vo);
        return vo;
    }
}
