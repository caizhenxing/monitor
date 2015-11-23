package com.xzm.monitor.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xzm.monitor.entity.MonitorInfo;
import com.xzm.monitor.service.MonitorSimpleService;

  

public class MyMethodInterceptor implements MethodInterceptor {  
  
    private final static Logger logger = LoggerFactory.getLogger(MyMethodInterceptor.class);
    
    @Autowired
    private MonitorSimpleService monitorSimpleService;
    
    
  
    public Object invoke(MethodInvocation invocation) throws Throwable {
    	Date date = new Date();
        //用 commons-lang 提供的 StopWatch 计时，Spring 也提供了一个 StopWatch  
        StopWatch clock = new StopWatch();  
        
        Object result = null;  
        boolean isMonitor = false;
        //监控的类名  
        String beanName = invocation.getMethod().getDeclaringClass().getSimpleName();  
        //监控的方法名  
        String methodName = beanName + "." + invocation.getMethod().getName();
        MonitorInfo monitorInfo = new MonitorInfo(beanName,methodName);
        //校验方法是否设置监控
        isMonitor = monitorSimpleService.checkMonitorMethod(monitorInfo,invocation);
        if(isMonitor){
        	clock.start(); 
        }
        try {  
            result = invocation.proceed();  
        } catch (Throwable e) {
        	Object[] objs = invocation.getArguments();
        	if(isMonitor){
        		clock.stop(); 
        		 //监控的参数  
                monitorInfo.setRemark("方法名：" + methodName + "参数:" + getString(objs) + e.getMessage());
                monitorInfo.setExecSuccess(false);
                monitorInfo.setProcessTime(date);
                monitorInfo.setExecuteTime(new Date(clock.getTime()));
            	monitorSimpleService.addMethodProcessInfo(monitorInfo);
        	}
        	logger.error("方法执行异常,方法名：" + methodName + "参数:" + getString(objs), e.getMessage());  
        	throw e;  
        }
        if(isMonitor){
        	clock.stop(); //计时结束 
        	monitorInfo.setExecSuccess(true);
        	monitorInfo.setProcessTime(date);
        	monitorInfo.setExecuteTime(new Date(clock.getTime()));
        	monitorSimpleService.addMethodProcessInfo(monitorInfo);
//        	if (logger.isInfoEnabled()) {  
//        		logger.info("执行时间:" + clock.getTime() + " ms [" + methodName + "]");  
//        	}  
        }
        return result;  
    }  
  
    @SuppressWarnings("unchecked")
	public String getString(Object[] objs) {  
        StringBuffer stringBuffer = new StringBuffer();  
        for (int i = 0, len = objs.length; i < len; i++) {  
            if (objs[i] instanceof String) {  
                stringBuffer.append("String类型：" + objs[i].toString());  
            } else if (objs[i] instanceof Map) {  
                HashMap<String, Object> hashMap = (HashMap<String, Object>) objs[i];  
                HashMap<String, Object> map = hashMap;  
                HashSet<String> set = (HashSet<String>) map.keySet();  
                stringBuffer.append("Map类型");  
                for (String str : set) {  
                    stringBuffer.append(str + "=" + map.get(str));  
                }  
            } else if (objs[i] instanceof Integer) {  
                stringBuffer.append("整数类型：");  
                stringBuffer.append(objs[i].toString());  
            } else {  
                stringBuffer.append(objs[i].toString());  
            }  
        }  
        return stringBuffer.toString();  
    }  
}  
