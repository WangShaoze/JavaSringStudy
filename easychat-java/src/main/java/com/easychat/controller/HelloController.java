package com.easychat.controller;

import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.websocket.MessageHandler;
import org.aspectj.bridge.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController extends ABaseController{

    @Resource
    private MessageHandler messageHandler;

    @GetMapping("/")
    public String helloWorld(){
        return "                   EasyChat Hello World                     ";
    }

    @GetMapping("/test")
    public ResponseVO test(){
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageContent("hihi:"+System.currentTimeMillis());
        messageHandler.sendMessage(messageSendDto);
        return getSuccessResponseVO(null);
    }
}
