package com.sqx.app.searchHistory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.searchHistory.domain.bo.*;
import com.sqx.app.searchHistory.domain.vo.SearchHistoryVo;
import com.sqx.app.searchHistory.domain.vo.SearchVo;

import java.util.List;

/**
 * 用户搜索历史 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
public interface SearchHistoryService {

    void saveInfo(SearchHistorySaveBo searchHistorySaveBo);

    IPage<SearchHistoryVo> pageInfo(SearchHistoryPageBo searchHistoryPageBo);

    SearchHistoryVo info(SearchHistoryInfoBo searchHistoryInfoBo);

    void updateInfo(SearchHistoryUpdateBo searchHistoryUpdateBo);

    void delete(SearchHistoryInfoBo searchHistoryInfoBo);

    List<SearchHistoryVo> listInfo(SearchHistoryListBo searchHistoryListBo);

    void clear();

    SearchVo search(SearchBo searchBo);

    List<SearchHistoryVo> hot();

}
