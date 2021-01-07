package com.kim.factory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BeanFactory {
    private static Properties props;

    private static Map<String, Object> beans;

    static {
        props = new Properties();
        InputStream in = BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties");
        try {
            assert in != null;
            props.load(in);
            beans = new HashMap<>();
            Enumeration keys = props.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement().toString();
                String beanPath = props.getProperty(key);
                Object value = Class.forName(beanPath).getDeclaredConstructor().newInstance();
                beans.put(key, value);
            }

        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            throw new ExceptionInInitializerError("初始化properties 失敗");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object getBean(String beanName) {

            return beans.get(beanName);
    }
}
