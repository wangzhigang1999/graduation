package com.bupt.graduation.aspect;


import com.bupt.graduation.annotation.AuthCheck;
import com.bupt.graduation.entity.Resp;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * @author wangz
 */
@Component
@Aspect
public class LoggerAspect {
    Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Pointcut("execution(* com.bupt.graduation.controller.*.*(..))")
    public void logPointCut() {
    }

    @Pointcut("@annotation(com.bupt.graduation.annotation.AuthCheck)")
    public void authPointCut() {
    }

    @Before("logPointCut()")
    public void beforeLog(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        assert attributes != null;
//        HttpServletRequest request = attributes.getRequest();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        logger.info(new Gson().toJson(new RequestPojo(request.getRequestURL().toString(), request.getRemoteAddr(), parameterMap, request.getMethod(), (joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()), Arrays.toString(joinPoint.getArgs()))));
//        startTime.set(System.currentTimeMillis());
    }

    @Around("authPointCut() && @annotation(authCheck)")
    public Object beforeAuth(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (Arrays.stream(authCheck.role()).noneMatch((i) -> i.equals(role))) {
            logger.info("Authorization failed");
            return new Resp(50014, "Unauthorized");
        }
        logger.info("Authorization success username ={} role={}", session.getAttribute("username"), role);
        return joinPoint.proceed();
    }

    @AfterReturning(returning = "object", pointcut = "logPointCut()")
    public void after(Object object) {
//        logger.info(object.toString());
//        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }

}
