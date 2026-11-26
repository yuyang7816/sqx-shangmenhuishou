package com.sqx.app.searchHistory.db.service;

import com.sqx.app.searchHistory.domain.entity.SearchHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.searchHistory.domain.vo.SearchHistoryVo;

import java.util.List;

/**
 * <p>
 * 用户搜索历史 服务类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
public interface ISearchHistoryService extends IService<SearchHistory> {

    List<SearchHistoryVo> hot();

}
