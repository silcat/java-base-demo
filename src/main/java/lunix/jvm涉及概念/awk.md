#元数据区
##概念
- 元数据区也是一块线程共享的内存区域，
- 保存被虚拟机加载的类信息、常量、静态变量以及即时编译器编译后的代码等数据
- 它被安置在一块堆外内存，大小由-XX:MaxMetaspaceSize指定。

````
1. 堆内内存:JVM所分配管理的内存（-Xms所设定内存）
2. 堆外外内存：非JVM管理的内存，NIO-DirectByteBuffer（https://www.jianshu.com/p/007052ee3773）
 - -XX:MaxDirectMemorySize来指定最大的堆外内存。
 - -Dsun.nio.MaxDirectMemorySize指定了这个属性，且它不等于-1。
 - 默认directMemory = Runtime.getRuntime().maxMemory()，这是一个native方法
````
##常量
* 常量指一切不变的东西，不要简单理解为final修饰的变量
## 类文件常量池
* 此时没有加载进内存，存放于文件中
* 用于存放编译期生成的各种字面量和符号引用，这部分内容将在类加载后进入方法区的运行时常量池中存放。
* 字面量是指面量和声明为 final 的（基本数据类型）常量值,
* 符号引用，就是字面量的引用，包括类和接口的全限定名(包括包路径的完整名)、字段的名称和描述符、方法的名称和描述符
````
  tring str1 = "aaa"; "aaa"及字面量 ，str1为符号引用
````
## 运行时常量池
* 类文件常量池会在类加载后加入方法区/元数据区中的运行时常量池【此时存在在内存中】
* 运行时常量池是全局共享的，也即是线程共享的。
* class文件中类文件常量池多个相同的字符串在运行时常量池只会存在一份
## 字符串常量池
* JDK1.7以及之后的版本中，字符串常量池存在于堆中，运行时常量池并没有包含字符串常量池

## intern()
* JDK7后，字符串常量池中存储的是对象的引用，而对象本身存储于堆中
    * 判断字符串常量是否在字符串常量池中
        * 存在直接返回此字符串对象的引用
        * 不存在且堆中已有该字符串对象，将该字符串对象的引用添加到字符串常量池
        * 不存在且堆中没有此字符串对象，则先在堆中创建字符串对象，再返回其引用。

````
    String str1 = new String("aa");//堆中创建str对象和"aa"对象，str对象引用赋予str1
    str1.intern();//将"aa"对象引用添加到字符串常量池中
    String str2 = "aa";//"aa"对象引用赋予str2
    System.out.println(str1 == str2); 
    解析：str1指str1对象，str2指向aa"对象。false；
    ====================================================================================
    //创建5个对象，两个str对象，"aa","bb"以及"aabb"对象，"aabb"对象引用赋予str3
    String str3 = new String("aa") + new String("bb");
    //"aabb"不在常量池中且堆中"aabb"对象，该对象引用是str3，将str3添加到常量池
    t3.intern();
    String str4 = "aabb";
    System.out.println(str3 == str4); 
　　解析：很明显了 jdk1.6中为false 在jdk1.7中为true；

````
````
　String str1 = "aaa";
　解析：str1指向方法区；

  String str2 = "bbb";
　解析： str2 指向方法区

  String str3 = "aaabbb";
  解析：str3指向方法区

  String str4 = str1 + str2;
  解析：此行代码上边已经说过原理。str4指向堆区

  String str5 = "aaa" + "bbb";
  解析：该行代码重点说明一下，jvm对其有优化处理，也就是在编译阶段就会将这两个字符串常量进行拼接，也就是"aaabbb";所以他是在方法区中的；’

  System.out.println(str3 == str4); // false or true
　解析：很明显 为false， 一个指向堆 一个指向方法区

  System.out.println(str3 == str4.intern()); // true or false
 解析：jdk1.6中str4.intern会把“aaabbb”放在方法区，1.7后在堆区，所以在1.6中会是true 但是在1.7中是false

  System.out.println(str3 == str5);// true or false
    解析：都指向字符串常量区，字符串长常量区在方法区，相同的字符串只存在一份，其实这个地方在扩展一下，因为方法区的字符串常量是共享的，在两个线程同时共享这个字符串时，如果一个线程改变他会是怎么样的呢，其实这种场景下是线程安全的，jvm会将改变后的字符串常量在
    　　字符串常量池中重新创建一个处理，可以保证线程安全
````
##类加载机制：加载 准备 验证 解析 初始化 使用 卸载
* 先java编译为class文件，jvm加载class文件到内存

