package com.sqx.app.payment.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.payment.record.db.service.IUserPaymentRecordService;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordInfoBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordPageBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordSaveBo;
import com.sqx.app.payment.record.domain.bo.UserPaymentRecordUpdateBo;
import com.sqx.app.payment.record.domain.entity.UserPaymentRecord;
import com.sqx.app.payment.record.domain.vo.UserPaymentRecordVo;
import com.sqx.app.payment.record.service.UserPaymentRecordService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户支付记录表 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-13 15:19:30
 */
@Service
public class UserPaymentRecordServiceImpl implements UserPaymentRecordService {

    @Resource
    private IUserPaymentRecordService iUserPaymentRecordService;

    @Override
    public void saveInfo(UserPaymentRecordSaveBo userPaymentRecordSaveBo) {
        UserPaymentRecord userPaymentRecord = new UserPaymentRecord();
        BeanUtils.copyProperties(userPaymentRecordSaveBo, userPaymentRecord);

        iUserPaymentRecordService.save(userPaymentRecord);
    }

    @Override
    public IPage<UserPaymentRecordVo> pageInfo(UserPaymentRecordPageBo userPaymentRecordPageBo) {
        Page<UserPaymentRecord> initPage = PageUtil.initPage(userPaymentRecordPageBo.getPage(), userPaymentRecordPageBo.getLimit());

        QueryWrapper<UserPaymentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .orderByDesc(UserPaymentRecord::getCreateTime);

        IPage<UserPaymentRecord> iPage = iUserPaymentRecordService.page(initPage, queryWrapper);
        List<UserPaymentRecordVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (UserPaymentRecord record : iPage.getRecords()) {
                UserPaymentRecordVo vo = new UserPaymentRecordVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<UserPaymentRecordVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public UserPaymentRecordVo info(UserPaymentRecordInfoBo userPaymentRecordInfoBo) {
        UserPaymentRecord userPaymentRecord = iUserPaymentRecordService.getById(userPaymentRecordInfoBo.getId());
        if (Objects.isNull(userPaymentRecord)) {
            throw new BizException("信息不存在");
        }

        UserPaymentRecordVo vo = new UserPaymentRecordVo();
        BeanUtils.copyProperties(userPaymentRecord, vo);
        return vo;
    }

    @Override
    public void updateInfo(UserPaymentRecordUpdateBo userPaymentRecordUpdateBo) {
        UserPaymentRecord userPaymentRecord = new UserPaymentRecord();
        BeanUtils.copyProperties(userPaymentRecordUpdateBo, userPaymentRecord);

        iUserPaymentRecordService.updateById(userPaymentRecord);
    }

    @Override
    public void delete(UserPaymentRecordInfoBo userPaymentRecordInfoBo) {
        iUserPaymentRecordService.removeById(userPaymentRecordInfoBo.getId());
    }
}
