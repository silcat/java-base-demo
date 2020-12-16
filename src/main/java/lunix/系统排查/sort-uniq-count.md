#sort 命令
* 格式: sort option  filename （全部文本内容）
  * option：
    * -u 输出行中去除重复行。
    * -r r表示降序
    * -o 输出结果
    * -k 列数 -t 间隔符
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

