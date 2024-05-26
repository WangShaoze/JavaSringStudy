package com.show.words.services.impl;

import com.show.words.entity.WsWord;
import com.show.words.query.WsWordQuery;
import com.show.words.mappers.WsWordMapper;
import com.show.words.services.WsWordService;
import com.show.words.utils.PaginationResultVO;
import com.show.words.utils.SimplePage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("wsWordService")
public class WsWordServiceImpl implements WsWordService {

    @Resource
    private WsWordMapper<WsWord, WsWordQuery> wsWordMapper;

    /**
     * 根据条件查询列表
     * */
    public List<WsWord> findListByParam(WsWordQuery query){
        return this.wsWordMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     * */
    public Integer findCountByParam(WsWordQuery query){
        return wsWordMapper.selectCount(query);
    }

    /**
     * 分页查询
     * */
    public PaginationResultVO<WsWord> findListByPage(WsWordQuery query){
        Integer count = this.findCountByParam(query);
        SimplePage simplePage = new SimplePage(query.getPageNo(),query.getPageSize(), count);
        query.setSimplePage(simplePage);
        List<WsWord> list = this.findListByParam(query);
        PaginationResultVO<WsWord> resultVO = new PaginationResultVO<>(simplePage.getPageNo(), simplePage.getPageSize(), count, simplePage.getPageTotal(), list);
        return resultVO;
    }


    /**
     * 新增
     * */
    public Integer add(WsWord bean) {
        return wsWordMapper.insert(bean);
    }

    /**
     * 批量新增
     * */
    public Integer addBatch(List<WsWord> listBean) {
        if (listBean==null||listBean.isEmpty()){
            return 0;
        }
        return wsWordMapper.insertBatch(listBean);
    }

    /**
     * 批量新增/修改
     * */
    public Integer addOrUpdateBatch(List<WsWord> listBean) {
        if (listBean==null||listBean.isEmpty()){
            return 0;
        }
        return wsWordMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据 Id查询
     */
    public WsWord getById(String id){
        return wsWordMapper.selectWordById(id);
    }

    /**
     * 根据 Id更新
     */
    public Integer updateById(WsWord bean, String id){
        return wsWordMapper.updateById(bean, id);
    }

    /**
     * 根据 Id删除
     */
    public Integer deleteById(String id){
        return wsWordMapper.deleteById(id);
    }

    /**
     * 根据 word 查询
     */
    public WsWord getByWord(String word){
        return wsWordMapper.selectWordByWord(word);
    }

    /**
     * 根据 word 更新
     */
    public Integer updateByWord(WsWord bean, String word){
        return wsWordMapper.updateByWord(bean, word);
    }

    /**
     * 根据 word 删除
     */
    public Integer deleteByWord(String word){
        return wsWordMapper.deleteByWord(word);
    }

}
