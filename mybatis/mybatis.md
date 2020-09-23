# mybatis

## mybatis 入门框架搭建

1. 使用映射配置文件配置
2. 使用注解方式配置

步骤

1. 创建实体类 如: User.java
2. 创建持久层Dao接口, 如IUserDao.java
3. 编写mybatisConfig.xml文件, 填写jdbc连接信息, 配置`mappers`, 指定映射配置文件地址
4. 创建Dao配置映射文件. `IUserDao.xml`

注意事项

- 映射配置文件的mapper文件标签namespace属性的取值必须是dao接口的全限定类名
- 映射配置文件的操作配置(select操作), id属性的取值必须是dao接口的方法名
- mybatis的映射配置文件位置必须和dao接口的包结构相同

#### jdbcConfig.properties文件

``````properties
driver = com.mysql.cj.jdbc.Driver
url = jdbc:mysql://127.0.0.1:3306/mybatis_data
username = root
password = root
``````

#### mybatis主配置文件 mybatisConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>      <!-- 对应图中的SqlMapConfig.xml -->
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!-- Mybatis的主配置文件 -->
<configuration>
    <!-- 配置properties，来引入连接数据库所需要的对象，即图中的jdbcConfig.properties -->
    <properties resource="jdbcConf.properties">  <!-- 配置信息与使用JDBC连接数据库所需要信息相同 -->
        <!-- resource用于指定配置文件的信息，按照类路径的写法来写，并且必须存在于类路径下 -->
    </properties>

    <!-- 配置环境 -->
    <environments default="mysql">
        <!-- 配置mysql环境 -->
        <environment id="mysql">
            <!-- 配置事务的类型 -->
            <transactionManager type="JDBC"></transactionManager>
            <!-- 配置数据源(连接池) -->
            <dataSource type="POOLED">
                <!-- 配置连接数据库的四个基本信息 -->
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
```
#### mybatis dao配置映射文件 IUserDao.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="dao.IUserDao">
    <!--Select的返回结果封装到 domain.User 对象中-->
    <select id="findAll" resultType="domain.User">
        select * from user;
    </select>
</mapper>
```

#### Mybatis 入门案例

```java
@Test
public static void testfindAll(String[] args) throws Exception {
        // 1. 读取配置文件
        InputStream in = Resources.getResourceAsStream("myBatisConf.xml");
        // 2. 创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        // 3. 使用工厂产生SqlSession对象
        SqlSession session = factory.openSession();
        // 4. 使用SqlSession创建Dao接口的代理对象
        IUserDao userDao = session.getMapper(IUserDao.class);
        // 5. 使用代理对象执行方法
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        // 6. 释放资源
        session.close();
        in.close();
    }
```

步骤:

1. 读取mybatis主配置文件
2. 创建SqlSessionFactory工厂
3. 使用工厂产生SqlSession对象
4. 使用SqlSession创建Dao接口的代理对象
5. 使用代理对象执行方法
6. 释放资源

## 自定义 mybatis 分析

### mybatis使用dao代理对象实现CURD
    1. 创建代理对象
    2. 在代理对象中使用CURD方法
