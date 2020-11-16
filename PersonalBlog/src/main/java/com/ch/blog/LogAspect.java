package com.ch.blog;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description:
 * @author: chenhao
 * @create:2020/10/26 9:38
 **/
@Aspect
@Component
public class LogAspect {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Controller层切点
     */
    //web下面所有的类（*）里的任何参数的所有的方法（*(..)）
    @Pointcut("execution(* com.ch.blog.web.*.*(..))")
    public void controllerAspect() {
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =attributes.getRequest();
        String url=request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        logger.info("Request: {}",requestLog);
    }

    @After("controllerAspect()")
    public void doAfter(){
//        logger.info("之后");
    }

    @AfterReturning(value = "controllerAspect()",returning = "result")
    public void doAfterRuning(Object result){
        logger.info("Result:{}" + result);
    }
    /**
     * 修改请求参数
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("进入到AOP了");
        //获取用户的缓存数据
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();

        // 获取方法名
       String methodName = joinPoint.getSignature().getName();

        // 反射获取目标类
        java.lang.Class<?> targetClass = joinPoint.getTarget().getClass();

        // 拿到方法对应的参数类型
        java.lang.Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();

        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);

        logger.info("是{}类的{}方法，方法的具体信息是{}，参数类型是{}",targetClass,methodName,objMethod,parameterTypes);
        System.out.println("是"+targetClass+"类的"+methodName+"方法，方法的具体信息是"+objMethod+"，参数类型是"+parameterTypes);
        Object[] obj = joinPoint.getArgs();
        //通过反射实例化参数对象
        //获取字节码
        try {
            Class<?> aClass = obj[0].getClass();
        } catch (Exception e) {
            //这里有可能出现无法找到setUserModelCache的情况 所以捕获异常直接继续
            //e.printStackTrace();
        }
        System.out.println("=====修改参数==========");
        return joinPoint.proceed(obj);
    }

    /**
     * 将参数封装到一个内部类
     */
    public class RequestLog{
        private String url;
        private String ip;
        private String ClassMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            ClassMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", ClassMethod='" + ClassMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
