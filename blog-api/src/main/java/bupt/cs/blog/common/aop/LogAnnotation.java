package bupt.cs.blog.common.aop;


import java.lang.annotation.*;

//type代表可以放在类上面，method代表可以放在方法上
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {


    //自定义两个参数需要用 模块名称 操作名称
    String module() default "";

    String operator() default "";

}