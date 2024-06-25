package com.easychat.websocket.netty1;

import com.easychat.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket.class);

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
        logger.info("有连接断开...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 收消息
        Channel channel = ctx.channel();
        logger.info("收到消息:{}", textWebSocketFrame.text());

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
