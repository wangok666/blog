package bupt.cs.blog.common.aop;


import bupt.cs.blog.utils.HttpContextUtils;
import bupt.cs.blog.utils.IpUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect //切面 定义了通知和切点的关系
@Slf4j //记录日志
public class LogAspect {

    @Pointcut("@annotation(bupt.cs.blog.common.aop.LogAnnotation)") //切点
    public void pt(){//代表此注解加在哪
    }

    //环绕通知，即对方法的前后都可以增强
    @Around("pt()") //通知类，标识为切点
    public Object log(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法,方法result执行完要返回
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        recordLog(point, time);
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        //通过反射得到方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================log start================================");
        log.info("module:{}",logAnnotation.module());
        log.info("operation:{}",logAnnotation.operator());

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}",className + "." + methodName + "()");

//        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);

        //获取request 设置IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest(); //工具类获取到HttpServletRequest
        log.info("ip:{}", IpUtils.getIpAddr(request)); //获取到ip地址


        log.info("excute time : {} ms",time);
        log.info("=====================log end================================");
    }

}
