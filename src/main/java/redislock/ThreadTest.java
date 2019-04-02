package redislock;

/**
 * @author 彭青松
 * @project xtone-framework
 * @company 成都信通信息技术有限公司
 * @date 2019/4/3 0:47
 * @description 描述当前类的用途
 */
public class ThreadTest {


    public static void main(String[] args) {
        TaskTest taskTest = new TaskTest();
        Thread thread_A = new Thread(taskTest, "thread_A");
        Thread thread_B = new Thread(taskTest, "thread____B");
        thread_A.start();
        thread_B.start();

    }


    public static class TaskTest implements Runnable {
        RedisLock redisLock = new RedisLock();

        @Override
        public void run() {
            redisLock.lock();
            System.out.println("线程" + Thread.currentThread().getName() + "开始执行");
            try {
                for (int i = 0; i < 2; i++) {
                    Thread.sleep(1000);
                    System.out.println("线程" + Thread.currentThread().getName() + "正在执行");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redisLock.unlock();
            System.out.println("线程" + Thread.currentThread().getName() + "结束执行");
        }
    }


}

