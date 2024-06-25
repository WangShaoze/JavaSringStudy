package com.easychat.websocket.netty;

import com.easychat.entity.config.AppConfig;
import com.easychat.utils.StringUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyWebSocketStarter
 * @Description WebSocket启动类
 * @Author
 * @Date
 * */
@Component("nettyWebSocketStarter")
public class NettyWebSocketStarter implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketStarter.class);

    // 定义线程组 （处理连接+处理消息）
    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workGroup = new NioEventLoopGroup();

    @Resource
    private HandlerWebSocket handlerWebSocket;

    @Resource
    private AppConfig appConfig;

    @PreDestroy
    public void close(){
        // 关闭线程组中的线程
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class).handler(
                    new LoggingHandler(LogLevel.DEBUG)
            ).childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline channelPipeline = channel.pipeline();
                    // 给管道设置几个重要的处理器
                    // 对 http 协议的支持，使用http的编码器、解码器
                    channelPipeline.addLast(new HttpServerCodec());
                    // 聚合解码器 httpRequest / httpContent lastHttpContent 到 fullHttpRequest
                    // 保证接收到Http请求的完整性
                    channelPipeline.addLast(new HttpObjectAggregator(64 * 1024));
                    // 心跳 long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit
                    // readerIdleTime 读超时时间 （测试端一定时间内未收到被测试段的消息）
                    // writerIdleTime 写超时时间 （测试段一定时间内未向被测试时间发送消息）
                    // allIdleTime 所有类型超时时间
                    // unit 时间单位
                    channelPipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
                    // 自己定义一个心跳超时的处理器
                    channelPipeline.addLast(new HandlerHeartBeat());
                    // 将http协议升级为ws协议，对websocket支持
                    channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 64 * 1024, true, true, 10000L));
                    // 自己定义一个Handler 处理消息
                    channelPipeline.addLast(handlerWebSocket);
                }
            });


            Integer wsPort = appConfig.getWsPort();
            String wsPortStr= System.getProperty("ws.port");
            if (!StringUtils.isEmpty(wsPortStr)){  // 同一台服务器在不同端口启动时配置该程序
                wsPort = Integer.parseInt(wsPortStr);
            }
            ChannelFuture channelFuture = serverBootstrap.bind(wsPort).sync();
            logger.info("Netty 启动成功, 端口:{}", appConfig.getWsPort());
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("Netty启动失败！！", e);
        } finally {
            // 关闭线程组中的线程
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
