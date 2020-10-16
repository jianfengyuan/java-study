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
        // 2. 创建SqlSessionFactory工厂, 使用了 "構建者模式", 
  			// 把對象的創建細節隱藏, 使用者直接調用方法可拿到對象
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

## 使用xml文件配置CURD方法

```xml
<mapper namespace="dao.IUserDao">
  <select id="findAll" resultType="domain.User">
    select * from user;
  </select>
  
  <!-- 保存用戶 -->
  <insert id="saveUser" parameterType="domain.User">
    <!-- 新增用戶id的返回值-->
    <!-- keyProperty對應實體類的字段, keyColumn對應數據庫中的字段-->
    <selectKey keyProperty="id" keyColumn="id" order="AFTER" resultType="java.lang.Integer">
      select last_insert_id();
    </selectKey>
    <!--  values 的注入屬性, 與實體類中的get方法後面的字段對應get(Id)  -->
    insert into user(username, address, sex, birthday)values(#{username},#{address},#{sex},#{birthday});
  </insert>
  
  <!-- 更新用戶 -->
  <update id="updateUser" parameterType="domain.User">
    update user set username=#{username},address=#{address},sex=#{sex},birthday=#{birthdat} where id=#{id}
  </update>
  <!-- 刪除用戶 -->
  <delete id="deleteUser" parameterType="java.lang.Integer">
    <!-- 當佔位符只有一個參數時, 這個參數的名字可以隨便寫,如: uid -->
    delete from user where id=#{uid}
  </delete>
  <!-- 根據id查詢用戶 -->
  <select id="findById" parameterType="java.lang.Integer" resultType="domain.User">
    select * from user where id=#{uid};
  </select>
  <!-- 根據名稱模糊查詢 -->
  <select id="findByName" parameterType="string" resultType="domain.User">
    <!-- 在實際調用是, 需要注入"%username%" -->
    <!-- 這種注入方式使用的是JDBC中preparedstatement的佔位符方式 -->
  	select * from user where username like #{name}
  </select>
  <!-- 獲取用戶記錄總數 -->
  <select id="findTotal" resultType="java.lang.Integer">
    select count(id) from user;
  </select>
  <!-- 根據queryVo的條件查詢用戶 -->
  <select id="findByVo" parameterType="domain.QueryVo" resultType="domian.User">
    select * from user where id=#{user.username}
  </select>
</mapper>
```

### OGNL(Object Graphic Navigation Language)表達式

通過對象的取值方式來獲取數據

mybatis中在parameterType提供屬性所屬的類, 因此不需要寫對象名 user.username-> username

## 採用配置DAO.xml的方式給字段取別名

```xml
<!-- 配置查詢結果的列名和實體類的屬性名的對應關係-->
<resultMap id="userMap" type="domain.User">
  <!-- property對應實體類屬性名(嚴格區分大小寫), column對應數據庫字段名-->
  <!-- 主鍵字段的映射 -->
  <id property="userId" column="id"/>
  <!-- 非主鍵字段的映射 -->
  <result property="userName" column="username"></result>
  <result property="userAddress" column="address"></result>
  <result property="userSex" column="sex"></result>
</resultMap>
<!-- 在執行查詢語句時, 加入字段resultMap, 啟用別名映射 -->
<select id="findAll" resultMap="userMap">
    select * from user;
</select>
```

## 給DAO.xml中的全限定類名起別名

在總配置文件中配置

```xml
<typeAliases>
  <!-- typeAliases 用於配置實體類全限定類名的別名, 當啟用alias時, 在dao.xml中的type屬性不再區分大小寫-->
	<typeAlias type="domain.User" alias="user"></typeAlias>
  <!-- 當實體類太多時, 配置typeAlias比較麻煩, 可以把整個包註冊別名, 類名即別名, 不再區分大小寫 -->
  <package name="domain">
</typeAliases>
```

在總配置文件中的`mappers`也可以用`<package>`對dao接口起別名

```xml
<mappers>
  <!-- 當指定了以後就不需要寫mapper以及resource或者class了-->
  <package name="dao"></package>
</mappers>
```

## Mybatis 連接池

三種配置連接池方式

在主配置文件中的dataSource=標籤, type屬性表示採用何種連接池方式

type屬性取值:

- POOLED 採用傳統的javax.sql.DataSource規範中的連接池, mybatis中有針對規範的實現
- UNPOOLED 採用傳統的獲取連接方式, 沒有使用連接池容器的方式存儲連接
- JNDI 採用服務器提供的JNDI技術實現, 來獲取DataSource對象, 不同服務器獲取連接的方式不同



## 動態SQL語句

```xml
      <!-- mybatisConfig.xml 配置總文件 -->
      <!-- 提前配置好 resultMap -->
      <!-- 配置好resultMap後, 變量名受大小寫影響 -->
      <resultMap id="userMap" type="USER">
        <!-- 主鍵字段別名 -->
      	<id property="UserId" column="userid"/>
        <result property="userName" column="username"/>
        <result property="userAddress" column="address"/>
        <result property="userSex" column="sex"/>
        <result property="userBirthday" column="birthday"/>
      </resultMap>
```

```xml
      <!-- dao配置文件 -->
      <!-- 根據條件查詢 -->
      <!-- 條件查詢寫法 1-->
      <select id="findUserByCondition" resultMap="userMap">
        <!-- 1=1 避免空注入-->
        select * from user where 1=1
        <!--  -->
        <!-- sql語句無關大小寫, java語句屬性區分大小寫 -->
        <if test="userName != null">
          username = #{userName}
        </if>
        <if test="userSex != null">
        	and sex = #{userSex}
        </if>
      </select>
      <!-- 條件查詢寫法 2-->
      <select id="findUserByCondition" resultMap="userMap">
        <!-- 使用 where標籤 -->
          select * from user
        <where>
          <!-- sql語句無關大小寫, java語句屬性區分大小寫 -->
          <if test="userName != null">
            username = #{userName}
          </if>
          <if test="userSex != null">
            and sex = #{userSex}
          </if>
        </where>
      </select>
      <!-- 根據集合查詢 -->
      <!-- foreach標籤 -->
      <select id="findUserInIds" resultMap="userMap" parameterType="queryvo">
        select * from user
        <where>
        	<if test="ids != null and ids.size()>0">
            <!-- item屬性的value 與 #{value}保持一致-->
          	<foreach collection="ids" open="and id in (" close=")" item="id" separator=",">
            	#{id}
            </foreach>
          </if>
        </where>
      </select>
      <!-- 抽取重複SQL語句 -->
      <sql id="defaultUser">
        select * from user
      </sql>
      <!-- 重複語句的引用 -->
      <select id="findUserInIds" resultMap="userMap" parameterType="queryvo">
        <include refid="defalutUser"></include>
        <where>
        	<if test="ids != null and ids.size()>0">
            <!-- item屬性的value 與 #{value}保持一致-->
          	<foreach collection="ids" open="and id in (" close=")" item="id" separator=",">
            	#{id}
            </foreach>
          </if>
        </where>
      </select>
```

## mybatis 中多表查詢

示例 : 用戶和賬戶

​			一個用戶可以有多個賬戶, 一個賬戶只能屬於一個用戶

步驟:

- 建立兩張表: 用戶表和賬戶表, 在賬戶表中建立用戶表的外鍵

- 建立兩個實體類: 使用戶和賬戶的實體類能體現一對多的關係
- 建立兩個配置文件: 用戶表和賬戶表的配置文件
- 實現配置: 查詢用戶時, 可以同時得到用戶下所包含的賬戶信息, 查詢賬戶時, 可以同時得到賬戶所屬用戶信息

mybatis中封裝映射關係

> resultMap参数说明：
>
> 1. id指定查询列中的唯一标识，订单信息中的唯一标识，如果有多个列组成唯一标识，配置多个id。
> 2. column：sql语句查询出的field
> 3. property：与sql查询出的field 对应的实体字段
>
> association参数说明：
>
> 1. property：要关联查询的用户信息，本例中指Orders下的user属性。
> 2. javaType：关联的属性的所属类
> 3. id：关联查询用户的唯一标识
> 4. column：sql语句查询出的field
> 5. property：与sql查询出的field 对应的实体字段

  ```xml
  <resultMap id="accountUserMap" type="account" javaType="封裝實體類全限定類名(未定義resultMap)/ resultMapId(已定義resultMap)">
    <id property="id" column="id"/>
    <result property="uid" column="uid"/>
    <result property="money" column="money"/>
    <!-- 一對一的關係映射: 配置封裝user的內容 -->
    <!-- 這裡外鍵就是user.uid -->
    <!-- column(數據表字段名, 外鍵) property(實體類屬性名稱) -->
    <association property="user" column="uid">
      <id property="id" column="id"></id>
      <result column="username" property="username"></result>
      <result column="address" property="address"></result>
      <result column="sex" property="sex"></result>
      <result column="birthday" property="birthday"></result>
    </association>
  </resultMap>
  <select id="findAll" resultType="accountUserMap">
    select u.*, a.id as aid, a.uid as uid, a.money from account a, user u where u.id = a.uid
  </select>
  ```

### 一對多映射關係

```xml
    
		<resultMap id="userAccountMap" type="domain.User">
        <id property="id" column="id"></id>
        <result property="username" column="username"></result>
        <result property="address" column="address"></result>
        <result property="sex" column="sex"></result>
        <result property="birthday" column="birthday"></result>
        <collection property="accounts" ofType="domain.Account">
          	<!-- 在SQL語句中使用了別名a.id as aid, 這裡的column才能用別名aid 否則會對不上 -->
            <id property="id" column="aid"></id>
            <result property="uid" column="uid"></result>
            <result property="money" column="money"></result>
        </collection>
    </resultMap>
		<select id="findUserAccount" resultMap="userAccountMap">
        select u.*, a.id as aid, a.uid, a.money from user u left outer join account a on u.id = a.uid
    </select>
```

## Mybatis 中的緩存機制

### 延遲加載

> 查詢數據時, 數據按需查詢, 按需加載, 不進行全部緩存

```xml
<!-- 配置延遲加載 -->
<!-- 使用延遲
```

### 立即加載

> 不管用不用, 一調用方法, 馬上發起查詢

### 什麼是緩存

### 為什麼使用緩存

### Mybatis的一級緩存和二級緩存

> 一級緩存: Mybatis中SQLSession對象的緩存, 當查詢執行後,
> 查詢的結果會同時存入SQLSession的一塊區域中
> 該區域的結構是一個Map, 當我們再次查詢同樣的數據, Mybatis會先去
> SQLSession中查詢是否有記錄, 有的話直接拿出來用.
> 當SQLSession對象消失時, Mybatis的一級緩存也隨之消失
>
> 二級緩存: Mybatis中SQLSessionFactory對象的緩存, 
> 由同一個SQLSessionFactory對象創建的SQLSession共享緩存.
> 二級緩存存放的內容是數據, 而不是對象, 因此不同SQLSession取出來的數據相同,
> 但不是相同對象
>
> 二级缓存是建立在同一个namespace下的，如果对表的操作查询可能有多个namespace，那么得到的数据就是错误的。
>
> 如果开启了二级缓存，那么在关闭sqlsession后(close)，才会把该sqlsession一级缓存中的数据添加到namespace的二级缓存中。
>
> 如果想让二级缓存命中率不为0,需要先开启一个session,执行一个sql语句,然后关闭该session,然后在创建一个新的session,执行相同的sql语句,这时,二级缓存才会命中

```xml
<!-- 二級緩存配置步驟 -->
<!-- 1. 配置主文件開啟二級緩存(SqlMapConfig.xml) -->
<settings>
  <setting name="cacheEnabled" value="true"></setting>
</settings>
<!-- 2. 當前映射文件支持二級緩存(IUserDao.xml) -->
<cache/>
<!-- 3. 當前操作支持二級緩存(select 標籤中配置) -->
<select id="findAll" resultType="domain.User" useCache="true">
        select * from user;
</select>
```

## Mybatis中的Annotation開發

如果使用Annotation開發, 工程文件中不能包含相關`Dao.xml` 配置文件