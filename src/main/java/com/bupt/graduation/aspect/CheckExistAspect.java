package com.bupt.graduation.aspect;

import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.entity.Resp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wangz
 */
@Component
@Aspect
public class CheckExistAspect {
    final PhotoDao photos;

    public CheckExistAspect(PhotoDao photos) {
        this.photos = photos;
    }

    @Pointcut("@annotation(com.bupt.graduation.annotation.ExistCheck)")
    public void existsCheck() {
    }


    @Around("existsCheck()")
    public Object beforeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String uuid = parameterMap.get("uuid")[0];

        // 去查对应的合照，不存在会抛出异常
        Integer count = photos.getCount(uuid);
        if (count <= 0) {
            return new Resp(false, 200, "the photo does not exists.", null);
        }
        return joinPoint.proceed();


    }

}
