package com.ycg.ksh.common.log;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.exception.TMCException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.util.*;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/19
 */
public class LogAspect {

    final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private boolean level = false;//是否打印方法里面的方法

    private static final ThreadLocal<List<String>> threadLocal = new ThreadLocal<List<String>>();

    /**
     * 环绕通知：包围一个连接点的通知，可以在方法的调用前后完成自定义的行为，也可以选择不执行。
     * 类似web中Servlet规范中Filter的doFilter方法。
     * @param proceedingJoinPoint 当前进程中的连接点
     * @return
     */
    public Object aroundAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long stime = System.currentTimeMillis();
        Throwable throwable = null;
        Object object = null;
        List<String> methodStack = threadLocal.get();
        try {
            if(methodStack == null){
                threadLocal.set(new ArrayList<String>());
            }
            Object[] args = proceedingJoinPoint.getArgs();
            if (args != null && args.length > 0){
                object = proceedingJoinPoint.proceed(args);
            }else{
                object = proceedingJoinPoint.proceed();
            }
        }catch (Throwable ex){
            throwable = ex;
            throw ex;
        } finally {
            if(methodStack == null){
                logSomething(proceedingJoinPoint, object, throwable, stime, logger.isDebugEnabled());
            }else{
                methodStack.add(methodSignature(proceedingJoinPoint));
            }
        }
        return object;
    }


    private Object value(Object object){
        if(object != null){
            String clazzName = object.getClass().getSimpleName();
            if(object instanceof BaseEntity){
                return object;
            }
            if(object instanceof Collection){
                return clazzName +"("+ ((Collection<?>) object).size() +")";
            }
            if(object instanceof Map){
                return clazzName +"("+ ((java.util.Map<?, ?>) object).size() +")";
            }
            if(object.getClass().isArray()){
                return clazzName +"("+ ((Object[]) object).length +")";
            }
            if(object instanceof Date){
                return DATE_TIME_FORMATTER.format(LocalDateTime.ofInstant(((Date) object).toInstant(), ZoneId.systemDefault()));
            }
            if(object instanceof TemporalAdjuster){
                return DATE_TIME_FORMATTER.format((TemporalAccessor) object);
            }
        }
        return object;
    }


    private void logSomething(final ProceedingJoinPoint proceedingJoinPoint, final Object returnValue,  final Throwable throwable,  final long stime, boolean printReturn){
        Globallys.executor().execute(new Runnable() {
            @Override
            public void run() {
                AspectObject aspectObject = buildSomething(proceedingJoinPoint, returnValue, throwable, stime);
                StringBuilder builder = new StringBuilder();
                builder.append("\r\n").append("++++++++++++++++++++++++++++++++++++++++++++方法调用日志+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                builder.append("\r\n类名方法\t").append(aspectObject.getClassName()).append("->").append(aspectObject.getSignature());
                if(StringUtils.isNotEmpty(aspectObject.getArgs())){
                    builder.append("\r\n请求参数\t").append(aspectObject.getArgs());
                }
                List<String> methodStack = threadLocal.get();
                if(CollectionUtils.isNotEmpty(methodStack)){
                    builder.append("\r\n调用方法");
                    methodStack.forEach(m -> builder.append("\r\n\t\t").append(m));
                }
                if(printReturn && StringUtils.isNotEmpty(aspectObject.getReturnValue())){
                    builder.append("\r\n返回数据\t").append(aspectObject.getReturnValue());
                }
                if(StringUtils.isNotEmpty(aspectObject.getException())){
                    builder.append("\r\n执行异常\t").append(aspectObject.getException());
                }
                builder.append("\r\n方法耗时\t").append(aspectObject.getTime()).append("毫秒");
                builder.append("\r\n").append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                logger.info(builder.toString());
            }
        });
    }

    private AspectObject buildSomething(final ProceedingJoinPoint proceedingJoinPoint, final Object returnValue,  final Throwable throwable,  final long stime){
        AspectObject aspectObject = new AspectObject();
        aspectObject.setTime(System.currentTimeMillis() - stime);
        aspectObject.setClassName(proceedingJoinPoint.getTarget().getClass().getName());
        Object[] args = proceedingJoinPoint.getArgs();
        Signature signature = proceedingJoinPoint.getSignature();
        aspectObject.setSignature(signature.getName());
        if(signature instanceof MethodSignature){
            StringBuilder _builder = new StringBuilder();
            String[] names = ((MethodSignature) signature).getParameterNames();
            for (int i = 0; i < args.length; i++) {
                Object key = names != null ? names[i] : i;
                Object arg = args[i];
                _builder.append(key).append("=").append(value(arg)).append(" ; ");
            }
            aspectObject.setArgs(_builder.toString());
        }
        Object rvalue= value(returnValue);
        if(rvalue != null){
            aspectObject.setReturnValue(rvalue.toString());
        }
        if(throwable != null){
            aspectObject.setException(throwable.getClass().getName());
        }
        if(throwable instanceof TMCException){
            aspectObject.setException(aspectObject.getException() + "->"+ throwable.getMessage());
        }
        return aspectObject;
    }

    private String methodSignature(final ProceedingJoinPoint joinPoint){
        StringBuilder _builder = new StringBuilder();
        try {
            Object target = joinPoint.getTarget();
            if(AopUtils.isJdkDynamicProxy(target)) {
                Object obj = get(get(target, "h"), "advised");
                if(obj != null){
                    Object _obj = ((AdvisedSupport)obj).getTargetSource().getTarget();
                    if(_obj != null){
                        _builder.append(_obj.getClass().getName());
                    }
                }
            }
            if(AopUtils.isCglibProxy(target)){
                Object obj = get(get(target, "CGLIB$CALLBACK_0"), "advised");
                if(obj != null){
                    Object _obj = ((AdvisedSupport)obj).getTargetSource().getTarget();
                    if(_obj != null){
                        _builder.append(_obj.getClass().getName());
                    }
                }
            }
            if(Proxy.isProxyClass(target.getClass())){
                Object object = get(target, "h");
                if(object instanceof org.apache.ibatis.binding.MapperProxy){
                    Class obj = (Class) get(object, "mapperInterface");
                    if(obj != null){
                        _builder.append(obj.getName());
                    }
                }
            }
            _builder.append(".").append(joinPoint.getSignature().getName());
            _builder.append("(");
            if(ArrayUtils.isNotEmpty(joinPoint.getArgs())){
                int i = 0;
                for (Object o : joinPoint.getArgs()) {
                    if(o == null){
                        continue;
                    }
                    _builder.append((i++) == 0 ? "" : ", ").append(o.getClass().getName());
                }
            }
            _builder.append(")");
        }catch (Exception e){
            e.printStackTrace();
        }
        return _builder.toString();
    }

    private Object get(Object object, String...fieldNames) throws Exception {
        Object _object = object;
        for (String fieldName : fieldNames) {
            _object = get(_object);
        }
        return _object;
    }

    private Object get(Object object, String fieldName) throws Exception {
        if(object != null){
            Field field = field(object.getClass(), fieldName);
            if(field != null){
                field.setAccessible(true);
                return field.get(object);
            }
        }
        return null;
    }

    private Field field(Class clazz, String fieldName) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        if(fields != null && fields.length > 0){
            for (Field field : clazz.getDeclaredFields()) {
                if(field.getName().equals(fieldName)){
                    return field;
                }
            }
        }
        Class superClass = clazz.getSuperclass();
        if(superClass != null){
            return field(superClass, fieldName);
        }
        return null;
    }

}
