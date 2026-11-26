package com.sqx.app.recruitment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.recruitment.domain.bo.*;
import com.sqx.app.recruitment.domain.vo.RecruitmentVo;

/**
 * 招募 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
public interface RecruitmentService {

    void saveInfo(RecruitmentSaveBo recruitmentSaveBo);

    IPage<RecruitmentVo> pageInfo(RecruitmentPageBo recruitmentPageBo);

    RecruitmentVo info(RecruitmentInfoBo recruitmentInfoBo);

    void updateInfo(RecruitmentUpdateBo recruitmentUpdateBo);

    void delete(RecruitmentInfoBo recruitmentInfoBo);

    void audit(RecruitmentAuditBo recruitmentAuditBo);

    RecruitmentVo myInfo();

}
