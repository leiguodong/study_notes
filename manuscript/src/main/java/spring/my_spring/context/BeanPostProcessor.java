package spring.my_spring.context;

import org.springframework.beans.BeansException;

/**
 * Created by lei on 2021/5/10.
 */
public interface BeanPostProcessor {
     Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException ;

     Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException ;
}
