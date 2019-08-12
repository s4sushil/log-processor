package com.creditsuisse.app.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TracePerformanceAspect {

    private final Logger logger = LoggerFactory.getLogger(TracePerformanceAspect.class);
    
    @Around("execution(* com.creditsuisse.app..*(..))")
    public Object logTracePerformanceAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        StopWatch watcher = new StopWatch();
        watcher.start();
        Object result = joinPoint.proceed();
        watcher.stop();

        //Log method execution time
        logger.info("Execution time of " + className + "." + methodName + " : " + watcher.getTotalTimeMillis() + " ms" );

        return result;
    }
}
