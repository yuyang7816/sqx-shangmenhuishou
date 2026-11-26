package com.sqx.app.deposit.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.deposit.db.service.IDepositRecordService;
import com.sqx.app.deposit.domain.bo.DepositRecordInfoBo;
import com.sqx.app.deposit.domain.bo.DepositRecordPageBo;
import com.sqx.app.deposit.domain.bo.DepositRecordSaveBo;
import com.sqx.app.deposit.domain.bo.DepositRecordUpdateBo;
import com.sqx.app.deposit.domain.entity.DepositRecord;
import com.sqx.app.deposit.domain.vo.DepositRecordVo;
import com.sqx.app.deposit.service.DepositRecordService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 保证金记录 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-10-13 14:47:55
 */
@Service
public class DepositRecordServiceImpl implements DepositRecordService {

    @Resource
    private IDepositRecordService iDepositRecordService;

    @Override
    public void saveInfo(DepositRecordSaveBo depositRecordSaveBo) {
        DepositRecord depositRecord = new DepositRecord();
        BeanUtils.copyProperties(depositRecordSaveBo, depositRecord);

        iDepositRecordService.save(depositRecord);
    }

    @Override
    public IPage<DepositRecordVo> pageInfo(DepositRecordPageBo depositRecordPageBo) {
        Page<DepositRecord> initPage = PageUtil.initPage(depositRecordPageBo.getPage(), depositRecordPageBo.getLimit());

        IPage<DepositRecordVo> voIPage = iDepositRecordService.pageInfo(initPage, depositRecordPageBo);
        return voIPage;
    }

    @Override
    public DepositRecordVo info(DepositRecordInfoBo depositRecordInfoBo) {
        DepositRecord depositRecord = iDepositRecordService.getById(depositRecordInfoBo.getId());
        if (Objects.isNull(depositRecord)) {
            throw new BizException("信息不存在");
        }

        DepositRecordVo vo = new DepositRecordVo();
        BeanUtils.copyProperties(depositRecord, vo);
        return vo;
    }

    @Override
    public void updateInfo(DepositRecordUpdateBo depositRecordUpdateBo) {
        DepositRecord depositRecord = new DepositRecord();
        BeanUtils.copyProperties(depositRecordUpdateBo, depositRecord);

        iDepositRecordService.updateById(depositRecord);
    }

    @Override
    public void delete(DepositRecordInfoBo depositRecordInfoBo) {
        iDepositRecordService.removeById(depositRecordInfoBo.getId());
    }
}
