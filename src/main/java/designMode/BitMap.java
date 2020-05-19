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
    int mark;
    int arrang = 0;
    int bsize = 1;

    /**
     * 后面优化为动态获取：d（i）= d（i-1）+ 2^i-1
     * @param cap
     */
    public BitMap(int cap,int markp) {
        bsize = 8/ markp;
        this.bits = new byte[getIndex(cap)+1];
        this.mark = markp;

        if (markp == 1){
            arrang = 1;
        }else if (markp == 2){
            arrang = 3;
        }
    }
    public int getIndex(int num){
        return num >> bsize;
    }
    public int getPosition(int num){
        return  num & (bsize - 1);
    }

    public void put(int num){
        int bitNum = getNum(num)+1;
        if (bitNum > 3){
            return;
        }
        bits[getIndex(num)] &= ~(((arrang) << mark * getPosition(num)) & 0xFF);
        bits[getIndex(num)] |=(((bitNum & (arrang)) << mark * getPosition(num))& 0xFF);
    }
    public int getNum(int num){
        return (bits[getIndex(num)] &((arrang) << mark * getPosition(num)))>> mark * getPosition(num);
    }
    public boolean contain(int num){
        return  (bits[getIndex(num)] & arrang << getPosition(num)) != 0;
    }

    public static void main(String[] args) {
        int cap = 10;
        BitMap bitMap = new BitMap(cap,2);
        bitMap.put(2);
        int num1 = bitMap.getNum(2);
        bitMap.put(2);
        int num = bitMap.getNum(2);
    }
}
