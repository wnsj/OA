package com.jiubo.oa;

import com.sun.org.apache.bcel.internal.generic.ARETURN;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @desc:java8学习
 * @date: 2020-01-13 09:23
 * @author: dx
 * @version: 1.0
 */
public class StudyJavaEight {

    //Lambda 表达式实例
    public static void main2(String[] args) {
        //类型声明
        MathOperation addition = (int a, int b) -> a + b;

        //不用类型声明
        MathOperation subtraction = (a, b) -> a - b;

        //大括号中的返回语句
        MathOperation multiplication = (a, b) -> {
            return a * b;
        };

        //没有大括号及返回语句
        MathOperation division = (a, b) -> a / b;

        //不用括号
        PrintMsg pm = message -> System.out.println(message);

        //用括号
        PrintMsg p = (message) -> System.out.println(message);

        StudyJavaEight studyJavaEight = new StudyJavaEight();

        System.out.println(" 10 + 5 = " + studyJavaEight.operate(10, 5, addition));

        System.out.println(" 10 - 5 = " + studyJavaEight.operate(10, 5, subtraction));

        System.out.println(" 10 * 5 = " + studyJavaEight.operate(10, 5, multiplication));

        System.out.println(" 10 / 5 = " + studyJavaEight.operate(10, 5, division));
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }

    public static void main(String[] args) {
//        Car car = Car.create(Car::new);
//        Car car2 = Car.create(Car::new);
//        List<Car> cars = Arrays.asList(car,car2);
        //cars.forEach(Car::repair);
        //cars.forEach(car2::follow);

//        List<String> strings = Arrays.asList("a", "", "c", "d");
//        List<String> newStr = strings.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList());
//        System.out.println(newStr);

//        Random random = new Random();
//        random.ints().limit(10).forEach(System.out::println);

//        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

//        List<Integer> list = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());

//        List<String> strs = Arrays.asList("a", "", "c");
        //long count = strs.stream().filter(i -> i.isEmpty()).count();
//        long count = strs.parallelStream().filter(i -> i.isEmpty()).count();
//        long count = strs.stream().parallel().filter(i -> i.isEmpty()).count();
//        long count = strs.parallelStream().parallel().filter(i -> i.isEmpty()).count();
//        System.out.println(count);

//        numbers.stream().sorted().forEach(System.out::println);
//        List<String> list = strs.stream().filter(i -> !i.isEmpty()).collect(Collectors.toList());
//        System.out.println("筛选列表:" + list);
//        String str = strs.stream().filter(i -> !i.isEmpty()).collect(Collectors.joining(","));
//        System.out.println("合并字符串:" + str);

//        IntSummaryStatistics intSummaryStatistics = numbers.stream().mapToInt(x -> x).summaryStatistics();
//        System.out.println("最大值:" + intSummaryStatistics.getMax());//7
//        System.out.println("最小值:" + intSummaryStatistics.getMin());//2
//        System.out.println("和:" + intSummaryStatistics.getSum());//25
//        System.out.println("平均数:" + intSummaryStatistics.getAverage());//3.57

//        String[] strArr = new String[]{"Hello", "World"};
//        List<String[]> list = Arrays.stream(strArr).map(w -> w.split("")).distinct().collect(Collectors.toList());
//        list.forEach(System.out::println);
//
//        List<String> strList = Arrays.stream(strArr).map(w -> w.split("")).flatMap(Arrays::stream).collect(Collectors.toList());
//        strList.forEach(System.out::println);

        Integer a = 1;
        Integer b = 2;
        // Optional.ofNullable - 允许传递为 null 参数
        Optional<Integer> integerOptional = Optional.ofNullable(a);
        // Optional.of - 如果传递的参数是 null，抛出异常 NullPointerException
        Optional<Integer> optionalInteger = Optional.of(a);

        // Optional.orElse - 如果值存在，返回它，否则返回默认值
        Integer value1 = integerOptional.orElse(new Integer(0));

        //Optional.get - 获取值，值需要存在
        Integer value2 = optionalInteger.get();

        System.out.println(value1);
        System.out.println(value2);

        optionalInteger.ifPresent(System.out::println);

        //获取当前时间(2020-01-13T14:44:36.520)
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间:" + now);

        //当前年月日（2020-01-13）
        LocalDate localDate = now.toLocalDate();
        System.out.println("localDate:" + localDate);

        //当前时分秒（14:44:36.520）
        LocalTime localTime = now.toLocalTime();
        System.out.println("localTime:" + localTime);

        System.out.println(now.getYear() + "年" + now.getMonth().getValue() + "月" + now.getDayOfMonth() + "日");

        //2014-12-12
        LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
        System.out.println("date3:" + date3);

        //22:15
        LocalTime date4 = LocalTime.of(22, 15);
        System.out.println("date4: " + date4);

        // 解析字符串
        LocalTime date5 = LocalTime.parse("20:15:30");
        System.out.println("date5: " + date5);

        //使用时区的日期时间API
        ZonedDateTime date1 = ZonedDateTime.parse("2015-12-03T10:15:30+05:30[Asia/Shanghai]");
        System.out.println("date1: " + date1);

        ZoneId id = ZoneId.of("Europe/Paris");
        System.out.println("ZoneId: " + id);

        ZoneId currentZone = ZoneId.systemDefault();
        System.out.println("当期时区: " + currentZone);
    }
}

interface MathOperation {

    int operation(int a, int b);

}

interface PrintMsg {
    void printMsg(String message);
}

interface Supplier<T> {
    T get();
}

class Car {

    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static Car create(Supplier<Car> supplier) {
        return supplier.get();
    }

    public static void collide(final Car car) {
        System.out.println("Collided " + car.toString());
    }

    public void follow(final Car another) {
        System.out.println("Following the " + another.toString());
    }

    public void repair() {
        System.out.println("Repaired " + this.toString());
    }
}

interface Vehicle {
    //默认方法
    default void print() {
        System.out.println("我是第一辆车!");
    }

    //静态方法
    static void blowHorn() {
        System.out.println("按喇叭!");
    }
}

interface FourWheeler {
    default void print() {
        System.out.println("我是第二辆车!");
    }
}

class Car2 implements Vehicle, FourWheeler {

    @Override
    public void print() {
        //1.重写方法实现自己的逻辑
        System.out.println("我自己重写的方法!");
        //2.调用接口的默认实现
        Vehicle.super.print();
    }

    public static void main(String[] args) {
        Car2 car2 = new Car2();
        car2.print();
        Vehicle.blowHorn();
    }
}