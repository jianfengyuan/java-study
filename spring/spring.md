## 基於xml 的bean配置

### bean的三種創建方式

```xml
<!-- 1. 使用默認構造函數創建 -->
<!-- 在spring的配置文件中使用bean標籤, 配以id和class屬性後, 且沒有其他屬性和標籤時 -->
<!-- 採用默認構造函數創建bean對象, 此時如果類中沒有默認構造函數, 則對象無法創建 -->
<bean id = "accountService(唯一名稱)" class="impl.AccountServiceImpl(全限定類名)"></bean>
<!-- 2. 根據第三方jar包文件的構造方法 創建bean對象 -->
<!-- 使用普通工廠中的方法創建對象: 先構建普通工廠對象, 調用工廠構造方法 -->
<bean id="instanceFactory" class="factory.InstanceFactory"></bean>
<bean id="accountService" factory-bean="instanceFactory" factory-method="getAccountService"/>
<!-- 3. 使用工廠中的靜態方法創建對象-->
<bean id="accountService" class="factory.StaticFactory" factory-method="getAccountService"/>
```

### bean的作用範圍

```xml
<!-- 創建出來的bean對象 默認是單例的 可以使用scope調整bean的作用範圍-->
<!-- scope取值: singleton, prototype, request, session, global-session -->
<bean id = "accountService(唯一名稱)" class="impl.AccountServiceImpl(全限定類名)" scope="singleton"></bean>
```

### bean對象的生命週期

```xml
<!-- bean對象的生命週期
		 單例對象:
				出生: 容器創建時, 對象出生
				活著: 容器存在, 對象存活
				死亡: 容器銷毀時, 對象消亡
		 多例對象:
				出生: 容器創建時, 對象被創建
				活著: 對象一直存活
				死亡: 由GC回收
-->
<bean id = "accountService(唯一名稱)" class="impl.AccountServiceImpl(全限定類名)" scope="singleton" init-method="init" destroy-method="destroy"></bean>
```

### spring中的依賴注入

依賴注入(dependency injection): 在當前類需要用到其他類的對象, 由spring提供, 用戶只需要再配置文件中說明依賴關係的維護. 經常變化的數據, 不適用於注入方式.
依賴注入的類型:
			基本類型和String
			其他bean類型(在配置文件中或者註解配置過的bean)
			複雜類型/ 集合類型
注入方式:
			1.使用構造函數提供
			2.使用set方法提供
			3.使用註解提供

```xml
<!-- 1. 使用構造函數注入(帶參) 
			使用的標籤: constructor-arg
			標籤出現的位置: bean標籤內部
			標籤中的屬性: 
					type: 用於指定要注入的數據的數據類型, 該數據類型也是構造函數中某個或某些參數的類型
					index: 用於指定要注入的數據給構造函數中指定索引位置的參數賦值. 索引位置從0開始
					name: 用於指定給構造函數中指定名稱的函數賦值
					value:
					ref:
			優勢: 在獲取bean對象時, 注入數據是必須的操作, 否則對象無法創建成功
			劣勢: 改變了bean對象的實例化方式, 使我們在創建對象時, 如果用不到這些數據也必須提供
-->
<bean id = "accountService(唯一名稱)" class="impl.AccountServiceImpl(全限定類名)" scope="singleton" init-method="init" destroy-method="destroy">
	<constructor-arg name="name" value="test"/>
  <constructor-arg name="age" value="16"/>
  <constructor-arg name="birthday" ref="now"/>
</bean>
<bean id="now" class="java.util.Date">
<!-- 2. set方法注入
				使用的標籤: property
				出現的位置: bean標籤的內部
				標籤的屬性: 
					name: 用於指定給構造函數中指定名稱的函數賦值
					value:
					ref:
				在實體類中創建set方法, 在配置文件中調用時 name參數用setName-> name(去掉set, 首字母小寫)
				優勢: 創建對象時沒有明確限制, 可以直接使用默認 
-->
<bean id = "accountService(唯一名稱)" class="impl.AccountServiceImpl(全限定類名)" scope="singleton" init-method="init" destroy-method="destroy">
	<property name="name" value="test"/>
  <property name="age" value="16"/>
  <property name="birthday" ref="now"/>
</bean>
```

## 基於Annotation的bean配置

```java
/**
* 在xml配置中, 常用配置如下
* <bean id = "accountService(唯一名稱)" class="impl.AccountServiceImpl(全限定類名)" scope="singleton" init-method="init" destroy-method="destroy">
	<property name="name" value="test"/>
  <property name="age" value="16"/>
  <property name="birthday" ref="now"/>
</bean>
* 在Annotation配置中:
* 在xml配置文件中配置完掃描包功能後可以啟用註解開發
* 用於創建對象的
* 		與xml配置文件中編寫一個<bean>標籤實現的功能一致
*			@Component:
* 			作用: 用於把當前類對象存入spring容器中
*				屬性: value: 用於指定bean的id, 為空時, 默認值是當前類名, 且首字母改小寫
*			@Controller: 一般用在表現層
*			@Service: 一般用在業務層
*			@Repository: 一般用在持久層
*			上面三個註解的作用和屬性與@Component一致
*			是spring框架為我們提供明確的三層使用的註解.
*	用於注入數據的
*			與xml配置文件中<bean>標籤下的<property>的作用一致
* 		@Autowired
*				作用: 自動按照類型注入, 只要容器中有唯一的一個bean對象類型和要注入的變量類型匹配(接口類型亦可), 就可注*							入成功.如果IOC中一個匹配的類型都沒有, 則匹配失敗. 如果IOC容器有多個匹配結果時, 
*							 先按照數據類型篩選出目標變量, 再按變量名稱篩選(與bean ID配對)出唯一對應的變量. 
*							 如果沒有對應的, 則報錯
*				出現位置: 可以是變量, 也可以是方法
*				在使用註解方法時 set方法不是必須的
*			@Qualifier:
*				作用: 在按照類中注入的基礎上再按照名稱注入. 他在給類成員注入時不能單獨使用(必須跟@Autowired組合使用). 
*						 但是再給方法參數注入時可以.
*				屬性: 用於指定注入bean的ID
*			@Resource:
*				作用: 直接按照bean的id注入, 可以單獨使用
*				屬性: name: 用於指定bean的ID
*			以上三個注入都只能注入其他bean類型數據, 而基本類型和String類型無法使用上述註解實現
*			集合類型的注入只能通過XML實現
*			@Value
*				作用: 用於注入基本類型和String類型的數據
*				屬性: value: 用於指定數據的值, 也可以使用SpEL(${表達式})
* 用於改變作用範圍的
*			@Scope:
*				作用: 用於指定bean的作用範圍
*				屬性: value: 指定bean的範圍取值(Singleton, Phototype...)
* 與生命週期相關的
*/
```

### 為什麼在調用註解開發時, setter不是必須的

> 在使用@Autowired 實現自動注入時, 他直接進入Spring的IOC容器(Map結構)中, 跳過了通過key找value的過程, 直接根據數據類型進行數據注入, 即使AccountDaoImpl是IAccountDao的實現類, 但接口類型仍然有標示性, 因此數據也可以注入到目標對象中

### bean对象的线程安全问题

​	https://blog.csdn.net/fuzhongmin05/article/details/100849867

### Spring配置類(Annotation)

```java
/*
*	@Configuration: 指定當前類是一個配置類
*	@ComponentScan: 用於通過註解指定spring在創建容器時要掃描的包
*		value/basePackages: 指定要掃描的包
* @Bean: 用於把當前方法的返回值作為bean對象放進spring容器IOC中
*		name: 用於指定bean的id, 當不寫時, 默認值是當前方法的名稱
**/
```

