package mybatis.sqlsession.proxy;

import mybatis.cfg.Mapper;
import mybatis.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

/**
 * @program: mybatisDesign
 * @description:
 * @author: Kim_yuan
 * @create: 2020-09-20 18:42
 **/

public class MapperProxy implements InvocationHandler {
    private Map<String, Mapper> mappers;
    private Connection conn;

    public MapperProxy(Map<String, Mapper> mappers, Connection connection) {
        this.mappers = mappers;
        this.conn = connection;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 获取方法名
        String methodName = method.getName();
        // 2. 获取方法所在类的名称
        String className = method.getDeclaringClass().getName();
        // 3. 组合key
        String key = className + "." + methodName;
        Mapper mapper = mappers.get(key);
        if (mapper == null) {
            throw new IllegalArgumentException();
        }
        return new Executor().selectList(mapper,conn);
    }
}
