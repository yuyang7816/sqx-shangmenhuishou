package com.sqx.app.searchHistory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.searchHistory.db.service.ISearchHistoryService;
import com.sqx.app.searchHistory.domain.bo.*;
import com.sqx.app.searchHistory.domain.entity.SearchHistory;
import com.sqx.app.searchHistory.domain.vo.SearchHistoryVo;
import com.sqx.app.searchHistory.domain.vo.SearchVo;
import com.sqx.app.searchHistory.service.SearchHistoryService;
import com.sqx.app.waste.item.db.service.IWasteItemService;
import com.sqx.app.waste.item.domain.entity.WasteItem;
import com.sqx.app.waste.item.domain.vo.WasteItemVo;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import com.sqx.common.shiro.utils.UserContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户搜索历史 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

    @Resource
    private ISearchHistoryService iSearchHistoryService;
    @Resource
    private IWasteItemService iWasteItemService;

    @Override
    public void saveInfo(SearchHistorySaveBo searchHistorySaveBo) {
        SearchHistory searchHistory = new SearchHistory();
        BeanUtils.copyProperties(searchHistorySaveBo, searchHistory);

        iSearchHistoryService.save(searchHistory);
    }

    @Override
    public IPage<SearchHistoryVo> pageInfo(SearchHistoryPageBo searchHistoryPageBo) {
        Page<SearchHistory> initPage = PageUtil.initPage(searchHistoryPageBo.getPage(), searchHistoryPageBo.getLimit());

        QueryWrapper<SearchHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(searchHistoryPageBo.getUserId()), SearchHistory::getUserId, searchHistoryPageBo.getUserId())
                .like(StringUtils.isNotBlank(searchHistoryPageBo.getKeyword()), SearchHistory::getKeyword, searchHistoryPageBo.getKeyword())
                .orderByDesc(SearchHistory::getCreateTime);

        IPage<SearchHistory> iPage = iSearchHistoryService.page(initPage, queryWrapper);
        List<SearchHistoryVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (SearchHistory record : iPage.getRecords()) {
                SearchHistoryVo vo = new SearchHistoryVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<SearchHistoryVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public SearchHistoryVo info(SearchHistoryInfoBo searchHistoryInfoBo) {
        SearchHistory searchHistory = iSearchHistoryService.getById(searchHistoryInfoBo.getId());
        if (Objects.isNull(searchHistory)) {
            throw new BizException("信息不存在");
        }

        SearchHistoryVo vo = new SearchHistoryVo();
        BeanUtils.copyProperties(searchHistory, vo);
        return vo;
    }

    @Override
    public void updateInfo(SearchHistoryUpdateBo searchHistoryUpdateBo) {
        SearchHistory searchHistory = new SearchHistory();
        BeanUtils.copyProperties(searchHistoryUpdateBo, searchHistory);

        iSearchHistoryService.updateById(searchHistory);
    }

    @Override
    public void delete(SearchHistoryInfoBo searchHistoryInfoBo) {
        iSearchHistoryService.removeById(searchHistoryInfoBo.getId());
    }

    @Override
    public List<SearchHistoryVo> listInfo(SearchHistoryListBo searchHistoryListBo) {
        QueryWrapper<SearchHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT keyword");
        queryWrapper.lambda()
                .eq(Objects.nonNull(searchHistoryListBo.getUserId()), SearchHistory::getUserId, searchHistoryListBo.getUserId())
                .like(StringUtils.isNotBlank(searchHistoryListBo.getKeyword()), SearchHistory::getKeyword, searchHistoryListBo.getKeyword())
                .orderByDesc(SearchHistory::getCreateTime);

        List<SearchHistory> searchHistoryList = iSearchHistoryService.list(queryWrapper);
        List<SearchHistoryVo> voList = new ArrayList<>();
        for (SearchHistory searchHistory : searchHistoryList) {
            SearchHistoryVo vo = new SearchHistoryVo();
            BeanUtils.copyProperties(searchHistory, vo);
            voList.add(vo);
        }

        return voList;
    }

    @Override
    public void clear() {
        LambdaQueryWrapper<SearchHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SearchHistory::getUserId, UserContextHolder.getAppUserId());

        iSearchHistoryService.remove(queryWrapper);
    }

    @Override
    public SearchVo search(SearchBo searchBo) {
        SearchVo vo = new SearchVo();

        if (StringUtils.isBlank(searchBo.getKeyword())) {
            return null;
        }

        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setUserId(searchBo.getUserId());
        searchHistory.setKeyword(searchBo.getKeyword());
        iSearchHistoryService.save(searchHistory);

        IPage<WasteItemVo> itemVoIPage = searchItem(searchBo);
        vo.setWasteItems(itemVoIPage);

        return vo;
    }

    private IPage<WasteItemVo> searchItem(SearchBo searchBo) {
        Page<WasteItem> initPage = PageUtil.initPage(searchBo.getPage(), searchBo.getLimit());

        LambdaQueryWrapper<WasteItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(WasteItem::getName, searchBo.getKeyword());

        IPage<WasteItem> itemPage = iWasteItemService.page(initPage, queryWrapper);
        List<WasteItemVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(itemPage.getRecords())) {
            for (WasteItem item : itemPage.getRecords()) {
                WasteItemVo vo = new WasteItemVo();
                BeanUtils.copyProperties(item, vo);

                voList.add(vo);
            }
        }

        IPage<WasteItemVo> voIPage = PageUtil.copyPage(itemPage, voList);
        return voIPage;
    }

    @Override
    public List<SearchHistoryVo> hot() {
        return iSearchHistoryService.hot();
    }
}
