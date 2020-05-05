package date.sort;

import com.sun.org.apache.xpath.internal.operations.Minus;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

public class Job {
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
    public static boolean isUnique(String astr) {
        HashSet set = new HashSet();
        char[] chars = astr.toCharArray();
        for (int i = 0;i< chars.length;i++){
            if (set.contains(chars[i])){
                return false;
            }
            set.add(chars[i]);
        }
        return true;
    }
    public static boolean CheckPermutation(String s1, String s2) {
        HashMap<Character, Integer> result = new HashMap();
        char[] ones = s1.toCharArray();
        char[] twos = s2.toCharArray();
        if(s1==null || s2 == null){
            return false;
        }
        if(ones.length!=twos.length){
            return false;
        }
        for (int i = 0;i< ones.length;i++){
            int i1 = 0;
            if (result.get(ones[i])!=null){
                i1 = result.get(ones[i]) ;
            }
            result.put(ones[i],i1+1);
        }
        for (int i = 0;i< twos.length;i++){
            if (result.get(twos[i]) == 0){
                return false;
            }
            int i2 = result.get(twos[i]) - 1;
            if (i2<0){
                return false;
            }
            result.put(ones[i],i2);
        }
        return true;
    }

    public static int lengthOfLongestSubstring(String s) {
        int length = s.length();
        int max = 1;
        Set<Character> result =  new HashSet();
        int start = 0;
        int end = 1;
        result.add(s.charAt(start));
        while (end < length){
            if(result.contains(s.charAt(end))){
                result.remove(s.charAt(start));
                start = end;
                result.add(s.charAt(start));
            }else{
                result.add(s.charAt(end));
            }
            max = end-start+1>max?end - start + 1:max;
            end = end +1;
        }
        return max;

    }

    public void twoSum(int[] nums,int target ) {
        int[] result =  new int[2];
        int end =  nums.length-1;
        int start = 0;
        while(start< end){
            int sum = nums[start] + nums[end];
            if(sum == target){
                result[0] = start;
                result[1] = end;
                break;
            }
            if(sum < target){
                start = start +1;
            }
            if(sum > target){
                end  = end -1;
            }
        }
        System.out.println(result.toString());
    }

    public static boolean canPermutePalindrome(String s) {
        HashSet hashSet = new HashSet();
        for (int i =0;i<s.length();i++){
            char c = s.charAt(i);
            if (hashSet.contains(c)){
                hashSet.remove(c);
            }else {
                hashSet.add(c);
            }
        }
        if (hashSet.size()<2){
            return true;
        }else {
            return false;
        }
    }
    public static boolean oneEditAway(String first, String second) {
        if (first == null ||second == null){
            return false;
        }
        if (first==""&& second!=""){
            return false;
        }
        int firstIndex =0;
        int secondIndex = 0;
        int firstLength = first.length();
        int secondLength = second.length();
        if (firstLength - secondLength >1 ||firstLength - secondLength <-1 ){
            return false;
        }
        if (first == null || second == null) return false;
        if (firstLength - secondLength >1 ||firstLength - secondLength <-1 ){
            return false;
        }
        String maxString;
        String minString;
        if (firstLength >secondLength){
             maxString = firstLength >secondLength?first:second;
             minString = firstLength <secondLength?first:second;
        }else {
            minString = firstLength >secondLength?first:second;
            maxString = firstLength <secondLength?first:second;
        }
        boolean result = true;
        for (int i = 0; i < minString.length(); i++){
            if (maxString.charAt(i) != minString.charAt(i)){
                result = maxString.substring(i + 1).equals(minString.substring(firstLength == secondLength ? i + 1 : i));
                break;
            }
        }
        return result;
    }

    public static String longestPalindrome(String s) {
        int length = s.length();
        int maxEndIndex = 0;
        int minStartIndex = 0;
        int maxLength =0;
        for (int i= 0;i<length/2;i++){
            int[] result = new int[4];
            result[0]= i;
            result[1] = length  - 1;
            result[3] = i;
            int[] ints = longestPalindrome(result, s);
            if (ints[2]==1 && ints[1]-ints[0] +1>maxLength){
                maxEndIndex = ints[1];
                minStartIndex = i;
                maxLength = ints[1]-ints[0] +1;
            }
        }
        return s.substring(minStartIndex,maxEndIndex+1);
    }
    public static int[] longestPalindrome(int[] result, String s) {
        int startIndex = result[0];
        int endIndex = result[1];
        int longestPalindrome = result[2];
        if (endIndex<startIndex){
            result[2] = 0;
            return result;
        }
        if (startIndex == endIndex){
            result[2] = 1;
            return result;
        }
        if (endIndex - startIndex ==1){
            if (s.charAt(startIndex)==s.charAt(endIndex)){
                result[2] = 1;
                return result;
            }else {
                result[2] = 0;
                return result;
            }
        }

        if (s.charAt(startIndex)!=s.charAt(endIndex)){
            result[0] = startIndex;
            result[1]= endIndex-1;
            return longestPalindrome(result, s);
        }else {
            result[0] = startIndex+1;
            result[1]= endIndex-1;
            int[] ints = longestPalindrome(result, s);
            int longestPalindrom = ints[2];
            if (longestPalindrom ==1){
                result[0] = startIndex;
                result[1]= endIndex;
                return result;
            }else {
                int index = result[3];
                if (index != startIndex){
                    return result;
                }
                result[0] = startIndex;
                result[1]= endIndex-1;
                return longestPalindrome(result, s);
            }
        }

    }


    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        ArrayList<List<Integer>> list = new ArrayList<>();
        int length = nums.length;
        int left = 0;
        int right = length -1;
        for (int i = 0;i<length-1;i++){
            int current = nums[i];
            if (i>0&&i+1<length-1){
                int next = nums[i + 1];
                if (next == current){
                    continue;
                }
            }
            left = i+1;
            while (left<right){
                int leftValue = nums[left];
                int rightValue = nums[right];
                int sum = current + leftValue +rightValue;
                if (sum== 0){
                    List<Integer> objects = new ArrayList<>();
                    objects.add(current);
                    objects.add(leftValue);
                    objects.add(rightValue);
                    list.add(objects);
                    while(left<right && nums[left]==nums[left+1]){
                        left=left+1;
                    }
                    while(left<right && nums[right]==nums[right-1]){
                        right=right-1;
                    }
                    left = left + 1;
                    right = right -1;

                }else if (sum< 0){
                    left = left + 1;
                }else {
                    right = right -1;
                }
            }
            right = length -1;
        }
        return list;

    }


    public static void main(String[] args) {
        String s = "";

        int[] ints = {-1,0,1,2,-1,-4};
        threeSum(ints);
    }
}
