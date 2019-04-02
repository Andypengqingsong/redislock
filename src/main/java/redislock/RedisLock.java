package redislock;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author 彭青松
 * @project xtone-framework
 * @company 成都信通信息技术有限公司
 * @date 2019/4/2 1:13
 * @description 描述当前类的用途
 */
public class RedisLock implements Lock {
    private ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
    private Thread exclusiveOnwerThrad;

    public void setExclusiveOnwerThrad(Thread exclusiveOnwerThrad) {
        this.exclusiveOnwerThrad = exclusiveOnwerThrad;
    }

    @Override
    public void lock() {
        //while循环模拟阻塞
        while (!tryLock()) {
            try {
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName()+"==阻塞中。。。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            //锁过期时间必须大于单个线程的执行时间，才会有效果。
            return tryLock(80000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        Jedis jedis = RedistemplateFactory.newIntance();
        Thread t = Thread.currentThread();
        String id = UUID.randomUUID().toString();
        if (jedis.setnx("lock", id) == 1) {
            jedis.pexpire("lock", unit.toMillis(time));
            stringThreadLocal.set(id);
            setExclusiveOnwerThrad(t);
            //连接一定要释放。不然用连接池之后出现获取不到连接
            jedis.close();
            return true;
        } else if (exclusiveOnwerThrad == t) {
            jedis.close();
            return true;
        } else {
            jedis.close();
            return false;
        }
    }

    @Override
    public void unlock() {
        String script = null;
        try {
            Jedis jedis = RedistemplateFactory.newIntance();
            InputStream resourceAsStream = getClass().getResourceAsStream("/redis.script");
            script = inputStream2String(resourceAsStream);
           //System.out.println("获取脚本文件===" + script);
            if (stringThreadLocal.get() == null) {
                return;
            }
            jedis.eval(script, Arrays.asList("lock"), Arrays.asList(stringThreadLocal.get()));
            jedis.close();
            stringThreadLocal.remove();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String inputStream2String(InputStream is) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer result = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
