package spring.my_spring.service.impl;

import spring.my_spring.context.Component;
import spring.my_spring.service.IOrderService;

/**
 * Created by lei on 2021/5/10.
 */
@Component("orderService")
public class OrderService implements IOrderService {
    public String getPrice(){
        return "价格";
    }
}
