#java参数传递
* 参考文档：https://mp.weixin.qq.com/s?__biz=Mzg3NjIxMjA1Ng==&mid=2247488846&idx=3&sn=c0e0a00b756681524e169877b17e0a78&chksm=cf34ef79f843666f6d494f32c7f446a8f509415d316bbffced6712e6394ef00a33ed6f27a2d1&scene=21#wechat_redirect
* java采用值传递，传递引用类型的数据时，传递的并不是引用本身，依然是值；只是这个值 是内存地址罢了
# 基本数据类型和对象的区别
* 基本数据类型都是直接存储在内存中的栈上的，数据本身的值就是存储在栈空间里面，而对象的 “引用”(存储对象在内存堆上的地址)是存储在有序的栈上的，而对象本身的值存储在堆上的；
* 当传递方法参数类型为基本数据类型（数字以及布尔值）时，传递是是值的拷贝，方法体内修改参数不影响原来参数的值
* 当传递方法参数类型为对象（引用类型）时，传递对象引用地址值的拷贝，方法体内修改对象参数会影响原来的参数
* 本质java只有值传递，但方便理解可以说基本数据类型是值传递，对象是引用传递（引用地址值的拷贝的传递）
#浅拷贝与深拷贝
* https://blog.csdn.net/bbj12345678/article/details/107779107?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control
* 深拷贝与浅拷贝问题中，会发生深拷贝的有java中的8中基本类型以及他们的封装类型，另外还有String类型。其余的都是浅拷贝
* 浅拷贝仅仅复制所考虑的对象，而不复制它所引用的对象，深拷贝把要复制的对象所引用的对象都复制了一遍
* 深拷贝和浅拷贝都是对象拷贝


#String的不可变性
* https://www.cnblogs.com/think-in-java/p/6127804.html
* https://www.zhihu.com/question/20618891

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

#抽象类与接口、继承类与实现接口区别
* https://segmentfault.com/a/1190000022539062