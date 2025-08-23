package ltd.newbee.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadDemo {

    // todo:优化一下
    // 添加默认无参构造函数
    public ThreadDemo() {
        // 默认构造函数
    }

    // note:
    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("线程启动1");
            System.out.println("线程结束2");
        }
    }


    public static void main1(String[] args) {
        // 创建线程
        MyThread t = new MyThread();
        t.start();
        // 线程的方法学习
        try {
            t.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 等待线程结束：等待另一个线程结束
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 中断一个线程
        t.interrupt();

        // 判断当前线程是否处于活动状态
        System.out.println(t.isAlive());
        if(t.isAlive()){
            System.out.println("线程正在运行");
            // 获取当前线程,让出线程的cpu
            Thread.yield();
            // 设置线程的优先级
            t.setPriority(Thread.MAX_PRIORITY);
        }

    }


    // note:Callable接口的代码案例：
    public static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            // 进行任务处理，并返回处理结果
            String result = "hello";
            System.out.println("MyCallable2");
            return result;
        }
    }

    public static void main(String[] args) {
        // 创建Callable对象
        MyCallable myCallable = new MyCallable();
        // 创建FutureTask对象
        FutureTask<String> futureTask = new FutureTask<>(myCallable);
        // 创建线程
        Thread t = new Thread(futureTask);
        // 启动线程
        t.start();
        // 获取线程执行结果
        try {
            String result = futureTask.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
