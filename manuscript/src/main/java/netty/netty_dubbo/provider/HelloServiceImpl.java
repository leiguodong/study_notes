package netty.netty_dubbo.provider;

import netty.netty_dubbo.HelloService;

/**
 * Created by lei on 2020/12/31.
 */
public class HelloServiceImpl implements HelloService{
    public String hello(String msg) {
        System.out.println("调用"+msg);
        return "已收到信息"+msg;
    }
}
