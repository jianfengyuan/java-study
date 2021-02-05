# Spring启动源码

> ![image-20210112174931175](/Users/kim/Documents/java-study/Spring源码分析.assets/image-20210112174931175.png)
>
> 从**ClassPathXmlApplicationContext**看起, 里面都是构造函数的重载, 找到详细实现的构造函数
>
> ```java
> public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz, @Nullable ApplicationContext parent)
> 			throws BeansException {
> 
> 		super(parent);
> 		Assert.notNull(paths, "Path array must not be null");
> 		Assert.notNull(clazz, "Class argument must not be null");
> 		this.configResources = new Resource[paths.length];
> 		for (int i = 0; i < paths.length; i++) {
> 			this.configResources[i] = new ClassPathResource(paths[i], clazz);
> 		}
> 		refresh(); // 重要函数, 从这里进去往下看
> 	}
> ```
>
> 深入refresh()方法, 找到在**AbstractApplicationContext**里的具体实现
>
> ```java
> private final Object startupShutdownMonitor = new Object();
> // 在初始化和close的时候进行加锁, 确保调用refresh()的时候不能调用close()方法
> @Override
> 	public void refresh() throws BeansException, IllegalStateException {
>     // 这里的加锁代码块, 防止多线程对上下文进行初始化
> 		synchronized (this.startupShutdownMonitor) {
> 			// Prepare this context for refreshing.
>       // 准备Spring上下文的刷新, 设置了起始时间, 设置active标志位
> 			prepareRefresh();
> 
> 			// Tell the subclass to refresh the internal bean factory.
>       // 执行AbstractRefreshableApplicationContext实现的obtainFreshBeanFactory
>       // 删除已有的BeanFactory, 创建新的BeanFactory
>       //加载BeanDefition并注册到BeanDefitionRegistry
> 			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
> 
> 			// Prepare the bean factory for use in this context.
> 			prepareBeanFactory(beanFactory);
> 
> 			try {
> 				// Allows post-processing of the bean factory in context subclasses.
> 				postProcessBeanFactory(beanFactory);
> 
> 				// Invoke factory processors registered as beans in the context.
> 				invokeBeanFactoryPostProcessors(beanFactory);
> 
> 				// Register bean processors that intercept bean creation.
> 				registerBeanPostProcessors(beanFactory);
> 
> 				// Initialize message source for this context.
> 				initMessageSource();
> 
> 				// Initialize event multicaster for this context.
> 				initApplicationEventMulticaster();
> 
> 				// Initialize other special beans in specific context subclasses.
> 				onRefresh();
> 
> 				// Check for listener beans and register them.
> 				registerListeners();
> 
> 				// Instantiate all remaining (non-lazy-init) singletons.
> 				finishBeanFactoryInitialization(beanFactory);
> 
> 				// Last step: publish corresponding event.
> 				finishRefresh();
> 			}
> 
> 			catch (BeansException ex) {
> 				if (logger.isWarnEnabled()) {
> 					logger.warn("Exception encountered during context initialization - " +
> 							"cancelling refresh attempt: " + ex);
> 				}
> 
> 				// Destroy already created singletons to avoid dangling resources.
> 				destroyBeans();
> 
> 				// Reset 'active' flag.
> 				cancelRefresh(ex);
> 
> 				// Propagate exception to caller.
> 				throw ex;
> 			}
> 
> 			finally {
> 				// Reset common introspection caches in Spring's core, since we
> 				// might not ever need metadata for singleton beans anymore...
> 				resetCommonCaches();
> 			}
> 		}
> 	}
> ```
>
> **BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)**代码分析
>
> ```java
> // 这里主要是Spring推断构造方法, 然后通过反射构造原始对象的过程
> protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
> 		// Make sure bean class is actually resolved at this point.
>     // 获取需要实例化的bean对象的Class对象
> 		Class<?> beanClass = resolveBeanClass(mbd, beanName);
> 
> 		if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
> 			throw new BeanCreationException(mbd.getResourceDescription(), beanName,
> 					"Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
> 		}
> 
> 		Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
> 		if (instanceSupplier != null) {
> 			return obtainFromSupplier(instanceSupplier, beanName);
> 		}
> 
> 		if (mbd.getFactoryMethodName() != null) {
> 			return instantiateUsingFactoryMethod(beanName, mbd, args);
> 		}
> 
> 		// Shortcut when re-creating the same bean...
> 		boolean resolved = false;
> 		boolean autowireNecessary = false;
> 		if (args == null) {
> 			synchronized (mbd.constructorArgumentLock) {
> 				if (mbd.resolvedConstructorOrFactoryMethod != null) {
> 					resolved = true;
> 					autowireNecessary = mbd.constructorArgumentsResolved;
> 				}
> 			}
> 		}
> 		if (resolved) {
> 			if (autowireNecessary) {
> 				return autowireConstructor(beanName, mbd, null, null);
> 			}
> 			else {
> 				return instantiateBean(beanName, mbd);
> 			}
> 		}
> 
> 		// Candidate constructors for autowiring?
> 		Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
> 		if (ctors != null || mbd.getResolvedAutowireMode() == AUTOWIRE_CONSTRUCTOR ||
> 				mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
> 			return autowireConstructor(beanName, mbd, ctors, args);
> 		}
> 
> 		// Preferred constructors for default construction?
> 		ctors = mbd.getPreferredConstructors();
> 		if (ctors != null) {
> 			return autowireConstructor(beanName, mbd, ctors, null);
> 		}
> 
> 		// No special handling: simply use no-arg constructor.
> 		return instantiateBean(beanName, mbd);
> 	}
> ```
>
> 

