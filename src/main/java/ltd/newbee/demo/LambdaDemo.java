package ltd.newbee.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LambdaDemo {


    public static void main1(String[] args) {
        MyFunctionalInterface myFunctionalInterface = new MyFunctionalInterface() {
            @Override
            public void myMethod() {
                System.out.println("myMethod");
            }
        };
        myFunctionalInterface.myMethod();
    }

    // note: lambda表达式实现一个：函数式接口
    // 由于lambda表达式是支持直接通过()->的方式去调用这种声明式接口的函数的，所以写起来要相对简洁一些。
    @FunctionalInterface
    interface MyFunctionalInterface {
        void myMethod();
    }


    // NOTE: lambda表达式实现一个：类型参数:允许类型参数可以推断出lambda表达式的类型
    public static <T> void doSomething(T t, Function<T, T> function) {
        T result = function.apply(t);
        System.out.println(result);
    }

    public static void main2(String[] args) {
        doSomething("hello", (String s) -> {
            return s.toUpperCase();
        });
    }

    // Note: lambda表达式实现一个：函数式方法(有点像是实现this的绑定关系，js中apply，bind)
    // 使用lambda表达式作为方法参数，以实现函数式接口。
    public static void donothing(String str, Function<String, String> function) {
        String result = function.apply(str);
        System.out.println(result);
    }

    public static void main4(String[] args) {
        donothing("hello-world", (String s) -> {
            return s.toUpperCase();
        });
    }


    public static void main(String[] args) {
        List list = new ArrayList<>();


        list.add("hello");
        list.add("world");
        list.add("java");

        list.stream().forEach(item->{
            System.out.println(item);
        });

        // stream统计功能
        List<Integer> list2 = new ArrayList();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        list2.add(4);
        list2.add(5);

        // collect 最后将数据结构转换，转成一个新的数据结构list
        List<Integer> resultList =  list2.stream().filter(x->
            x > 1
        ).collect(Collectors.toList());
        System.out.println(resultList);

        List<Integer> resultList2 =  list2.stream().filter(x->
            x > 1
        ).map(x->
            x * 2
        ).collect(Collectors.toList());
        System.out.println(resultList2);

        // 注意：因为max返回单值
        Integer resultList3 =  list2.stream().max((x,y)->x.compareTo(y)).get();
        System.out.println(resultList3);





    }


}
