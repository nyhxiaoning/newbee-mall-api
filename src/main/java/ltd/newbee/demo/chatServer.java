package ltd.newbee.demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class chatServer {
    // 保存在线用户列表
    private static List<Socket> socketList = new ArrayList<>();

    public static void main(String[] args) {

        try {
            // 启动socket的服务器
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("服务器已开启，等待客户端连接...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("客户端已连接");
                // 将客户端加入在线列表

                socketList.add(socket);

                // 启动线程，处理客户端的消息
                new ChatHandler(socket).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 注意：因为main是一个静态方法，所以只能引入本文件的static，不能是public
    static   class ChatHandler extends Thread {
        // 局部变量
        private Socket socket;

        // todo:构造函数初始化
        public ChatHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 读取客户端消息
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = null;

                while ((msg = br.readLine()) != null) {
                    System.out.println("收到客户端消息：" + msg);
                    for (Socket s : socketList) {
                        if (!s.equals(socket)) {
                            PrintWriter pw = new PrintWriter(s.getOutputStream());
                            pw.println(msg);
                            pw.flush();
                        }
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                // 客户端下线，从在线列表移除该客户端连接
                socketList.remove(socket);
            }
        }
    }


}
