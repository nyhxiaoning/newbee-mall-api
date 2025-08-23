package ltd.newbee.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureDemo {
    public static void main1(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            System.out.println("子线程开始执行");
            Thread.sleep(2000);
            System.out.println("子线程执行完毕");
            return 100;
        });

        Thread t = new Thread(task);
        t.start();
        System.out.println("主线程开始执行");
        System.out.println("结果：" + task.get());
    }



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("子线程开始执行");
                Thread.sleep(2000);
                System.out.println("子线程执行完毕");
                return "idea执行完成";
            }
        });
        Thread t = new Thread(task);
        t.start();
        if(task.isCancelled()){
            System.out.println("任务是否被取消：" + task.isCancelled());
        }

        t.sleep(2000);
        if(task.isDone()){
            System.out.println("任务是否完成：" + task.isDone());
            String result = (String) task.get();
            System.out.println("结果：" + result);
        }

    }
}

