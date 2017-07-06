package com.sheng.example;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;

/**
 * Created by Administrator on 2017/6/30 0030.
 */
public class RedisClient {
    private static JedisPool pool;
    private static String redisServerIp="192.168.31.7";

    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     *
     */
    private static void createJedisPool() {

        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(1000);
        // 设置最大连接数
//        config.setMaxActive(1000);

        // 设置最大阻塞时间，记住是毫秒数milliseconds
//        config.setMaxWait(1000);
        // 设置空间连接
        config.setMaxIdle(10);
        config.setMaxTotal(100);
        config.setMinIdle(2);
        // 创建连接池
        pool = new JedisPool(config, redisServerIp, 6379);

    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (pool == null)
            createJedisPool();
    }

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    private static Jedis getJedis() {

        if (pool == null)
            poolInit();
        return pool.getResource();
    }

    /**
     * 归还一个连接
     *
     * @param jedis
     */
    private static void returnRes(Jedis jedis) {
        pool.returnResource(jedis);
    }

    void set(String sessionId, Session session) {
        jedis = getJedis();
        jedis.append(sessionId, serialize(session));
        returnRes(jedis);
    }

    void replace(String sessionId, Session session) {
        set(sessionId, session);

    }

    Jedis jedis = null;

    void delete(String sessionId) {
        jedis = getJedis();
        jedis.del(sessionId);
        returnRes(jedis);
    }

    Object get(String sessionId) {
        jedis = getJedis();
        Object obj = deserialize(jedis.get(sessionId));
        returnRes(jedis);
        return obj;
    }

    private static Object deserialize(String str) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(Base64.decode(str));
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("deserialize session error", e);
        } finally {
            try {
                ois.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static String serialize(Object obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return Base64.encodeToString(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("serialize session error", e);
        } finally {
            try {
                oos.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static void main(String[] args){
        Jedis jedisCli = new Jedis("192.168.31.7", 6379); //新建Jedis对象
        jedisCli.select(2); //切换Redis数据库
        jedisCli.set("firstJedis", "hello,Jedis"); //与Redis命令行操作基本一致
    }
}
