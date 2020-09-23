package mybatis.sqlsession;

public interface SqlSession {
    /**
     * 根据参数创建一个代理对象
     * */
    <T> T getMapper(Class<T> daoInterfaceClass);

    void close();
}
