package com.hml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisDemo2ApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 通用操作
     */
    @Test
    void testOperate() {
        //获取redis中的所有key
        Set keys = redisTemplate.keys("*");
        for (Object key : keys) {
            System.out.println(key);
        }

        //判断某个key是否存在
        Boolean myset = redisTemplate.hasKey("myset");
        System.out.println(myset);


        //删除指定key
        Boolean myZset = redisTemplate.delete("key1");
        System.out.println(myZset);

        //获取指定key对应的value的数据类型
        DataType dataType = redisTemplate.type("myset");
        System.out.println(dataType.name());

    }

    /**
     * 操作Set类型数据
     */
    @Test
    void testZset() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        //存值
        zSetOperations.add("zset","aa",11);
        zSetOperations.add("zset","bb",10);
        zSetOperations.add("zset","cc",9);
        zSetOperations.add("zset","dd",22);
        zSetOperations.add("zset","aa",9);

        //取值
        Set<String> myZset = zSetOperations.range("zset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }

        //修改分数
        zSetOperations.incrementScore("zset","b",20.0);

        //取值
        myZset = zSetOperations.range("zset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }

        //删除成员
        zSetOperations.remove("zset","a","b");

        //取值
        myZset = zSetOperations.range("zset", 0, -1);
        for (String s : myZset) {
            System.out.println(s);
        }

    }

    /**
     * 操作Set类型数据
     */
    @Test
    void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();
        //存值
        setOperations.add("myset","11","22","33","44","55","33","44","55");

        //取值
        Set myset = setOperations.members("myset");
        for (Object o : myset) {
            System.out.println(o);
        }

        //删除成员
        setOperations.remove("myset","11","22");

        //取值
        Set myset1 = setOperations.members("myset");
        for (Object o : myset1) {
            System.out.println(o);
        }

    }

    /**
     * 操作list类型数据
     */
    @Test
    void testList() {
        ListOperations listOperations = redisTemplate.opsForList();
        //存值
        listOperations.leftPush("mylist","a");
        listOperations.leftPushAll("mylist","a","b","c","d");

        //取值
        List mylist = listOperations.range("mylist", 0, -1);
        for (Object value: mylist) {
            System.out.println(value);
        }

        //获得列表长度
        Long size = listOperations.size("mylist");

        int lsize = size.intValue();
        for (int i = 0; i < lsize; i++) {
            //出队列
            String element =(String) listOperations.rightPop("mylist");
            System.out.println(element);
        }




    }


    /**
     * 操作Hash类型数据
     */
    @Test
    void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        //存值
        hashOperations.put("001","name","zhangsan");
        hashOperations.put("002","age","22");
        hashOperations.put("003","address","yunnan");

        //取值
        Object name = hashOperations.get("001", "name");
        System.out.println(name);
        Object age = hashOperations.get("002", "age");
        System.out.println(age);
        Object address = hashOperations.get("003", "address");
        System.out.println(address);

        //获得hash结构的所有字段
        Set keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        //获得hash结构中的所有值
        List values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }

    }

    /**
     * 操作String类型数据
     */
    @Test
    void testString() {
        //ValueOperations：简单K-V操作
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("city3","beijing");

        String city1 = valueOperations.get("city3");
        System.out.println(city1);

        //存值，同时设置过期时间
        redisTemplate.opsForValue().set("key1","value1",10l, TimeUnit.SECONDS);

        //存值，如果存在则不执行任何操作
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
        System.out.println(aBoolean);
    }

}
