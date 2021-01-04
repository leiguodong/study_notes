package zookeeper.rpc;

/**fu
 * 服务发现接口
 * Created by lei on 2020/12/26.
 */
public interface IServiceDiscovery {
    String discovery(String serviceName);
}
