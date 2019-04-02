package redislock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 彭青松
 * @project xtone-framework
 * @company 成都信通信息技术有限公司
 * @date 2019/4/2 20:01
 * @description 描述当前类的用途
 */
public class RedistemplateFactory {
    private static JedisPool pool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大10个连接
        jedisPoolConfig.setMaxTotal(5);
        pool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);
    }

    public static Jedis newIntance() {
        return pool.getResource();
    }

    public static void main(String[] args) {
        String ping = newIntance().ping();
        System.out.println(ping);
    }

}
