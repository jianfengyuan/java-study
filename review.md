# Java基础

## 多态

#### 向上转型

子类引用的对象转换为父类类型称为向上转型 -> 父类引用指向子类对象

```java
Animal a = new Cat(); // correct
```

如果子类重写了父类方法, 则引用执行的方法为子类方法, 因为其本质上仍是子类对象

父类的引用不能调用父类未实现的方法

对象能调用的方法主要看引用类型

#### 向下转型

把父类对象转为子类对象

```java
Animal a = new Cat();
Cat c = ((Cat) a); // correct
c.eat();
/* -------------------------------- */
// 子类引用不能直接指向父类对象
// 因为子类是父类的拓展, 而父类对象没有子类的拓展, 因此子类型不能直接作为引用
Cat d = (Cat) new Animal();
d.run(); // wrong
```

## 反射

> 加载完类后, 在堆内存的方法区中产生了一个Class类型的对象(一个类只有一个Class对象), 这个对象就包含了完整的类的结构信息. 我们可以通过这个对象看到类的结构. 反射的本质就是通过==JVM生成的class对象反向获取已加载对象的各种信息==

## 泛型

## 	Collections

### 		HashMap

#### Hashmap实现框架

链表 + 哈希表

<img src="/Users/kim/Documents/java-study/review.assets/image-20210105163759459.png" alt="image-20210105163759459" style="zoom:50%;" />

#### HashMap常用域变量

```java
    
		static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16 初始大小, 必须为2的次幂
		
    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30; // 哈希表的最大长度

    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f; // 默认的加载因子

    /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     */
		// 为什么是8, 因为在理想的情况下,随机hashcode算法下所有bin节点的分布频率遵从泊松分布,
		// 只有非常小的概率, 才有可能链表长度达到8, 即很小概率会发生 链表->红黑树 的转换
    static final int TREEIFY_THRESHOLD = 8; // 当链表长度达到8时就转变成红黑树

    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     */
    static final int UNTREEIFY_THRESHOLD = 6; // 当链表长度降到6时就转成普通链表

    /**
     * The smallest table capacity for which bins may be treeified.
     * (Otherwise the table is resized if too many nodes in a bin.)
     * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
     * between resizing and treeification thresholds.
     */
    static final int MIN_TREEIFY_CAPACITY = 64;
		
		 /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     */
    transient int modCount;
```

#### HashTable默认初始值的含义

> - 为什么HashTable的大小要为2的次幂数?
>
>   降低发生碰撞次数, 使散列更加均匀 -> 计算HashTable的下标索引时更加均匀
>
>   表的长度为2的次幂, 那么(length - 1)的二进制最后一位必定是1, 在对hash值做与运算时, 最后一位可能为1, 也可能为0, 那HashTable的所有位置都可以用到. 如果(length - 1) 为偶数, 那与操作以后的最后一位永远是0, 则奇数位的bucket不能取到, 浪费一半的储存空间

#### HashMap.put()

> ==jdk 1.7 中== put()方法处理过程
>
> 1. 处理key为null的情况
> 2. 计算key的hash值, 得到bucket的下标
> 3. 遍历链表节点, 如果找到key相同的节点, 则替换修改value值
> 4. 未找到相同key的节点, 新增一个链表节点
>
> 特殊key值处理(key为null的情况)
>
> 1. hashmap中是允许key和value值为null的, 且key为null只存储一份, 多次存储 会覆盖旧的value
> 2. key为null的存储位置在bucket的头部, 即bucket[0] 的链表
> 3. 如果第一次对key=null做put操作, 会在bucket[0] 处插入一个新的链表节点
>
> ```java
> // jdk 1.7 实现
> // 首先选择table[0]位置的链表，然后对链表做遍历操作，如果有结点的key为null，则将新value值替换掉旧value值，返回旧value值，如果未找到，则新增一个key为null的Entry结点。
> private V putForNullKey(V value) {
>     for (Entry<K,V> e = table[0]; e != null; e = e.next) {
>         if (e.key == null) {
>             V oldValue = e.value;
>             e.value = value;
>             e.recordAccess(this);
>             return oldValue;
>         }
>     }
>     modCount++;
>     addEntry(0, null, value, 0);
>     return null;
> }
> void addEntry(int hash, K key, V value, int bucketIndex) {
>     // 当k-v对的数量大于threadhold, 且当前的bucket下标有链表存在
>   	// 那么就做扩容处理. 扩容后, 重新计算hash值, 得到新的bucket下标
>   	// 再新增新节点
>   	if ((size >= threshold) && (null != table[bucketIndex])) {
>         resize(2 * table.length);
>         hash = (null != key) ? hash(key) : 0;
>         bucketIndex = indexFor(hash, table.length);
>     }
>     createEntry(hash, key, value, bucketIndex);
> }
> 
> // jdk 1.8 实现
> final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
>                    boolean evict) {
>         Node<K,V>[] tab; Node<K,V> p; int n, i;
>         if ((tab = table) == null || (n = tab.length) == 0)
>             n = (tab = resize()).length;
>         if ((p = tab[i = (n - 1) & hash]) == null)
>           // 如果bucket == null, 直接插入新链表节点
>             tab[i] = newNode(hash, key, value, null);
>         else {
>             Node<K,V> e; K k;
>             if (p.hash == hash &&
>                 ((k = p.key) == key || (key != null && key.equals(k))))
>               // bucket的链表头即为对应key值, 直接替换val
>                 e = p;
>             else if (p instanceof TreeNode)
>               // 判断是否是红黑树结构, 使用插入树节点方式插入
>                 e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
>             else {
>                 for (int binCount = 0; ; ++binCount) {
>                     if ((e = p.next) == null) {
>                       	//  插入新链表节点
>                         p.next = newNode(hash, key, value, null);
>                       // 如果链表节点大于阈值
>                         if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
>                             treeifyBin(tab, hash); // 转化成红黑树
>                         break;
>                     }
>                   // 遍历链表, 直到找到对应的key
>                     if (e.hash == hash &&
>                         ((k = e.key) == key || (key != null && key.equals(k))))
>                         break;
>                     p = e;
>                 }
>             }
>             if (e != null) { // existing mapping for key
>                 V oldValue = e.value;
>                 if (!onlyIfAbsent || oldValue == null)
>                     e.value = value;
>                 afterNodeAccess(e);
>                 return oldValue;
>             }
>         }
>         ++modCount;
>   			// 判断是否需要扩容
>         if (++size > threshold)
>             resize(); 
>         afterNodeInsertion(evict);
>         return null;
>     }
> ```

#### HashMap 扩容

>- 什么时候触发扩容
>
>  ==避免链表过长==
>
>  **场景1**：哈希table为null或长度为0；
>  **场景2**：Map中存储的k-v对数量超过了阈值**`threshold`**；
>  **场景3**：链表中的长度超过了`TREEIFY_THRESHOLD`，但表长度却小于`MIN_TREEIFY_CAPACITY`
>
>```java
>// jdk 1.8后 hashmap扩容源码
>final Node<K,V>[] resize() {
>  			// 1. 扩容
>        Node<K,V>[] oldTab = table;
>        int oldCap = (oldTab == null) ? 0 : oldTab.length;
>        int oldThr = threshold;
>        int newCap, newThr = 0;
>        if (oldCap > 0) {
>            if (oldCap >= MAXIMUM_CAPACITY) {
>                // 如果大于最大容量 -> 不扩容
>                threshold = Integer.MAX_VALUE;
>                return oldTab;
>            }
>            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
>                     oldCap >= DEFAULT_INITIAL_CAPACITY)
>              	// 扩容两倍
>                newThr = oldThr << 1; // double threshold
>        }
>        else if (oldThr > 0) 
>        		// 
>          	// initial capacity was placed in threshold
>            newCap = oldThr;
>        else {
>          	// 初始化流程
>          	// zero initial threshold signifies using defaults
>            newCap = DEFAULT_INITIAL_CAPACITY;
>            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
>        }
>        if (newThr == 0) {
>            float ft = (float)newCap * loadFactor; // 更新阈值
>            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
>                      (int)ft : Integer.MAX_VALUE);
>        }
>        threshold = newThr;
>        @SuppressWarnings({"rawtypes","unchecked"})
>  			// 2. 迁移数据
>        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
>        table = newTab;
>        if (oldTab != null) {
>            for (int j = 0; j < oldCap; ++j) {
>                Node<K,V> e;
>              // 遍历每个不为null的bucket
>                if ((e = oldTab[j]) != null) {
>                    oldTab[j] = null;
>                  // 如果链表只有一个节点, 则原地存储
>                    if (e.next == null)
>                        newTab[e.hash & (newCap - 1)] = e;
>                    else if (e instanceof TreeNode)
>                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
>                    else { // preserve order
>                      // "lo"前缀的代表要在原bucket上存储，"hi"前缀的代表要在新的bucket上存储
>               			 // loHead代表是链表的头结点，loTail代表链表的尾结点
>                        Node<K,V> loHead = null, loTail = null;
>                        Node<K,V> hiHead = null, hiTail = null;
>                        Node<K,V> next;
>                        do {
>                            next = e.next;
>                          // 以oldCap=8为例，
>                          //   0001 1000  e.hash=24
>                          // & 0000 1000  oldCap=8
>                          // = 0000 1000  --> 不为0，需要迁移
>                          // 这种规律可发现，[oldCap, (2*oldCap-1)]之间的数据，
>                          // 以及在此基础上加n*2*oldCap的数据，都需要做迁移，剩余的则不用迁移
>                            if ((e.hash & oldCap) == 0) {
>                                if (loTail == null)
>                                    loHead = e;
>                                else
>                                    loTail.next = e;
>                                loTail = e;
>                            }
>                            else {
>                                if (hiTail == null)
>                                    hiHead = e;
>                                else
>                                    hiTail.next = e;
>                                hiTail = e;
>                            }
>                        } while ((e = next) != null);
>                        if (loTail != null) {
>                            loTail.next = null;
>                            newTab[j] = loHead;
>                        }
>                        if (hiTail != null) {
>                            hiTail.next = null;
>                          // 需要搬迁的结点，新下标为从当前下标往前挪oldCap个距离
>                            newTab[j + oldCap] = hiHead;
>                        }
>                    }
>                }
>            }
>        }
>        return newTab;
>    }
>```

#### 计算bucket下标

##### 计算hash值

> 使用了一种可尽量减少碰撞的算式(高位运算), 得到hashCode, 然后使用key的hashCode作为算式输入, 得到hash值

##### 取模的逻辑

> ```java
> // jdk 1.7 写法
> static int indexFor(int h, int length) {
>     return h & (length-1);
> }
> // jdk 1.8 内嵌在方法中 (n - 1) & hash
> final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
>                    boolean evict) {
>         Node<K,V>[] tab; Node<K,V> p; int n, i;
>         if ((tab = table) == null || (n = tab.length) == 0)
>             n = (tab = resize()).length;
>         if ((p = tab[i = (n - 1) & hash]) == null)
>             tab[i] = newNode(hash, key, value, null);
>         else {
> 				...
>     }
> ```

#### HashMap中的快速失效策略(fail-fast)

> 在hashmap中, ==modCount==用于实现hashmap中的fail-fast. 在对Map做迭代操作时, 会将==modCount==变量赋值给==expectedModCount==. 在迭代过程中用于做内容修改次数的一致性检验. 如果此时有其他线程或本线程的其他操作对此Map内容做修改时, 会导致==modCount==和==expectedModCount==不一致, 立即抛出异常 ==ConcurrentModificationException==.

```java
// 错误姿势
public static void main(String[] args) {
  Map<String, Integer> testMap = new HashMap<>();
  testMap.put("s1", 11);
  testMap.put("s2", 22);
  testMap.put("s3", 33);


  for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
      String key = entry.getKey();
      if ("s1".equals(key)) {
          testMap.remove(key);
      }
  }
}


---- output ---
Exception in thread "main" java.util.ConcurrentModificationException
  at java.util.HashMap$HashIterator.nextNode(HashMap.java:1437)
  at java.util.HashMap$EntryIterator.next(HashMap.java:1471)
  at java.util.HashMap$EntryIterator.next(HashMap.java:1469)
    ...
  
// 正确姿势
Iterator<Entry<String, Integer>> iterator = testMap.entrySet().iterator();
while (iterator.hasNext()) {
    Entry<String, Integer> next = iterator.next();
    System.out.println(next.getKey() + ":" + next.getValue());
    if (next.getKey().equals("s2")) {
        iterator.remove();
    }
}
```

#### HashMap VS HashTable

|            | HashMap                | HashTable                                 |
| :--------- | ---------------------- | ----------------------------------------- |
| 初始值     | 初始值为2的次幂数      | 初始值为11, 扩容为原大小的2*d + 1         |
| null值     | key 和 value可以为null | key和value都不可以为null                  |
| 线程安全性 | 线程不安全             | 线程安全(使用**synchronize**保证线程安全) |

HashTable虽然线程安全, 但是使用synchronize这个重量级锁导致效率比较低

#### HashMap数据不安全性

- 数据覆盖: 因为bucket没有加锁, 因此在高并发的情况下, 对bucket的操作会互相覆盖
- 扩容死循环(jdk 1.7 以前): jdk1.8以后使用双链表尾插法, 解决死循环问题

#### 线程安全的HashMap -> ConcurrentHashMap

## IO

# Java进阶

## 	高并发

## 	GC

## JVM

### 类的加载(ClassLoader)

> 当我们新建一个对象new student(), JVM会从磁盘中寻找并加载student.class对象JVM内存中. JVM自动创建一个关于student.class的class, 这个class只能由JVM产生, 其构造方法是private的. 这个class包含的是student.class的一些信息, 并且一个类只会生成一个class对象

## 对象内存分布

# Spring

## Spring

### IoC(控制反转)

> 把对象的创建, 初始化, 销毁交给Spring处理, 而不是由开发者控制, 实现控制反转

### 依赖注入(Dependency Injection)

> 

## Spring MVC

### Spring MVC工作流程

<img src="/Users/kim/Documents/java-study/review.assets/image-20210106173422068.png" alt="image-20210106173422068" style="zoom:50%;" />

<img src="https://user-gold-cdn.xitu.io/2018/12/11/1679c3c51136aeb9?imageslim" alt="img" style="zoom:50%;" />

> 1. 当请求到达springmvc前段控制器的时候, 会到达DispatcherServlet的doService()方法
>
> 2. 然后调用doDispatcher()方法
>
> 3. 在doDispatcher() 调用了getHandler()方法获取合适的处理器, 这里会判断这个请求是否带有文件, 如果是MultipartRequest, 后面还有清理操作. 
>
>    ```java
>    processedRequest = checkMultipart(request);
>    multipartRequestParsed = (processedRequest != request);
>    
>    // Determine handler for the current request.
>    mappedHandler = getHandler(processedRequest);
>    ```
>
> 4. getHandler()会获取当前请求的处理器链**HandlerExecutionChain**, 当前处理器链封装了负责请求的处理器及其方法
>
>    <img src="/Users/kim/Documents/java-study/review.assets/image-20210108201111853.png" alt="image-20210108201111853" style="zoom:50%;" />
>
>    ```java
>    // AbstractDetectingUrlHandlerMapping
>    // 查询对应的handler
>    protected void detectHandlers() throws BeansException {
>    		ApplicationContext applicationContext = obtainApplicationContext();
>    		String[] beanNames = (this.detectHandlersInAncestorContexts ?
>    				BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class) :
>    				applicationContext.getBeanNamesForType(Object.class));
>    
>    		// Take any bean name that we can determine URLs for.
>    		for (String beanName : beanNames) {
>    			String[] urls = determineUrlsForHandler(beanName);
>    			if (!ObjectUtils.isEmpty(urls)) {
>    				// URL paths found: Let's consider it a handler.
>    				registerHandler(urls, beanName);
>    			}
>    		}
>    
>    		if ((logger.isDebugEnabled() && !getHandlerMap().isEmpty()) || logger.isTraceEnabled()) {
>    			logger.debug("Detected " + getHandlerMap().size() + " mappings in " + formatMappingName());
>    		}
>    	}
>    ```
>
>    
>
>    ```java
>    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
>    		if (this.handlerMappings != null) {
>    			for (HandlerMapping mapping : this.handlerMappings) {
>    				HandlerExecutionChain handler = mapping.getHandler(request); 
>    				if (handler != null) {
>    					return handler;
>    				}
>    			}
>    		}
>    		return null;
>    	}
>    ```
>
> 5. 然后根据获取到的处理器获取对应的处理器适配器, 通过getHandlerAdapter()获取
>
>    ```java
>    // Determine handler adapter for the current request.
>    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler()); 
>    /* =========================================================================*/
>    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
>    		if (this.handlerAdapters != null) {
>    			for (HandlerAdapter adapter : this.handlerAdapters) {
>    				if (adapter.supports(handler)) {
>    					return adapter;
>    				}
>    			}
>    		}
>    		throw new ServletException("No adapter for handler [" + handler +
>    				"]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
>    	}
>    ```
>
> 6. 然后调用handle()方法处理请求, 返回modelAndView对象, ModelAndView包含了视图逻辑名和渲染视图时需要用到的模型数据对象
>
>    ```java
>    // Actually invoke the handler.
>    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
>    // go into handle()
>    // AbstractHandlerMethodAdapter
>    @Override
>    	@Nullable
>    	public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
>    			throws Exception {
>    
>    		return handleInternal(request, response, (HandlerMethod) handler);
>    	}
>    // go into handleInternal()
>    // RequestMappingHandlerAdapter
>    protected ModelAndView handleInternal(HttpServletRequest request,
>    			HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
>    
>    		ModelAndView mav;
>    		checkRequest(request);
>    
>    		// Execute invokeHandlerMethod in synchronized block if required.
>    		if (this.synchronizeOnSession) {
>    			HttpSession session = request.getSession(false);
>    			if (session != null) {
>    				Object mutex = WebUtils.getSessionMutex(session);
>    				synchronized (mutex) {
>    					mav = invokeHandlerMethod(request, response, handlerMethod);
>    				}
>    			}
>    			else {
>    				// No HttpSession available -> no mutex necessary
>    				mav = invokeHandlerMethod(request, response, handlerMethod);
>    			}
>    		}
>    		else {
>    			// No synchronization on session demanded at all...
>    			mav = invokeHandlerMethod(request, response, handlerMethod);
>    		}
>    
>    		if (!response.containsHeader(HEADER_CACHE_CONTROL)) {
>    			if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
>    				applyCacheSeconds(response, this.cacheSecondsForSessionAttributeHandlers);
>    			}
>    			else {
>    				prepareResponse(response);
>    			}
>    		}
>    
>    		return mav;
>    	}
>    // go into invokeHandlerMethod() -> 真正调用的方法, 最后返回modelAndView对象
>    protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
>    			HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
>    
>    		ServletWebRequest webRequest = new ServletWebRequest(request, response);
>    		try {
>    			WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
>    			ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
>    
>    			ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
>    			if (this.argumentResolvers != null) {
>    				invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
>    			}
>    			if (this.returnValueHandlers != null) {
>    				invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
>    			}
>    			invocableMethod.setDataBinderFactory(binderFactory);
>    			invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
>    
>    			ModelAndViewContainer mavContainer = new ModelAndViewContainer();
>    			mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
>    			modelFactory.initModel(webRequest, mavContainer, invocableMethod);
>    			mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
>    
>    			AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
>    			asyncWebRequest.setTimeout(this.asyncRequestTimeout);
>    
>    			WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
>    			asyncManager.setTaskExecutor(this.taskExecutor);
>    			asyncManager.setAsyncWebRequest(asyncWebRequest);
>    			asyncManager.registerCallableInterceptors(this.callableInterceptors);
>    			asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);
>    
>    			if (asyncManager.hasConcurrentResult()) {
>    				Object result = asyncManager.getConcurrentResult();
>    				mavContainer = (ModelAndViewContainer) asyncManager.getConcurrentResultContext()[0];
>    				asyncManager.clearConcurrentResult();
>    				LogFormatUtils.traceDebug(logger, traceOn -> {
>    					String formatted = LogFormatUtils.formatValue(result, !traceOn);
>    					return "Resume with async result [" + formatted + "]";
>    				});
>    				invocableMethod = invocableMethod.wrapConcurrentResult(result);
>    			}
>    
>    			invocableMethod.invokeAndHandle(webRequest, mavContainer);
>    			if (asyncManager.isConcurrentHandlingStarted()) {
>    				return null;
>    			}
>    
>    			return getModelAndView(mavContainer, modelFactory, webRequest);
>    		}
>    		finally {
>    			webRequest.requestCompleted();
>    		}
>    	}
>    ```
>
> 7. 得到modelAndView对象后, DispatcherServlet需要得到逻辑名对应的真实对象, 这项视图解析的工作通过调用ViewResolver来完成
>
> 8. 当得到真实的视图对象后, DispatcherServlet将请求分派给这个view对象, 由他完成Model数据的渲染工作, **processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException)**
>
>    ==7,8 两步在processDispatchResult()方法中的render()方法中执行==
>
> 9. 最终客户端得到响应

>SpringMVC各個模块的作用
>
>使用SpringMVC时，所有的请求都是最先经过DispatcherServlet的，然后由DispatcherServlet选择合适的HandlerMapping和HandlerAdapter来处理请求，==HandlerMapping的作用就是找到请求所对应的方法==，而==HandlerAdapter则来处理和请求相关的的各种事情==。

SpringMVC 如何处理request (从request 到controller的过程, 图中 1- 6的过程)

> 流程 1: DispatcherServlet 接收处理request
>
> ==DispatcherServlet== 是整个Spring MVC 的入口, 在==DispatcherServlet==之前做工作的其实是J2EE, 一个WEB程序的入口是web.xml(在没有使用Spring Boot的情况下), 一个请求过来, J2EE会先在==web.xml==文件中寻找合适的==servlet-mapping==, 找到了就交给对应的Servlet处理. 因为我们需要Spring MVC处理所有请求, 因此**/***表示Spring MVC匹配所有请求, 所有请求都会交给==DispatcherServlet== 处理.  
>
> ```xml
> <servlet>
>     <servlet-name>Spring web</servlet-name>
>     <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
>     <init-param>
>       <param-name>contextConfigLocation</param-name>
>       <param-value>classpath:spring/web-context.xml</param-value>
>     </init-param>
>     <load-on-startup>1</load-on-startup>
>   </servlet>
>  
>   <servlet-mapping>
>     <servlet-name>Spring web</servlet-name>
>     <url-pattern>/*</url-pattern>
>   </servlet-mapping>
> ...
> ```

#### DispatcherServlet

> 流程 2-3
>
> <img src="/Users/kim/Documents/java-study/review.assets/image-20210106175944616.png" alt="image-20210106175944616" style="zoom:50%;" />
>
> DispatcherServlet 是一个标准的Servlet对象, 声明周期也有3个, 初始化, 请求处理, 销毁. 分别对应Servlet的init(), service() 和destroy()
>
> 左边这条线就是Servlet的线，HttpServlet以及之上就是J2EE部分的代码，关注的是对请求的处理，比如doGet，doPost这些。下面HttpServletBean获取环境变量以方便子类使用。然后FrameworkServlet主要维护了自己的上下文对象webApplicationContext。我们知道一般的Servlet是不维护上下文对象的，而DispatcherServlet就是因为继承了FrameworkServlet，所以拥有了自己的上下文。
>
> ```java
> // HttpServlet.service()部分代码    
> protected void service(HttpServletRequest req, HttpServletResponse resp)
>      throws ServletException, IOException {
>      String method = req.getMethod();
>      if (method.equals(METHOD_GET)) {
>          long lastModified = getLastModified(req);
>          if (lastModified == -1) {
>              // servlet doesn't support if-modified-since, no reason
>              // to go through further expensive logic
>              doGet(req, resp);
>          } 
>        ...
>      } else if (method.equals(METHOD_HEAD)) {
>          long lastModified = getLastModified(req);
>          maybeSetLastModified(resp, lastModified);
>          doHead(req, resp);
> 
>      } else if (method.equals(METHOD_POST)) {
>          doPost(req, resp);
> 
>      } else if (method.equals(METHOD_PUT)) {
>          doPut(req, resp);
>      }
>    ....
> ```
>
> DispatcherServlet 初始化过程
>
> **Servlet** 接口的init()接口
>
> ```java
> public void init(ServletConfig config) throws ServletException;
> ```
>
> 实现类**GenericServlet**实现一个带参的init()方法, 接收J2EE环境传来的配置对象Config
>
> ```java
> 		@Override
>  public void init(ServletConfig config) throws ServletException {
>      this.config = config;
>      this.init();
>  }
> ```
>
> **HttpServletBean**初始化方法
>
> ```java
> @Override
> 	public final void init() throws ServletException {
> 
> 		// Set bean properties from init parameters.
>  // 获取配置文件，就是web.xml中contextConfigLocation的值
> 		PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
> 		if (!pvs.isEmpty()) {
> 			try {
>      // 将Servlet包装成一个bean
> 				BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
>      //获取服务器信息
> 				ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
> 				bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
> 				// 初始化bean
>      initBeanWrapper(bw);
>      // 设置配置文件到bean
> 				bw.setPropertyValues(pvs, true);
> 			}
> 			catch (BeansException ex) {
> 				if (logger.isErrorEnabled()) {
> 					logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
> 				}
> 				throw ex;
> 			}
> 		}
> 
> 		// Let subclasses do whatever initialization they like.
>  // initServletBean() 是HttpServletBean的一個抽象方法
> 		initServletBean();
> 	}
> ```
>
> HttpServletBean提供了两个抽象方法供子类实现==initServletBean()==和==initBeanWrapper(BeanWappper bw)== 在DispatcherServlet的父类 ==FrameworkServlet== 中实现的是 ==initServletBean()==
>
> **FrameworkServlet**实现initServletBean()方法, 但这个不重要, **DispatcherServlet**没有使用该方法, 而是使用了initWebApplicationContext中的**onRefresh()**方法
>
> ```java
> protected WebApplicationContext initWebApplicationContext() {
> // 检查ApplicationContext是否被初始化过, 如果有就直接拿来用
> 		WebApplicationContext rootContext =
> 				WebApplicationContextUtils.getWebApplicationContext(getServletContext());
> 		WebApplicationContext wac = null;
> 
> 		if (this.webApplicationContext != null) {
> 			// A context instance was injected at construction time -> use it
> 			wac = this.webApplicationContext;
> 			if (wac instanceof ConfigurableWebApplicationContext) {
> 				ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
> 				if (!cwac.isActive()) {
> 					// The context has not yet been refreshed -> provide services such as
> 					// setting the parent context, setting the application context id, etc
> 					if (cwac.getParent() == null) {
> 						// The context instance was injected without an explicit parent -> set
> 						// the root application context (if any; may be null) as the parent
> 						cwac.setParent(rootContext);
> 					}
> 					configureAndRefreshWebApplicationContext(cwac);
> 				}
> 			}
> 		}
> // 如果ApplicationContext没被初始化就找能用的
> 		if (wac == null) {
> 			// No context instance was injected at construction time -> see if one
> 			// has been registered in the servlet context. If one exists, it is assumed
> 			// that the parent context (if any) has already been set and that the
> 			// user has performed any initialization such as setting the context id
> 			wac = findWebApplicationContext();
> 		}
>  // 如果ApplicationContext没找到, 就新建一个
> 		if (wac == null) {
> 			// No context instance is defined for this servlet -> create a local one
> 			wac = createWebApplicationContext(rootContext);
> 		}
> 		if (!this.refreshEventReceived) {
> 			// Either the context is not a ConfigurableApplicationContext with refresh
> 			// support or the context injected at construction time had already been
> 			// refreshed -> trigger initial onRefresh manually here.
> 			synchronized (this.onRefreshMonitor) {
> 				onRefresh(wac);
> 			}
> 		}
> 
> // 保存自己的上下文信息到ApplicationContext中
> 		if (this.publishContext) {
> 			// Publish the context as a servlet context attribute.
> 			String attrName = getServletContextAttributeName();
> 			getServletContext().setAttribute(attrName, wac);
> 		}
> 		return wac;
> 	}
> ```
>
> **DispatcherServlet**重写了onRefresh()方法, 根据上下文信息, 在里面初始化各种Bean
>
> ```java
> @Override
> 	protected void onRefresh(ApplicationContext context) {
> 		initStrategies(context);
> 	}
> 
> 	/**
> 	 * Initialize the strategy objects that this servlet uses.
> 	 * <p>May be overridden in subclasses in order to initialize further strategy objects.
> 	 */
> 	protected void initStrategies(ApplicationContext context) {
>     // 用于处理文件上传
> 		initMultipartResolver(context);
>     // 支持国际化,区域解析器。
> 		initLocaleResolver(context);
>     // 动态更换样式的支持(主题)
> 		initThemeResolver(context);
>     // 负责根据http请求选择合适的controller
> 		initHandlerMappings(context);
> 		initHandlerAdapters(context);
> 		initHandlerExceptionResolvers(context);
> 		initRequestToViewNameTranslator(context);
>     // 视图解析器：定义了如何通过view 名称来解析对应View实例的行为
> 		initViewResolvers(context);
> 		initFlashMapManager(context);
> 	}
> ```
>
> 
>
 #### DispatcherServlet的各种處理器
 ##### **MultipartResolver**
>
> **MultipartResolver**: 当Web请求到达DispatcherServlet 并等待处理的时候, DispatcherServlet首先会检查是否從WebApplicationContext中找到一個名称为multipartResolver的MultipartResolver的实例. 如果可以获得该实例, DispatcherServlet将调用MultipartResolver的 isMultipart(request) 方法检查当前request是否为multipart类型(是否包含文件). 如果是, **DispatcherServlet**将调用**MultipartResolver**的**resolveMultipart**方法, 对原始的request进行装饰, 并返回一个**MultipartHttpServletRequest** 供后继处理流程使用( HttpServletRequest -> MultipartServletRequest ), 否则, 直接返回HttpServletRequest.
>
> ```java
> protected HttpServletRequest checkMultipart(HttpServletRequest request) throws MultipartException {
>   // 判断multipartResolver 不为空 且 请求包含文件
> 		if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
> 			if (WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class) != null) {
> 				if (request.getDispatcherType().equals(DispatcherType.REQUEST)) {
> 					logger.trace("Request already resolved to MultipartHttpServletRequest, e.g. by MultipartFilter");
> 				}
> 			}
> 			else if (hasMultipartException(request)) {
> 				logger.debug("Multipart resolution previously failed for current request - " +
> 						"skipping re-resolution for undisturbed error rendering");
> 			}
> 			else {
> 				try {
> 					return this.multipartResolver.resolveMultipart(request);
> 				}
> 				catch (MultipartException ex) {
> 					if (request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) != null) {
> 						logger.debug("Multipart resolution failed for error dispatch", ex);
> 						// Keep processing error dispatch with regular request handle below
> 					}
> 					else {
> 						throw ex;
> 					}
> 				}
> 			}
> 		}
> 		// If not returned before: return original request.
> 		return request;
> 	}
> ```
>
> 
>
> **MultipartResolver** 接口
>
> ```java
> public interface MultipartResolver {
> 
> 	/**
> 	 * Determine if the given request contains multipart content.
> 	 * <p>Will typically check for content type "multipart/form-data", but the actually
> 	 * accepted requests might depend on the capabilities of the resolver implementation.
> 	 */
> 	boolean isMultipart(HttpServletRequest request);
> 
> 	/**
> 	 * Parse the given HTTP request into multipart files and parameters,
> 	 * and wrap the request inside a
> 	 * {@link org.springframework.web.multipart.MultipartHttpServletRequest}
> 	 * object that provides access to file descriptors and makes contained
> 	 * parameters accessible via the standard ServletRequest methods.
> 	 */
> 	MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException;
> 
> 	/**
> 	 * Cleanup any resources used for the multipart handling,
> 	 * like a storage for the uploaded files.
> 	 */
> 	void cleanupMultipart(MultipartHttpServletRequest request);
> 
> }
> ```
>
> 
>
> MultipartResolver的实现类 **CommonsMultipartResolver**: 封装成 **DefaultMultipartHttpServletRequest** , 若为懒加载模式, 会在 DefaultMultipartHttpServletRequest 的 initializeMultipart() 方法调用 parseRequest() 方法对请求数据进行解析，而 initializeMultipart()(==@DefaultMultipartHttpServletRequest==) 方法又是被 getMultipartFiles() (==@AbstractMultipartHttpServletRequest== )方法调用，即当需要获取文件信息时才会去解析请求数据，这种方式用了懒加载的思想。
>
> ```java
> /** DefaultMultipartHttpServletRequest **/
> protected Map<String, String[]> getMultipartParameters() {
> 		if (this.multipartParameters == null) {
> 			initializeMultipart();
> 		}
> 		return this.multipartParameters;
> 	}
> /** CommonsMultipartResolver **/ 
> @Override
> 	public MultipartHttpServletRequest resolveMultipart(final HttpServletRequest request) throws MultipartException {
> 		Assert.notNull(request, "Request must not be null");
>     // 判断是否懒加载
> 		if (this.resolveLazily) {
> 			return new DefaultMultipartHttpServletRequest(request) {
> 				@Override
>         // 懒加载: 当getMultipartFiles()方法被调用时，如果还未解析请求数据，则调用initializeMultipart()方法进行解析
> 				protected void initializeMultipart() {
> 					MultipartParsingResult parsingResult = parseRequest(request);
> 					setMultipartFiles(parsingResult.getMultipartFiles());
> 					setMultipartParameters(parsingResult.getMultipartParameters());
> 					setMultipartParameterContentTypes(parsingResult.getMultipartParameterContentTypes());
> 				}
> 			};
> 		}
> 		else {
>       // 立即解析请求数据，并将解析结果封装到DefaultMultipartHttpServletRequest对象中
> 			MultipartParsingResult parsingResult = parseRequest(request);
> 			return new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(),
> 					parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
> 		}
> 	}
> 
> ```
>
> MultipartResolver的另一个实现类 **StandardServletMultipartResolver** (SpringBoot默认使用): 使用Servlet 3.0解析Multipart请求
>
> ```java
> 	@Override
> 	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
> 		return new StandardMultipartHttpServletRequest(request, this.resolveLazily);
> 	}
> ```
>
> 包装成**StandardMultipartHttpServletRequest**对象, 在里面对Request解析
>
> ```java
> public StandardMultipartHttpServletRequest(HttpServletRequest request, boolean lazyParsing)
> 			throws MultipartException {
>       super(request);
>       if (!lazyParsing) {
>         parseRequest(request);
>       }
> 	}
> /*
> * parseRequest() 方法利用了 servlet3.0 的 request.getParts() 方法获取上传文件，并将其封装到 MultipartFile 对象中。
> */
> private void parseRequest(HttpServletRequest request) {
> 		try {
> 			Collection<Part> parts = request.getParts();
> 			this.multipartParameterNames = new LinkedHashSet<>(parts.size());
> 			MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap<>(parts.size());
> 			for (Part part : parts) {
> 				String headerValue = part.getHeader(HttpHeaders.CONTENT_DISPOSITION);
> 				ContentDisposition disposition = ContentDisposition.parse(headerValue);
> 				String filename = disposition.getFilename();
> 				if (filename != null) {
> 					if (filename.startsWith("=?") && filename.endsWith("?=")) {
> 						filename = MimeDelegate.decode(filename);
> 					}
> 					files.add(part.getName(), new StandardMultipartFile(part, filename));
> 				}
> 				else {
> 					this.multipartParameterNames.add(part.getName());
> 				}
> 			}
> 			setMultipartFiles(files);
> 		}
> 		catch (Throwable ex) {
> 			handleParseFailure(ex);
> 		}
> 	}
> ```
>

##### HandlerMapping

> 根据http请求选择合适的controller是MVC中一项十分关键的功能，在Spring MVC中，HandlerMapping接口是这一活动的抽象。
>
> 在SpringMVC中, 关于HandlerMapping的使用, 主要包括: 注册和查找. 在HandlerMapping的实现中, 持有一个
>
> **handlerMap HashMap<String, Object>** , 其中key 为http请求的path信息, value可以是一个字符串, 或者是一个处理请求的HandlerExcutionChain, 如果是String, 则会被视为Spring的Bean名称.
>
> Spring MVC 默认使用**BeanNameURLHandlerMapping** 可以根据Bean的name属性映射到URL中.
>
> > 默认情况下Spring MVC会加载当前系统中所有实现了HandlerMapping接口的bean.
>
> Spring MVC提供了一个HandlerMapping接口的抽象类AbstractHandlerMapping，而AbstractHandlerMapping同时还实现了Ordered接口并继承了WebApplicationObjectSupport类。可以让HandlerMapping通过设置setOrder()方法提高优先级，并**通过覆盖initApplicationContext()方法实现初始化的一些工作。**
>
> <img src="/Users/kim/Documents/java-study/review.assets/image-20210107174217951.png" alt="image-20210107174217951" style="zoom:50%;" />
>
> 以**SimpleUrlHandlerMapping**为例

##### HandlerAdapter

> 用来处理请求参数
>
> **initHandlerAdapters**
>
> ```java
> 
> ·private void initHandlerAdapters(ApplicationContext context) {
>         this.handlerAdapters = null;
>         if(this.detectAllHandlerAdapters) {
>             Map ex = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
>             if(!ex.isEmpty()) {
>                 this.handlerAdapters = new ArrayList(ex.values());
>                 OrderComparator.sort(this.handlerAdapters);
>             }
>         } else {
>             try {
>                 HandlerAdapter ex1 = (HandlerAdapter)context.getBean("handlerAdapter", HandlerAdapter.class);
>                 this.handlerAdapters = Collections.singletonList(ex1);
>             } catch (NoSuchBeanDefinitionException var3) {
>                 ;
>             }
>         }
>  
>         if(this.handlerAdapters == null) {
>             this.handlerAdapters = this.getDefaultStrategies(context, HandlerAdapter.class);
>             if(this.logger.isDebugEnabled()) {
>                 this.logger.debug("No HandlerAdapters found in servlet \'" + this.getServletName() + "\': using default");
>             }
>         }
>  
>     }
> ```
>
> > 1. 如果detectAllHandlerAdapters属性为true(默认为true)，根据类型匹配(HandlerAdapter)机制查找上下文及父Spring容器中所有匹配的Bean，将它们作为该类型组件；
> > 2. 如果detectAllHandlerAdapters属性为false，查找名为handlerAdapter类型为HandlerAdapter的Bean作为该类型组件；
> > 3. 如果通过以上方式都找不到，使用DispatcherServlet.properties配置文件中指定的三个实现类分别创建一个适配器，添加到适配器列表中。

## SpringBoot

# MySQL

## 数据库引擎

### MyISAM VS Innodb

### 事务

## SQL优化

### 索引

# 中间件

## 	Redis

## 	Kafka

# 网络协议

## OSI 七层模型 与 TCP/IP 五层模型

<img src="/Users/kim/Documents/java-study/review.assets/image-20210108105822021.png" alt="image-20210108105822021" style="zoom:50%;" />

网络接口层(数据链路层, 物理层) : 负责二进制数据传输, 010101电信号传输

网络层: IP协议, IP寻址

传输层: TCP, UDP协议

应用层(应用层, 表示层, 会话层): 基于传输层规定应用程序的数据格式, 如Email, WWW, FTP

<img src="/Users/kim/Documents/java-study/review.assets/image-20210108112439883.png" alt="image-20210108112439883" style="zoom:50%;" />

## TCP(可靠传输协议)

### TCP表头控制位

> 八位从左到右分别是 CWR，ECE，URG，ACK，PSH，RST，SYN，FIN
>
> **ACK: **该位设为 1, 确认应答的字段有效, TCP规定除了最初建立连接时的 SYN 包之外该位必须设为 1;
>
> **SYN: **用于建立连接, 该位设为 1, 表示希望建立连接, 并在其序列号的字段进行序列号初值设定;
>
> **FIN: **该位设为 1, 表示今后不再有数据发送, 希望断开连接. 当通信结束希望断开连接时, 通信双方的主机之间就可以相互交换 FIN 位置为 1 的 TCP 段.

### 三次握手

> <img src="/Users/kim/Documents/java-study/review.assets/image-20210108140155214.png" alt="image-20210108140155214" style="zoom:50%;" />
>
> 假设 A为客户端, B为服务端
>
> 1. A向B发送请求报文 SYN=1, ACK=0, 选择一个初始序号x, **这个序号表示的是按字节数计算**
> 2. B 收到请求报文, 如果同意建议连接, 则向A 发送连接确认报文, SYN=1, ACK=1, 确认号为x+1, 表明收到报文x, 并请求报文x+1. 同时选择初始序号y.
> 3. A收到B的连接确认报文后, 还要向B发送确认, 序号为x+1, 请求y+1号报文
> 4. B收到A的确认后, 连接建立, 开始发送数据

### 四次挥手(一般但不一定由客户端发起)

> <img src="/Users/kim/Documents/java-study/review.assets/image-20210108140132895.png" alt="image-20210108140132895" style="zoom:50%;" />
>
> 假设A为客户端, B为服务端
>
> 1. A向B发送FIN=1, ACK=1, 并包含一个希望接受者看到自己当前的序列号x 的报文,  此后A端不能再发送数据, 但是可以接收数据
> 2. B收到报文, 上层应用程序会被告知A端发起了关闭工作,  然后回复报文带有确认序号x+1, 表示收到报文x
> 3. 当A端收到B端的确认报文, A端进入等待关闭状态, 继续接收B端没有发送完成的数据
> 4. 等B端所有数据发送完成, B端發送FIN=1, ACK=1報文, 序列号为y
> 5. A端接收到B端的报文后回复确认报文y+1, 然后进入TIME-WAIT状态, 等待2个MSL(最大报文存活时间)后释放连接

### 为什么需要三次握手, 四次挥手?

> - 为什么要三次握手
>
>   客户端和服务端通信前要进行连接, "3次握手"的目的在于让双方都明确自己和对方的收发能力是否正常, **每次都是接收到数据包的一方可以得到一些结论，发送的一方其实没有任何头绪。我虽然有发包的动作，但是我怎么知道我有没有发出去，而对方有没有接收到呢？**
>
>   1. 第一次握手: 客戶端向服务端发送请求信息, 服务端接收到信息. **服务端** 确认 服务端接收功能正常, 客户端发送功能正常
>
>      服务端 ( ==服务端接收功能正常, 客户端发送功能正常== ), 客户端()
>
>   2. 第二次握手: 服务端向客户端发送回复信息, 客户端收到信息. **客户端** 确认 客戶端发送和接收功能正常, 服务端发送功能正常. **服务端** 确认  服务端接收功能正常
>
>      服务端(==服务端接收功能正常, 客户端发送功能正常==) 客户端(==客户端接收和发送功能正常, 服务端接收和发送功能正常==)
>
>   3. 第三次握手: 客户端向服务端发送确认信息, 服务端收到信息
>
>      服务端(==服务端接收和发送功能正常, 客户端发送和接收功能正常==) 客户端(==客户端接收和发送功能正常, 服务端接收和发送功能正常==)
>
>   到此建立连接成功, 双方都明确自己的收发功能正常
>
> - 为什么要四次挥手
>
>   TCP连接是双向传输的对等的模式, 就是说双方都可以同时向对方发送或接收数据. 当有一方要关闭连接时, 会发送指令告知对方, 我要关闭连接了
>
>   1. 第一次挥手: 客户端向服务端发送FIN=1, ACK=1, 表示客户端希望关闭连接
>   
>   2. 第二次挥手: 服务端接收到包后, 只能返回ACK, 表示知道客户端想要断开连接. **而不能立即返回FIN报文**, 因为结束数据传输的"指令"只能由应用层发送, 而传输层只是"搬运工", 无法自主关闭连接. 当服务端接收到这个FIN报文后, 就进入**CLOSE-WAIT**状态
>   
>      **CLOSE-WAIT**: 这个状态是为了让服务器发送还没传送完毕的数据. 
>   
>   3. 第三次挥手: 等待服务端发送完数据后, 服务端会向客户端发送FIN报文, 表示服务端希望关闭连接
>   
>   4. 第四次挥手: 客户端接收到服务端的FIN报文, 则会回复ACK报文, 表示知道, 然后进入**TIME-WAIT**状态, 客户端在两个MSL后悔自动关闭连接, 而服务端在接收到ACK报文后, 就自动关闭连接
>   
>      **TIME-WAIT**: 这个状态是为了确保最后一个确认报文能够到达, 如果服务端没有收到客户端发送的确认报文, 会重新发送FIN报文, 客户端如果在期间收到新的FIN报文, 则需要再等待2个MSL; 另外等待一段时间, 也是为了让本连接持续时间锁产生的所有报文能从网络中消失, 使得下一个新的连接不会出现旧的连接请求报文.
>

### TCP粘包, 拆包 及解决办法

#### 什么是粘包和拆包

> - 正常数据包的发送和接收: 包与包之间没有粘连, 包没有被拆开
>
>   <img src="/Users/kim/Documents/java-study/review.assets/image-20210108164758774.png" alt="image-20210108164758774" style="zoom:50%;" />
>
> - 粘包: 接收端只收到一个数据包, 但这一个数据包中包含了发送端发送的两个数据包的信息. 由于接收端不知道这两个数据包的界限, 所以对于接收端来说很难处理
>
>   <img src="/Users/kim/Documents/java-study/review.assets/image-20210108164957499.png" alt="image-20210108164957499" style="zoom:50%;" />
>
> - 拆包: 接收端虽然接收到两个数据包, 但是一个包是数据不完整的, 另一个是数据包多出了一块, 这种情况即发生了粘包和拆包, 对于接收端来说同样是不好处理的
>
>   <img src="/Users/kim/Documents/java-study/review.assets/image-20210108165139333.png" alt="image-20210108165139333" style="zoom:50%;" />

#### 为什么会发生拆包和粘包

> - 要发送的数据大于TCP发送缓存区剩余空间大小, 就会发生拆包
> - 待发送数据大于最大报文长度, TCP在传输前将会进行拆包
> - 要发送的数据小于TCP发送缓存区的大小, TCP将多次写入缓冲区的数据一次发送出去, 将会发生粘包
> - 接收端的应用层没有及时读取接收缓冲区的数据, 将会发生粘包

#### 粘包, 拆包的解决办法

> 由于TCP本身是面向字节流的, 无法理解上层的业务数据, 所以在底层是无法保证数据包不被拆包重组的, 因此这个问题只能通过上层协议涉及解决
>
> - **消息定长: **发送端将每个数据包封装为固定长度(长度不足的用0补充), 这样接收端每次接收缓冲区中读取固定长度的数据, 就不会发生粘包
> - **设置消息边界: **服务端从网络流中按消息边界分离出消息内容
> - **将消息分为消息头和消息体: **消息头中包含表示消息总长度(或者消息体长度)的字段

### 滑动窗口

> <img src="/Users/kim/Documents/java-study/review.assets/image-20210108173358152.png" alt="image-20210108173358152" style="zoom:50%;" />
>
> 窗口是缓存的一部分, 用在暂时存放字节流. 发送方和接收方各有一个**字节流**, 接收方通过TCP报文中的窗口字段告诉发送方自己的窗口大小, 发送方根据这个值和其他信息设置自己的发送窗口大小. 这里控制的是==字节数大小==
>
> 发送窗口内的字节都允许被发送, 接收窗口内的字节都允许被接收. 如果发送窗口左部的字节已经发送并且收到了确认, 那么就将发送窗口向右滑动一定的距离, 直到左部第一个字节不是已发送且已确认状态; 接收窗口的滑动类似, 接收窗口左部字节已经发送确认并交付主机, 那么窗口就向右滑动
>
> **接收窗口只会对窗口内最后一个按序到达的字节进行确认**. 如: 接收窗口已经接收到的字节为{31, 34, 35}, 其中{31} 按序到达, {34, 35}不是, 因此只对字节31进行确认, 发送端接收到一个字节的确认后, 发送端就知道, 31之前的所有字节都已经被接收.

### 流量控制

> 流量控制是为了控制发送方的速率, 保证接收方来得及接收
>
> 在滑动窗口中则为控制发送方的滑动窗口大小, 从而影响发送方的发送速率.
>
> 发送端主机还会时不时发送一个**窗口探测的数据段**以获取最新的窗口大小信息

### 拥塞控制

> 发送方还需要维护一个叫 **拥塞窗口(cwnd)** 的状态变量, 这里控制的是==数据包数==, 注意: 拥塞窗口只是一个变量, 真正控制发送速率的是**发送窗口**的大小, 拥塞窗口指某一源端数据流在一个RTT内可以最多发送的**数据包**数
>
> <img src="/Users/kim/Documents/java-study/review.assets/image-20210108175317884.png" alt="image-20210108175317884" style="zoom:50%;" />
>
> - 慢开始(慢启动), 拥塞避免
>
>   发送端最初执行慢开始, 令cwnd=1, 发送方只能发送一个报文段, 每当收到确认后, 将cwnd翻倍, 同时设定一个阈值, 当cwnd超过这个阈值时, 进入**拥塞避免**, 每轮cwnd只增加 1, 如果出现了**超时**, 则将**阈值减半**, 重新开始慢启动
>
> - 快重传, 快恢复
>
>   <img src="/Users/kim/Documents/java-study/review.assets/image-20210108192438848.png" alt="image-20210108192438848" style="zoom:50%;" />
>
>   在接收方, 要求每次接收到报文都应该对最后一个已经收到的有序报文段进行确认(回复确认最后一个有序字节数, 这里用报文数描述). 例如已经接收到报文M1 和M2,此时收到M4, 应当发送对M2的确认
>
>   在发送方, 如果收到三个重复的确认, 那么可以知道下一个报文段丢失, 此时执行快重传, 立即重传下一个报文段. 例如: 收方陆续收到M5, M6, M7, 但M3 丢包, 发送方会收到3个M2的确认, 则M3丢失, 立即重传M3
>
>   在这种情况下, 只是丢失个别报文段, 而不是拥塞, 因此执行快速恢复, 阈值变为拥塞窗口的一半, 然后拥塞窗口从阈值开始, 直接进入拥塞避免.

## Socket套接字





