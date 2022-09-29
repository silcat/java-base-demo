#架构
* ZADD paihangbang-id-1 100 menber1 / .../ ZADD paihangbang-id-1 100 menbern
* ZINCREBY  paihangbang-id-1 +2 menber1
#销量最高的10件商品
* ZREVRANGE paihangbang-id-1 0 9
#最近7天销量最高的10件商品（时间+商品两个维度）
* ZADD 20221010-spu 100 menber1 / .../ ZADD 20221017-spu  100 menbern
* ZUNIONSTORE last-seven-high 20221010-spu ... 20221017-spu
* ZREVRANGE last-seven-high 0 9
#认识的人
* sadd uidA B，C，D  sadd uidB A，C
* sdiff A[B]差集
    * 可能认识的人
* sinter 交集
    * 共同关注的人
* sunion set1 set2 并
#点赞
* sadd(key,value)  
    * 朋友圈点赞（不可重复）
* incr(key)
    * 点赞小红心
#订阅消息    
* lpush(key,value)    将元素推入列表的左端
    * 公众号订阅消息
    * 商品评价列表
* lrange(key，start，end)     获取列表在给定范围的所有元素
    * 查阅订阅消息
#附件的人/美团外卖    
* georadius（key，精度，纬度，10）返回距离中心不超过10km的元素
    * 美团地图附近酒店推送