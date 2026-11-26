package com.sqx.app.searchHistory.db.mapper;

import com.sqx.app.searchHistory.domain.entity.SearchHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.searchHistory.domain.vo.SearchHistoryVo;

import java.util.List;

/**
 * <p>
 * 用户搜索历史 Mapper 接口
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-17 13:23:01
 */
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {

    List<SearchHistoryVo> hot();
}
