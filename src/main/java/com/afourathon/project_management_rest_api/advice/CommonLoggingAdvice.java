package com.afourathon.project_management_rest_api.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Aspect
@Component
public class CommonLoggingAdvice {

	Logger log = LoggerFactory.getLogger(CommonLoggingAdvice.class);

	@Pointcut(value="execution(* com.afourathon.project_management_rest_api.controller.*.*(..)) && "
			+ "!execution(* com.afourathon.project_management_rest_api.controller.*.getAll*(..))")
	public void commonControllerMethodsPointcut() {}
	
	@Around("commonControllerMethodsPointcut()")
	public Object commonControllerMethodsLogger(ProceedingJoinPoint joinPoint) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().toString();
		Object[] array = joinPoint.getArgs();
		
		log.info(className + " : executing=[operation=" + methodName + ", args="
				+ mapper.writeValueAsString(array) + "]");
		
		Object object = joinPoint.proceed();
		
		log.info(className + " : executed=[operation=" + methodName + ", response="
				+ mapper.writeValueAsString(object) + "]");
		
		return object;
	}
	
	@Pointcut(value="execution(* com.afourathon.project_management_rest_api.controller.*.getAll*(..))")
	public void getControllerMethodsPointcut() {}
	
	@Before("getControllerMethodsPointcut()")
	public void getControllerMethodsLogger(JoinPoint joinPoint) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().toString();
		Object[] array = joinPoint.getArgs();
		
		log.info(className + " : executing=[operation=" + methodName + ", args="
				+ mapper.writeValueAsString(array) + "]");
	}

}
