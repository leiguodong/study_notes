package spring.my_spring.service.impl;


import spring.my_spring.context.*;
import spring.my_spring.service.IOrderService;
import spring.my_spring.service.IUserService;

/**
 * Created by lei on 2021/5/9.
 */
@Component("userService")
@Scope("protoType")
@ToProxy
public class UserService implements IUserService,BeanNameAware,InitializingBean{
      @AutoWired
      private IOrderService orderService;

      private String beanName;
      public void test(){
          System.out.println(orderService.getPrice());
      }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化");
    }
}
