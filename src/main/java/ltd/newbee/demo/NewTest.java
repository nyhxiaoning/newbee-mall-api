// 引入包名
package ltd.newbee.demo;

// Scanner类常用于从控制台或其他输入源获取用户输入

import java.time.Year;
import java.util.Scanner;

public class NewTest {
    public static int a1 = 1;

    //    基本数据格式测试
    void test1() {
        // 定义整型数据
        int a = 1;
        int b = 2;
        int c = a + b;
        System.out.println(c);
        byte d = 127;
        System.out.println(d);

        // 浮点型数据
        float e = 1.1f;
        System.out.println(e);

        // 双精度浮点型数据
        double f = 1.1;
        System.out.println(f);

        // 字符型数据
        char g = 'a';
        System.out.println(g);

        // 布尔型数据
        boolean h = true;
        System.out.println(h);

        System.out.println(Integer.MAX_VALUE);
    }


    /**
     * 输入输出测试:
     */
    void inputoutput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你的名字：");
        String name = scanner.next();
        System.out.println("Hello, " + name);

        int age = scanner.nextInt();
        System.out.println("你的年龄是：" + age);

        try {
            String nameRegex = scanner.next("[a-zA-Z]+");
            System.out.println("你的名字是正则：" + nameRegex);
        } catch (Exception e) {
            System.out.println("你输入的名字不正确，不是英文字符");
        }


    }


    public class Test11 {

        public int a;
        private int b = 111;
        protected int c;

        public void Test11Fn() {
            System.out.println("这是父类");
        }
    }

    public class ChildTest11 extends Test11 {
        public void test1() {
            System.out.println(a);
            // System.out.println(b);// 继承，也不能访问私有变量
            System.out.println(c);
        }

        @Override
        public void Test11Fn() {
            System.out.println("这是子类，重写了父类");
        }


        public void Test11Fn(String num) {
            System.out.println("这是子类，重载了父类");
            System.out.print(num);
        }
    }

    public abstract  class Animal{
        public abstract void say();
    }

    public class Dog extends Animal{
//        @Override
        public void say() {
            System.out.println("汪汪汪");
        }
    }

    public class Cat extends Animal{
//        @Override
        public void say() {
            System.out.println("喵喵喵");
        }
    }

   public class Test2 {

        public void test1() {
            Test11 newTest1Instance = new Test11();
            int a = newTest1Instance.a;
            newTest1Instance.b = 11113;
            newTest1Instance.c = 3;
            System.out.println(a);
            System.out.println(newTest1Instance.b);
            System.out.println(newTest1Instance.c);


        }

    }


    public class OutclassTest {
        private int a=12;
        public int b=12;


        public  class InnerclassTest{
            public void innerclassTest(){
                System.out.println("这是内部类");
                System.out.println(a);
                System.out.println(b);
            }
        }
    }

    public void TestFn() {
        Test2 test2Instance = new Test2();
        test2Instance.test1();

    }


    public static void main(String[] args) {
        System.out.println(1);
        // 调用类中的方法，首先看看是不是静态方法
        // 静态方法的调用方式：NewTest.test1();
        NewTest newNewTestInstance = new NewTest();
        newNewTestInstance.test1();

        // 输入输出的Scaner函数类使用
        //        newNewTestInstance.inputoutput();


        // public\private\protected
        //        newNewTestInstance.TestFn();

        // 内部类和外部类:外部类中内部类的访问方式：new NewTest().new OutclassTest();

        OutclassTest outclassTestInstance = new NewTest().new OutclassTest();

        System.out.println(outclassTestInstance.a);
        System.out.println(outclassTestInstance.b);

        // 继承和多态
        // 继承的案例代码：
        NewTest test11Instance = new NewTest();
        ChildTest11 childTest11Instance = test11Instance.new ChildTest11();
        childTest11Instance.test1();

        // 多态的案例代码
        NewTest test11Instance1 = new NewTest();
        Animal animal = test11Instance1.new Dog();
        animal.say();
        Animal animal2 = test11Instance1.new Cat();
        animal2.say();

    }
}
