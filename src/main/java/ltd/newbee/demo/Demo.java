package ltd.newbee.demo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基本语法的学习入口内容
 * Demo 鼠标放在url上，command放在url上即可跳转
 * TODO：第一处：知识汇总：二哥的java学习指导一条龙跳转：https://javabetter.cn/basic-grammar/type-cast.html
 * TODO:第二处：知识汇总： https://to-be-better-javaer.vercel.app/#/
 */
public class Demo {
    public void main(String[] args){
        // TODO:各种基本数据结构的使用
        System.out.println("hello world1111");
        System.out.println(1232111);
        System.out.println(333333);
        // 如果是一个方法如何声明：
        System.out.println("输出一个类名");
        System.out.println(new ltd.newbee.demo.Demo());


        // 基本类型学习
        boolean isStudent = true;
        int mynumber = 123;
        char testname = 'A';
        if( isStudent ){
            System.out.println("true");
            System.out.println(mynumber);
            System.out.println(testname);
        }else {
            System.out.println("false");
            System.out.println(mynumber);
            System.out.println(testname);
        }
        // 基本类型互相转换：
        int value_int = 65;
        char value_char = (char) value_int;
        System.out.println(value_char);
        // 包装器的调用
        Character.getNumericValue('1');//字符串转成数字
        Integer.toString(111);
        int [] arrays = {1,2,3};
        System.out.println(Arrays.toString(arrays));
        System.out.println("直接第一次打印数组：arrays");
        System.out.println((arrays));

        int[] anArray;
        anArray = new int[] {1, 2, 3, 4, 5};
        int[] anArray2 = new int[] {4,6,1,3,9,2};
        Arrays.sort(anArray2);
        Arrays.binarySearch(anArray2,4);// 内置二分法

        List<Integer> aList = new ArrayList<>();//数组转换List
        System.out.println(aList);
        System.out.println("aList打印完成");
        List<Integer> aList2 = Arrays.asList(1,3,5,7,9);
        for(int element:anArray){
            aList.add(element);
        }

        // 二维数组
        int [][] arrNumbers = {{1,4,2,3},{4,1,2,1}};
        System.out.println(arrNumbers[0][1]);
        System.out.println(aList);
        System.out.println(aList2);
        System.out.println(123);
        String [] cmowers = {"沉默","王二","一枚有趣的程序员"};
        System.out.println(cmowers);
        System.out.println(Arrays.toString(cmowers));
        System.out.println("cmowers第二次打印数组");

        Arrays.asList(cmowers).stream().forEach(s -> System.out.println(s));
        System.out.println("test" == "1111");
        System.out.println("test1" == "111123");
        Arrays.stream(cmowers).forEach(System.out::println);




        // 定义基本的enum枚举
        // java8不支持enum：这里默认使用：更新项目依赖，重启一下项目，使用java16+可以
        enum PlayType{
            TENIS,
            FOOTS,
            BASKETB
        }

        // 基本语句
        for(int i=0;i<10;i++){
            if(i==5){
                break;
            }
            System.out.println(i);
            System.out.println(PlayType.BASKETB);
        }

        try{
            int num = Integer.parseInt("abc");
        }catch(NumberFormatException e){
            System.out.println("invalid value num");
        }


        Weather weather = new Weather();
        weather.setTemperature(1);
        weather.setLocation("beijing");
        System.out.println("最后输出：Weather");
        System.out.println(weather.getTemperature());
        System.out.println(weather.getTemperature());
        System.out.println(weather);
        String str = "Hello, world!";
        str = "test1111111asdfffffsss";
        String subStr = str.substring(7, 12);  // 从第7个字符（包括）提取到第12个字符（不包括）
        System.out.println(subStr);  // 输出 "world"
        System.out.println(subStr.indexOf("e"));

    }
}
