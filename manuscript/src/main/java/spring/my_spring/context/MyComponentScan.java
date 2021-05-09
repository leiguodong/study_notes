package spring.my_spring.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lei on 2021/5/9.
 */
//Retenttion 作用范围 RUNTIME:运行期
// Target 注解位置type：类上面
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyComponentScan {
    String value() default "";
}
