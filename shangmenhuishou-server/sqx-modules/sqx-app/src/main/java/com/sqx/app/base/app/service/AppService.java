package com.sqx.app.base.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.app.domain.bo.AppInfoBo;
import com.sqx.app.base.app.domain.bo.AppPageBo;
import com.sqx.app.base.app.domain.bo.AppSaveBo;
import com.sqx.app.base.app.domain.bo.AppUpdateBo;
import com.sqx.app.base.app.domain.entity.App;
import com.sqx.app.base.app.domain.vo.AppVo;

import java.util.List;

/**
 * app升级 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-11-18 15:55:50
 */
public interface AppService {

    void saveInfo(AppSaveBo appSaveBo);

    IPage<AppVo> pageInfo(AppPageBo appPageBo);

    AppVo info(AppInfoBo appInfoBo);

    void updateInfo(AppUpdateBo appUpdateBo);

    void delete(AppInfoBo appInfoBo);

    List<App> selectNewApp();
}
