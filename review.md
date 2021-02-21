# Java基础

## 多态

#### 重载与重写

> **重载**就是同样的一个方法可以根据输入数据的不同, 做出不同的处理
>
> ​	重载: 发生在同一个类中, 方法名必须相同, 参数类型不同, 个数不同, 顺序不同, 方法返回值和访问修饰符可以不同. 编译器通过方法给出的参数类型与特定方法调用所使用的值类型进行匹配
>
> **重写**就是当子类继承自父类的相同方法, 输入数据一样, 但要做出有别于父类的响应时, 就要重写父类方法
>
> ​	重写: 重写发生在运行期, 是子类对父类允许访问的方法的实现过程进行重新编写. 
>
> > 1. 返回值类型, 方法名, 参数列表必须相同, 抛出的异常小于/等于父类, 访问修饰符类型大于/等于父类
> > 2. 如果父类方法访问修饰符为static/ private/ final则子类不能不能重写该方法, 但是被static修饰的方法可以再次被声明
> > 3. 构造方法没办法被重写
> > 4. 如果方法返回类型是void和基本数据类型, 则返回值重写时不可修改, 但是如果方法的返回值是引用类型, 重写时是可以返回该引用类型的子类

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

## StringBuilder, StringBuffer和String

> ```java
> public class Main {   
>     public static void main(String[] args) {
>         String str1 = "hello world";
>         String str2 = new String("hello world");
>         String str3 = "hello world";
>         String str4 = new String("hello world");
>          
>         System.out.println(str1==str2);
>         System.out.println(str1==str3);
>         System.out.println(str2==str4);
>       	String string = "";
>         for(int i=0;i<10000;i++){
>           // 每次循环会new一个StringBuilder对象, 然后执行append操作
>           // 最后通过toString返回String对象
>             string += "hello";
>         }
>     }
> }
> /* ============輸出結果=================*/
> false
> true
> false
> ```
>
> - String类被final修饰, 不可继承, ==String类的任何操作都不是在原字符串上修改的==, 而是会生成新的字符串对象, String的字符串内容属于**字符串常量**, 存放在运行时常量池中.
> - StringBuilder类同样被final修饰, 不可继承, 与String不同的是, StringBuilder是基于**char[ ]**, 默认初始长度为16, 因此StringBuilder的字符串内容可以被动态修改. StringBuilder不是线程安全的
> - StringBuffer类被final修饰, 不可继承, 实现方法与StringBuilder类似. StringBuffer的方法被**synchronized**修饰, 因此是线程安全的.
>
> ```java
> public class Main {
>     public static void main(String[] args) {
>       	String string = "";
>         for(int i=0;i<10000;i++){
>           // 每次循环会new一个StringBuilder对象, 然后执行append操作
>           // 最后通过toString返回String对象
>             string += "hello";
>         }
>       /*********可以看成代码被JVM优化成******************/
>       StringBuilder str = new StringBuilder(string);
> 	　　 str.append("hello");
> 　		　str.toString();
>     }
> }
> ```
>
> - 编译期的优化
>
>   ```java
>    // String 对象的字符串拼接其实是被 JVM 解释成了 StringBuffer 对象的拼接
>   String S1 = "This is only a" + " simple " + " test ";
>   StringBuffer Sb = new StringBuilder("This is only a").append(" simple").append(" test");
>   // 拼接的字符串来自另外的String对象的话，Java Compiler就不会自动转换了
>   String S2 = "This is only a";
>   String S3 = " simple";
>   String S4 = " test";
>   String S1 = S2 +S3 + S4;
>   ```
>
>   

## 动态代理

> - JDK的动态代理对象不需要实现接口, 但要求目标对象必须实现接口, 否则不能使用动态代理
>
> ```java
> public class TestProxy {
>     public static void main(String[] args) {
>       // t是被代理对象
>         p t = new pImpl();
>       // o是代理对象
>         p o = (p) Proxy.newProxyInstance(TestProxy.class.getClassLoader(), t.getClass().getInterfaces(), new InvocationHandler() {
>             @Override
>             public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
>                 if (method.getName().equals("test")){
>                     System.out.println("增强了test方法");
>                     return method.invoke(t, args);
>                 }
>                 return null;
>             }
>         });
>         o.test();
>         System.out.println(o);
>     }
> }
> 
> interface p{
>     void test();
> }
> 
> class pImpl implements p {
>     @Override
>     public void test() {
>         System.out.println("实现test方法");
>     }
> }
> ```
>
> - CGLIB和JDK动态代理的区别
>
>   CGLIB

## 	Collections

### Vector, ArrayList和LinkedList

> - ArrayList和LinkedList都不是线程安全的, Vector是线程安全的
> - Vector底层结构为Object[]数组, ArrayList底层结构为Object[]数组, LinkedList底层结构为链表(jdk 1.6以前是双向循环链表, 1.7以后是双向链表)
> - 插入和删除: ArrayList默认插入到数组尾部(时间复杂度O(1)), 指定位置插入/删除, 需要将第i和第i个元素以后的元素前移/后移一位(复杂度为O(n-i)); LinkedList默认插入到链表尾部(复杂度接近O(1)), 指定位置插入/删除, 需要遍历到指定位置操作(复杂度接近O(n))

### 		HashMap

#### Hashmap实现框架

链表 + 哈希表

<img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210105163759459.png" alt="image-20210105163759459" style="zoom:50%;" />

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
>         bucketIndex = indexFor(hash, table.length); // 00001111按位与操作
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

## ==和equals()

> ==: 判断两个对象的地址是否相同
>
> equals(): 如果类没有重写equals()方法, 通过equals()判断两个对象是否相等时, 与 **"=="**相同; 也可以重写equals()方法, 用于判断两个对象的内容是否相同
>
> - hashCode()
>
>   Object的hashcode方法是本地方法, 是用c/c++实现的, 该方法通常用对象的内存地址转化为整数之后返回
>
> - 为什么重写equals方法必须重写hashCode()方法
>
>   如果两个对象相等, 则hashcode必定相等, 但两个对象的hashcode相等, 两个对象却未必相同, hashCode()和equals()保持一致, 如果equals方法返回true, 那么两个对象的hasCode()返回值必须一样.

## IO

> ```
> exec 8<> /dev/tcp/www.baidu.com/80  # 创建8号IO,开启对百度的TCP连接
> echo -e "GET / HTTP/1.0\n" >& 8 # 向8号IO发送HTTP请求
> cat 0<& 8 # 8号IO得到的数据 复制到0号IO中 并显示
> ```
>
> https://www.cnblogs.com/leeSmall/p/8616316.html

### BIO(Blocked-IO)

> - BIO是传统Socket连接, 需要先开启一个**ServerSocket**监听某个端口, 等待Socket连接, 当有Socket连接后, 会生成一个Socket连接返回, 然后可以开始通信, 这里Socket的getInputStream是从网卡中抓取信息.
> - BIO有两个阻塞点, 一个是**ServerSocket.accept()**阻塞等待连接, 另一个是**Socket.getInputStream.read()**阻塞等待接收数据. 在没有多线程的情况下, BIO无法处理并发通信.
> - 开线程解决阻塞的缺点: 阻塞等待浪费CPU资源, 而且有可能很多线程是无用的, 线程的数量是有上限的.
>
> ```java
> public class Server {
>     static byte[] buffer = new byte[1024];
>     public static void main(String[] args) {
>         try {
>             // 创建一个监听服务
>             ServerSocket serverSocket = new ServerSocket();
>             // 监听服务绑定8080端口
>             // InetSocketAddress表示本地地址
>             serverSocket.bind(new InetSocketAddress(8080));
>             // 阻塞等待一个socket连接
>             Socket socket = serverSocket.accept();
>             // 阻塞, 从socket中读取信息
>             int count = socket.getInputStream().read(buffer);
>             System.out.println(new String(buffer));
>         } catch (IOException e) {
>             e.printStackTrace();
>         }
>     }
> }
> 
> public class Client {
>     public static void main(String[] args) {
>         try {
>             Socket socket = new Socket("127.0.0.1", 8080);
>             socket.getOutputStream().write("1111".getBytes());
>         } catch (IOException e) {
>             e.printStackTrace();
>         }
>     }
> }
> ```

### NIO(Nettey 是NIO的封裝)

### AIO

# Java进阶

## Java的引用類型

### 強引用

> ```java
> Object o = new Object();
> ```

### 软引用

> 软引用是一个对象, 里面的内容指向另一个对象. GC默认不会回收被软引用指向的对象, 如果新对象进来, 堆内存空间不足, 就会把软引用的对象回收掉. 
>
> ```java
> public static void main(String []args) {
>             SoftReference<byte[]> m = new SoftReference<>(new byte[1024 * 1024 * 5]);
>             System.out.println(m.get());
>             System.gc();
>             try {
>                 TimeUnit.SECONDS.sleep(1);
>             } catch (InterruptedException e) {
>                 e.printStackTrace();
>             }
>             System.out.println(m.get());
>             byte[] b = new byte[1024 * 1024 * 6];
>             System.out.println(m.get());
>         }
> // 运行结果
> [B@470e2030
> [B@470e2030
> null
> ```
>
> 

### 弱引用

> 弱引用遇到GC就会被回收
>
> ```java
> public static void main(String []args) {
>   WeakReference<Father> w = new WeakReference<>(new Father());
>   System.out.println(w.get());
>   System.gc();
>   System.out.println(w.get());
> }
> // 运行结果
> com.kim.community.Father@3fb4f649
> null
> ```

### 虛引用

> 虚引用是最弱的引用, 调用phantomReference.get()方法, 也无法取得虚引用的对象. 创建虚引用对象时, 需要加入一个队列变量, 当虚引用对象被回收时, 通过队列可以检测到是否有虚引用被回收. 虚引用主要用作堆外内存清理, 在NIO中, JVM管理**DirectByteBuffer**, 这个**DirectByteBuffer**是直接指向堆外内存的虚引用, 当**DirectByteBuffer**对象被清理时, 把清理消息放进队列, 同时GC线程会根据消息队列, 清理堆外内存

## 	高并发

### Synchronized

#### JUC的lock与synchronized有什么区别

> **synchronized给出的答案是在软件层面依赖JVM，而j.u.c.Lock给出的答案是在硬件层面依赖特殊的CPU指令。**

#### Synchronized锁的是什么?

> 本质是修改对象头的**Markword**参数
>
> synchronized修饰静态方法: 锁的是方法所属的Class对象 
>
> synchronized修饰非静态方法: 锁的是对象
>
> synchronized修饰类对象的执行代码块: 依赖的是**monitorenter**和**monitorexit**实现同步控制

#### synchronized可重入性

> - 什么是可重入性:
>
>   可重入性: 在synchronized修饰的代码块中调用其他被synchronized修饰的方法, 程序依然可以继续执行. 这是为了避免程序被自己锁死的情况.
>
> - 可重入是如何实现的
>
>   每个锁(**Mark Word**)关联一个线程持有者和一个计数器, 如果计数器为0, 表示该锁没有被任何线程持有, 那么任何线程都可能或得该锁进而调用相应的方法. 当一个线程请求成功后, JVM会记下持有锁的线程, 并将计数器记为1. 此时其他线程请求该锁, 则必须等待. 而持有该锁的线程如果再次请求这个锁, 就可以再次拿到这个锁, 同时计数器会递增, 每退出一个synchronized方法, 计数器会递减, 当计数器为0时, 释放该锁
>
>   ![image-20210111195539258](/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210111195539258.png)

### ReentrantLock与Synchronized的区别

> - ReentrantLock基于AQS(AbstractQueuedSynchronizer)和LockSupport的实现, 拥有比synchronized更加灵活的功能, 如: 中断锁等候, 条件变量, 公平锁和非公平锁等... 

### Volatile

> Volatile主要实现**内存可见性**和**禁止指令重排序**

### AQS(AbstractQueuedSynchronizer)

> AQS提供了一个一个用于实现依赖于先进先出(FIFO)等待队列的阻塞锁和相关的同步器(信号量，事件等)的框架
>
> - AQS的核心思想
>
>   如果被请求的共享资源空闲, 则将当前的请求资源的线程设置为有效线程, 并将共享资源设定为锁定状态; 如果请求共享资源被占用, 那么需要一套线程阻塞等待以及被环形时锁分配的机制.
>
> - 如何实现
>
>   一个volatie的int类型标志(state)表示共享资源, 锁的算法是CLH, 将获取不到锁的线程封装成节点放到队列中.

### ThreadLocal(弱引用实例)

> ThreadLocal指的是每个线程内部存放线程的本地变量, 与其他线程不共享. ThreadLocal可以看做一个容器, 可以区分不同线程的本地变量. 
>
> ```java
> public class TestThreadLocal {
>     static ThreadLocal<Person> tl = new ThreadLocal<>();
> 
>     public static void main(String[] args) {
>         new Thread(()->{
>             try {
>                 TimeUnit.SECONDS.sleep(2);
>             } catch (InterruptedException e) {
>                 e.printStackTrace();
>             }
>             System.out.println(tl.get());
>         }).start();
>         new Thread(()->{
>             try {
>                 TimeUnit.SECONDS.sleep(1);
>             } catch (InterruptedException e) {
>                 e.printStackTrace();
>             }
>             tl.set(new Person());
>         }).start();
>     }
> }
> // 输出结果
> null
> ```

#### ThreadLocal如何实现线程变量本地化?

> 每创建一个Thread对象, Thread里包含一个ThreadLocalMap, 这个ThreadLocalMap就是由ThreadLocal管理, 每当执行**ThreadLocal.set**(), 就会获取当前线程对象, 然后获取**ThreadLocalMap**对象, 把变量存入到ThreadLocalMap中.
>
> 从源码中可以看到ThreadLocalMap是一个Entry的**链表集合**, Entry是一个弱引用对象的子类, 同时也是一个k-v对, k为ThreadLocal对象, v为要存入的对象. ThreadLocalMap 的寻址方式与HashMap类似. 
>
> - **为什么Entry是一个弱引用对象**
>
>   目的是避免内存泄漏. 在通常使用中一般会有一个**强引用 tl**指向ThreadLocal对象, 而Entry中如果也是强引用, 则当需要释放ThreadLocal时, 即使令tl=null, ThreadLocal仍然被key强引用, 使ThreadLocal无法被回收, 导致内存泄漏. 如果Entry的key是对ThreadLocal对象的弱引用, 那么当tl=null时, ThreadLocal就可以被回收, 避免内存泄漏. 同时在切断ThreadLocal的强引用之前, 要先执行tl.remove(), 因为如果ThreadLocal先被回收, key变成null, 对应的value就永远无法被找到, 这会导致value值的内存泄漏.
>
> ```java
> // ThreadLocal.java
> public void set(T value) {
>         Thread t = Thread.currentThread();
>         ThreadLocalMap map = getMap(t);
>         if (map != null) {
>             map.set(this, value);
>         } else {
>             createMap(t, value);
>         }
>     }
> // go to set()
> private void set(ThreadLocal<?> key, Object value) {
> 
>   // We don't use a fast path as with get() because it is at
>   // least as common to use set() to create new entries as
>   // it is to replace existing ones, in which case, a fast
>   // path would fail more often than not.
> 
>   Entry[] tab = table;
>   int len = tab.length;
>   int i = key.threadLocalHashCode & (len-1);
> //  这里的Entry是一个弱引用对象的子类
>   for (Entry e = tab[i];
>        e != null;
>        e = tab[i = nextIndex(i, len)]) {
>     ThreadLocal<?> k = e.get();
> 
>     if (k == key) {
>       e.value = value;
>       return;
>     }
> 
>     if (k == null) {
>       replaceStaleEntry(key, value, i);
>       return;
>     }
>   }
> 
>   tab[i] = new Entry(key, value);
>   int sz = ++size;
>   if (!cleanSomeSlots(i, sz) && sz >= threshold)
>     rehash();
> }
> // go to Entry
> static class Entry extends WeakReference<ThreadLocal<?>> {
>   /** The value associated with this ThreadLocal. */
>   Object value;
> 
>   Entry(ThreadLocal<?> k, Object v) {
>     super(k);
>     value = v;
>   }
> }
> // Thread.java
> /* ThreadLocal values pertaining to this thread. This map is maintained
>      * by the ThreadLocal class. */
>     ThreadLocal.ThreadLocalMap threadLocals = null;
> ```

#### ThreadLocal的应用场景

> Spring中的事务管理, 每个线程从connection连接池中获取一个connection放入自己的ThreadLocalMap中, 每次事务管理都会从ThreadLocalMap中获取connection, 线程结束时, 先把connection放回连接池, 然后再执行**tl.remove()**这是为了避免内存泄漏. 而connection交给ThreadLocalMap来管理是为了对程序的**解耦**

### 锁升级机制

> - 无锁
> - 偏向锁
> - 自旋锁
> - 重量级锁
>
> ![image-20210111195802762](/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210111195802762.png)

### 线程池

> - 主要参数
>
>   corePoolSize: 核心线程数, 如果等于0, 则任务执行完后, 没有新任务请求进入时, 销毁线程池中的线程. 如果大于0, 即使本地任务执行完毕, 核心线程也不会被销毁
>
>   maximumPoolSize: 最大线程数. 必须大于等于1, 且大于等于corePoolSize, 如果与corePoolSize相等, 则线程池大小固定. 如果大于corePoolSize, 则最多创建maximumPoolSize个线程执行任务. 
>
>   keepAliveTime: 线程空闲时间. 线程池中线程空闲时间达到了keepAliveTime时, 线程会被销毁, 只到剩下corePoolSize个线程为止. 默认情况下, 线程池的最大线程数大于corePoolSize时, keepAliveTime才会起作用
>
>   unit: TimeUnit表示时间单位
>
>   workQueue: 缓存队列, 当请求线程数大于maximumPoolSize时, 线程进入BlockingQueue阻塞队列.
>
>   threadFactory: 线程工厂. 用来生产一组相同任务的线程. 主要用于设置生成的线程名词前缀, 是否为守护线程以及优先级
>
>   handler: 执行拒绝策略对象. 当达到任务缓存上限时(超过workQueue参数能存储的任务数), 执行拒接策略, 可以看做简单的限流保护. 
>
> - 友好的拒绝策略
>
>   保存在数据库中国, 进行削峰填谷, 在空闲时再拿出来执行
>
>   转向某个提示页面
>
>   打印日志
>
> - sleep()和wait()的区别
>
> - run()和start()区别
>
> - notify()和notifyAll()区别
>
> - 线程的状态
>
>   RUNNING: 
>
>   SHUTDOWN: 
>
>   STOP: 
>
>   TIDYING: 
>
>   TERMINATED: 

## 	GC

## JVM

### JVM內存分布

> - JDK1.6以前
>
>   堆区, 方法区(运行时常量池), 虚拟机栈区, 本地方法栈区, 程序计数器, 直接内存
>
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210125104619372.png" alt="image-20210125104619372" style="zoom:50%;" />
>
> - JDK 1.8 以后
>
>   堆区, 虚拟机栈区, 本地方法栈区, 程序计数器, 直接内存(**Metaspace(原方法区)**)
>
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210125104645276.png" alt="image-20210125104645276" style="zoom:50%;" />
>
> - 各个分区的作用
>
>   线程私有
>
>   - 程序计数器
>
>     可以看做当前线程执行的字节码行号指示器. 字节码解释器工作时, 通过改变这个计数器来选取下一条需要执行的字节码指令. 为了线程切换后也能恢复到正确的执行位置, 每个线程都需要一个线程私有的计数器.
>
>   - 虚拟机栈(VM Stack)
>
>     虚拟机栈由栈帧组成, 每个栈帧都有: **局部变量表, 操作数栈, 动态链接, 方法出口信息**
>
>     局部变量表主要存放了编译期可知的八大数据类型, 对象引用(reference类型)
>
>   - 本地方法栈(Native Method Stack)
>
>     与虚拟机栈相似, 执行的是Nactive Method, 此方法由C/C++编写. 在Hotspot虚拟机中, 本地方法栈和虚拟机栈合二为一.
>
>   线程共享
>
>   - 堆(Heap)
>
>     是JVM管理的内存中最大的一块, 这一块内存是**线程共享**的, 在虚拟机启动时创建. 此区域主要存储对象实例, 几乎所有的对象实例以及数组都在这里分配内存. 根据GC的算法, 还可以细分成Eden, Survivor_0, Survivor_1. 大部分对象都会现在Eden区分配, 在一次新生代垃圾回收后, 会进入s0或s1, 当对象积累到某个年龄, 则会晋升到老年代, 放入老年区
>
>   - 方法区(Method Area)
>
>     用于存放已被虚拟机加载的类信息, 常量, 静态变量, 即时编译器编译后的代码
>
>     - 运行时常量池
>
>       Class文件中处理有类的版本, 字段, 方法, 接口等描述信息, 还有常量池表
>
>   - 直接内存(Direct Memory)

### 类的加载(ClassLoader)

> 当我们新建一个对象new student(), JVM会从磁盘中寻找并加载student.class对象JVM内存中. JVM自动创建一个关于student.class的class, 这个class只能由JVM产生, 其构造方法是private的. 这个class包含的是student.class的一些信息, 并且一个类只会生成一个class对象
>
> - 双亲委派机制
>
>   当某个类加载器需要加载某个`.class`文件时, 它首先把这个任务委托给他的上级类加载器, 递归这个操作, 如果上级的类加载器没有加载, 自己才会去加载这个类. 防止重复加载同一个.class, 保证核心的.class不被篡改.
>
>   - 各种类加载器
>
>     - BootrapClassLoader(启动类加载器)
>
>       c++编写, 加载java 核心库, 构造ExtClassLoader 和 AppClassLoader
>
>     - ExtClassLoader(标准扩展类加载器)
>
>       java编写, 加载扩展库, 如classpath中的jre, javax.*
>
>     - AppClassLoader(系统类加载器)
>
>       java编写, 加载project根目录的class
>
>     - CustomClassLoader(自定义类加载器)
>
>       java编写, 可以加载指定路径的class文件
>
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210125194740750.png" alt="image-20210125194740750" style="zoom:50%;" />
>
> - 对象创建过程
>
>   1. 类加载检查
>
>      虚拟机遇到一条new 指令时, 首先去检查这个指令的参数是否能在常量池中定位到这个类的符号引用. 并且检查这个符号引用代表的类是否被加载过(双亲委派机制), 解析和初始化过. 
>
>   2. 分配内存
>
>      通过类加载检查后, 开始为新生对象分配内存, 对象所占内存大小在类加载完成后就可以确定. 分配内存的方式分为**指针碰撞**和**空闲列表**(根据GC的方式不同使用不同的方法分配内存), 
>
>      - 分配内存的线程安全问题
>
>   3. 初始化零值
>
>      内存分配完成后, JVM需要将分配到的内存空间都初始化为零值.
>
>   4. 设置对象头
>
>      初始化零值完成后, JVM要对对象设置对象头, 如: 对象属于哪个类的实例, 如何才能找到类的元数据信息, 对象的哈希码, 对象的GC分代年龄, 这些信息存放在对象头中. 还有对象锁的信息也会存在对象头的**Mark Word**中.
>
>   5. 执行init()方法
>
>      以上方法完成后一个新生对象就创建完成, 但是仍不是程序所需要的目标对象, 还要对对象中的属性进行赋值, 对象才创建完成.

### 对象内存结构

> Java对象由三部分组成: **对象头, 实例数据, 对齐填充**
>
> - 对象头
>
>   对象头包含两个部分信息: **Mark Word**和**类型指针(klass)**
>
>   - Mark word
>
>     用于存储对象自身的运行时数据, 如: HashCode, GC分代年龄, 锁状态标志, 线程持有的锁, 偏向线程的ID, 偏向时间戳等. ==在32位虚拟机中占4B, 在64位虚拟机中占8B==
>
>   - 类型指针(klass)
>
>     对象指向它的类元数据(存放在方法区/ MetaSpace)的指针, JVM通过这个指针来确定这个对象对应的类信息. 如果对象是一个数组, 对象头中还需要存储这个数组的长度以确定数组的大小. ==在32位虚拟机中占4B, 在64位虚拟机中占8B, 开启指针压缩后, 占4B==
>
> - 实例数据
>
>   实例数据包含两种: 八大基本数据类型(byte(1B), boolean(1B), char(2B), short(2B), int(4B), float(4B), long(8B), double(8B) )和引用类型reference(4B)
>
> - 对齐填充(padding)
>
>   JVM内存管理要求Java对象内存起始地址必须为8的倍数, 因此Java对象大小必须为8的倍数, 如果对象头+实例大小不为8的倍数, 需要用padding进行填充

# 数据库

## 数据库引擎

> - 数据库引擎是表级别的, 即数据库引擎是用于限制数据表的
>
> - MyISAM引擎(表锁)
>
>   - 表的数据结构分为**.frm**(存储数据表字段结构), **.MYD**(存储数据)**.MYI**(存储数据索引), 数据和索引分开存储的称为**稀疏索引**
>   - 查找过程: 先判断搜索条件是否为索引字段, 通过B+树(**.MYI**)找到对应数据行的磁盘地址, 通过磁盘地址就可以在**.MYD**中直接获得数据
>
> - InnoDB引擎(行锁)
>
>   - 表的数据结构分位**.frm**(存储字段信息) 和 **.ibd**(存储索引和数据), 即B+树同时存储索引和数据, 则叶子节点包含了完整的数据记录(称为 **聚集索引**)
>
>   - Innodb推荐使用**整型自增主键**作为索引, 非主键索引的叶子阶段存储的是主键值
>
>     **主键索引(Primary Index)**
>
>     刚开始存储数据时, 如果不设置主键, MySQL会查找一个没有重复数据的字段作为索引, 如果所有字段都存在重复数据, MySQL就在表后维护一个自增序列的隐藏列在组织索引
>
>   - 为什么推荐使用整型递增主键
>
>     因为在搜索的过程中需要多次进行比较, 整型的比较要比字符串的比较速度快, 因此推荐整型索引
>
>     而且整型最大只占8个字节, 字符串占的内存比较大, 用整型索引比较节省存储空间
>
>     为什么推荐递增主键, 因为如果主键不是递增的, 插入数据时, 有可能会触发B+树的分裂再平衡, 降低插入效率
>
>   - 二级索引(Secondary Index, 普通索引)
>
>     **二级索引**其实际也是非聚集索引, 二级索引树中, 叶子节点只保存了主键信息, 因此在二级索引树中查找到相关索引后, 再根据相关索引从主键索引中查询

## 索引

> - 索引是帮助MySQL搞笑获取数据的**排好序**的**数据结构**
>
> - 为什么索引用B树或者B+树
>
>   当索引的字段是顺序的时候, BST的形态会变成链表状, 并不能起到优化作用
>
>   当索引是红黑树时, 当数据量很大时, 红黑树的高度过高, 搜索效率也不高
>
>   当索引是Hash表时, 不利于范围查找的效率
>
>   当索引是B树时, B树每个节点都存储数据, 而且叶子节点不是双向链表结构, 可存储的数据少, 而且区间查找比较慢
>
> - MySql中B+树的结构
>
>   > - 非叶子节点不存储数据, 只存储索引(冗余), 可以存储更多索引
>   > - 叶子节点包含所有索引字段
>   > - 叶子节点用指针连接, **是一个双向链表结构**, 提高区间访问的性能
>   > - innodb中设置每页大小为16k, 即B+树中每个节点可以存储16kb索引(约1170个, 索引6byte+下级节点8byte), 那一个h为3的B+树可以容纳2000万个索引
>
> - 联合索引
>
>   按照索引的组合顺序进行排序
>
> - 索引优化方法
>
>   - 最左前缀原则
>
>     原理: 在联合索引的情况下, B+树的叶子节点是按照索引的组合顺序进行从左到右进行排序的, 如果查询的语句不符合最左前缀法则, 则索引也用不上了. 索引字段中间断开了也会使得索引失效, e.g: (name, age, position)联合索引, 只使用了(name, position) 作为查找条件, 则索引失效
>
>     PS: 最左原则并不是查询条件的顺序, 只要包含的字段符合最左前缀原则即可(MySQL会自己优化顺序)
>
>     e.g. 下图中如果使用仅使用**age/ position**字段进行查询, 可以看到在叶子节点的序列是乱序的, 则索引失效
>
>     <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210115182250959.png" alt="image-20210115182250959" style="zoom:50%;" />
>
>     - union, in, or都能够命中索引, 如果or的前半步包含索引, 后半部不包含索引, 则索引失效
>
>     - 负向查找条件不能使用索引(!=, <>, not in, not exists, not like)
>
>     - 使用in查询时, 数据间的区分度要搞, 回表比例要大约在30%以内(原因存疑)
>
>       **filtered:** 这个字段表示存储引擎返回的数据在server层过滤后, 剩下多少满足查询的记录数量的比例
>
>     - **联合索引第一个字段用范围不会走索引**(原因存疑)
>
>       联合索引第一个字段就用范围查找不会走索引, mysql内部可能觉得第一个字段就用范围, 结果集应该很大, 回表效率不高, 还不如就全表扫描
>
>     - 索引不能做任何计算或类型转换, 这样会使得索引失效
>
>     - 索引字段使用LIKE时一般不能以通配符"%"开头
>
>       如果使用了LIKE "%xx%", 应当使用**覆盖索引**
>
> - **联合索引**
>
>   - 为什么使用联合索引?
>
>     减少开销, 建立一个联合索引(col1, col2, col3), 实际相当于建了(col1), (col1, col2), (col1, col2, col3) 三个索引
>
>     覆盖索引, 对联合索引(col1,col2,col3)，如果有如下的sql: select col1,col2,col3 from test where col1=1 and col2=2。那么MySQL可以直接通过遍历索引取得数据，而无需回表，这减少了很多的随机io操作。
>
>     **效率高**。索引列越多，通过索引筛选出的数据越少。有1000W条数据的表，有如下sql:select from table where col1=1 and col2=2 and col3=3,假设假设每个条件可以筛选出10%的数据，如果只有单值索引，那么通过该索引能筛选出1000W * 10%=100w条数据，然后再回表从100w条数据中找到符合col2=2 and col3= 3的数据，然后再排序，再分页；如果是联合索引，通过索引筛选出1000w * 10% * 10% * 10%=1w

## explain执行计划表

> 1. **id**
>
>    id表示查询中执行select字句或者操作表的顺序, id的值越大代表优先度越高, 越先执行
>
>    - id相同
>
>      执行记录的id相同, 表示具有相同优先级, 执行顺序由上而下
>
>    - id不同
>
>      先执行id较大的语句, 子查询的id最大, 最先执行
>
> 2. **select-type**
>
> 3. **table** 
>
> 4. **partitions**
>
>    如果查詢的是分区表时, partitions显示分区表命中的分区情况
>
> 5. **type**
>
>    - system
>
>      当表仅有一行记录时, 数据量很少, 往往不需要磁盘IO, 这时才会出现system type
>
>    - const
>
>      当mysql对查询某部分进行优化, 并**转换**为一个常量时, 使用const
>
>    - eq_ref
>
>      唯一性索引扫描, 对于每个索引键, 表中只有一条记录与之匹配, 使用eq_ref, 常见于主键或唯一索引扫描
>
>    - ref
>
>      非唯一性索引扫描, 返回匹配某个单独值的所有行.
>
>    - range
>
>      使用索引选择行, 仅检索给定范围的行, 即真多一个有索引的字段, 给定范围检索数据, 在where语句中使用非等值条件查询, type都是range
>
>    - index
>
>      遍历索引树读取全表
>
>    - all
>
>      从磁盘中读取遍历全表
>
> 6. **possible_keys**
>
> 7. **key**
>
> 8. **key_len**
>
> 9. **ref**
>
> 10. **rows**
>
> 11. **filtered**
>
> 12. **Extra**

## 事务

> - ACID
>
>   原子性, 一致性, 隔离性, 持久性
>
> - 异常类型
>
>   1. 第一类数据丢失
>
>      某一事务的回滚, 导致另外一个事务已更新的数据丢失
>
>      | 时间 | 取款事务A                             | 转账事务B                 |
>      | ---- | ------------------------------------- | ------------------------- |
>      | T1   | **开始事务**                          |                           |
>      | T2   |                                       | **开始事务**              |
>      | T3   | 查询账户余额为1000元                  |                           |
>      | T4   |                                       | 查询账户余额为1000元      |
>      | T5   |                                       | 汇入100元把余额改为1100元 |
>      | T6   |                                       | **提交事务**              |
>      | T7   | 取出100元把余额改为900元              |                           |
>      | T8   | **撤销事务**                          |                           |
>      | T9   | **余额恢复为1000** **元（丢失更新）** |                           |
>
>   2. 第二类数据丢失
>
>      某一事务的提交, 导致另外一个事务已更新的数据丢失
>
>      | 时间 | 转账事务A                             | 取款事务B                |
>      | ---- | ------------------------------------- | ------------------------ |
>      | T1   |                                       | **开始事务**             |
>      | T2   | **开始事务**                          |                          |
>      | T3   |                                       | 查询账户余额为1000元     |
>      | T4   | 查询账户余额为1000元                  |                          |
>      | T5   |                                       | 取出100元把余额改为900元 |
>      | T6   |                                       | **提交事务**             |
>      | T7   | 汇入100元                             |                          |
>      | T8   | **提交事务**                          |                          |
>      | T9   | **把余额改为1100** **元（丢失更新）** |                          |
>
>   3. 脏读
>
>      读取了其他事务未提交的数据
>
>      | 时间 | 转账事务A                      | 取款事务B                      |
>      | ---- | ------------------------------ | ------------------------------ |
>      | T1   |                                | **开始事务**                   |
>      | T2   | **开始事务**                   |                                |
>      | T3   | 查询账户余额为1000元           |                                |
>      | T4   | 汇入100元                      |                                |
>      | T5   |                                | 查询账户余额为1100元(**脏读**) |
>      | T6   |                                | 取出100元把余额改为1000元      |
>      | T7   |                                | **提交事务**                   |
>      | T8   | **提交事务**                   |                                |
>      | T9   | **把余额改为1000** (白拿100块) |                                |
>
>   4. 不可重复读
>
>      两次读取的数据不一致
>
>      | 时间 | 转账事务A                         | 取款事务B                 |
>      | ---- | --------------------------------- | ------------------------- |
>      | T1   | **开始事务**                      | **开始事务**              |
>      | T2   | 查询账户余额为1000元              |                           |
>      | T3   |                                   | 查询账户余额为1000元      |
>      | T4   | 汇入100元                         |                           |
>      | T5   | **提交事务**                      |                           |
>      | T6   |                                   | 查询账户余额为1100元      |
>      | T7   |                                   | 取出100元把余额改为1000元 |
>      | T8   |                                   | **提交事务**              |
>      | T9   | **把余额改为1100** (取款记录丢失) |                           |
>
>   5. 幻读
>
>      事务两次读取行数不一致
>
> - 隔离级别
>
>   - read uncommitted(读未提交, 级别最低, 可以解决**第一第二类数据丢失问题**)
>
>     https://segmentfault.com/a/1190000012654564
>
>     如果一个事务已经开始写数据, 则另外一个事务不允许同时进行写操作, 但允许其他事务对此行进行读操作(**不对目标加S锁, 因此不会阻塞读操作**). 该隔离级别可以使用**"X锁"**实现
>
>     ```
>     Transactions running at the READ UNCOMMITTED level do not issue shared locks to prevent other transactions from modifying data read by the current transaction. 
>     READ UNCOMMITTED transactions are also not blocked by exclusive locks that would prevent the current transaction from reading rows that have been modified but not committed by other transactions. 
>     When this option is set, it is possible to read uncommitted modifications, which are called dirty reads. Values in the data can be changed and rows can appear or disappear in the data set before the end of the transaction. 
>     This option has the same effect as setting NOLOCK on all tables in all SELECT statements in a transaction. 
>     This is the least restrictive of the isolation levels.
>     ```
>
>   - read committed(读已提交,  可以解决**脏读问题**)
>
>     数据读取的操作不加锁 因此可以随意读取, **但读取不到其他事务未提交的数据(MVCC机制)**, 数据的写入, 修改和删除是需要加**X锁**的, 会阻塞其他事务的写操作, 所以有**可能出现不可重复读问题**
>
>     read commited隔离级别下, **每个快照读都会生成并获取最新的Read view**, 所以可以消除脏读, 但不能消除不可重复读问题
>
>     https://segmentfault.com/a/1190000012655091
>
>   - repeatable read(可重复读, 可以解决**不可重复读问题**)
>
>     在RR级别中, **在同一个事务中的第一个快照读才会创建read view**, 后面的快照读都是读取同一个read view, 所以同一个事务多次查询的结果都是一样的, 这样就解决了不可重复读问题
>
>     https://segmentfault.com/a/1190000012650596
>
>   - Serializable(序列化)
>
> - MVCC机制
>
>   - 实现原理: MVCC主要靠**版本链, Undo log, read view**
>
>   - 版本链:
>
>     先关注3个隐藏字段**db_trx_id, db_roll_pointer, db_row_id**
>
>     - db_trx_id: 最近修改(修改/插入)事务ID, 记录**创建这条记录/最后修改该记录**的事务ID
>     - db_roll_pointer: 回滚指针, 指向这条记录的上一个版本(存储于rollback segment里)
>     - db_row_id: 隐含的自增id, 如果数据表没有主键, Innodb会自动以这个字段产生一个簇族索引
>
>     每次对数据库记录进行改动, 都会记录一条Undo日志, 每条Undo日志都有一个**roll_pointer**属性(INSERT操作对应Undo日志没有该属性, 因为它没有更早的版本), 可以将Undo日志连起来, 构成版本链. 链头结点是当前记录的最新值, **每个版本中包含生成该版本时对应的事务ID**
>
>   - Read view
>
>     事务进行`快照读`操作的时候生产的`读视图`(Read View)，在该事务执行的快照读的那一刻，会生成数据库系统当前的一个`快照`. 记录并维护系统当前`活跃事务的ID`(没有commit，当每个事务开启时，都会被分配一个ID, 这个ID是递增的，所以越新的事务，ID值越大)，是系统中当前不应该被`本事务`看到的`其他事务id列表`. **read view**主要用作数据的可见性判断
>
>     ```c++
>     // db_trx_id 该条数据的事务id
>     // low_limit_id: 创建当前read view 时“当前系统最大事务版本号+1”
>     // up_limit_id: 创建当前read view 时“系统正处于活跃事务最小版本号”
>     // creator_trx_id: 创建当前read view的事务版本号
>     // trx_ids: 当前系统活跃(未提交)事务版本号集合
>     
>     // 如果数据事务ID小于read view中的最小活跃事务ID, 说明该数据是在当前事务之前就已经存在了, 所以可以显示
>     // 数据事务ID等于当前事务ID, 该数据就是当前事务产生的, 也可以显示
>     db_trx_id < up_limit_id || db_trx_id == creator_trx_id -> return true;
>     
>     // 如果数据事务ID大于read view中的当前系统的最大事务ID, 说明该数据是在当前read view创建之后产生的, 因此不显示
>     db_trx_id >= low_limit_id -> return false;
>     
>     // 如果db_trx_id 存在于 trx_ids: 说明当前read view生成时, 这个事务还在活跃, 没有commit, 这个版本的数据不可见
>     // 如果db_trx_id 不存在与 trx_ids: 说明修改该数据的事务已经commit, 可以显示
>     (db_trx_id < low_limit_id) and (db_trx_id in trx_ids?)
>     ```
>
> - Undo log(实现原子性)
>
>   原子性提现在**回滚**机制上, 而**rollback**依赖Undo log. 当事务对数据库进行修改操作时, innodb会生成对应的Undo log, 记录SQL相关信息, 如果db发生回滚, 则会根据Undo log做相反的工作.
>
>   - INSERT Undo log
>
>     代表事务在INSERT新记录时产生的Undo log, 只在事务回滚时需要, 并且在事务提交后可以被立即丢弃
>
>   - UPDATE Undo log
>
> - Redo log(实现持久性)
>
>   Innodb为数据库持久性提供了一个Buffer, 这个Buffer包含了数据库磁盘中数据页的映射, 当数据库读取数据时, 会先从Buffer中查找, 如果Buffer中没有, 就经过磁盘IO进行读取, 读取到数据后放到Buffer中. 当要写入数据时, 也先会往buffer中写入数据, 然后定期把Buffer中的数据持久化到磁盘中. redo log就是为了增加Buffer的数据安全性, 如果从Buffer持久化到磁盘的过程中发生宕机, 则可以先Undo, 然后再redo, 重新把数据写入Buffer, 等待下次刷新. 所以增强后的增删数据流程是: 先把操作添加到redo log(预写式日志: 先把操作记录在日志中, 然后再执行操作), 然后把数据写入Buffer, 最后持久化到磁盘. 
>
>   - 为什么写入日志要比Buffer数据持久化性能高?
>
>     写入日志是**追加模式**, **顺序IO**,  
>
>     数据持久化是**随机读写IO**, 每次修改数据的位置不定. 数据库中修改数据以**page(16k)**为单位, 修改一次数据都要把整页数据写入, 因此消耗了很多IO
>
>   - 更新策略: 
>
>     0. 当提交事务时, 不将缓冲区的redo日志写入磁盘的日志文件, 而等待主线程每秒刷新
>     1. 在事务提交时, 将缓冲区的redo日志同步写入磁盘, 保证一定会写入成功
>     2. 在事务提交时, 将缓冲区的redo日志异步写入磁盘, 其不能保证commit时肯定会写入redo日志文件

## 锁

> - 锁只有在执行**commit**或者**rollback**的时候才会释放, 并且所有的锁都是在**同一时刻**被释放
>
> - 锁的种类
>
>   - share lock(共享锁, S锁)
>
>     S锁表示对数据进行读操作, 多个事务可以同时为一个对象家S锁
>
>   - exclusive lock(排他锁, X锁, 写锁)
>
>     X锁表示对数据进行写操作, 如果一个事务对对象加了X锁, 其他事务就不能再给他加任何锁
>
>   - Intention Share Lock(IS, 意向共享锁)
>
>   - Intention Exclusive Lock(IX, 意向排他锁)
>
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210119202756678.png" alt="image-20210119202756678" style="zoom:50%;" />
>
>   如果一个事务请求的锁模式与当前的锁兼容， InnoDB 就将请求的锁授予该事务； 反之， 如果两者不兼容，该事务就要等待锁释放
>
> - 锁种类的细分
>
>   - Gap Lock(间隙锁)
>   - Record Lock(记录锁)
>   - Next-Key Lock
>   - Insert Intention Lock(插入意图锁)
>
> - 锁的粒度
>
>   - 表级锁
>
>     MyISAM使用的表锁, InnoDB支持表级锁
>
>   - 行级锁
>
>     InnoDB默认使用行级锁
>
>   - 页级锁

# Spring

## Spring

### IoC(控制反转)

> 把对象的创建, 初始化, 销毁交给Spring处理, 而不是由开发者控制, 实现控制反转
>
> 所以, 所有的Bean都是对象, 但不是所有的对象都是Bean, 只有被Spring容器管理的对象才叫Bean

### 依赖注入(Dependency Injection)

> Spring给容器实例化出来的Bean注入属性

### Bean的生命周期

> 先找到目标class -> BeanDefinition -> BeanFactoryPostProcessor(可以在这里通过BeanFactory获取对应beanName的BeanDefinition然后修改BeanDefinition的属性) -> 基于BeanDefinition中的beanClass实例化对象 -> 填充属性 -> Aware(执行回调函数, 把实现Aware接口的对象的方法都执行一遍) -> 初始化(InitializingBean) -> aop(如果没有开启, 就没有) -> 放入单例池(如果没有执行AOP, 那单例池中就是原始对象Map<BeanName, 原始对象>, 如果执行了AOP, 放入单例池的就是代理对象Map<BeanName, 代理对象>)

### BeanFactory

> - ObjectFactory的实现类
>
> - BeanFactory的组建
>
>   扫描被@Component 描述的类, 为其创建BeanDefinition对象, 存入BeanDefinitionMap, **并没有实例化Bean对象**
>
> - 创建Bean对象
>
>   当执行getBean()方法时, 才会实例化对应的Bean对象
>
> - @Autowired
>
>   Spring中有**AutowiredAnnotationBeanPostProcessor**, 专门处理自动注入的注解

### FactoryBean

> - 是一个Bean, 可以根据FactoryBean的名字获取这个Bean对象, 但是返回的Bean对象并不是实现FactoryBean的自定义对象所对应的类, 而是FactoryBean的getObject()实现方法所返回的类.

### Spring整合MyBatis

> MyBatis中实现的都是interface, 当需要执行SQL语句时, 真正执行语句的是一个由MyBatis创建的**代理对象**, 因此在Spring整合MyBatis的过程中, 直接把代理对象注入到Bean中即可, 并不需要重写Mybatis.

### Spring依赖循环

> - 为什么Spring需用用三级缓存处理循环依赖
>
>   避免AOP重复代理
>
> - Spring解决循环依赖的过程
>
>   假设Service A 和Service B互相依赖
>
>   1. 在创建ServiceA时, 首先创建的是一个半成品原始对象, 在进行属性注入时, 发现需要注入ServiceB对象, 然后开始创建ServiceB对象
>
>      > 这里发生了什么? 在创建ServiceA时, **addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory)**会把**addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));**添加到三级缓存
>
>   2. 开始创建ServiceB时, 发现需要ServiceA, 于是生成了ServiceA的代理对象, ServiceB对象继续走Bean的生命周期, 完成Bean的创建并放入单例池
>
>      > 这是发生了什么? ServiceB会在三级缓存中找到ServiceA的**singletonFactory**对象, 然后执行getObject()方法, 得到了ServiceA的原始对象==(如果有AOP的话, 得到ServiceA的代理对象)==并存入二级缓存, 在产生ServiceA的代理对象的同时, 会把ServiceA的**singletonFactory**对象从三级缓存中删除.
>
>   3. 最后ServiceA再执行一次getSingleton()方法, 从二级缓存中获取代理对象, 把ServiceA的代理对象存入单例池

## Spring MVC

### Spring MVC工作流程

<img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210106173422068.png" alt="image-20210106173422068" style="zoom:50%;" />

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
>    <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108201111853.png" alt="image-20210108201111853" style="zoom:50%;" />
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
> <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210106175944616.png" alt="image-20210106175944616" style="zoom:50%;" />
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
> <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210107174217951.png" alt="image-20210107174217951" style="zoom:50%;" />
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

## 	Redis

> 1. Session共享 -> 服务无状态 -> 方便扩容
> 2. 规避无效请求
>
> - 布隆过滤器
>
>   插入数据: 一个数据经过多个hash算法得到hash值, hash值经过与计算得到二进制数列的下标, 并把该位置1
>
>   查询数据: 当一个数据进来经过多个hash算法计算, 二进制数列中其对应位都为1, 才为合法数据, 否则为非法数据
>
>   - 为什么要多个hash算法计算hash值
>
>     因为不同数据有可能发生hash碰撞, 因此多个hash算法可以降低布隆过滤器的误判几率
>
> - Redis持久化方式
>
> 	- RDB
>
> 		RDB持久化机制, 在一段时间内, 达到了某个修改次数后, Redis就会把内存`数据快照(Snapshot)`持久化到硬盘中
>
> 		持久化原理: Redis单独fork一个子进程出来对数据进行持久化, 进程先会把数据写进一个临时文件中, 当数据持久化过程结束后, 对临时文件改名, 替换上次持久化的文件. 在持久化的过程中, 不涉及主进程的IO操作, 因此非常高效
>
> 		**优势**: 持久化过程高效, 适合大规模数据备份及恢复; 
>
> 		**劣势**: 每隔一段时间进行备份, 当服务意外宕机时, 会丢失最后一次快照的所有修改, 因此适合对一致性和完整性要求不高的项目; 因为fork 出一个子进程, 内存中的数据被克隆了一份, 因此所占内存为约为原来的两倍; 当数据量非常大的时候, fork的过程也相当耗时, 会导致Redis一些毫秒级服务不能响应
>
> 	- AOF
>
> 		AOF持久化机制, 是日志型持久化机制, 把Redis所有写命令(增, 删, 改)都存储到日志文件中, 读命令不存储. 当需要恢复数据时, 把日志文件中的命令执行一次, 达到持久化目的.
>
> 		日志文件只能在文件末尾追加内容, 不能改写文件.
>
> 		Redis启动时, 就会把日志文件读取并执行命令, 把数据恢复到内存中
>
> 		Rewrite机制
>
> 		由于AOF使用文件追加的方式创建日志, 日志文件会越来越大, 因此新增了重写(rewrite机制): 当日志文件因为持续增长超过了阈值时, Redis就会触发重写机制, 对AOF文件进行压缩, 只保留恢复数据的最小指令集, 主进程需要fork出一个子进程来重写日志文件(也是先写进临时文件再进行替换), 子进程遍历内存中的数据, 每条记录用set操作描述, Redis会记录上次进行重写的AOF大小, 默认配置是当AOF文件为上次备份的AOF文件大小的两倍时或文件大于64M时触发重写机制.
>
> 		**优势:** AOF还有两个机制: appendsync everysec, 异步记录, 每秒记录指令, 如果意外宕机, 只会丢失1s内的操作和appendsync always, 同步持久化, 每执行一次操作都会把指令记录到日志中, 虽然不比RDB高效, 但是完整性和一致性较好
>
> 		**劣势:** 相同数据而言, AOF文件远大于RDB文件, 因为是重新执行一次指令, 因此恢复数据速度较慢. 根据同步策略的不同, AOF可能会比RDB效率稍慢(异步策略与RDB效率相同)
>
> - 哨兵机制
>
> - 主从复制
>
> 	- 主要用途
>
> 		读写分离: 适用于读多写少情景, 增加从机, 提高读的速度, 提高并发度
>
> 		数据容灾恢复: 从机复制主机数据, 相当于对主机数据做备份
>
> - Redis单线程?
>
> - 淘汰策略
>
> - 缓存雪崩
>
> - 缓存穿透
>
> - 缓存击穿

## 	Kafka

> - kafka的数据存储在硬盘中
>
> - Kafka中主要组成部分
>
>   - Producer(生产者)
>
>     负责生产消息
>
>   - Consumer(消费者)
>
>     负责消费消息
>
>   - Broker
>
>     可以看做Kafka实例, 多个Broker组成一个Kafka Cluster, 每个Broker可以包含多个Topic
>
>   - Topic
>
>     Producer会把消息推送到特定的Topic, Consumer通过订阅Topic来消费消息
>
>   - Partition
>
>     一个Topic可以包含多个Partition, 并且同一个Topic下的Partition可以分布在不同的Broker上

## Zookeeper

> - CAP理论(Consistency 一致性, Availability可用性, Partition tolerance分区容错性)
>
> 	zookeeper保证了CP, 当zookeeper挂掉超过一半节点时, 服务器就会挂掉, 因此不能保证高可用性. Zookeeper 的follower节点宕掉重启后, 重新执行日志指令, 追上进度, 因此保证了最终一致性
>
> - 用途: 注册中心, 分布式锁
>
> - ZAB协议
>
> - Paxos算法
>
> - 领导者选举算法
>
>   zxid越大 -> 数据越新 -> 能力越强
>
>   ![image-20210215211717036](/Users/jianfengyuan/Library/Application Support/typora-user-images/image-20210215211717036.png)
>
>   上图中 因为==过半机制==, 所以节点2胜选成为leader. 当有新的节点3加入集群, 节点3会向各个节点发送自己的选票, 但是收到的选票都是选节点2为leader, 也是因为==过半机制==因此节点3自动成为follower
>
> - 2PC(二阶段提交)
>
>   预提交 - ack -提交
>
>   ![image-20210215213254696](/Users/jianfengyuan/Library/Application Support/typora-user-images/image-20210215213254696.png)

# 网络协议

## OSI 七层模型 与 TCP/IP 五层模型

<img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108105822021.png" alt="image-20210108105822021" style="zoom:50%;" />

网络接口层(数据链路层, 物理层) : 负责二进制数据传输, 010101电信号传输

网络层: IP协议, IP寻址

传输层: TCP, UDP协议

应用层(应用层, 表示层, 会话层): 基于传输层规定应用程序的数据格式, 如Email, WWW, FTP

<img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108112439883.png" alt="image-20210108112439883" style="zoom:50%;" />

## TCP(可靠传输协议)

![image-20210113181559042](/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210113181559042.png)

### TCP表头控制位

> 八位从左到右分别是 CWR，ECE，URG，ACK，PSH，RST，SYN，FIN
>
> **ACK: **该位设为 1, 确认应答的字段有效, TCP规定除了最初建立连接时的 SYN 包之外该位必须设为 1;
>
> **SYN: **用于建立连接, 该位设为 1, 表示希望建立连接, 并在其序列号的字段进行序列号初值设定;
>
> **PSH: **接收方应该尽快将这个报文段交给应用层
>
> **FIN: **该位设为 1, 表示今后不再有数据发送, 希望断开连接. 当通信结束希望断开连接时, 通信双方的主机之间就可以相互交换 FIN 位置为 1 的 TCP 段.

### 三次握手

> <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108140155214.png" alt="image-20210108140155214" style="zoom:50%;" />
>
> 假设 A为客户端, B为服务端
>
> 1. A向B发送请求报文 SYN=1, ACK=0, 选择一个初始序号x, **这个序号表示的是按字节数计算**
> 2. B 收到请求报文, 如果同意建议连接, 则向A 发送连接确认报文, SYN=1, ACK=1, 确认号为x+1, 表明收到报文x, 并请求报文x+1. 同时选择初始序号y.
> 3. A收到B的连接确认报文后, 还要向B发送确认, 序号为x+1, 请求y+1号报文
> 4. B收到A的确认后, 连接建立, 开始发送数据

### 四次挥手(一般但不一定由客户端发起)

> <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108140132895.png" alt="image-20210108140132895" style="zoom:50%;" />
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
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108164758774.png" alt="image-20210108164758774" style="zoom:50%;" />
>
> - 粘包: 接收端只收到一个数据包, 但这一个数据包中包含了发送端发送的两个数据包的信息. 由于接收端不知道这两个数据包的界限, 所以对于接收端来说很难处理
>
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108164957499.png" alt="image-20210108164957499" style="zoom:50%;" />
>
> - 拆包: 接收端虽然接收到两个数据包, 但是一个包是数据不完整的, 另一个是数据包多出了一块, 这种情况即发生了粘包和拆包, 对于接收端来说同样是不好处理的
>
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108165139333.png" alt="image-20210108165139333" style="zoom:50%;" />

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

> <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108173358152.png" alt="image-20210108173358152" style="zoom:50%;" />
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
> <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108175317884.png" alt="image-20210108175317884" style="zoom:50%;" />
>
> - 慢开始(慢启动), 拥塞避免
>
>   发送端最初执行慢开始, 令cwnd=1, 发送方只能发送一个报文段, 每当收到确认后, 将cwnd翻倍, 同时设定一个阈值, 当cwnd超过这个阈值时, 进入**拥塞避免**, 每轮cwnd只增加 1, 如果出现了**超时**, 则将**阈值减半**, 重新开始慢启动
>
> - 快重传, 快恢复
>
>   <img src="/Users/jianfengyuan/Documents/java_study/java-study/review.assets/image-20210108192438848.png" alt="image-20210108192438848" style="zoom:50%;" />
>
>   在接收方, 要求每次接收到报文都应该对最后一个已经收到的有序报文段进行确认(回复确认最后一个有序字节数, 这里用报文数描述). 例如已经接收到报文M1 和M2,此时收到M4, 应当发送对M2的确认
>
>   在发送方, 如果收到三个重复的确认, 那么可以知道下一个报文段丢失, 此时执行快重传, 立即重传下一个报文段. 例如: 收方陆续收到M5, M6, M7, 但M3 丢包, 发送方会收到3个M2的确认, 则M3丢失, 立即重传M3
>
>   在这种情况下, 只是丢失个别报文段, 而不是拥塞, 因此执行快速恢复, 阈值变为拥塞窗口的一半, 然后拥塞窗口从阈值开始, 直接进入拥塞避免.

## HTTP协议

> - Http历史
>
>   - HTTP/0.9
>
>     非常古老的版本, 只有一个命令 **GET**
>
>   - HTTP/1.1
>
>     最广泛的HTTP协议版本
>
>     - 常见字段
>
>       - Host字段
>
>         客户端发送请求时, 用来指定服务器的域名
>
>       - Content-length字段
>
>         服务器在返回数据时, 会有内容长度字段, 表明本次回应的数据长度
>
>       - Connection字段
>
>         早期Http协议每建立一次TCP连接只能发送一次数据, 在Http/1.0版本中添加了Connection字段, 当Connection=keep-active, 则需要客户端/服务器主动关闭连接, 在HTTP/1.1版本中则默认为持久连接
>
>       - Content-Type
>
>         用于服务器回应时, 告诉客户端, 本次返回的数据格式
>
>     - GET和POST的区别
>
>       - GET请求: 从服务器中请求资源, url长度受限(取决于浏览器的限制),
>
>         POST请求: 向服务器发送数据, 数据放在报文的body中.
>
>     - HTTP的缺点
>
>       HTTP协议传输是无状态的明文传输
>
>       - 无状态传输的解决办法
>
>          Cookie和Session就是为了解决HTTP协议无状态传输的办法, 客户端向服务器发出请求, 服务器在回复时, 会携带Cookie字段, Cookie装的是客户端特定的SessionID信息, Cookie会保存在客户端中一段时间, 在Cookie的有效期内, 客户端发送带有Cookie的请求到服务器, 服务器通过Session就可以知道客户端的 登录状态.
>
>       - 明文传输的解决办法
>
>         HTTPS, 引入SSL/TLS层, 对报文进行混合加密
>
>     - HTTP和HTTPS的区别
>
>       HTTPS引入SSL(现在已经被废弃)/TLS层
>
>       - HTTPS通信过程
>
>         客户端先向服务器索要公钥, 然后用公钥加密信息, 服务器收到密文后, 用自己的私钥解密
>
>         - 如何保证公钥可信?
>
>           CA(数字证书认证机构), 服务器将公钥放在数字证书中(数字证书由CA颁发), 只要证书是可信的, 那公钥就是可信的
>
>       ![HTTPS 连接建立过程](https://cdn.jsdelivr.net/gh/xiaolincoder/ImageHost/%E8%AE%A1%E7%AE%97%E6%9C%BA%E7%BD%91%E7%BB%9C/HTTP/23-HTTPS%E5%B7%A5%E4%BD%9C%E6%B5%81%E7%A8%8B.png)



# 计算机基础

## 进程和线程

> 进程是系统分配资源的最小单位, 线程是代码执行的最小单位

