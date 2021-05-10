package spring.my_spring;

//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.my_spring.context.MyApplicationContext;
import spring.my_spring.service.IUserService;
import spring.my_spring.service.impl.UserService;

/**
 * Created by lei on 2021/5/9.
 */
public class Test {
    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        context.getBean("userService",UserService.class);
        MyApplicationContext applicationContext = new MyApplicationContext(AppConfig.class);
        System.out.println(applicationContext.getBean("userService"));
        IUserService userService = (IUserService)applicationContext.getBean("userService");
        userService.test();
//        System.out.println(applicationContext.getBean("userService"));
//        System.out.println(applicationContext.getBean("userService"));
    }
}
