package ltd.newbee.demo;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;

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

    public void ObjectSerializationDemo(){
        String OBJECT_FILENAME = "data.obj";

        try {

            ObjectOutputStream objectOutputStream =  new ObjectOutputStream(new FileOutputStream(OBJECT_FILENAME));
            objectOutputStream.writeObject(new Person("张三", 18));
            objectOutputStream.writeObject(new Person("李四", 19));

            // 读取
            System.out.println("开始读取");
            System.out.println(objectOutputStream);

            // 从文件对象中反序列化出对象
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(OBJECT_FILENAME));
                Person person = (Person) objectInputStream.readObject();
                System.out.println(person);
                System.out.println("读取完毕");

            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            System.out.println("异常-ObjectSerializationDemo");
            e.printStackTrace();
        }
    }



    static class Person implements java.io.Serializable {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person [name=" + name + ", age=" + age + "]";
        }
    }

    public void PipeObjStream(){
        try {
            PipedOutputStream pipeos = new PipedOutputStream();
            PipedInputStream pipeis = new PipedInputStream(pipeos);
            pipeos.connect(pipeis);// 管道连接
            byte[] bytes = {11, 21, 3, 40, 5};
            pipeos.write(bytes);

            // 给管道输出流写入数据
            pipeos.write(bytes);

            int datas = pipeis.read();

            // 打印写入管道的数据内容
            while (datas != -1) {
                System.out.println(datas);
                datas = pipeis.read();

            }


        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void StreamReaderWriter(){
        try {
            // 从字节流转成字符流
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);

            // 使用缓冲流提升读写性能
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            outputStreamWriter.write("请输入内容：");
            outputStreamWriter.flush();

            // 循环读取用户输入，直到输入"exit"
            String userInput;
            while ((userInput = bufferedReader.readLine()) != null) {
                // 检查是否退出
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                // 回显用户输入
                bufferedWriter.write("你输入的内容是：");
                bufferedWriter.write(userInput);
                bufferedWriter.newLine();

                // 计算输入长度并输出
                bufferedWriter.write("输入长度：" + userInput.length() + " 个字符");
                bufferedWriter.newLine();

                // 提示继续输入
                bufferedWriter.write("继续输入（或输入exit结束）：");
                bufferedWriter.newLine();
                bufferedWriter.flush(); // 刷新缓冲区
            }



            // 输出结束信息
            bufferedWriter.write("程序已结束。");
            bufferedWriter.newLine();
            bufferedWriter.flush();




        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void ReaderWriter(){
        // FileReader 读取文件
        // FileWriter 写入文件


        try {

                FileReader reader1 = new FileReader("data.txt");
                FileWriter writer1 = new FileWriter("output.txt");

                  int c1;
                while ((c1 = reader1.read()) != -1) {
                    writer1.write(c1);
                }
                reader1.close();

                } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // StringReader和StringWriter
    public void StringReaderWriter() {
        try {
            String str = "Hello World!";
            StringBuilder builder = new StringBuilder();
            StringReader reader = new StringReader(str);
            StringWriter writer = new StringWriter();

            int c1 ;
            while ((c1 = reader.read()) != -1) {
                writer.write(c1);
            }
            System.out.println(writer.toString());
            System.out.println(builder.toString());
            System.out.println(str);
            System.out.println(str.equals(builder.toString()));

        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void CharArrayReaderFn() {
        try{
            // 这两行是什么意思？？？？
            char[] charArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
            char[] newCharArray = new char[charArray.length];

            CharArrayReader reader = new CharArrayReader(charArray);
            CharArrayWriter writer = new CharArrayWriter();
            int i;
            while ((i = reader.read()) != -1) {
                writer.write(i);
            }
            newCharArray = writer.toCharArray();
            System.out.println(newCharArray);
            System.out.println(new String(newCharArray));

        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] str) {
        System.out.println("List和Set学习");

        // 对象实例化
        ListSet listSet = new ListSet();
        listSet.DQueue();

        // 文件读写操作：字节流
//        listSet.FileCopy();

        // 字符数组中读取字节流
        listSet.StreamContent();

        // DataOutputStream和DataInputStream
        listSet.DataStreamDemo();

        // 对象序列化
//        listSet.ObjectSerializationDemo();

        // 管道数据处理
//        listSet.PipeObjStream();

        // 字符流
//        listSet.StreamReaderWriter();

        // 读写文件流
//        listSet.ReaderWriter();

        // StringReader：从字符串中读取数据。然后读取后，这里向字符串中写入数据
        listSet.StringReaderWriter();

        // CharArrayReaderFn
        listSet.CharArrayReaderFn();


    }
}
