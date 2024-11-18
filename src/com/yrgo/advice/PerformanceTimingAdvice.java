package com.yrgo.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class PerformanceTimingAdvice {

    public Object performTimingMeasurement (ProceedingJoinPoint method)
            throws Throwable {

        //before
        long startTime =System.nanoTime();

        try {
            //proceed to target
            Object value= method.proceed();
            return value;
        }finally {
            //after
            long endTime= System.nanoTime();
            long timeTaken = endTime - startTime;
            System.out.println("Time taken for the method " +
                    method.getSignature().getName() + " from the class " + method.getClass().getName() + " took " + timeTaken
                    /1000000 + "ms");
        }
    }
}

