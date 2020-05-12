package strategyDemo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 回溯算法
 * 解决一个回溯问题，实际上就是一个决策树的遍历过程
 * 其核心就是 for 循环里面的递归，在递归调用之前「做选择」，在递归调用之后「撤销选择」
 * 一;概念
 * 1、路径：也就是已经做出的选择。
 * 2、选择列表：也就是你当前可以做的选择。
 * 3、结束条件：也就是到达决策树底层，无法再做选择的条件
 * 二.模板
 *   result = []
 *   void backtrack(路径, 选择列表):
 *     if 满足结束条件:
 *         result.add(路径)
 *         return
 *
 *     for 选择 in 选择列表:
 *         做选择
 *         backtrack(路径, 选择列表)
 *         撤销选择
 */
public class backtrackStrategy {

   static List<List<Integer>> res = new LinkedList<>();

    /**
     * 输入一组不重复的数字，返回它们的全排列
     * @param nums
     * @return
     */

    static void backtrack(int[] nums, LinkedList<Integer> track) {
        // 触发结束条件
        if (track.size() == nums.length) {
            res.add(new LinkedList(track));
            System.out.println(track.toString());
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            // 排除不合法的选择
            if (track.contains(nums[i])){
                continue;
            }
            // 做选择
            track.add(nums[i]);
            // 进入下一层决策树
            backtrack(nums, track);
            // 取消选择
            track.removeLast();
        }
    }



    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        // 记录「路径」
        LinkedList<Integer> track = new LinkedList<>();
        backtrack(arr, track);
    }
}
