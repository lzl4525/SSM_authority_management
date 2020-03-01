package com.gdut.ssm.controller;


import com.gdut.ssm.domain.SysLog;
import com.gdut.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {

    private Date visitTime;
    private Class clazz;
    private Method method;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ISysLogService sysLogService;

    //前置通知
    @Before("execution(* com.gdut.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {

        visitTime = new Date();
        clazz = jp.getTarget().getClass(); //获取具体访问类对象
        String methodName = jp.getSignature().getName();   //获取访问方法名称
        Object[] args = jp.getArgs();
        if(args == null || args.length == 0){
            method = clazz.getMethod(methodName);
        }else {
            Class[] classeArgs = new Class[args.length];
            for(int i =0; i<args.length; i++){
                classeArgs[i] = args[i].getClass();
            }
            method = clazz.getMethod(methodName,classeArgs);
        }

    }

    //后置通知
    @After("execution(* com.gdut.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception{

        long time = new Date().getTime() - visitTime.getTime();  //获取访问时长
        //获取url
        String url = "";
        if(clazz != null && method != null && clazz != LogAop.class){
            RequestMapping classAnnotation = (RequestMapping)clazz.getAnnotation(RequestMapping.class);
            if(classAnnotation != null){
                String[] classValue = classAnnotation.value();

                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if(methodAnnotation != null){
                    String[] methodValue = methodAnnotation.value();
                    url = classValue[0] + methodValue[0];

                    //获取ip地址
                    String ip = request.getRemoteAddr();
                    //获取当前操作用户
                    SecurityContext context = SecurityContextHolder.getContext();
                    User user = (User) context.getAuthentication().getPrincipal();
                    String username = user.getUsername();
                    //将日志相关信息封装到syslog中
                    SysLog sysLog = new SysLog();
                    sysLog.setExecutionTime(time);
                    sysLog.setIp(ip);
                    sysLog.setMethod("[类名]"+clazz.getName()+"[方法名]"+method.getName());
                    sysLog.setUrl(url);
                    sysLog.setUsername(username);
                    sysLog.setVisitTime(visitTime);

                    //完成日志记录操作
                    sysLogService.save(sysLog);
                }
            }
        }

    }

}
