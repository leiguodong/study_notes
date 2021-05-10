package spring.my_spring.context;

import org.springframework.beans.factory.parsing.ParseState;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lei on 2021/5/9.
 */
public class MyApplicationContext {
    private Class configClass;
    //单例池
    private ConcurrentHashMap singletonObjects = new ConcurrentHashMap();
    //类定义容器
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap();

    //类扩展容器BeanPostProcessor
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<BeanPostProcessor>();
    public MyApplicationContext(Class configClass){
        this.configClass = configClass;
        scan(configClass);
        for(Map.Entry<String,BeanDefinition> entry : beanDefinitionMap.entrySet()){
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if(beanDefinition.getScope().equals("singleton")){
                 Object bean = createBean(beanName,beanDefinition);
                 singletonObjects.put(beanName,bean);
            }
        }
    }
    public void scan(Class configClass){
        //解析配置类 myComponentScan是自定义扫描注解
        MyComponentScan myComponentScan = (MyComponentScan)configClass.getDeclaredAnnotation(MyComponentScan.class);
        String path = myComponentScan.value();//扫描路径

        //扫描
        //Bootstrap---> jre/lib       启动类加载器
        //Ext---------> jre/ext/lib   扩展类加载器
        //app----------> classpath    应用类加载器
        ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
        //classLoader 的相对路径就是 classpath
        path = path.replace(".","/");
        System.out.println(path);
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File file1:files){
//                while(file1.isDirectory()){
//                    scanDirectory(file1,classLoader);
//                }
                //此处获取的路径是D:\....\*.class 需要转换成 com.spring.servic.*.class
                System.out.println(file1.getAbsolutePath());
                String fileName = file1.getAbsolutePath();
                if(fileName.endsWith(".class")){
                    String className = fileName.substring(fileName.indexOf("spring"),fileName.indexOf(".class"));
                    className = className.replace("\\",".");
                    Class<?> aClass = null;
                    try {
                        aClass = classLoader.loadClass(className);
                        if(aClass.isAnnotationPresent(Component.class)){
                            //判断当前扫描对象类 是不是一个BeanPostProcessor
                            if(BeanPostProcessor.class.isAssignableFrom(aClass)){
                                try {
                                    BeanPostProcessor instance = (BeanPostProcessor)aClass.getDeclaredConstructor().newInstance();
                                    //存入beanPostProcessor容器
                                    beanPostProcessorList.add(instance);
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                            }
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(aClass);
                            //获取类的注解
                            Component component = aClass.getDeclaredAnnotation(Component.class);
                            String beanName = component.value();

                            //scope注解定义 单例singleton or 原型 prototype
                            if(aClass.isAnnotationPresent(Scope.class)){
                                Scope scope = aClass.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scope.value());
                            }else{
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName,beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public Object getBean(String beanName){
        if(beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = (BeanDefinition) beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                Object o = singletonObjects.get(beanName);
                return o;
            }else{
                //原型的话 每次获取都需要创建
                Object bean = createBean(beanName,beanDefinition);
                return bean;
            }
        }else{
            throw new NullPointerException("没有"+beanName+"的类型定义");
        }
    }
    public Object createBean(String beanName,BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            //依赖注入
            for(Field declaredField : clazz.getDeclaredFields()){
                //属性上由自动注入注解的才自动注入
                if(declaredField.isAnnotationPresent(AutoWired.class)){
                    Object bean = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(instance,bean);
                }

                //BeanNameAware 自定义扩展 回调
                if(instance instanceof BeanNameAware){
                    ((BeanNameAware) instance).setBeanName(beanName);
                }
                //beanPostProcessor  bean初始化 前操作
                for(BeanPostProcessor beanPostProcessor: beanPostProcessorList){
                    beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
                }
                //初始化 InitializingBean
                if(instance instanceof InitializingBean){
                    try {
                        ((InitializingBean) instance).afterPropertiesSet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //beanPostProcessor  bean初始化 后操作
                for(BeanPostProcessor beanPostProcessor: beanPostProcessorList){
                    beanPostProcessor.postProcessAfterInitialization(instance,beanName);
                }
            }
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
