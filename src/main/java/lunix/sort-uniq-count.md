#sort 命令
* 格式: sort option  filename （全部文本内容）
  * option：
  ````
    -n：依照数值的大小排序；
    -u 输出行中去除重复行。
    -r r表示降序
    -o 输出结果
    -k 列数 -t 间隔符
    -r：以相反的顺序来排序； 
    -t<分隔字符>：指定排序时所用的栏位分隔字符；
    -b：忽略每行前面开始出的空格字符； 
    -c：检查文件是否已经按照顺序排序； 
    -d：排序时，处理英文字母、数字及空格字符外，忽略其他的字符； 
    -f：排序时，将小写字母视为大写字母； 
    -i：排序时，除了040至176之间的ASCII字符外，忽略其他的字符； 
    -m：将几个排序号的文件进行合并； 
    -M：将前面3个字母依照月份的缩写进行排序； 
  ````
* 格式: uniq option1 num1 | option2 num2  filename （从下倒上查看）
  * option：
    * -n num(日志尾部最后num行日志) , 
    * -n +num(从第num行到文末) ,
    * -f/-numf(循环读取尾部num行日志) 
    * -c num(最后num个字符) 
* 格式: tail option1 num1 | option2 num2  filename （从下倒上查看）
  * option：
    * -n num(前num行日志) , 
    * -c num(前num个字符) 
##test文件内容
````
1：2
2：3
3：5
4：4
````
##正则表达式

* sort -n -k 2 -t ":" test -o test1
  
````
1：2
2：3
4：4
3：5
````
* cat >test1 << EOF 创建test1文件
````
>test1
>test2
>EOF
````
* cat test1
````
test1
test2
````

