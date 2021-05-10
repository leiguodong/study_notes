package spring.my_spring.cmd;

import org.springframework.beans.BeansException;
import spring.my_spring.context.BeanPostProcessor;

/**
 * Created by lei on 2021/5/10.
 */
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
