#什么是hashcode
* Hash是散列的意思：把任意长度的输入，通过散列算法变换成固定长度的输出，该输出就是散列值（hashcode）；
#hashcode的作用
* 用于判断是否为同一个对象，类似对象的身份证
* HashCode是用来在散列存储结构中确定对象的存储地址的，提高查找的快捷性；
    * 如果没有hashcode，则需要遍历查找
* 如果两个对象相同,两个对象的hashCode一定要相同,反之不代表两个对象就相同，只能说明这两个对象在散列存储结构中，存放于同一个位置
* 注意 ：对象相等 ： hashcode相等且equel()相同
#hashcode 生成 算法
## Object
* 调用navite方法根据一定规则生成
## String
*  hashcode = 31 * hashcode + 变量
## HashMap
* hashcode =  h ^ (h >>> 16) 高16位与低16位
##ThreadLocal
* hashcode= hashcode.getAndAdd(0x61c88647);每次累加0x61c88647
    * hashcode mod lenth -1 均匀分布
* 
    ````
    ThreadLocal内存泄漏须要满足以下条件
    线程没有终结
    GC回收掉了弱引用
    没有再次调用ThreadLocal的set、get方法
    没有手工清除数据，即没有调用remove方法
    ````    
## guava
* Hashing:帮助类，封装各种散列类型HashFunction
* HashFunction：创建Hasher对象或者直接根据输入条件返回HashCode结果
* Hasher：根据加入的数据源得到HashCode。数据源通过提供的putXxx（）方法添加、hash()方法返回计算结果HashCode。
````
HashFunction hf = Hashing.md5();
HashCode hc = hf.newHasher()
    .putString("abc", Charsets.UTF_8)
    .putObject(person, personFunnel)
    .hash();
````
# Hash碰撞冲突
##是什么
* HashCode()的作用就是保证对象返回唯一hash值，但当两个对象计算值一样时，这就发生了碰撞冲突。
##如何解决
* 开放定址法
    * ThreadLocal 中实现
    * 数据结构为entry[],entry对象包含key和value
    * 遍历entry[i]如果已经存在且key相同则更新值，否则新增entry
* 链地址法（拉链法）
    * HashMap 中实现    