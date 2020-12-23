#mat
## 概念
 - java内存分析工具
## Shallow heap & Retained heap 概念
 - Shallow Size 
  对象自身占用的内存大小，不包括它引用的对象。 
 - Retained Size 
  Retained Size就是当前对象被GC后，从Heap上总共能释放掉的内存
 - ![RUNOOB 图标](http://dl.iteye.com/upload/attachment/0068/2171/5bbac0c5-ccd3-33af-aece-8ae3cbb6ed16.jpg)
 ````
 A对象的Retained Size=A对象的Shallow Size 
 B对象的Retained Size=B对象的Shallow Size + C对象的Shallow Size 
 这里不包括D对象，因为D对象被GC Roots直接引用。
 ````

 