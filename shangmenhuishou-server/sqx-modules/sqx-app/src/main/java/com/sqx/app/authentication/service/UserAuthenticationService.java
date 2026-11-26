package com.sqx.app.authentication.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.authentication.domain.bo.*;
import com.sqx.app.authentication.domain.vo.UserAuthenticationVo;

/**
 * 用户实名认证 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-01 14:08:36
 */
public interface UserAuthenticationService {

    void saveInfo(UserAuthenticationSaveBo userAuthenticationSaveBo);

    IPage<UserAuthenticationVo> pageInfo(UserAuthenticationPageBo userAuthenticationPageBo);

    UserAuthenticationVo info(UserAuthenticationInfoBo userAuthenticationInfoBo);

    void updateInfo(UserAuthenticationUpdateBo userAuthenticationUpdateBo);

    void delete(UserAuthenticationInfoBo userAuthenticationInfoBo);

    void audit(UserAuthenticationAuditBo userAuthenticationAuditBo);

    UserAuthenticationVo owner();
}
