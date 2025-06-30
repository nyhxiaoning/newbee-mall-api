package ltd.newbee.demo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IODemo {

    public void BioServer(){
        try {
            // 快速启动一个服务端
            // telnet localhost 8089
            // Hello Bio Server
            // 服务器已收到：Hello Bio Server
            ServerSocket serverSocket = new ServerSocket(8089);
            System.out.println("服务端启动，等待连接...");

            Socket socket = serverSocket.accept(); // 阻塞等待
            System.out.println("客户端已连接");

            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);
            System.out.println("服务器已收到：" + new String(bytes, 0, len));

            inputStream.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println("报错内容提示serverSocket");
            e.printStackTrace();
        }
    }

    public void SocketClient(){
        try {
            Socket socket = new Socket("127.0.0.1", 8089);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("HelloServer".getBytes());
            outputStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println("报错内容提示clientSocket");
            e.printStackTrace();
        }
    }



    public static void main(String[] str){
        System.out.print("IODemo");
        IODemo ioobj = new IODemo();


        // 单独线程启动当前的服务：
        // 启动服务端线程
        new Thread(() -> ioobj.BioServer()).start();

        try {
            Thread.sleep(900); // 500ms 等待服务端准备好

            // 启动客户端线程
            new Thread(() -> ioobj.SocketClient()).start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
