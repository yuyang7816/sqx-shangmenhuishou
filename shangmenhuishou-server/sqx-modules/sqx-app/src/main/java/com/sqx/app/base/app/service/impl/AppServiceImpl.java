package com.sqx.app.base.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.app.domain.vo.AppVo;
import com.sqx.app.base.app.db.service.IAppService;
import com.sqx.app.base.app.domain.bo.AppInfoBo;
import com.sqx.app.base.app.domain.bo.AppPageBo;
import com.sqx.app.base.app.domain.bo.AppSaveBo;
import com.sqx.app.base.app.domain.bo.AppUpdateBo;
import com.sqx.app.base.app.domain.entity.App;
import com.sqx.app.base.app.service.AppService;
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
 * app升级 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-11-18 15:55:50
 */
@Service
public class AppServiceImpl implements AppService {

    @Resource
    private IAppService iAppService;

    @Override
    public void saveInfo(AppSaveBo appSaveBo) {
        App app = new App();
        BeanUtils.copyProperties(appSaveBo, app);

        iAppService.save(app);
    }

    @Override
    public IPage<AppVo> pageInfo(AppPageBo appPageBo) {
        Page<App> initPage = PageUtil.initPage(appPageBo.getPage(), appPageBo.getLimit());

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .orderByDesc(App::getId);

        IPage<App> iPage = iAppService.page(initPage, queryWrapper);
        List<AppVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (App record : iPage.getRecords()) {
                AppVo vo = new AppVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<AppVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public AppVo info(AppInfoBo appInfoBo) {
        App app = iAppService.getById(appInfoBo.getId());
        if (Objects.isNull(app)) {
            throw new BizException("信息不存在");
        }

        AppVo vo = new AppVo();
        BeanUtils.copyProperties(app, vo);
        return vo;
    }

    @Override
    public void updateInfo(AppUpdateBo appUpdateBo) {
        App app = new App();
        BeanUtils.copyProperties(appUpdateBo, app);

        iAppService.updateById(app);
    }

    @Override
    public void delete(AppInfoBo appInfoBo) {
        iAppService.removeById(appInfoBo.getId());
    }

    @Override
    public List<App> selectNewApp() {
        LambdaQueryWrapper<App> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(App::getVersion);

        return iAppService.list(queryWrapper);
    }
}
