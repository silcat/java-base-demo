package strategyDemo;

/**
 * 树
 */
public class ArraytStrategy {
    /**
     *在4x4的棋盘上摆满了黑白棋子，黑白两色的位置和数目随机其中左上角坐标为(1,1),右下角坐标为(4,4),现在依次有一些翻转操作，要对一些给定支点坐标为中心的上下左右四个棋子的颜色进行翻转，请计算出翻转后的棋盘颜色。
     *
     * 给定两个数组A和f,分别为初始棋盘和翻转位置。其中翻转位置共有3个。请返回翻转后的棋盘。
     *
     * 测试样例：
     */
    public static int[][] flipChess(int[][] A, int[][] f) {
        int length = A[0].length;
        for (int i = 0; i<f.length;i++){
            int x =f[i][0];
            int y =f[i][1];
            if (y-1>=0&&y+1<length){
                int yTemp = A[x][y-1];
                A[x][y-1] = A[x][y+1];
                A[x][y+1] = yTemp;
            }
            if (x-1>=0&&x+1<A.length){
                int xTemp = A[x-1][y];
                A[x-1][y] = A[x+1][y];
                A[x+1][y] = xTemp;
            }

        }
        return A;
 }


    public static void main(String[] args) {

        int[][] a = {{0,0,1,3},{1,0,1,0},{0,1,1,0},{0,0,1,0}};
        int[][] b = {{2,2},{3,3},{4,4}};
        int[][] c = {{0,1,0},{2,0,0}};
        int[][] ints = flipChess(a, b);
        System.out.print( a[0][3]);

    }

}
