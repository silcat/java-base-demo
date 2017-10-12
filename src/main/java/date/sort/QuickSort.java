package date.sort;

import java.util.Arrays;

/**
 * 快速排序
 * 快速排序使用分治法策略来把一个序列（list）分为两个子序列（sub-lists）。
 * 步骤为：
 * 从数列中挑出一个元素，称为"基准"（pivot)
 * 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。
 * 在这个分区结束之后，该基准就处于数列的中间位置。这个称为分区（partition）操作。
 * 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
 * 最差:O(n2)
 * 最优:O(n log n)
 * 平均:O(n log n)
 */
public class QuickSort {

    public static void quickSort(int[] a, int left, int right) {
        if (left < right) {//递归出口条件
            int i = left;//左指针
            int j = right;//右指针
            int x = a[left];//选择第一个元素作为标尺
            while (i < j) {
                while (i < j && a[j] >= x) j--;//从右向左找第一个小于x的数
                if (i < j) a[i++] = a[j];
                while (i < j && a[i] < x) i++;//从左向右找第一个大于等于x的数
                if (i < j) a[j--] = a[i];
            }
            a[i] = x;//插入标尺
            quickSort(a, left, i - 1);//递归左边
            quickSort(a, i + 1, right);//递归右边
        }
    }


    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2, 1};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
