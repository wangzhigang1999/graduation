package com.bupt.graduation.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wangz
 */
@Component
@Aspect
public class LoggerAspect {
    final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    final ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Pointcut("execution(* com.bupt.graduation.controller.*.*(..))")
    public void logPointCut() {
    }


    @Before("logPointCut()")
    public void beforeLog(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
    }


    @AfterReturning(returning = "object", pointcut = "logPointCut()")
    public void after(Object object) {
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }

}
