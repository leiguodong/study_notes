package spring.beans;

/**
 * 单例工厂顶层设计
 * Created by lei on 2020/11/23.
 */
public interface BeanFactory {
    /**
     * 根据beanName从IOC容器中获得一个实例Bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

}
