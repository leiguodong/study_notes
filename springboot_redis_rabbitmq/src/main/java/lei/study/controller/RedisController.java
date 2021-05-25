package lei.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lei on 2021/5/22.
 */
@RestController
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/init")
    public void init(){
        Map<String,Point> map = new HashMap<>();
        map.put("祥云街七号院",new Point(39.755129,116.205027));
        map.put("半岛广场",new Point(39.753585,116.210666));
        redisTemplate.opsForGeo().add("my_map",map);

    }
    @GetMapping(value = "/position")
    public Point position(String member){
        List<Point> list =  redisTemplate.opsForGeo().position("my_map",member);
        return list.get(0);
    }
    @GetMapping(value = "/hash")
    public Point hash(String member){
        List<Point> list =  redisTemplate.opsForGeo().hash("my_map",member);
        return list.get(0);
    }
    @GetMapping(value = "/distance")
    public Distance distance(String member1,String member2){
        Distance distance =  redisTemplate.opsForGeo().distance("my_map",member1,member2, RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance;
    }
    /**
     * 通过经纬度查找附近的
     */
    @GetMapping(value ="/radiusByxy")
    public GeoResults radiusByxy(){
        //这个坐标
        Circle circle = new Circle(39.755129,116.205027,Metrics.KILOMETERS.getMultiplier());
        //返回50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance();
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = this.redisTemplate.opsForGeo().radius("my_map",circle,args);
        return geoResults;
    }
}
