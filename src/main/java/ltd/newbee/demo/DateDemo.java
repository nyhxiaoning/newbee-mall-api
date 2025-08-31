package ltd.newbee.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDemo {
    // note: java.util.Date
    public static void main(String[] args) throws ParseException {
        Date date1 = new Date();
        Date date2 = new Date(1000);
        Date date3 = new Date(1000, 1, 1);
        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);

        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long time3 = date3.getTime();
        System.out.println(time1);
        System.out.println(time2);
        System.out.println(time3);


        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));

        // 设置月份10月，15日
        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        System.out.println(calendar.getTime());

        // 当前的日期进行增加3 天
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        System.out.println(calendar.getTime());

        String dateString = "2021-01-31";
        SimpleDateFormat formatdate1 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatdate1);
        Date date = formatdate1.parse(dateString);
        System.out.println(date);
        System.out.println(formatdate1.format(date));



    }
}
