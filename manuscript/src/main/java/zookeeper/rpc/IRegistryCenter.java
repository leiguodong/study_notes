package zookeeper.rpc;

/**
 * 服务注册接口
 * Created by lei on 2020/12/26.
 */
public interface IRegistryCenter {
    /**
     * 服务注册名称和服务注册地址实现
     * @param serviceName
     * @param serviceAddress
     */
    void registry(String serviceName,String serviceAddress);
}
