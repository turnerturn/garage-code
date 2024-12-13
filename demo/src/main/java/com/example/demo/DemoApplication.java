package com.example.demo;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@EnableAspectJAutoProxy
@SpringBootApplication
@RestController
public class DemoApplication {

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}



@Slf4j
@Component
 class DatasourcePasswordFetcher {

    public DatasourcePasswordFetcher() {
        super();
    }
    public String getPassword() {
        log.info("Fetching password");
        return "fetched-password";
    }
}



@Aspect
@Slf4j
@Component
class HikariAspect {

    @Autowired
    private final DatasourcePasswordFetcher datasourcePasswordFetcher;

    public HikariAspect(final DatasourcePasswordFetcher datasourcePasswordFetcher) {
        this.datasourcePasswordFetcher = datasourcePasswordFetcher;
    }

     //Example-2
     @Pointcut("execution(public getPassword(..))")
     public void allGetPasswordMethods() {}

    @Around("allGetPasswordMethods()")
    public Object aroundHikariMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getSignature().getDeclaringTypeName();

        String methodName = joinPoint.getSignature().getName();
        log.info("Before method...  Class={},  Method={}",className,methodName);
        // Proceed with the original method call
        Object result = joinPoint.proceed();

        className = joinPoint.getSignature().getDeclaringTypeName();
        methodName = joinPoint.getSignature().getName();
        log.info("After method...  Class={},  Method={}",className,methodName);
        return result;
    }

    //@Around("execution(* com.zaxxer.hikari.HikariConfig.getPassword())")
    public Object aroundHikariCrendentialGetPassword(ProceedingJoinPoint joinPoint) throws Throwable {
        // You can add any logic you want to execute before the method call here
        log.info("Before method: {}", Optional.ofNullable(joinPoint).map(ProceedingJoinPoint::getSignature).map(sig -> sig.getName()).orElse("null"));

        // Proceed with the original method call
        Object result = joinPoint.proceed();

        // You can add any logic you want to execute after the method call here
        log.info("After method: {}", Optional.ofNullable(joinPoint).map(ProceedingJoinPoint::getSignature).map(sig -> sig.getName()).orElse("null"));

        return datasourcePasswordFetcher.getPassword();
    }
}
