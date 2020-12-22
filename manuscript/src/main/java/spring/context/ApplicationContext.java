package spring.context;

import spring.beans.BeanFactory;
import spring.beans.support.DefaultListableBeanFactory;

/**
 * Created by lei on 2020/11/23.
 */
public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory{

    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
