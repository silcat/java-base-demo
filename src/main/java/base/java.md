#java参数传递
* 参考文档：https://mp.weixin.qq.com/s?__biz=Mzg3NjIxMjA1Ng==&mid=2247488846&idx=3&sn=c0e0a00b756681524e169877b17e0a78&chksm=cf34ef79f843666f6d494f32c7f446a8f509415d316bbffced6712e6394ef00a33ed6f27a2d1&scene=21#wechat_redirect
* java采用值传递，传递引用类型的数据时，传递的并不是引用本身，依然是值；只是这个值 是内存地址罢了

#浅拷贝与深拷贝
* https://blog.csdn.net/bbj12345678/article/details/107779107?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control
* 深拷贝和浅拷贝是只针对Object和Array这样的引用数据类型的
* 浅拷贝只复制指向某个对象的指针，而不复制对象本身，新旧对象还是共享同一块内存。
* 深拷贝会另外创造一个一模一样的对象，新对象跟原对象不共享内存，修改新对象不会改到原对象。

#String的不可变性
* https://www.cnblogs.com/think-in-java/p/6127804.html
* https://www.zhihu.com/question/20618891

#java new一个对象发生了什么
* 假设是第一次使用该类，这样的话new一个对象就可以分为两个过程：加载类，初始化类和创建对象
## 加载类
 * 由类加载器负责根据一个类的全限定名来读取此类的二进制字节流到JVM内部，并存储在运行时内存区的方法区，然后将其转换为一个与目标类型对应的java.lang.Class对象实例 
* 加载  
   * ClassLoader.loadClass(String name) 方法
   * 使用双亲委派机制 根据一个类的全限定名来读取此类的二进制字节流到JVM内部，并存储在运行时内存区的方法区，然后将其转换为一个与目标类型对应的java.lang.Class对象实例。
   * 双亲委派    
       * 原因：对于任意一个类，都需要由加载它的类加载器和这个类本身来一同确立其在Java虚拟机中的唯一性，为了保证相同的class文件，在使用的时候，是相同的对象，jvm设计的时候，采用了双亲委派的方式来加载类
       * 流程：通过递归的方式： 子类先委托父类加载 ->父类加载器有自己的加载范围，范围内没有找到，则不加载，并返回给子类->子类在收到父类无法加载的时候，才会自己去加载
       * 分类：boostrapClassLoader<- ExtClassLoader<-AppClassLoader<-自定义ClassLoader
            * Launcher 加载创建了默认加载器
            * AppClassLoader 的父加载器为ExtClassLoader，严格意义上ExtClassLoader没有父加载器，当它找不到会委托boostrapClassLoader寻找
            * 当我们继承ClassLoader实现 自定义加载器默认加载器为AppClassLoader。
            * boostrapClassLoader 是 C++实现的，java获取不到
                * 负责加载<JAVA_HOME>/lib下的类，及jdk下的类
            * ExtClassLoader
                * 负责加载<JAVA_HOME>/lib/ext
                * 设置环境变量：java -Djava.ext.dirs=/tmp/classes 加载指定目录拓展类
            * AppClassLoader
                * 加载java.class.path 目录下的类
            * 自定义ClassLoader    
                * 加载java.class.path 目录下的类
                * 实现参考文档：https://www.cnblogs.com/aflyun/p/10618617.html
       * 破坏双亲委派:DriverManager在spi中实现破坏双亲委派。        
                
* 验证     
* 准备
    * 为类中的所有静态变量分配内存空间，并为其设置一个初始值（由于还没有产生对象，实例变量不在此操作范围内）
    * 被final修饰的static变量（常量），会直接赋值；

* 解析
    * 将常量池中的符号引用转为直接引用（得到类或者字段、方法在内存中的指针或者偏移量，以便直接调用该方法），这个可以在初始化之后再执行。
    * 注意：    
        ````
        验证、准备、解析三个阶段又合称为链接阶段，链接阶段要做的是将加载到JVM中的二进制字节流的类数据信息合并到JVM的运行时状态中。 
        ````  
##初始化类（先父后子）
* 为静态变量赋值
* 执行static代码块。
    * 注意：static代码块只有jvm能够调用
    * 如果是多线程需要同时初始化一个类，仅仅只能允许其中一个线程对其执行初始化操作，其余线程必须等待，只有在活动线程执行完对类的初始化操作之后，才会通知正在等待的其他线程。
* 因为子类存在对父类的依赖，所以类的加载顺序是先加载父类后加载子类，初始化也一样。不过，父类初始化时，子类静态变量的值也有有的，是默认值。
* 最终，方法区会存储当前类类信息，包括类的静态变量、类初始化代码（定义静态变量时的赋值语句 和 静态初始化代码块）、实例变量定义、实例初始化代码（定义实例变量时的赋值语句实例代码块和构造方法）和实例方法，还有父类的类信息引用。     
## 创建对象
* 在堆区分配对象需要的内存：分配的内存包括本类和父类的所有实例变量，但不包括任何静态变量
* 对所有实例变量赋默认值:将方法区内对实例变量的定义拷贝一份到堆区，然后赋默认值
* 执行实例初始化代码: 初始化顺序是先初始化父类再初始化子类，初始化时先执行实例代码块然后是构造方法
* 如果有类似于Child c = new Child()形式的c引用的话，在栈区定义Child类型引用变量c，然后将堆区对象的地址赋值给它
* 需要注意的是，每个子类对象持有父类对象的引用，可在内部通过super关键字来调用父类对象，但在外部不可访问    

#java序列化
## 参考文档
* https://www.cnblogs.com/9dragon/p/10901448.html
* https://www.cnblogs.com/weirdo-lenovo/p/11418905.html
##什么是序列化
* 序列化：将对象写入到IO流中
* 反序列化
    * 从IO流中恢复对象
    * 反序列化时必须有序列化对象的class文件
* 意义：序列化机制允许将实现序列化的Java对象转换位字节序列，这些字节序列可以保存在磁盘上，或通过网络传输，以达到以后恢复成原来的对象。序列化机制使得对象可以脱离程序的运行而独立存在。
* 使用场景：
    * 所有可在网络上传输的对象都必须是可序列化的，如传入的参数或返回的对象都是可序列化的，否则会出错；
    * 所有需要保存到磁盘的java对象都必须是可序列化的
##序列化实现方式
* 类应该实现Serializable接口
    * 普通序列化（对象都是基本类型），只需要实现接口
    * 如果一个可序列化的类的成员不是基本类型，也不是String类型，那这个引用类型也必须是可序列化的；否则，会导致此类不能序列化。
    * 由于java序利化算法不会重复序列化同一个对象，只会记录已序列化对象的编号
    * transient
        * 将不需要序列化的属性前添加关键字transient，序列化对象的时候，这个属性就不会被序列化     
        * 被transient修饰的属性是默认值。对于引用类型，值是null；基本类型，值是0；boolean类型，值是false。
    * 通过重写writeObject与readObject方法，
        * 可以自己选择哪些属性需要序列化， 哪些属性不需要。如果writeObject使用某种规则序列化，则相应的readObject需要相反的规则反序列化，以便能正确反序列化出对象。
    * readResolve：反序列化时替换反序列化出的对象，反序列化出来的对象被立即丢弃。此方法在readeObject后调用。    
    * 同一对象序列化多次，只有第一次序列化为二进制流，以后都只是保存序列化编号，不会重复序列化        

* Externalizable：强制自定义序列化    
##序列化版本号serialVersionUID
* java序列化提供了一个private static final long serialVersionUID 的序列化版本号，只有版本号相同，即使更改了序列化属性，对象也可以正确被反序列化回来。
* 如果反序列化使用的class的版本号与序列化时使用的不一致，反序列化会报InvalidClassException异常。
* 序列化版本号可自由指定，如果不指定，JVM会根据类信息自己计算一个版本号，这样随着class的升级，就无法正确反序列化；不指定版本号另一个明显隐患是，不利于jvm间的移植，可能class文件没有更改，但不同jvm可能计算的规则不一样，这样也会导致无法反序列化。
* 如果新类中实例变量的类型与序列化时类的类型不一致，则会反序列化失败，这时候需要更改serialVersionUID
* 建议所有可序列化的类加上serialVersionUID 版本号，方便项目升级。