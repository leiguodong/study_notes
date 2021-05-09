package spring.my_spring.context;

/**
 * Created by lei on 2021/5/9.
 */
public class MyApplicationContext {
    private Class configClass;
    public MyApplicationContext(Class configClass){
        this.configClass = configClass;
    }
    public Object getBean(String beanName){
        return null;
    }
}
