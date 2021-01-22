#akw命令
* 格式: akw option  'pattern{command}' filename
  * option：-F(指定分隔符 -F ":") -F "[ ,]"(正则表达式[] 指定两个分割符是 空格和 逗号)
  * pattern：
    * 正则表达式作为模式: $5~/test/ 第5列匹配"test",~ 表示匹配符号
    * 比较表达式作为模式:NF>2
    * 范围模式:pattern1,pattern2 从pattern1开始 到 pattern2 结束
    * 特殊模式BEGIN和END
  * command：{print $1}
##akw参数解析
![Image text](file:///C:/Users/yangtianfeng/Desktop/%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202017-02-26%20%E4%B8%8B%E5%8D%886.22.15.png)
* RS记录分隔符，表示每行的结束标志
* NR行号（记录号）
* FS字段分隔符，每列的分隔标志或结束标志
* NF就是每行有多少列，每个记录中字段的数量
* $符号表示取某个列（字段）,$1$2$NF
* NF表示记录中的区域（列）数量，$NF取最后一个列（区域。）
* FS（-F）字段（列）分隔符，-F（FS）“：”<==>‘BEGIN{FS=':'}’
* RS 记录分隔符（行的结束标识）默认分隔符为'\n'
* NR 行号
* ORS 输出行分隔符 OFS 输出列分隔符
##test文件内容
````
inet addr:192.168.197.133  Bcast:192.168.197.255  
Mask:255.255.255.0
````
##命令示例
* akw 'NR==1' test 
````
inet addr:192.168.197.133  Bcast:192.168.197.255  
Mask:255.255.255.0
````
* awk -F ":" 'NR==1{print NR, $1,$2，$NF}' test 等价于 awk 'BEGIN{FS=":"}NR==1{print NR, $1,$2，$NF}' test 
````
1 inet addr 192.168.197.133  Bcast 192.168.197.255
````
* awk -F "[ :]" 'NR==1{print NR, $1,$2,$NF}' test
````
1 inet addr
````
* awk -F "[ :]" 'NR==1{print NR,$1}NR==2{print NR,$NF}'
````
1 inet addr:192.168.197.133  Bcast:192.168.197.255  
2 Mask:255.255.255.0
````
* awk -F "[ :]" 'NR==1{print NR, $0}' test
````
1 inet addr
````
* awk 'BEGIN{RS=":"}{print NR,$0}' test 
````
1 inet addr
2 192.168.197.133  Bcast
3 192.168.197.255  
Mask
4 255.255.255.0
````
* awk 'BEGIN{RS=":";ORS ="列分割符"; OFS ="行分割符"}{print NR,$0}' test 
````
1行分割符inet addr列分割符2行分割符192.168.197.133  Bcast列分割符3行分割符192.168.197.255  
Mask列分割符4行分割符255.255.255.0
````
* awk -F "[ :]" '$2~/addr/{print NR,$0}' test
````
awk -F "[ :]" '$2~/addr/{print NR,$0}' test
````
* awk -F "[ :]" 'NR>1&&$2~/addr/{print NR,$0}' test
````
````
* awk '/^Mask/,NR==2{print NR,$0}' test 
````
2 Mask:255.255.255.0
````
awk '/\d{4}\-02-\d{1,2}/{print NR,$0} ' institution.es
