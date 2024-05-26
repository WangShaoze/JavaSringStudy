package com.show.words.controller;

import com.show.words.query.WsWordQuery;
import com.show.words.services.WsWordService;
import com.show.words.utils.ABaseController;
import com.show.words.utils.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ws_words_service")
public class WordController extends ABaseController {
    @Resource
    private WsWordService wsWordService;

    /*
    * 分页查询
    * */
    @RequestMapping("/selectWordList")
    public ResponseVO selectWordList(WsWordQuery query){
        return getSuccessResponseVO(wsWordService.findListByPage(query));
    }



}
