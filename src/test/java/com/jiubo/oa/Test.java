package com.jiubo.oa;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @desc:
 * @date: 2020-01-09 08:55
 * @author: dx
 * @version: 1.0
 */
public class Test {

    private static final Object MONITOR = new Object();

    int a = 1;

    int b = 2;

    public int excute(TestInterface testInterface) {
        return testInterface.test(a, b);
    }

    //守护线程（当main线程结束则thread也结束）
    public static void main3(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(10000);
                IntStream.range(1, 1000).forEach(i -> System.out.println(Thread.currentThread().getName() + "--" + i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
//        thread.setDaemon(true);
        //设置最高优先级
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        thread.interrupt();
//        thread.join();
//        IntStream.range(1, 1000).forEach(i -> System.out.println(Thread.currentThread().getName() + "--" + i));
//        Optional.of("main 执行完成!").ifPresent(System.out::println);
    }

    public static void main2(String[] args) {

//创建线程
//        new Thread(() -> {
//            System.out.println("线程名:" + Thread.currentThread().getName());
//        }).start();

//模板方法
//        new TemplateClazz() {
//            @Override
//            protected void wrapPrint(String message) {
//                System.out.println("****" + message + "****");
//            }
//        }.printMessage("模板方法的使用");

//取票
//        TicketWindow ticketWindow = new TicketWindow();
//        new Thread(ticketWindow, "一号窗口").start();
//        new Thread(ticketWindow, "二号窗口").start();
//        new Thread(ticketWindow, "三号窗口").start();

        //税率计算
//        TaxCalculator taxCalculator = new TaxCalculator(10000, 2000) {
//            @Override
//            protected double calcTax() {
//                return getSalary() * 0.1 + getBonus() * 0.15;
//            }
//        };
//        double calcuate = taxCalculator.calcuate();
//        System.out.println("交税:" + calcuate);

//        SimpleCalculatorStrategy simpleCalculatorStrategy = new SimpleCalculatorStrategy();
//        double calcuate = new TaxCalculator2(10000, 2000, simpleCalculatorStrategy).calcuate();
//        System.out.println(calcuate);

//        TaxCalculator2 taxCalculator2 = new TaxCalculator2(10000, 2000, (s, b) -> s * 0.1 + b * 0.15);
//        System.out.println(taxCalculator2.calcuate());
        System.out.println(new Test().excute((a, b) -> a + b));
    }

    public static void main4(String[] args) {
        Thread thread = new Thread(() -> {
            boolean flag = true;
            while (flag) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("线程被打断了!");
                    flag = false;
                }
            }
        });
        thread.start();
        thread.interrupt();
    }

    public static void main5(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                synchronized (MONITOR) {
                    try {
                        MONITOR.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("线程被打断了");
                    }
                }
            }
        });
        thread.start();
        thread.interrupt();
    }

    public static void main(String[] args) {
        ThreadService threadService = new ThreadService();
        threadService.excute(() -> {
            try {
                Thread.sleep(5000);
                threadService.setFinished(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadService.shutdown(10000);
    }
}

class TemplateClazz {

    public final void printMessage(String message) {
        System.out.println("######################");
        wrapPrint(message);
        System.out.println("######################");
    }

    protected void wrapPrint(String message) {
    }
}

//取票
class TicketWindow implements Runnable {

    private static int index = 1;

    private final static int MAX = 50;

    @Override
    public void run() {
        while (index <= MAX) {
            System.out.println(Thread.currentThread().getName() + "号码:" + index++);
        }
    }
}

//税率计算
class TaxCalculator {
    private final double salary;

    private final double bonus;

    public TaxCalculator(double salary, double bonus) {
        this.salary = salary;
        this.bonus = bonus;
    }

    protected double calcTax() {
        return 0.0d;
    }

    public final double calcuate() {
        return calcTax();
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }
}

//税率计算2
class TaxCalculator2 {
    private final double salary;

    private final double bonus;

    private CalculatorStrategy calculatorStrategy;

    public TaxCalculator2(double salary, double bonus, CalculatorStrategy calculatorStrategy) {
        this.salary = salary;
        this.bonus = bonus;
        this.calculatorStrategy = calculatorStrategy;
    }

    public final double calcuate() {
        return this.calculatorStrategy.calcTax(getSalary(), getBonus());
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }
}

@FunctionalInterface
interface CalculatorStrategy {
    public double calcTax(double salary, double bonus);
}

class SimpleCalculatorStrategy implements CalculatorStrategy {

    @Override
    public double calcTax(double salary, double bonus) {
        return salary * 0.1 + bonus * 0.15;
    }
}

@FunctionalInterface
interface TestInterface {

    public int test(int a, int b);
}

class ThreadService {

    private Thread excuteThread;

    private volatile boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void excute(Runnable task) {
        excuteThread = new Thread() {
            @Override
            public void run() {
                Thread runner = new Thread(task);
                runner.setDaemon(true);
                runner.start();
                try {
                    runner.join();
                    finished = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        excuteThread.start();
    }

    public void shutdown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
            if (System.currentTimeMillis() - currentTime >= mills) {
                System.out.println("任务超时需要被关闭!");
                excuteThread.interrupt();
                break;
            }
            try {
                excuteThread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("执行线程被打断!");
                break;
            }
        }
        finished = false;
    }
}

