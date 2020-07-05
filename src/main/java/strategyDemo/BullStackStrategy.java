package strategyDemo;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * 单调栈
 */
public class BullStackStrategy {
    /**
     * 最大矩形
     */
    public static int largestRectangleArea(int[] heghits){
        int length = heghits.length;
        if (length == 1){
            return heghits[0];
        }
        int ans = 0;
        Deque<Integer> stack = new ArrayDeque<>(length);
        for (int index = 0;index<length;index++){
            while (!stack.isEmpty()&& heghits[stack.peekLast()]> heghits[index]){
                int currentHeghit = heghits[stack.pollLast()];
                while (!stack.isEmpty()&& heghits[stack.pollLast()] == currentHeghit){
                    stack.pollLast();
                }
                int currentWith;
                if (stack.isEmpty()){
                    currentWith = index;
                }else {
                    currentWith = index -1 - stack.peekLast();
                }
                ans = Math.max(ans, currentHeghit * currentWith);
            }
            stack.offerLast(index);
        }
        return ans;
    }


    /**
     * 接雨水
     */
    public static int largestRainWater(int[] heghits){
        int length = heghits.length;
        if (length < 3){
            return -1;
        }
        int ans = 0;
        Deque<Integer> stack = new ArrayDeque<>(length);

        for (int index = 0;index < length; index++){
            while (!stack.isEmpty()&&heghits[stack.peekLast()]<heghits[index]){
                Integer pop = stack.pollLast();
                while (!stack.isEmpty()&&heghits[stack.peekLast()] == heghits[pop]){
                    stack.pollLast();
                }
                if (!stack.isEmpty()){
                    ans += (Math.min(heghits[stack.peekFirst()],heghits[index])-heghits[pop])*(index - stack.peekLast() -1);
                }
            }
            stack.offerLast(index);
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr = {1,6,1,2,4,7};

        System.out.println( largestRectangleArea(arr));
        System.out.println( largestRainWater(arr));

    }
}
