package com.easychat.websocket.netty;

import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.redis.RedisComponent;
import com.easychat.utils.StringUtils;
import com.easychat.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName HandlerWebSocket
 * @Description 自定义WebSocket处理器
 * @Author
 * @Date
 * */
@Component("handlerWebSocket")
@ChannelHandler.Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket.class);

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private ChannelContextUtils channelContextUtils;

    /**
     * 通道继续后调用，一般用来做初始化
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有新的连接加入...");
    }

    /**
     * 连接断开，通道关闭后需要做的操作
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelContextUtils.removeContext(ctx.channel());
        logger.info("有连接断开...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 收消息
        Channel channel = ctx.channel();
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
//        logger.info("收到:{} 发来的消息:{}", userId, textWebSocketFrame.text());
        redisComponent.saveHeartBeat(userId);

//        // 发消息给群里的好友
//        channelContextUtils.send2Group(textWebSocketFrame.text());
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = complete.requestUri();  // /ws?token=hfjsahjkldhsa-ewrewe-fdsfdsfds
            String token = getToken(url);
            if (token==null){
                ctx.channel().close();
                return;
            }
            logger.info("url:{}", url);
            logger.info("token:{}", token);

            TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDto(token);
            if (tokenUserInfoDto==null){
                ctx.channel().close();
                return;
            }
            channelContextUtils.addContext(tokenUserInfoDto.getUserId(), ctx.channel());
        }
    }


    private String getToken(String url) {
        // /ws?token=hfjsahjkldhsa-ewrewe-fdsfdsfds
        if (StringUtils.isEmpty(url) || !url.contains("?")) {
            return null;
        }
        String[] queryParams = url.split("\\?");
        if (queryParams.length!=2){
            return null;
        }

        String[] params = queryParams[1].split("=");
        if (params.length!=2){
            return null;
        }

        return params[1];

    }
}
