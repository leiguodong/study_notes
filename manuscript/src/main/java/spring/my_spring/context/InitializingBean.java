package spring.my_spring.context;

/**
 * Created by lei on 2021/5/10.
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
