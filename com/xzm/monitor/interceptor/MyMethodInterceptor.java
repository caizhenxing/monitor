package com.xzm.monitor.interceptor;

import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xzm.monitor.entity.MonitorInfo;
import com.xzm.monitor.service.MonitorSimpleService;
/**
  * @ClassName: MyMethodInterceptor
  * @Description: TODO
  * @author Comsys-xuzhenmin
  * @date 2015年11月21日 下午3:44:43
  *
 */
public class MyMethodInterceptor implements MethodInterceptor {  
  
    private final static Logger logger = LoggerFactory.getLogger(MyMethodInterceptor.class);
    
    @Autowired
    private MonitorSimpleService monitorSimpleService;
    
    
  
    public Object invoke(MethodInvocation invocation) throws Throwable {
    	Date date = new Date();
        StopWatch clock = new StopWatch();  
        Object result = null;  
        boolean isMonitor = false;
        String beanName = invocation.getMethod().getDeclaringClass().getSimpleName();  
        String methodName = beanName + "." + invocation.getMethod().getName();
        MonitorInfo monitorInfo = new MonitorInfo(beanName,methodName);
        isMonitor = monitorSimpleService.checkMonitorMethod(monitorInfo,invocation);
        if(isMonitor){
        	clock.start(); 
        }
        try {  
            result = invocation.proceed();  
        } catch (Throwable e) {
        	if(isMonitor){
        		clock.stop(); 
                monitorInfo.setRemark("方法名：" + methodName + "错误信息:" + e.getMessage());
                monitorInfo.setExecSuccess(false);
                monitorInfo.setProcessTime(date);
                monitorInfo.setExecuteTime(new Date(clock.getTime()));
            	monitorSimpleService.addMethodProcessInfo(monitorInfo);
        	}
        	logger.error("方法执行异常,方法名：{} 错误信息：{}" , methodName , e.getMessage());  
        	throw e;  
        }
        if(isMonitor){
        	clock.stop(); //计时结束 
        	monitorInfo.setExecSuccess(true);
        	monitorInfo.setProcessTime(date);
        	monitorInfo.setExecuteTime(new Date(clock.getTime()));
        	monitorSimpleService.addMethodProcessInfo(monitorInfo);
        }
        return result;  
    }  
  
}  
