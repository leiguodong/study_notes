package zookeeper.rpc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2020/12/26.
 */
public class ServiceDiscoveryWithZK implements IServiceDiscovery {
    private static String CONNECTION_STR = "192.168.13.102.2181,192.168.13.102:2181,192.168.13.104:2181";
    private static List list = new ArrayList();//服务的本地缓存
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
    public String discovery(String serviceName) {
         String path = "/"+serviceName;
         try{
             curatorFramework.getChildren().forPath(path);
         }catch (Exception e){
             e.printStackTrace();
         }
         return null;
    }
    private void registryWatch(final String path) throws Exception{
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework,"/watch",true);
        PathChildrenCacheListener nodeCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {

            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);

    }
}
