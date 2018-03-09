package java8;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by admin on 2018/3/5.
 */
public class DateTest {
    public static void main(String... args) {

        //  获取当前日期(YYYY-mm-dd 2018-03-06)
        LocalDate nowDate = LocalDate.now();
        System.out.println(nowDate);
        // 获取当前时间(HH:mm:a 11:15:18)去掉了毫秒
        LocalTime nowTime = LocalTime.now().withNano(0);
        System.out.println(nowTime);
        // 获取当前全时间(YYYY-mm-ddTHH:mm:a 2018-03-06T11:15:18)
        LocalDateTime dateTime= LocalDateTime.now().withNano(0);
        System.out.println(dateTime);
        //标准格式：字符串转全时间,可以去掉格式
        LocalDateTime formDateTime1 = LocalDateTime.parse(dateTime.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime formDateTime2 = LocalDateTime.parse(dateTime.toString());
        System.out.println(formDateTime1);
        //标准格式：字符串转日期
        LocalDate formDate1 = LocalDate.parse(nowDate.toString(), DateTimeFormatter.ISO_DATE);
        System.out.println(formDate1);
        //标准格式：字符串转时间
        LocalTime formTime1 = LocalTime.parse(nowTime.toString());
        System.out.println(formTime1 );
        //自定义格式
        DateTimeFormatter myFormatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String myFormDateTime1= dateTime.format(myFormatter1);
        System.out.println( myFormDateTime1);
        //获取时间戳
        Instant now = Instant.now();
        long nowTimestamp = Timestamp.from(now).getTime();
        System.out.println( nowTimestamp);
        //LocationDateTime 转时间戳
        long timestamp = Timestamp.valueOf(dateTime).getTime();
        System.out.println(timestamp);
        //时间戳与LocationDateTime互相转换
        Instant instant = Instant.ofEpochMilli(nowTimestamp);
        LocalDateTime DateTime1 = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).withNano(0);
        System.out.println(DateTime1);
        //LocationDateTime转换为DATE
        Instant instant1 = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date fromoLcationDateTime = Date.from(instant1);
        System.out.println(fromoLcationDateTime);
        //DATE转换为LocationDateTime
        Instant instant2 = fromoLcationDateTime.toInstant();
        LocalDateTime DateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault()).withNano(0);
        System.out.println(DateTime2);
        //比较时间
        boolean before = dateTime.isBefore(formDateTime2);
        boolean equals = dateTime.equals(formDateTime2);
        boolean after = dateTime.isAfter(formDateTime2);
        System.out.println(before+""+equals+""+after);
        //日期加减
        LocalDateTime localDateTime = dateTime.plusYears(1).plusHours(1).minusDays(1L);
        System.out.println(localDateTime);
        //日期间相差时长(年月日分别比较)
        Period between = Period.between(localDateTime.toLocalDate(), dateTime.toLocalDate());
        System.out.println(between.getYears());
        //日期间相差时长(总数比较)
        long days = dateTime.until(localDateTime, ChronoUnit.DAYS);
        System.out.println(days );
        // 4 得到年月 ----打印输出----- 2018 3
        int year = dateTime.getYear();int month = dateTime.getMonth().getValue();
        System.out.println(year+""+month+"" );

    }
}
