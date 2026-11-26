package com.sqx.app.recruitment.db.service;

import com.sqx.app.recruitment.domain.entity.Recruitment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 招募 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 15:30:33
 */
public interface IRecruitmentService extends IService<Recruitment> {

    Recruitment getByUserId(Long userId);
}
