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


    public void ArrValue() {
        int[] arr = new int[10];
        String[] str = new String[10];
        arr[0] = 1;
        str[0] = "112";
        System.out.println(arr[0]);
        System.out.println(str[0]);
        System.out.println("数组和字符串");

        System.out.println("数组可以使用对象？？？？");
        String[][] students = {
                {"张三", "18", "男"},
                {"李四", "20", "女"},
                {"王五", "19", "男"},
                {"赵六", "17", "女"},
                {"赵七", "22", "男"}
        };
        for (int i = 0; i < students.length; i++) {
            System.out.println("姓名：" + students[i][0] + "，年龄：" + students[i][1] + "，性别：" + students[i][2]);
        }

        String[] names = {"张三", "李四", "王五", "赵六", "赵七"};
        for (int i = 0; i < names.length; i++) {
            System.out.println("姓名String[]：" + names[i]);
        }

        // 循环的学习
        // for
        for (int i = 0; i < 10; i++) {
            System.out.println("i的值为：" + i);
        }

        // do-while:最后结束前，会执行一次循环
        int i1 = 0;
        do {
            i1++;
            System.out.println("i1的值为：" + i1);
        } while (i1 < 10);

        // while
        int i2 = 0;
        while (i2 < 10) {
            i2++;
            if (i2 == 5) {
                continue;
            }
            if (i2 == 8) {
                break;
            }
            System.out.println("i2的值为：" + i2);
        }

        // 双循环，打印星星
        for (int i = 1; i <= 10; i++) {
            for (int k = 10; k > i; k--) {
                System.out.print("   ");
            }
            for (int j = 1; j <= i; j++) {
                System.out.print("*  ");
            }
            System.out.println();
        }

        // lambda表达式
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

        list.forEach(e -> System.out.println(e));
        list.forEach(System.out::println);

        // switch
        int a = 1;
        switch (a) {
            case 1:
                System.out.println("当前的a的值是" + 1);
                break;
            case 2:
                System.out.println("当前的a的值是" + 2);
                break;
            default:
                System.out.println("default");
                break;
        }

        // Java场景的运算符有哪些？
        String str22 = "Hello World";
        boolean result = str22 instanceof String;
        System.out.println("当前的instanceof类型对比是" + result);

        // 字符串常用变量和函数使用
        String strstr = "Hello World";
        System.out.println("字符串的长度是" + strstr.length());
        System.out.println("字符串的索引是" + strstr.indexOf("World"));
        System.out.println("字符串的索引是" + strstr.lastIndexOf("World"));
        System.out.println("字符串的索引是" + strstr.charAt(0));
        System.out.println("字符串的截取内容是" + strstr.substring(0, 5));
        System.out.println("字符串的替换内容是" + strstr.replace("World", "World2"));
        System.out.println("字符串的转换大写内容是" + strstr.toUpperCase());
        System.out.println("字符串的转换小写内容是" + strstr.toLowerCase());
        System.out.println("字符串的去掉前后空格内容是" + strstr.trim());
        System.out.println(strstr.equals("World"));


        // 包装类型：
        Integer integer = 1;
        Long long1 = 1L;
        Float float1 = 1.0f;
        Double double1 = 1.0;
        Boolean boolean1 = true;
        Character character = 'a';
        System.out.println(integer);
        System.out.println(long1);
        System.out.println(float1);
        System.out.println(double1);
        System.out.println(boolean1);
        System.out.println(character);


        // string 字符
        Byte b = new Byte((byte) 127);
        System.out.println("Byte object: " + b);
        System.out.println(b == 127);
        System.out.println(b.equals(127));

        System.out.println("Byte value: " + b.byteValue());

    }


    public void HashMapFn() {
        // 基本hash使用
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("1", "1");
        hashMap.put("2", "2");
        hashMap.put("3", "3");
        System.out.println(hashMap.get("1"));
        System.out.println(hashMap.get("2"));
        System.out.println(hashMap.get("3"));

        System.out.println(hashMap.get("4"));
        // 遍历hash表
//        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
//            System.out.println(entry.getKey() + "--" + entry.getValue());
//        }

        // remove
        hashMap.remove("1");

        // contains
        System.out.println(hashMap.containsKey("1"));
        System.out.println(hashMap.containsValue("1"));
        System.out.println(hashMap.isEmpty());

        System.out.println("Hashtable部分学习");
        Hashtable<String, String> hashtable = new Hashtable();
        hashtable.put("1", "1");
        hashtable.put("2", "2");


//        for (Map.Entry entry : hashtable.entrySet()) {
//            System.out.println(entry.getKey() + "--" + entry.getValue());
//        }

        // linkhashMap
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("1", "1");
        linkedHashMap.put("2", "2");
//        for (Map.Entry entry : linkedHashMap.entrySet()) {
//            System.out.println(entry.getKey() + "--" + entry.getValue());
//        }

        System.out.println("hashMap");
        System.out.println(linkedHashMap.containsValue("1"));
        System.out.println(linkedHashMap.containsValue("3"));

        // 获取指定时间
        Date date = new Date();
        System.out.println(date);

        // 获取指定时间
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());

        // 获取格式化的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date));

        // 实例化一个时间对象，然后添加内容：添加时间
        //时间添加
        Date date2 = new Date();// 1.获取当前的时间对象
        Calendar calendar2 = Calendar.getInstance();// 2.获取日历对象
        calendar.setTime(date2);// 3.设置日历的时间
        calendar.add(Calendar.MONTH, 1);// 4.添加时间
        date2 = calendar2.getTime();
        System.out.println(date2);

        // 获取当前的时区
        TimeZone timeZone = TimeZone.getDefault();
        System.out.println(timeZone.getDisplayName());

        // 时间比较函数功能
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        if (localDateTime.isAfter(localDateTime1)) {
            System.out.println("localDateTime > localDateTime1");
        } else if (localDateTime.isBefore(localDateTime1)) {
            System.out.println("localDateTime < localDateTime1");
        }


    }


    // enum
    enum TestEnum {
        ONE,
        TWO,
        THREE;

        public static void main(String[] str) {
            System.out.println(TestEnum.ONE);
        }
    }
    // List学习记录
    public void ListValue() {
        // list
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        System.out.println(list);

        for (String str : list) {
            System.out.println(str);
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        // list2
        List list2 = new ArrayList();
        list2.add("1");
        list2.add(22);

        System.out.println(list2);
//        for (Object obj : list2) {
//            System.out.println(obj);
//        }
    }
    public static void main(String[] str) {

        System.out.println("数组");
        ListSet listSet = new ListSet();
        listSet.ArrValue();
        System.out.println("List");

        System.out.println("List和Set学习");

        // 对象实例化
        ListSet listSet2 = new ListSet();
        listSet.DQueue();

        // 文件读写操作：字节流
        listSet.FileCopy();

        // 字符数组中读取字节流
        listSet.StreamContent();

        // DataOutputStream和DataInputStream
        listSet.DataStreamDemo();
    }

}
