package com.bupt.graduation.aspect;

import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.entity.Resp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangz
 */
@Component
@Aspect
public class CheckExistAspect {
    @Autowired
    PhotoDao photos;

    @Pointcut("@annotation(com.bupt.graduation.annotation.ExistCheck)")
    public void authPointCut() {
    }


    @Around("authPointCut()")
    public Object beforeLog(ProceedingJoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        assert attributes != null;
//        HttpServletRequest request = attributes.getRequest();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        String uuid = parameterMap.get("uuid")[0];
        try {
//            photos.getIdByUuid(uuid);
            return joinPoint.proceed();
        } catch (Throwable e) {
            return new Resp(false, 200, "the photo does not exists.", null);
        }

    }

}
