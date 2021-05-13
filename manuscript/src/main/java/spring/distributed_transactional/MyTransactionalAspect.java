package spring.distributed_transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import spring.my_spring.context.Component;

/**
 * Created by lei on 2021/5/13.
 */
@Component
@Aspect
public class MyTransactionalAspect {
    @Around("@annotation()spring.distributed_transactional.MyTransactional")
    public void invoke(ProceedingJoinPoint point){
        //创建一个全局事务

        try{
            point.proceed();
           //业务执行成功 注册一个分支提交事务事务
            // xxxx.commit();
        } catch (Throwable throwable) {
            //报错 注册一个事务回滚的状态
            throwable.printStackTrace();
        }
    }
}
