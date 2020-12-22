package spring.beans.support;

import spring.beans.config.BeanDefinition;
import spring.context.support.AbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lei on 2020/11/23.
 */
public class DefaultListableBeanFactory  extends AbstractApplicationContext {
    private final Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
