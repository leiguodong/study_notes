package zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2020/12/24.
 */
public class CuratorDemo {
    private static String CONNECTION_STR = "192.168.13.102.2181,192.168.13.102:2181,192.168.13.104:2181";
    public static void main(String[] args) {
//        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("");
        CuratorFramework curatorFramework1 = CuratorFrameworkFactory.builder().
                connectString("192.68.13.102:2181").
                sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        //ExponentialBackoffRetry
        //RetryOneTime 仅仅只重试一次
        //RetryUntilElapsed 重试知道成功
        curatorFramework1.start();
        //crud
        curatorFramework1.create();
        curatorFramework1.setData();//修改
        curatorFramework1.delete();//删除
        curatorFramework1.getData();//查询
    }
    private static void createData(CuratorFramework curatorFramework){
        try {
            curatorFramework.create().creatingParentsIfNeeded().forPath("/data/program");
//            //PERSISTENT 持久化节点
//            PERSISTENT_SEQUENTIAL 持久化有序节点
//            EPHEMERAL 临时节点
//            EPHEMERAL_SEQUENTIAL 临时有序节点
//                    CONTAINER(4, false, false, true, false),
//                    PERSISTENT_WITH_TTL(5, false, false, false, true),
//                    PERSISTENT_SEQUENTIAL_WITH_TTL(6, false, true, false, true);
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("/data/program","test".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void updateData(CuratorFramework curatorFramework){
        try {
            curatorFramework.setData().forPath("/data/program","123".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void deleteData(CuratorFramework curatorFramework){
        try {
            Stat stat = new Stat();
            //删除之前先获取节点版本号，如果这期间节点版本被别的服务修改，这里删除会失败
            curatorFramework.getData().storingStatIn(stat).forPath("/data/program");
            curatorFramework.delete().withVersion(stat.getVersion()).forPath("/data/program");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建带权限的节点
     */
    private static void createNodeWithACL(CuratorFramework curatorFramework){
        /**
         * READ = 1
         * WRITE = 2
         * CREATE = 4
         * DELETE = 8
         * ADMIN = 16
         * ALL = 31
         */
        List<ACL> list = new ArrayList<>();
        //
        try {
            ACL acl = new ACL(ZooDefs.Perms.READ,new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
            list.add(acl);
            curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/auth");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param curatorFramework
     */
    private static void WatcherNode(CuratorFramework curatorFramework){

    }

    /**
     * watcher监听节点事件
     * @param curatorFramework
     */
    private static void addListenserWithNode(CuratorFramework curatorFramework){
        final NodeCache nodeCache = new NodeCache(curatorFramework,"/watch",false);
        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("receive node Changed");
                System.out.println(nodeCache.getCurrentData().getPath()+"/"+new String(nodeCache.getCurrentData().getData()));
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        try {
            nodeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
