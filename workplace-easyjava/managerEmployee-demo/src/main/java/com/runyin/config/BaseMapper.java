package com.runyin.config;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T,P> {
    // 插入操作
    Integer insert(@Param("bean") T t);

    // 插入或者更新操作
    Integer insertOrUpdate(@Param("bean") T t);

    // 批量插入操作
    Integer insertBatch(@Param("list") List<T> list);

    // 批量插入或更新操作
    Integer insertOrUpdateBatch(@Param("list") List<T> list);

    // 根据参数查询集合
    List<T> selectList(@Param("query") P p);

    // 根据参数查询数量
    Integer selectCount(@Param("query") P p);
}
