package com.example.fundoo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);
    private static final long SLOW_THRESHOLD_MS = 500;

    @Pointcut("execution(* com.example.fundoo.service..*(..))")
    public void serviceLayer() {}

    @Around("serviceLayer()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;

        if (elapsed > SLOW_THRESHOLD_MS) {
            log.warn("SLOW METHOD: {} took {}ms (threshold: {}ms)",
                    joinPoint.getSignature().toShortString(), elapsed, SLOW_THRESHOLD_MS);
        } else {
            log.debug("{} executed in {}ms", joinPoint.getSignature().toShortString(), elapsed);
        }

        return result;
    }
}
