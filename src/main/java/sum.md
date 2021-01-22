# 算法
* 标号1-n的n个人首尾相接，1到3报数，报到3的退出，求最后一个人的标号
    ````
    Node root =  new Node(1);
    Node pre;                                                                             
    for（int i= 2;i<=n;i++）{
       Node new = new Node(i)；
       root.next = new；
        pre = new;
        if(i = n){
          pre.next = root;
       }
    }
    while(root.next.next !=null){
       root.next.next = root.next.next.next;  
       root = root.next.next;
    }
    return root.next.value;
    ````
* 请编写一个红包随机算法。需求为：给定一定的金额，一定的人数，保证每个人都能随机获得一定的金额。比如100元的红包，10个人抢，每人分得一些金额。约束条件为，最佳手气金额不能超过最大金额的90%。
* 节点，并且返回链表的头结点。例如，对一个链表: 1->2->3->4->5, 和 n = 2. 当删除了倒数第二个节点后，链表变为 1->2->3->5. 说明:给定的 n 保证是有效的。要求:只允许对链表进行一次遍历。
* 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
* 找到一个无序数组中找两个特定数，使其相加等于特定数字，请写代码java将它找出来，并指出时间复杂度。例如 【10,25,19,89,75,56,34,54,16，9，-5】找到相加等于28的【19，9 】
    ````
    int[] nums;(无序数组)
      int targe = 10;（目标值）                                                                           
      nums = Arrays.sort(nums);
      int left = 0；
      int right = nums.length;
      while（left<= right）{
       if(nums[left]+ nums[right] = targe){
         break;
        }else if(nums[left]+ nums[right] < targe){
         left++;
        }eles{
         right--;
        }
      } 
    if(nums[0]+ nums[nums.lengt-1] != targe){
     无目标值
    }eles{
     返回 left 和right
    }
    ````
* 给定仅包含字符'（'，'）'，'{'，'}'，'['和']' 的字符串，请确定输入字符串是否有效。输入字符串在以下情况下有效：1.开括号必须用相同类型的括号闭合。2.开括号必须以正确的顺序关闭。请注意，空字符串也被视为有效。

# java基础
* 用Java反射机制实现对象的浅copy方法，即将srcObj对象的属性的值copy到对象destObj的同名属性中：public void copyBeanProperties(Object srcObj, Object destObj){}
````	
	public static Object copy(Object source){
		//创建一个新的对象(空对象)
		Object target=null;		
		try {
			Class clz=source.getClass();//获取源对象的class对象
			target=clz.newInstance();//源对象必须有空的构造器
			//获取类中的所有属性
			Field[] fields=clz.getDeclaredFields();
			for (Field field : fields) {
				//获取属性名
				String fieldName=field.getName();
				//根据属性名称获取setter/getter方法名
				String set="set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				String get="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
				//根据方法名称获取方法对象
				Method method_set=clz.getMethod(set, field.getType());
				Method method_get=clz.getMethod(get);
				//执行源对象的get方法，获取返回值
				Object returnVal=method_get.invoke(source);				
				//执行目标对象的set方法，将源对象方法的返回值作为参数设置给目标对象
				method_set.invoke(target, returnVal);			
			}			
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
		return target;
	}
````
#JUC
* 问答 下面的代码在绝大部分时间内都运行得很正常，请问在什么情况下会出现问题？问题的根源在哪里？
````
   import java.util.LinkedList;
   public class Stack {
     LinkedList list = new LinkedList();
     public synchronized void push(Object x) {
       synchronized(list) {
         list.addLast( x );
         notify();
       }
     }
 
     public synchronized Object pop()
       throws Exception {
       synchronized(list) {
         if( list.size() <= 0 ) {
           wait();
         }
         return list.removeLast();
       }
     }
   }
   这里有可能会死锁：当外界调用pop(),但堆栈中没有元素，即list.size() <= 0，Stack进入停滞状态（wait()）（注意此时list被锁定了 ，但Stack不会被锁定），此时外界仍然可以进入push()（因为wait()不会锁定Stack），但此时在push()中调用list.addLast()时，因为list 被锁定，所以无法调用。 
````

# 系统设计
* 设计最多保存n个项目的本地缓存的类，用于缓存，需要满足LRU、线程安全
````
````
* 有一个系统，由于可靠性的原因，要求把系统数据完整保存在3个不同的物理机上。外部的应用需要对这些数据进行读写操作。如果让你来设计这个系统，你会考虑采用什么方案，请简要说明思路即可。
* 根据钉钉群组红包的游戏规则（用户发红包，用户领红包，用户可以查看红包），设计一套红包的数据表结构
* 通过JDK自带工具实现一个间隔并发执行的定时任务
#命令查询
* 通过java实现如下shell命令:sed -i 's/abc/efg/g' *.txt 约束:1.文件比较小，2.单进程允许创建的最大线程数10，3.需要考虑线程复用。
  *  base.stream
* 日志切分在运维中扮演着重要角色，现有1个500G的日志文件A，时间跨度为2019年1月25日到4月28日。怎么获取其中2月某天时段的记录生成文件B，注：操作服务器系统资源有限，请使用一条命令实现。
   * sed -rn '/2021-02-22 18/'p institution.eslog 




