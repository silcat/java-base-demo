#cat 命令
* 格式: cat option  filename （全部文本内容）
  * option：
    * -n(对输出的所有行编号,由1开始对所有输出的行数编号) , 
    * -b(对非空行输出的所有行编号,由1开始对所有输出的行数编号) 
    * -E(在每行结束处显示 $) 
    * -T(在tabs字符显示为 ^I) 
    * -A（-vET）
* 格式: tail option1 num1 | option2 num2  filename （从下倒上查看）
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
1a
2b
````
##正则表达式

* cat -n test
  
````
1 1a
2 2b
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

