package interview.myJMM;

import java.util.concurrent.TimeUnit;
class MyDate{
    int number = 0;
    private volatile MyDate myDate = new MyDate();
    public void addTo60(){
        this.number = 60;
    }
}
/**
 * 验证volatile的可见性
 *  1.1 假如int number = 0 ;number变量之前没有添加volatile关键字修饰
 */
public class JMMtest {
    public static void main(String[] args) {
        final MyDate myDate = new MyDate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "is come in");
                try {
                    TimeUnit.SECONDS.sleep(3);
                    myDate.addTo60();
                    System.out.println(Thread.currentThread().getName() + "update number value" + myDate.number);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        },"AAA").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "is come in");
                try {
                    TimeUnit.SECONDS.sleep(3);
                    myDate.addTo60();
                    System.out.println(Thread.currentThread().getName() + "update number value" + myDate.number);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        },"BBB").start();
        while (myDate.number ==0){

        }
        System.out.println(Thread.currentThread().getName()+"mission is over");
    }
}
