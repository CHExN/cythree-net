package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Java反射工具类
 */
@Slf4j
public class JavaReflectionUtil {

    /**
     * Java反射bean的get方法
     *
     * @param objectClass 对象类
     * @param fieldName 方法名
     * @return Method
     */
    @SuppressWarnings("unchecked")
    public static Method getGetMethod(Class objectClass, String fieldName) {
        char[] cs = fieldName.toCharArray();
        cs[0] -= 32;
        try {
            return objectClass.getMethod("get" + String.valueOf(cs));
        } catch (Exception e) {
            log.error("Java反射bean的get方法异常", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Java反射bean的set方法
     *
     * @param objectClass 对象类
     * @param fieldName 方法名
     * @return Method
     */
    @SuppressWarnings("unchecked")
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            char[] cs = fieldName.toCharArray();
            cs[0] -= 32;
            return objectClass.getMethod("set" + String.valueOf(cs), parameterTypes);
        } catch (Exception e) {
            log.error("Java反射bean的set方法异常", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行set方法
     *
     * @param o 执行对象
     * @param fieldName 属性
     * @param value 值
     */
    public static void invokeSet(Object o, String fieldName, Object value) {
        Method method = getSetMethod(o.getClass(), fieldName);
        try {
            method.invoke(o, new Object[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行get方法
     *
     * @param o 执行对象
     * @param fieldName 属性
     */
    public static Object invokeGet(Object o, String fieldName) {
        Method method = getGetMethod(o.getClass(), fieldName);
        try {
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
