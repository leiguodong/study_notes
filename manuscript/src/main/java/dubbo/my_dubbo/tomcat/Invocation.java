package dubbo.my_dubbo.tomcat;

import java.io.Serializable;

/**
 * Created by lei on 2021/5/26.
 */
public class Invocation implements Serializable{
    private String interfaceName;
    private String methodName;
    private Class[] parmaType;
    private Object[] params;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParmaType() {
        return parmaType;
    }

    public void setParmaType(Class[] parmaType) {
        this.parmaType = parmaType;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getInterfaceName() {

        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
