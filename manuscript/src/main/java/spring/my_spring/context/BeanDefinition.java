package spring.my_spring.context;

/**
 * Created by lei on 2021/5/10.
 */
public class BeanDefinition {


    private String scope;
    private Class clazz;


    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }


    public String getScope() {
        return scope;
    }

    public Class getClazz() {
        return clazz;
    }
}
