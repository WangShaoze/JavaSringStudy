package com.show.words.mappers;

import org.apache.ibatis.annotations.Param;

public interface WsWordMapper<T,P> extends BaseMapper {
    /**
     * 根据Id查询
     * */
    T selectWordById(@Param("id") String id);

    /**
     * 根据 Id 更新
     */
    Integer updateById(@Param("bean") T t, @Param("id") String id);

    /**
     * 根据 Id 删除
     */
    Integer deleteById(@Param("id") String id);


    /**
     * 根据 word 查询
     * */
    T selectWordByWord(@Param("word") String word);

    /**
     * 根据 word 更新
     */
    Integer updateByWord(@Param("bean") T t, @Param("word") String word);

    /**
     * 根据 word 删除
     */
    Integer deleteByWord(@Param("word") String word);
}
