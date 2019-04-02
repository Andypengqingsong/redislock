package javatset;

/**
 * @author 彭青松
 * @project xtone-framework
 * @company 成都信通信息技术有限公司
 * @date 2019/4/2 1:56
 * @description 描述当前类的用途
 */
public class Person {
//    public static void main(String[] args) {
//        Person person = new Person();
//        System.out.println(person);
//    }

    public static void main(String[] args) throws InterruptedException {
        Person person = new Person();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++)
                    try {
                        Thread.sleep(1000);
                        System.out.println("子线程在执行....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }).start();


        for(int i=0;i<10;i++){
            Thread.sleep(1000);
            System.out.println("主线程在执行....");
        }
    }
}
