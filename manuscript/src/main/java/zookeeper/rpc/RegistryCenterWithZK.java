package zookeeper.rpc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Created by lei on 2020/12/26.
 */
public class RegistryCenterWithZK implements IRegistryCenter {
    private static String CONNECTION_STR = "192.168.13.102.2181,192.168.13.102:2181,192.168.13.104:2181";
    CuratorFramework curatorFramework = null;
    {
        //初始化zookeeper的连接，会话超时时间是5s，衰减重试
         curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECTION_STR).
                sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();
    }
    @Override
    public void registry(String serviceName, String serviceAddress) {
           String servicePath = "/"+serviceName;
           try {
               if(curatorFramework.checkExists().forPath(servicePath)==null){
                   //创建持久化节点
                   curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
               }
               String addressPath = servicePath+"/"+serviceAddress;
               //serviceaddress ip:port 创建服务地址的临时节点
               curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
               System.out.println("服务注册成功");
           }catch (Exception e){
               e.printStackTrace();
           }
    }
}
