package ltd.newbee.demo;

import lombok.Data;


// TODO:当前的文件模块，需要最上面声明
// 位于 Java 源文件的第一行（注释和空行除外），用于定义该文件中代码所属的 包（Package）





@Data
public class Weather {
    private int temperature;
    private String location;
    private int level;

    public static void print(String... str){
        System.out.println("永远 19 ");
    }

    public void otherTemperature(String test){
        System.out.println("12121");
    }

    public char getCurrentDay(){
        return '1';
    }
}
