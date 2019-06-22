package com.ycg.ksh.common.util;

import com.ycg.ksh.common.exception.ParameterException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:110686@ycgwl.com">ding xuefeng</a>
 * @createDate 2017-04-28 10:12:29
 */
public abstract class Assert {
	
	public static void assignable(Object source, Class<?> clazz) {
		if(source != null && !clazz.isAssignableFrom(source.getClass())) {
			throw new ClassCastException(source.getClass().getName() +" cannot cast to "+ clazz.getName());
		}
	}
	
	public static void regx(String source, String regxString, String message) {
		notNull(source, message);
		if (!Pattern.matches(regxString, source)) {
			throw new ParameterException(source, message);
		}
	}
	
	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new ParameterException(expression, message);
		}
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new ParameterException(expression, message);
		}
	}
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new ParameterException(object, message);
		}
	}
	public static void notBlank(String object, String message) {
		if (object == null || object.length() <= 0) {
			throw new ParameterException(object, message);
		}
	}
	public static void notBlank(Float object, String message) {
		if (object == null || object <= 0) {
			throw new ParameterException(object, message);
		}
	}
	public static void notBlank(Integer object, String message) {
		if (object == null || object <= 0) {
			throw new ParameterException(object, message);
		}
	}

	public static void notBlank(String arg1, String arg2, String message) {
		if(StringUtils.isBlank(arg1) && StringUtils.isBlank(arg2)){
			throw new ParameterException(arg1 +", "+ arg2, message);
		}
	}


	public static void notBlank(Long object, String message) {
		if (object == null || object <= 0) {
			throw new ParameterException(object, message);
		}
	}
	
	public static void notZero(Long object, String message) {
		if (object == null || object == 0) {
			throw new ParameterException(object, message);
		}
	}
	
	public static void notBetween(double source, double min, double max) {
		if (source < min || source > max) {
			throw new ParameterException(source, "数值范围必须在["+ min +","+ max +"]区间内");
		}
	}
	
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new ParameterException(object, message);
		}
		/*if(object instanceof String){
			notBlank(object.toString(), message);
		}
		if(object instanceof Number){
			if(((Number) object).doubleValue() <= 0){
				throw new ParameterException(object, message);
			}
		}*/
	}
	public static void hasLength(String text, String message) {
		if (text == null || text.length() <= 0) {
			throw new ParameterException(text, message);
		}
	}
	public static void hasText(String text, String message) {
		if (text == null || text.length() <= 0) {
			throw new ParameterException(text, message);
		}
	}

	public static void notEmpty(Object[] array, String message) {
		if (array == null || array.length <= 0) {
			throw new ParameterException(Arrays.toString(array), message);
		}
	}

	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new ParameterException(Arrays.toString(array), message);
				}
			}
		}
	}

	public static void notEmpty(Collection<?> collection, String message) {
		if (collection == null || collection.size() <= 0) {
			throw new ParameterException(collection, message);
		}
	}

	public static void notEmpty(Map<?, ?> map, String message) {
		if (map == null || map.size() <= 0) {
			throw new ParameterException(map, message);
		}
	}
	
	public static void equalsLength(String text, int length, String message) {
		if (text == null || text.length() != length) {
			throw new ParameterException(text, message);
		}
	}

	public static void notIn(Integer object, Integer[] values, String message) {
		if (object == null) {
			throw new ParameterException(object, message);
		}
		boolean result = false;
		for (Integer value : values) {
			if(result = (object - value == 0)){
				break;
			}
		}
		if(!result){
			throw new ParameterException(object, message);
		}
	}
}
