-- 导入common函数库
local common = require('test.common')
local read_http = common.read_http
local read_redis = common.read_redis
local read_redis_gray = common.read_redis_gray
-- 导入cjson库
local cjson = require('cjson')
-- 导入共享词典，本地缓存
local item_cache = ngx.shared.cache
-- 封装查询函数
function read_data(key, expire, path, params)
    -- 查询本地缓存

    local val = item_cache:get(key)
    if not val then
        ngx.log(ngx.ERR, "本地缓存查询失败，尝试查询Redis， key: ", key)
        -- 查询redis
        val = read_redis("127.0.0.1", 6379, key)
        -- 判断查询结果
        if not val then
            ngx.log(ngx.ERR, "redis查询失败，尝试查询http， key: ", key)
            -- redis查询失败，去查询http
            val = read_http(path, params)
        end
    end
    -- 查询成功，把数据写入本地缓存
    item_cache:set(key, val, expire)
    -- 返回数据
    return val
end

-- 获取路径参数
local id = ngx.var[1]

-- 查询商品信息
local resp =  read_data("item:id"..id ,1000,  "/testbakend", nil)
-- JSON转化为lua的table
--local item = cjson.decode("{\"name\":\"ytf\"}")
-- 把item序列化为json 返回结果
ngx.say(resp)