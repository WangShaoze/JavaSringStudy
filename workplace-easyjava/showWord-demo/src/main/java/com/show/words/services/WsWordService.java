package com.show.words.services;

import com.show.words.entity.WsWord;
import com.show.words.query.WsWordQuery;
import com.show.words.utils.PaginationResultVO;

import java.util.List;

public interface WsWordService {
    /**
     * 根据条件查询列表
     * */
    List<WsWord> findListByParam(WsWordQuery query);

    /**
     * 根据条件查询数量
     * */
    Integer findCountByParam(WsWordQuery query);
    /**
     * 分页查询
     * */
    PaginationResultVO<WsWord> findListByPage(WsWordQuery query);


    /**
     * 新增
     * */
    Integer add(WsWord bean);

    /**
     * 批量新增
     * */
    Integer addBatch(List<WsWord> listBean);

    /**
     * 批量新增/修改
     * */
    Integer addOrUpdateBatch(List<WsWord> listBean);

    /**
     * 根据 Id查询
     */
    WsWord getById(String id);

    /**
     * 根据 Id更新
     */
    Integer updateById(WsWord bean, String id);

    /**
     * 根据 Id删除
     */
    Integer deleteById(String id);

    /**
     * 根据 word 查询
     */
    WsWord getByWord(String word);

    /**
     * 根据 word 更新
     */
    Integer updateByWord(WsWord bean, String word);

    /**
     * 根据 word 删除
     */
    Integer deleteByWord(String word);
}
