#sed命令
* 格式: sed &option  '&filter，&filter...&comand' filename
  * option：-n(取消默认的sed软件的输出，常与sed命令的p连用) , -r(支持正则替换) ,-i(保存更改结果)
  * &filter：1,3(1到3行)  /.../（正则匹配）
  * &comand：a(添加)  d(删除) i(插入) N(不清空模式空间) p(打印模式空间内容) s(替换) :x; bx;command (实现一行命令语句可以执行多条sed命令*) 
##test文件内容
````
1a
2b
````
##命令示例
>1.指定行添加多个内容 
* sed '2a 2b1\\n2b1' test 
````
1a
2b
2b1\n2b1
````
* sed '2a 2b1\n2b2' test
````
1a
2b
2b1
2b1
````
>2.指定行添加多个内容 
* sed '2i 2b1\n2b1' test 
````
1a
2b1
2b1
2b
````
>3.删除内容(单/多行)
* sed '2d' test 
````
1a
````
* sed '1,2d' test 
````
````
>4.替换内容
* sed 's#a#b#g' test 或 sed 's/a/b/g' test
````
1b
2b
````
* sed 's'#'a'#'&x'#g' test 其中&x=b2
````
1b
b2
````
* sed ':a;N;$!ba;s/\n/,/g' test
````
1b,2b
````
* sed  -i '2a 3a' test 
````
1a
2b
3a
````
sed 1,3s/a/d/g test 
````
1d
2b
3d
````
sed 1,2s/a/d/g test 
````
1d
2b
3a
````
* sed  -i '3d' test 
````
1a
2b
````
> 分组替换()和\1的使用
* sed -rn '1s#(.*)#\1#gp'(将部分匹配结果打印)
````
1a
````
>更换
* sed  -i '2c, b2' test 
````
1a
b2
````
>查询
* sed -n '2p' test 
````
b2
````
*  sed -n '/a/,/b/p' test
````
1a
b2
````
* sed '=' test | sed 'N;s#\n# #'
````
1 1a
2 b2
````