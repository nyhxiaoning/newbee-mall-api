package ltd.newbee.demo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class chatDemo {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            System.out.println("已连接");
            // 监听当前的服务器的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 读取服务端消息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String msg = null;
                    try {
                        while ((msg = br.readLine()) != null) {
                            System.out.println("收到服务端消息了：：：：" + msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // 收到消息后，发给服务器消息
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner sc = new Scanner(System.in);
            String line = null;
            while ((line = sc.nextLine()) != null) {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
