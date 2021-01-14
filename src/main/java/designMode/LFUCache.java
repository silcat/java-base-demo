package designMode;


import java.util.HashMap;
import java.util.LinkedHashSet;

public class LFUCache {

    //key到val的映射，称为KV表
    HashMap<Integer,Integer> keyToVal;
    //key到freq的映射，称为KF表
    HashMap<Integer,Integer> keyToFreq;
    //freq到key列表的映射，称为FK表
    HashMap<Integer, LinkedHashSet<Integer>> freqToKeys;
    //记录最小频次
    int minFreq=0;
    //记录LFU缓存的最大容量
    int cap=0;

    public LFUCache(int capacity){
        keyToVal = new HashMap<>();
        keyToFreq = new HashMap<>();
        freqToKeys = new HashMap<>();
        this.cap = capacity;
        this.minFreq = 0;
    }
    public int get(int key){
        if (!keyToVal.containsKey(key)){
            return -1;
        }
        increaseFreq(key);
        return keyToVal.get(key);
    }

    private void increaseFreq(int key){
        int freq = keyToFreq.get(key);
        //更新KF表
        keyToFreq.put(key,freq+1);
        //更新FK表
        //将key从freq对应的列表中删除
        freqToKeys.get(freq).remove(key);
        //将key加入freq+1对应的链表中
        freqToKeys.putIfAbsent(freq+1,new LinkedHashSet<>());
        freqToKeys.get(freq+1).add(key);
        //如果freq对应的列表空了，移除这个freq
        if(freqToKeys.get(freq).isEmpty()){
            freqToKeys.remove(freq);
            //如果这个freq恰好是minFreq,更新minFreq
            if(freq == this.minFreq) {
                this.minFreq++;
            }
        }
    }

    private void removeMinFreqKey(){
        //freq最小的key列表
        LinkedHashSet<Integer> keyList = freqToKeys.get(this.minFreq);
        //最先插入的key是该被淘汰的那个key
        int deleteKey = keyList.iterator().next();
        //更新KF表
        keyList.remove(deleteKey);
        if(keyList.isEmpty()) freqToKeys.remove(this.minFreq);
        keyToVal.remove(deleteKey);
        keyToFreq.remove(deleteKey);
    }
    public void put(int key,int val){
        if(keyToVal.containsKey(key)){
            keyToVal.put(key,val);
            increaseFreq(key);
            return;
        }
        //key不存在，要插入
        //容量已满，则淘汰一个freq最小的key
        if(this.cap <= keyToVal.size()){
            removeMinFreqKey();
        }
        //插入key和val，对应的freq为1
        //插入KV表
        keyToVal.put(key,val);
        //插入KF表
        keyToFreq.put(key,1);
        //插入FK表
        freqToKeys.putIfAbsent(1,new LinkedHashSet<>());
        freqToKeys.get(1).add(key);
        //插入新key后最小的freq肯定是1
        this.minFreq = 1;
    }


    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);       // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        cache.get(2);       // 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        cache.get(1);       // 返回 -1 (未找到)
        cache.get(3);       // 返回  3
        cache.get(4);       // 返回  4
    }
}
