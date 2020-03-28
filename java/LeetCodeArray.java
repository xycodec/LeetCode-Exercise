package com.xycode.leetcode;

import com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator;
import org.testng.annotations.Test;
import sun.awt.image.ImageWatched;

import java.util.*;

/**
 * ClassName: LeetCodeArray
 *
 * @Author: xycode
 * @Date: 2020/3/21
 * @Description: this is description of the LeetCodeArray class
 **/
public class LeetCodeArray {

    //53. Maximum Subarray
    public int maxSubArray(int[] nums) {
        if(nums==null||nums.length==0) return 0;
        if(nums.length==1) return nums[0];
        int tmpSum=0;
        int maxSum=Integer.MIN_VALUE;
        for (int num : nums) {
            if (tmpSum <= 0) tmpSum = num;
            else tmpSum += num;
            if (tmpSum > maxSum) maxSum = tmpSum;
        }
        return maxSum;
    }

    @Test
    public void testMaxSubArray(){
        int[] array={-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(maxSubArray(array));
    }

    //84. Largest Rectangle in Histogram (柱状图中的最大矩形)
    //tip 描述: 给定一组柱状图,求其能够形成的最大面积(使用单调栈)
    public int largestRectangleArea(int[] heights) {
        if(heights==null||heights.length==0) return 0;
        if(heights.length==1) return heights[0];
        int[] heightsTmp=new int[heights.length+1];
        for(int i=0;i<heights.length;++i){
            heightsTmp[i]=heights[i];
        }
        heightsTmp[heights.length]=0;
        int len = heights.length+1;
        int maxArea = 0;
        int h, w;
        Stack<Integer> st=new Stack<>();
        for (int i = 0; i < len; ++i) {
            while (!st.empty() && heightsTmp[i] <= heightsTmp[st.peek()]) {
                h = heightsTmp[st.pop()];
                w = st.empty() ? i : i - (st.peek() + 1);//s.peek()是上一个(不能扩展的)边界
                System.out.println(h+","+w+"; "+(st.isEmpty()?"":st.peek()));
                maxArea = Math.max(maxArea, h * w);
            }
            st.push(i);
        }
        return maxArea;
    }

    @Test
    public void testLargestRectangleArea(){
        int[] array={2,1,5,6,2,3};
        System.out.println(largestRectangleArea(array));
    }

    //notice: 单调栈
    private int maximalRectangle(char[] heights){
        if(heights==null||heights.length==0) return 0;
        if(heights.length==1) return heights[0]-'0';
        int[] heightsTmp=new int[heights.length+1];
        for(int i=0;i<heights.length;++i){
            heightsTmp[i]=heights[i]-'0';
        }
        heightsTmp[heights.length]=0;//末尾加个0,充当一个dummy的作用,就不用单独再收尾了(即计算最后边界处形成的矩形面积)
        int len = heights.length+1;
        int maxArea = 0;
        int h, w;
        Stack<Integer> st=new Stack<>();
        for (int i = 0; i < len; ++i) {
            while (!st.empty() && heightsTmp[i] <= heightsTmp[st.peek()]) {
                h = heightsTmp[st.pop()];//高度
                w = st.empty() ? i : i - (st.peek() + 1);
                //st.peek()是之前小于当前高度的柱状图的索引,所以这里需要减去(st.peek() + 1)
                maxArea = Math.max(maxArea, h * w);
            }
            st.push(i);
        }
        return maxArea;
    }
    //85. Maximal Rectangle
    public int maximalRectangle(char[][] matrix) {
        if(matrix==null||matrix.length==0||matrix[0].length==0) return 0;
        char[][] heights=new char[matrix.length][matrix[0].length];
        heights[0]=Arrays.copyOf(matrix[0],matrix[0].length);
        for(int i=1;i<matrix.length;++i){
            for(int j=0;j<matrix[0].length;++j){
                if(matrix[i][j]=='0') {
                    heights[i][j]='0';
                }else{
                    heights[i][j]= (char) (matrix[i][j]+heights[i-1][j]-'0');
                }
            }
        }

        int maxArea=0;
        for(int i=0;i<heights.length;++i){
            maxArea=Math.max(maxArea,maximalRectangle(heights[i]));
        }
        return maxArea;
    }

    @Test
    public void testMaximalRectangle() {
        System.out.println(maximalRectangle(new char[][]{
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}
        }));
        System.out.println(maximalRectangle(new char[][]{
                {'0'}
        }));
    }

    //498. Diagonal Traverse
    public int[] findDiagonalOrder(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return new int[0];
        int m = matrix.length, n = matrix[0].length;
        int[] ans = new int[m * n];
        int cnt = 0;
        for (int i = 0; i < m + n - 1; ++i) {
            if (i % 2 == 1) {
                for (int j = Math.min(i, n - 1); j >= Math.max(i - m + 1, 0); --j)
                    ans[cnt++] = matrix[i - j][j];
            } else {
                for (int j = Math.max(i - m + 1, 0); j <= Math.min(i, n - 1); ++j)
                    ans[cnt++] = matrix[i - j][j];
            }
        }
        return ans;
    }

    @Test
    public void testFindDiagonalOrder(){
        int[][] array={{1,2,3},{4,5,6},{7,8,9}};
        for(int i:findDiagonalOrder(array))
            System.out.print(i+" ");
        System.out.println();
    }

    //220. Contains Duplicate III
    //tip 描述: 给定一个整数数组，找出数组中是否有两个不同的索引i和j,
    // 使得|nums[i]-nums[j]|<=t, 且|i-j|<=k
//    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
//        int len=nums.length;
//        if(len<=1||k<=0||t<0) return false;
//        TreeMap<Long,Integer> mp=new TreeMap<>();//值 -> 索引
//        for(int i=0;i<len;++i){
//            mp.put((long) nums[i], i);//全部添加完毕,之后才搜索(饿式搜索)
//        }
//        for(int i=0;i<len;++i){
//            //对数时间,取出nums[i]-t ~ nums[i]+t范围的所有元素
//            Map<Long,Integer> tmpMap=mp.subMap((long)nums[i]-t,(long)nums[i]+t+1);
//            if(!tmpMap.isEmpty()){
//                for(long key:tmpMap.keySet()){
//                    int tmpIndex=tmpMap.get(key);
//                    if(i==tmpIndex) continue;//注意:得是不同的索引
//                    if(Math.abs(i-tmpIndex)<=k){//检查索引是否在合法范围内
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        if(nums==null||nums.length<=1||k<=0||t<0) return false;
        int len=nums.length;
        TreeMap<Long, Integer> mp = new TreeMap<>();//值(long类型防止溢出) -> 索引
        for(int i = 0; i< nums.length; i++) {
            //对数时间,取出nums[i]-t ~ nums[i]+t范围的所有元素
            Map<Long, Integer> tmpMap = mp.subMap((long)nums[i] - t, (long)nums[i] + t + 1);
            if (!tmpMap.isEmpty()) {
                for(long key: tmpMap.keySet()) {
                    int tmpIndex=tmpMap.get(key);
                    if (i - tmpIndex <= k) {//tmpIndex是i之前的索引,所以直接减就好了
                        return true;
                    }
                }
            }
            mp.put((long)nums[i], i);//边添加,边搜索,懒式搜索
        }
        return false;
    }

    //239. Sliding Window Maximum
    //描述: 求数组中滑动窗口的最大值
    public int[] maxSlidingWindow(int[] nums, int k) {
        if(nums==null||nums.length==0||nums.length<k) return new int[0];
        int[] ans=new int[nums.length-k+1];
        Deque<Integer> deque=new LinkedList<>();//这个队列中存储的是索引
        for(int i=0;i<nums.length;++i){
            if(deque.isEmpty()){
                deque.addLast(i);
            }else{
                if(i-deque.getFirst()+1>k){//检查窗口长度
                    deque.removeFirst();
                }
                while (!deque.isEmpty()&&nums[deque.getLast()]<nums[i]){//把较小的值的索引扔出去
                    deque.removeLast();
                }
                deque.addLast(i);//添加当前索引
            }
            if(i>=k-1) ans[i-k+1]=nums[deque.getFirst()];
        }
        return ans;
    }

    @Test
    public void testMaxSlidingWindow() {
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7},3)));
    }

    //340. Longest Substring with At Most K Distinct Characters
    //tip 描述: 至多包含K个不同字符的最长子串
    int lengthOfLongestSubstringDistinct(String s,int k){
        Map<Character,Integer> mp=new HashMap<>();
        int ans=0;
        for(int i=0,j=0;j<s.length();++j){
            char cj=s.charAt(j);
            mp.put(cj,mp.getOrDefault(cj,0)+1);
            while (mp.size()>k){//不同的字符数超过了k,就缩小滑动窗口直至不同的字符数符合要求
                char ci=s.charAt(i);
                mp.put(ci,mp.get(ci)-1);
                if(mp.get(ci)==0){//窗口不含字符ci了,就remove
                    mp.remove(ci);
                }
                ++i;
            }
            ans=Math.max(ans,j-i+1);//子串的长度就是滑动窗口的长度(j-i+1)
        }
        return ans;
    }

    @Test
    public void testLengthOfLongestSubstringDistinct() {
        System.out.println(lengthOfLongestSubstringDistinct("abcdadb",3));// ->4
    }

    private boolean match(Map<Character,Integer> word,Map<Character,Integer> dict){
        if(word.size()!=dict.size()) return false;
        for(char c:word.keySet()){
            if(word.get(c)<dict.get(c)) return false;
        }
        return true;
    }
    //76. Minimum Window Substring
    //tip 描述: 在字符串s上找到一个最小长度的窗口,该窗口内应包含字符串t的所有字符,返回该窗口对应的子串
    public String minWindow(String s, String t) {
        if(s==null||t==null||s.length()==0||t.length()==0) return "";
        if(s.equals(t)) return s;
        Map<Character,Integer> mp=new HashMap<>();
        Map<Character,Integer> dict=new HashMap<>();
        for(char c:t.toCharArray()){
            dict.put(c,dict.getOrDefault(c,0)+1);
        }
        String ans="";
        s+="$";//dummy,为了方便处理合法字符出现在最后一位的情况
        for(int i=0,j=0;j<s.length();){
            char left=s.charAt(i),right=s.charAt(j);
            if(match(mp,dict)){//匹配完毕
                if(ans.equals("")||ans.length()>j-i){
                    ans=s.substring(i,j);
                }
                if(mp.containsKey(left)){
                    if(mp.get(left)==1) mp.remove(left);
                    else mp.put(left,mp.get(left)-1);
                }
                ++i;
            }else{
                if(j!=s.length()-1&&dict.containsKey(right)){//注意最后一位的dummy不能计数
                    mp.put(right,mp.getOrDefault(right,0)+1);
                }
                ++j;
            }
        }
        return ans;
    }

    //tip: 优化,使用数组代替HashMap,貌似速度没快多少
    private boolean match(char[] word,char[] dictCount,Set<Character> dict){
        for(char c:dict.toArray(new Character[0])){
            if(word[c]<dictCount[c]) return false;
        }
        return true;
    }
    public String minWindow2(String s, String t) {
        if(s==null||t==null||s.length()==0||t.length()==0) return "";
        if(s.equals(t)) return s;
        char[] word=new char[256];
        char[] dictCount=new char[256];
        Set<Character> dict=new HashSet<>();
        for(char c:t.toCharArray()){
            ++dictCount[c];
            dict.add(c);
        }
        String ans="";
        s+="$";//dummy,为了方便处理合法字符出现在最后一位的情况
        for(int i=0,j=0;j<s.length();){
            char left=s.charAt(i),right=s.charAt(j);
            if(match(word,dictCount,dict)){//匹配完毕
                if(ans.equals("")||ans.length()>j-i){
                    ans=s.substring(i,j);
                }
                if(dict.contains(left)){//只针对t中出现的字符进行计数
                    if(word[left]>0) --word[left];
                }
                ++i;
            }else{
                if(j!=s.length()-1&&dict.contains(right)){//注意最后一位的dummy不能计数
                    ++word[right];
                }
                ++j;
            }
        }
        return ans;
    }
    @Test
    public void testMinWindow() {
        System.out.println(minWindow2("ADOBECODEBANC","ABC"));
    }

    //30. Substring with Concatenation of All Words
    //tip 描述: 找到s的子串,使得words里的单词在该串中只出现一次并且不包含其它字符,
    // 返回这些子串的起始索引(题目保证words里的单词长度都相同)
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> ans=new ArrayList<>();
        if(words.length==0) return ans;
        int len=words[0].length();
        if(len==0){//针对空串的处理
            for(int i=0;i<=s.length();++i) ans.add(i);
            return ans;
        }
        Map<String,Integer> wordCount=new HashMap<>();
        for (String word : words) {
            wordCount.put(word,wordCount.getOrDefault(word,0)+1);
        }
        int index=0;
        int threshold=s.length()-words.length*len+1;
        while(index<threshold){
            int tmpIndex=index;
            Map<String,Integer> tmpWordCount=new HashMap<>(wordCount);
            int total=words.length;//需要匹配的单词数
            while(tmpIndex<s.length()){
                String tmpStr=s.substring(tmpIndex,tmpIndex+len);//按指定长度取单词
                if(tmpWordCount.containsKey(tmpStr)){
                    int cnt=tmpWordCount.get(tmpStr);
                    if(cnt>0) {
                        tmpWordCount.put(tmpStr,cnt-1);
                        if(total>0) {
                            --total;//需要匹配的单词数减一
                            if(total==0){//匹配完毕
                                ans.add(index);//添加起始索引
                                break;
                            }
                            tmpIndex+=len;
                        }
                    }else break;//取出的单词不匹配,break
                }else break;//取出的单词不匹配,break
            }
            ++index;
        }
        return ans;
    }

    @Test
    public void testFindSubstring(){
        System.out.println(findSubstring("barfoothefoobarmanbarfoothefoobarman", new String[]{"foo", "bar"}));
    }

}
