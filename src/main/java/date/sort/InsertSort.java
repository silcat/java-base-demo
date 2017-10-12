package date.sort;

import java.util.Arrays;

/**
 * 插入排序
 * 就是寻找第一个比自己大的元素的时候用折半查找进行优化:
 * 一般来说，插入排序都采用in-place在数组上实现。具体算法描述如下：
 * 从第一个元素开始，该元素可以认为已经被排序
 * 取出下一个元素，在已经排序的元素序列中从后向前扫描
 * 如果该元素（已排序）大于新元素，将该元素移到下一位置
 * 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置
 * 将新元素插入到该位置后
 * 重复步骤2~5
 * 最差:O(n2)
 * 最优:O(n)
 * 平均:O(n2)
 */
public class InsertSort {
    /**
     * 希尔排序
     * 希尔排序，也称递减增量排序算法，是插入排序的一种更高效的改进版本。希尔排序是非稳定排序算法。
     * 根据步长进行分组,对每一组进行插入排序.
     * 最差:O(因步长各异,最好的n log2 n)
     * 最优:O(n)
     * 平均:O(因步长各异)
     */
    public static void shellSort(int[] a){
        int igap=a.length;//初始化步长,第一次步长为数组长度的一半
        for(int gap=igap/2;gap>0;gap/=2){//步长
            for(int i=0;i<gap;i++){//共做步长次插入排序
                for(int j=i+gap;j<a.length;j+=gap){//直接插入排序算法
                    for(int k=i;k<=j;k++){
                        if(a[k]>=a[j]){//找到了第一个比自己大的元素,插入到该元素之前
                            int temp=a[j];
                            for(int p=j-1;p>=k;p--) a[p+1]=a[p];//每个元素向后移动一格
                            a[k]=temp;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 折半插入排序
     */
    public static void halfInsertSort(int[] a){
        for(int i=1;i<a.length;i++){
            int low=0;
            int high=i;
            while(low<=high){
                int half=(low+high)/2;
                if(a[half]<a[i]){//插入点在高半区
                    low=half+1;
                }else{//插入点在低半区
                    high=half-1;
                }
            }
            //low或者high指向的元素就是第一个比自己大的元素 插入到之前
            int temp=a[i];
            for(int k=i-1;k>=low;k--) a[k+1]=a[k];//每个元素向后移动一格
            a[low]=temp;
        }
    }
    /**
     * 插入排序
     */
    public static void sort(int[] arr) {
        int length = arr.length;//数组长度，将这个提取出来是为了提高速度。
        int insertNum;//要插入的数
        for (int i = 1; i < length; i++) {//插入的次数
            insertNum = arr[i];//要插入的数
            int j = i - 1;//已经排序好的序列元素个数
            while (j >= 0 && arr[j] > insertNum) {//序列从后到前循环，将大于insertNum的数向后移动一格
                arr[j + 1] = arr[j];//元素移动一格
                j--;
            }
            arr[j + 1] = insertNum;//将需要插入的数放在要插入的位置。
        }
    }

    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
        halfInsertSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
