package com.leewan.aop;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leewan.bean.User;
import com.leewan.pageContext.ContextConstant;
import com.leewan.util.ChenjwMap;
import com.leewan.util.R;
import com.leewan.util.UID;
import com.leewan.util.UserUtils;

/**
 * 操作权限拦截
 * @author JackyCjw
 *
 */
@Aspect
@Component
public class OperateAuthAspect {

	@Autowired
	private HttpServletRequest request;
	
	@Around("@annotation(com.leewan.aop.OperateAuth)")
	public Object preAuth(ProceedingJoinPoint point) throws Throwable {
		
		Class<?> className = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        try {
            Method method = className.getMethod(methodName, argClass);
            if (method.isAnnotationPresent(OperateAuth.class)) {
            	OperateAuth annotation = method.getAnnotation(OperateAuth.class);
            	String[] auths = annotation.value();
            	User user = UserUtils.getUser();
        		Set<String> authority = user.getAuthority();
        		for(String auth : auths) {
        			if(!authority.contains(auth)) {
        				return R.noAuth().setData("对不起，您没有该功能的操作权限，请联系管理员进行授权！");
        			}
        		}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return point.proceed();
	}
	
	public static void main(String[] args) {
		System.out.println(UID.getUUID());
	}
}
