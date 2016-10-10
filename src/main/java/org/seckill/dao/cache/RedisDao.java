package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by hanyh on 16/10/10.
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JedisPool jedisPool;
    private final RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port) {
        this.jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId) {

        //redis逻辑操作
        Jedis jedis = jedisPool.getResource();
        try {
            //redis内部并没有实现对象序列化
            //get-->byte[]-->自己反序列化-->object 所以要使性能达到极致 需要使用自定义的序列化 网络传输效率会很高
            //目前是开源的protostuff效率比较高 protostuff需要普通的pojo也就是setter和getter
            String key = "seckill:" + seckillId;
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null) {
                //不为空说明redis里面有值就需要protostuff去转化
                Seckill seckill = schema.newMessage();
                ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                return seckill;
            }
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        } finally {
            jedis.close();
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {

        Jedis jedis = jedisPool.getResource();

        try {
            String key = "seckill:" + seckill.getSeckillId();
            byte[] bytes = ProtobufIOUtil.toByteArray(seckill, schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //超时缓存 超过这个时间缓存就失效
            int timeout = 60 * 60;
            String result = jedis.setex(key.getBytes(), timeout, bytes);
            return result;
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        } finally {
            jedis.close();
        }

        return null;
    }
}
