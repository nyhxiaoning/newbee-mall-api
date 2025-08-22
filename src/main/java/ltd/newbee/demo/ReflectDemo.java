package ltd.newbee.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectDemo {
    static class Person {
        String name;
        int age;
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void sayHello(){
            System.out.println("你好，我是"+this.name + "，今年"+this.age+"岁");
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        // 获取Class对象
        Class clazz = Person.class;
        System.out.println(clazz.getName());

        // 根据Class 对象获取构建函数
        Constructor constructor = clazz.getConstructor(String.class, int.class);
        // 根据构建函数，创建实例
        Person person = (Person) constructor.newInstance("张三", 18);
        System.out.println(person.getName());

        System.out.println(person.getAge());
        // *************************************** Field ******************************************
        // 获取当前的实例的属性：
        Field age = clazz.getDeclaredField("age");
        Field name = clazz.getDeclaredField("name");

        // 将私有属性设置为可访问
        age.setAccessible(true);
        name.setAccessible(true);
        age.set(person, 19);

        // *************************************** Method ***************************************
        Method sayHello = clazz.getDeclaredMethod("sayHello");
        sayHello.invoke(person);

        }
}
