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
* 格式: uniq option  filename （从下倒上查看）
  * option：
  ````
    -c：在每列旁边显示该行重复出现的次数； 
    -u：仅显示不重复的行； 
    -d:仅显示重复出现的行列； 
    -f<栏位>：忽略比较指定的栏位； 
    -s<n>：忽略比较文件中的前n个字符； 
    -w<n>：比较文件中的前n个字符。
  ````
* 格式: tail option1 num1 | option2 num2  filename （从下倒上查看）
  * option：
    * -n num(前num行日志) , 
    * -c num(前num个字符) 
##test文件内容
````
aa  10
aa  11
bb  13
cc  19
ee  15
cc  19
````
##正则表达式

* sort  -k 2 -t " " test 
  
````
aa  10
aa  11
bb  13
ee  15
cc  19
cc  19
````
* sort  -k 2 -t " " -r test 
````
cc  19
cc  19
ee  15
bb  13
aa  11
aa  10
````
* sort -k 2 -t " " -u test
````
aa  10
aa  11
bb  13
ee  15
cc  19
````
* uniq -c命令只有在相邻的情况下才会生效
* awk -F " " '{print $2}' test |sort | uniq -c
````
1 
1 10
1 11
1 13
1 15
2 19
````
* awk -F " " '{print $2}' test  | uniq -c
````
1 
1 10
1 11
1 13
1 19
1 15
1 19
````
* awk -F " " '{print $2}'  test|sort | uniq -c |sort -n -r -o > test1
````
  2 19
  1 15
  1 13
  1 11
  1 10
  1 
````

