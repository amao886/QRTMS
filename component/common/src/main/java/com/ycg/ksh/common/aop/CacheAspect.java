package com.ycg.ksh.common.aop;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.common.exception.TMCException;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.util.*;

public class CacheAspect {

  final Logger logger = LoggerFactory.getLogger(CacheAspect.class);

  private static final String THREAD_LOCAL_FLAG = "THREAD_LOCAL_FLAG";

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final ThreadLocal<Map<String, Object>> caches = ThreadLocal.withInitial(() -> new HashMap<String, Object>());

  private static final Collection<String>  cacheTypes = new ArrayList<String>();

  static {
    cacheTypes.add("java.lang.Byte");
    cacheTypes.add("java.lang.Short");
    cacheTypes.add("java.lang.Integer");
    cacheTypes.add("java.lang.Long");
    cacheTypes.add("java.lang.Double");
    cacheTypes.add("java.lang.Float");
    cacheTypes.add("java.lang.Character");
    cacheTypes.add("java.lang.Boolean");
    cacheTypes.add("java.lang.String");
  }

  private boolean cache = true;
  private boolean log = true;
  private boolean logReturn = false;

  /**
   * 环绕通知：包围一个连接点的通知，可以在方法的调用前后完成自定义的行为，也可以选择不执行。
   * 类似web中Servlet规范中Filter的doFilter方法。
   * @param proceedingJoinPoint 当前进程中的连接点
   * @return
   */
  public Object aroundAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object object = null;
    long stime = System.currentTimeMillis();
    Throwable throwable = null;
    boolean alreadySet = alreadySet();
    try {
      String cacheKey = cacheKey(proceedingJoinPoint);
      if((object = cache(cacheKey)) == null){
        Object[] args = proceedingJoinPoint.getArgs();
        if (args != null && args.length > 0){
          object = proceedingJoinPoint.proceed(args);
        }else{
          object = proceedingJoinPoint.proceed();
        }
        cache(cacheKey, object);
      }
    }catch (Throwable ex){
      throw (throwable = ex);
    } finally {
      if(!alreadySet){
          clean();
          logSomething(proceedingJoinPoint, object, throwable, stime);
      }
    }
    return object;
  }

  private boolean alreadySet(){
    Map<String, Object> map = caches.get();
    if(!map.containsKey(THREAD_LOCAL_FLAG)){
      map.put(THREAD_LOCAL_FLAG, System.nanoTime());
      return false;
    }
    return true;
  }
  private boolean possibleCacheKey(Object object) {
    if(object == null){ return false; }
    Class<?> clazz = object.getClass();
    if(!clazz.isPrimitive()) {
      return cacheTypes.contains(clazz.getName());
    }
    return true;
  }

  private String cacheKey(ProceedingJoinPoint joinPoint) {
    //String cacheKey = null;
    Signature signature = joinPoint.getSignature();
    if(signature instanceof MethodSignature){
      Class<?> clazz = ((MethodSignature) signature).getReturnType();
      if(clazz != null && BaseEntity.class.isAssignableFrom(clazz)) {
        if(ArrayUtils.isNotEmpty(joinPoint.getArgs())){
          if(!Arrays.stream(joinPoint.getArgs()).anyMatch(a -> { return !possibleCacheKey(a); })){
            StringBuilder builder = new StringBuilder(clazz.getSimpleName().toLowerCase());
            for (Object arg : joinPoint.getArgs()) {
              builder.append("#").append(arg);
            }
            return builder.toString();
          }
        }
      }
    }
    return null;
  }

  private Object cache(String cacheKey) {
    if(StringUtils.isNotBlank(cacheKey) && cache){
      Object object = caches.get().get(cacheKey);
      if(object != null){
        logger.debug("cache <- {}", cacheKey);
      }
      return object;
    }
    return null;
  }

  private void cache(String cacheKey, Object object) {
    if(object != null && StringUtils.isNotBlank(cacheKey) && cache){
      caches.get().put(cacheKey, object);
      logger.debug("cache -> {}", cacheKey);
    }
  }

  private void clean(){
    caches.get().clear();
    logger.debug("clean cache");
  }

  private void logSomething(final ProceedingJoinPoint joinPoint, final Object returnValue,  final Throwable throwable,  final long stime){
      if(!log){
        return;
      }
      final long endTime = System.currentTimeMillis();
    Globallys.executor(() -> {
      StringBuilder builder = new StringBuilder();
      builder.append("\r\n").append("++++++++++++++++++++++++++++++++++++++++++++方法调用日志+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
      Object target = joinPoint.getTarget();
      Signature signature = joinPoint.getSignature();
      builder.append("\r\n类名方法\t").append(target.getClass().getName()).append("->").append(signature.getName());
      builder.append("\r\n方法名称\t").append(signature.getName());
      Object[] args = joinPoint.getArgs();
      if(ArrayUtils.isNotEmpty(args)){
        if(signature instanceof MethodSignature){
          String[] names = ((MethodSignature) signature).getParameterNames();
          builder.append("\r\n请求参数\t");
          for (int i = 0; i < args.length; i++) {
            builder.append("\r\n\t\t").append(names != null ? names[i] : i).append(" = ").append(args[i]);
          }
        }
      }
      if(logReturn && returnValue != null){
        builder.append("\r\n返回数据\t").append(value(returnValue));
      }
      boolean printThrowable = false;
      if(throwable != null){
        printThrowable = true;
        builder.append("\r\n执行异常\t");
        if(throwable instanceof TMCException){
          printThrowable = ((TMCException) throwable).isPrintThrowable();
          builder.append(throwable.getClass().getName() + "->"+ throwable.getMessage());
        }else{
          builder.append(throwable.getClass().getName());
        }
      }
      builder.append("\r\n方法耗时\t").append(endTime - stime).append("毫秒");
      builder.append("\r\n").append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
      logger.info(builder.toString());
      if(printThrowable){
        logger.error("业务异常", throwable);
      }
    });
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
        return clazzName +"("+ ((Map<?, ?>) object).size() +")";
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



/*
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
      if(_builder.length() == 0){
        _builder.append(target.getClass().getName());
      }
      _builder.append(".").append(joinPoint.getSignature().getName());
      _builder.append("(");
      if(ArrayUtils.isNotEmpty(joinPoint.getArgs())){
        int i = 0;
        for (Object o : joinPoint.getArgs()) {
          _builder.append((i++) == 0 ? "" : ", ").append(o.getClass().getName());
        }
      }
      _builder.append(")");
    }catch (Exception e){
      e.printStackTrace();
    }
    return _builder.toString();
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

 */





  public void setCache(boolean cache) {
    this.cache = cache;
  }

  public void setLog(boolean log) {
    this.log = log;
  }

  public void setLogReturn(boolean logReturn) {
    this.logReturn = logReturn;
  }
}
