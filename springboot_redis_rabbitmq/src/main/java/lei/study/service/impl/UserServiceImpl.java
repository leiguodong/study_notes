package lei.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by lei on 2021/5/21.
 */
public class UserServiceImpl {
    @Autowired
    private RedisTemplate redisTemplate;

    public boolean doLogin(){
//        redisTemplate.opsForValue().set("user:"+ticket,user);
        return true;
    }
}
