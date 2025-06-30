package ltd.newbee.demo;

import java.io.*;
import java.util.*;

public class ListSet {

    public void DQueue() {
        System.out.println("DQueue");

        // 队列
        LinkedList queue1 = new LinkedList();
        queue1.add("1");
        queue1.add("2");
        queue1.add("3");
        System.out.println(queue1.poll());
        System.out.println(queue1.getFirst());
        System.out.println(queue1.getLast());
        System.out.println(queue1.removeFirst());
        System.out.println(queue1.removeLast());
        System.out.println(queue1.size());
        System.out.println(queue1.isEmpty());

        // ArrayDeque
        ArrayDeque queue2 = new ArrayDeque();
        queue2.add("1");
        queue2.add("2");
        queue2.add("3");
        System.out.println("ArrayDeque----");
        System.out.println(queue2);
        System.out.println(queue2.getFirst());
        System.out.println(queue2.getLast());
        System.out.println(queue2.removeFirst());
        System.out.println(queue2.removeLast());
        System.out.println(queue2.size());
        System.out.println(queue2.isEmpty());

        // PriorityQueue

        // 可以根据传入的数组进行排序，后面的大于前面的，升序
        // 修正比较器为升序排列
        PriorityQueue<Integer> queue3 = new PriorityQueue<>(
                new Comparator<Integer>() {
                    public int compare(Integer o1, Integer o2) {
                        return o1 - o2;  // 升序：o1 小于 o2 时返回负数
//                        return o2 - o1;  // 降序：o1 大于 o2 时返回负数
                    }
                }
        );
        ;
        queue3.add(10);
        queue3.add(2);
        queue3.add(3);
        queue3.add(13);
        queue3.add(113);
        queue3.add(103);
        queue3.add(1);
        System.out.println("PriorityQueue:::");
        System.out.println(queue3);
        System.out.println(queue3);

        // 2. 使用 poll() 方法按升序获取元素
        System.out.println("\n使用 poll() 按升序输出:");
        while (!queue3.isEmpty()) {
            System.out.print(queue3.poll() + " "); // 输出: 1 2 3 10 13 103 113
        }


        // 3.Stack
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Stack:::");
        System.out.println(stack);
        //  // 查看栈顶元素
        //    Integer peek = stack.peek();
        System.out.println(stack.peek());
        System.out.println(stack.pop());

        // 4.BitSet
        BitSet bitSet1 = new BitSet();
        BitSet bitSet2 = new BitSet();
        // 放入一些数到bitSet里面去
        for (int i = 0; i < 16; i++) {
            if ((i % 2) == 0) {
                bitSet1.set(i);
            }
            if ((i % 5) != 0){
                bitSet2.set(i);
            }
        }

        System.out.println("Initial pattern in bits1: ");
        System.out.println(bitSet1);
        System.out.println("\n Initial pattern in bits2: ");
        System.out.println(bitSet2);

        // AND bits
        bitSet2.and(bitSet1);
        System.out.println("\n bits2 AND bits1: ");
        System.out.println(bitSet2);

        // OR bits
        bitSet2.or(bitSet1);
        System.out.println("\n bits2 OR bits1: ");
        System.out.println(bitSet2);
    }



    public void FileCopy(){
        try {
            FileInputStream intext = new FileInputStream("C:\\Users\\henry\\Desktop\\file.txt");
            FileOutputStream outtext = new FileOutputStream("C:\\Users\\Mohamed\\Desktop\\file2.txt");
            int length = 0;
            byte[] buffer1 = new byte[1024];
            // 读取文件intext后，写入outtext文件
            while ((length = intext.read(buffer1)) != -1) {
                outtext.write(buffer1, 0, length);
            }
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
    }


    public void StreamContent(){
        try {
            String str = "Yes you are a worker";
            ByteArrayInputStream byteio = new ByteArrayInputStream(str.getBytes());
            System.out.println(byteio.available());
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            int c = 0;
            while ((c = byteio.read()) != -1) {
                byteout.write(c);
            }
            System.out.println(byteout.toString());
        } catch (Exception e) {
            System.out.println("异常StreamContent");
            e.printStackTrace();
        }
    }

    public void DataStreamDemo(){

        String filename = "data.txt";
        try {

            FileOutputStream fileoutOrigin = new FileOutputStream(filename);
            // 以字节的方式向输出流中写入基本的Java数据类型
            DataOutputStream dataoutOrigin = new DataOutputStream(fileoutOrigin);
            // 将流数据内容准备写入文件中
            FileInputStream filein = new FileInputStream(filename);
            DataInputStream dataioOrign = new DataInputStream(filein);

            dataoutOrigin.writeUTF("xi写入一些字符内容，，，，，");
            dataoutOrigin.writeUTF("xi写入一些字符内容，，，，，");
            // 写入整型数据
            dataoutOrigin.writeInt(100);
            dataoutOrigin.writeInt(200);

            // 读取字符
            String str2 = dataioOrign.readUTF();
            System.out.println(str2);
            // 读取整型数据
            System.out.println(dataioOrign.readInt());
            System.out.println(dataioOrign.readBoolean());



        }
        catch (Exception e) {}
    }
    public static void main(String[] str) {
        System.out.println("List和Set学习");

        // 对象实例化
        ListSet listSet = new ListSet();
        listSet.DQueue();

        // 文件读写操作：字节流
        listSet.FileCopy();

        // 字符数组中读取字节流
        listSet.StreamContent();

        // DataOutputStream和DataInputStream
        listSet.DataStreamDemo();
    }
}
