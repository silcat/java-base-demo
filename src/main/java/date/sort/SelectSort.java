package date.sort;

import java.util.Arrays;

/**
 * 简单排序
 * 首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，然后，
 * 再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。
 * 最差:O(n2)
 * 最优:O(n2)
 * 平均:O(n2)
 */
public class SelectSort {
    public static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {//循环次数
            int key = arr[i];
            int position = i;
            for (int j = i + 1; j < arr.length; j++) {//选出最小的值和位置
                if (arr[j] < key) {
                    key = arr[j];
                    position = j;
                }
            }
            arr[position] = arr[i];//交换位置
            arr[i] = key;
        }
    }

    public static void main(String[] args) {
        int[] arr = {9, 6, 7, 4, 5, 3, 2, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
