#grep 命令
* 格式: grep option  pattern  filename
  * option：-v(反向匹配) , -w(整词匹配) ,-n(列出所有匹配行，并且显示行号) ，-i（忽略大小写），-F（忽略正则匹配）
  * pattern：正则，字符串
##test文件内容
````
1a2b
c3 \d4

c67b
bz
zog
zoo
````
##正则表达式

* ^ (^以word开头的内容):grep  "^c" test
  
````
c3d4
c67a
````
* ^$（表示空行，不是空格）： grep -n "^$" test
````
3
````
* .	(代表且只能代表除\n任意一个字符):grep ".zo" test
````
````
* .	(代表且只能代表除\n任意一个字符):grep "zo." test
````
zog
zoo
````
* \(匹配转义自焚)：grep "\\" test
````
c3 \d4
````
* *(匹配前面字表达z或者zo零次或者多次)：grep "zo*" test 匹配结果为 在z，zo，zoo
````
bz
zog
zoo
````
* ?(匹配前面子表达式最多一次)：grep "zo？" test 匹配结果为 在z，zo，zo
````
bz
zog
zoo
````

* +(匹配前面的子表达式一次或多次)：grep "zo+" test 匹配结果为 在zo，zoo
````
zog
zoo
````
* {n}(精确匹配表达式呢次)：grep "zo{1}" test 匹配结果为 在zo，zo
````
zog
zoo
````
* {n,}(至少匹配表达式n次)：grep "zo{1}" test 匹配结果为 在zo，zoo
````
zog
zoo
````
* [abc](匹配中括号内所有字符)：grep "[zog]" test 匹配结果z,z,o,o,g,z
````
bz
zog
zoo
````