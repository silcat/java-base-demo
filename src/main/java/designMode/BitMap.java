package designMode;

/**
 * 一数据结构
 * 1 byte = 8 bit
 * 例子:1<<2
 * ori ·0·0·0·0·0·0·0·1 -> atfer·0·0·0·0·0·1·0·0
 * ori | atfer(或运算)         ·0·0·0·0·0·1·0·1   afterYU
 * atfer & afterYU(与运算)     ·0·0·0·0·0·1·0·0   是否存在
 *
 * 二.原理
 * 通过每增加一个num，bits[index] 对应position的位置 会更新为 1，通过判断num的对应position值是否为1来判断是否存在
 *
 * 三.优势
 * int = 4 byte = 32 bit
 * 假如用int类型来存储，则内存开销将是 bitMap 的 32 倍；
 */
public class BitMap {
    byte[] bits;

    public BitMap(int cap) {
        this.bits = new byte[getIndex(cap)+1];
    }
    public int getIndex(int num){
        return num>>>3;
    }
    public int getPosition(int num){
        return  num & 0x07;
    }

    public void put(int num){
        bits[getIndex(num)] |=1<<getPosition(num);
    }
    public boolean contain(int num){
        return  (bits[getIndex(num)] & 1 << getPosition(num)) != 0;
    }

    public static void main(String[] args) {
        int cap = 10000;
        BitMap bitMap = new BitMap(cap);
        for (int i= 0;i<cap;i++){
            bitMap.put(i);
        }
        boolean contain = bitMap.contain(cap + 1);
        boolean contain1 = bitMap.contain(123);
    }
}
