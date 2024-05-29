package com.easyChat.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketServer {
    public static void main(String[] args) {
        ServerSocket server = null;
        Map<String, Socket> CLIENT_MAP =  new HashMap<>();
        try {
            server = new ServerSocket(1024);
            System.out.println("服务已经启动，等待客户端连接...");
            while (true){
                Socket socket = server.accept();
                String ip = socket.getInetAddress().getHostAddress();
                System.out.println("有客户端连接ip:"+ip+"port:"+ socket.getPort());
                String clientKey = ip+socket.getPort();
                CLIENT_MAP.put(clientKey, socket);
                new Thread(()->{
                    InputStream inputStream = null;
                    while (true){
                        try {
                            inputStream = socket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf8");
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String readData = bufferedReader.readLine();
                            System.out.println("收到"+socket.getPort()+"端消息——>"+readData);

                            CLIENT_MAP.forEach((k, v)->{
                                OutputStream outputStream = null;
                                try {
                                    outputStream = v.getOutputStream();
                                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, "utf8"));
                                    printWriter.println(socket.getPort()+":"+readData);
                                    printWriter.flush();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
