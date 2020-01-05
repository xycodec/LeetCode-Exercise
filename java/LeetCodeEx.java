package com.xycode.leetcode;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * ClassName: LeetCodeEx
 *
 * @Author: xycode
 * @Date: 2020/1/4
 * @Description: this is description of the LeetCodeEx class
 **/
public class LeetCodeEx {
    //41. First Missing Positive
    public int firstMissingPositive(int[] nums) {
        if(nums.length==0) return 1;
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<nums.length;++i){
            list.add(nums[i]);
        }
        Collections.sort(list);
        if(list.get(0)>1) return 1;
        int index=0;
        while(index<nums.length){
            if(list.get(index)<=0){
                ++index;
            }else{
                if(index>0&&list.get(index-1)<=0&&list.get(index)>1) return 1;
                if(index<nums.length-1){
                    if(list.get(index+1)-list.get(index)>1) return list.get(index)+1;
                    ++index;
                }else{
                    break;
                }
            }
        }
        return list.get(nums.length-1)+1>0?list.get(nums.length-1)+1:1;
    }


    //30. Substring with Concatenation of All Words
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> ans=new ArrayList<>();
        if(words.length==0) return ans;
        int len=words[0].length();
        if(len==0){
            for(int i=0;i<words.length;++i) ans.add(i);
            return ans;
        }
        Map<String,Integer> mp=new HashMap<>();
        for (String word : words) {
            if (!mp.containsKey(word)) mp.put(word, 1);
            else mp.put(word, mp.get(word) + 1);
        }
        int index=0;
        int threshold=s.length()-words.length*len+1;
        while(index<threshold){
            int tmpIndex=index;
            Map<String,Integer> tmpMp=new HashMap<>(mp);
            int total=words.length;
            while(tmpIndex<s.length()){
                String tmpStr=s.substring(tmpIndex,tmpIndex+len);
                if(tmpMp.containsKey(tmpStr)){
                    int cnt=tmpMp.get(tmpStr);
                    if(cnt>0) {
                        tmpMp.put(tmpStr,cnt-1);
                        if(total>0) {
                            --total;
                            if(total==0){
                                ans.add(index);
                                break;
                            }
                            tmpIndex+=len;
                        }
                    }else break;
                }else break;
            }
            ++index;
        }

        return ans;
    }

    @Test
    public void testFindSubstring(){
        System.out.println(findSubstring("barfoothefoobarman", new String[]{"foo", "bar"}));
    }


    //42. Trapping Rain Water
    //tip: time complexity: O(hMax * N), TLE!!!
//    public int trap(int[] height) {
//        if(height==null||height.length<=1) return 0;
//        int hMax=-1;
//        for(int i=0;i<height.length;++i){
//            if(height[i]>hMax){
//                hMax=height[i];
//            }
//        }
//        int ans=0;
//        while (hMax>0){
//            List<Integer> tmp=new ArrayList<>();
//            for(int i=0;i<height.length;++i){
//                if(height[i]>=hMax){
//                    tmp.add(i);
//                }
//            }
//            ans+=tmp.get(tmp.size()-1)-tmp.get(0)-tmp.size()+1;
//            --hMax;
//        }
//        return ans;
//    }

    //tip: O(N)
    public int trap(int[] height) {
        int l=0, r=height.length-1;
        int ans=0;
        int lMax=0, rMax=0;
        while (l<r) {
            if (height[l]<height[r]) {//左边比较低(右边比较高)
                if(height[l]>=lMax){
                    lMax=height[l];//更新左边最高的
                }else{
                    ans+=lMax-height[l];
                    //因为当前的高度height[l]低于前面的某个lMax,
                    // 并且height[l]又低于右边的某个height[r],所以当前height[l]所处的位置一定可以盛水
                    // (可以盛lMax-height[l]的水,注意lMax是小于height[r]的)
                }
                ++l;
            } else {
                if(height[r]>=rMax){
                    rMax = height[r];
                }else{
                    ans += rMax - height[r];
                }
                --r;
            }
        }
        return ans;
    }

    @Test
    public void testTrap(){
        int[] a={0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(trap(a));
    }


    //44. Wildcard Matching
    private Map<Integer, Boolean> dp = new HashMap<>();
    private boolean match(String s, int i, String p, int j){
        int key = Objects.hash(i,j);

        if(dp.containsKey(key))
            return dp.get(key);

        if (i == s.length() || j == p.length()){
            if (i == s.length() && j == p.length()){
                return true;
            }else if (i == s.length()){
                while (j < p.length() && p.charAt(j) == '*')
                    j++;

                if (j == p.length())
                    return true;
                else
                    return false;
            }else{
                return false;
            }
        }

        boolean isMatch = true;

        if (p.charAt(j) == '*'){
            isMatch = match(s, i, p, j + 1);// match empty

            if (!isMatch)
                isMatch = match(s, i + 1, p, j + 1);//match last one

            if (!isMatch)
                isMatch = match(s, i + 1, p, j);//continue match
        }else if (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i)){
            isMatch = match(s, i + 1, p, j + 1);
        }else{
            isMatch = false;
        }

        dp.put(key, isMatch);

        return isMatch;
    }

    public boolean isMatch(String s, String p){
        return match(s, 0, p, 0);
    }

    @Test
    public void testIsMatch(){
        System.out.println(String.format("%d%d",10,20));
        System.out.println(10<<16);
        System.out.println(isMatch("aaabababaaabaababbbaaaabbbbbbabbbbabbbabbaabbababab",
                "*ab***ba**b*b*aaab*b"));
    }

    //55. Jump Game
    short[] memo;
    public boolean jump(int[] nums,int pos){
        if(memo[pos]!=0) return memo[pos] == 1;
        if(pos==nums.length-1) {
            return true;
        }
        if(nums[pos]+pos>=nums.length-1){
            return true;
        }
        int t=nums[pos]+pos;
        for(int i=t;i>pos;--i){
            if(jump(nums,i)){
                memo[pos]=1;
                return true;
            }

        }
        memo[pos]=-1;
        return false;
    }

    public boolean canJump(int[] nums) {
        memo=new short[nums.length];
        return jump(nums,0);
    }

    public boolean canJumpDP(int[] nums) {
        short[] dp=new short[nums.length];
        for(int i=nums.length-2;i>=0;--i){
            if(nums[i]+i>=nums.length-1){
                dp[i]=1;
                continue;
            }
            int t=nums[i]+i;
            for(int j=i+1;j<=nums[i]+i;++j){
                if (dp[j] == 1) {
                    dp[i]=1;
                    break;
                }
            }
        }
        return dp[0]==1;
    }

    //45. Jump Game II
    public int jumpMini(int[] nums) {
        if(nums.length==1) return 0;
        int lastPos = nums.length - 1;
        int step=0;
        while(lastPos>0){
            int curPos=lastPos;
            for (int i = lastPos-1; i >= 0; --i) {
                if (i + nums[i] >= lastPos) {
                    curPos=Math.min(curPos,i);
                }
            }
            ++step;
            lastPos=curPos;

        }
        return step;
    }

    @Test
    public void testJump(){
        int[] a={2,3,1,1,4};
        System.out.println(jumpMini(a));
    }


    //57. Insert Interval
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int[][] ans;
        if(intervals.length==0){
            ans=new int[1][2];
            ans[0]=newInterval;
            return ans;
        }
        if(newInterval[1]<intervals[0][0]){
            ans=new int[1+intervals.length][2];
            ans[0][0]=newInterval[0];
            ans[0][1]=newInterval[1];
            for(int i=1;i<=intervals.length;++i){
                ans[i]=intervals[i-1];
            }
            return ans;
        }
        if(newInterval[0]>intervals[intervals.length-1][1]){
            ans=new int[1+intervals.length][2];
            for(int i=0;i<intervals.length;++i){
                ans[i]=intervals[i];
            }
            ans[intervals.length][0]=newInterval[0];
            ans[intervals.length][1]=newInterval[1];
            return ans;
        }

        int l=-1,r=Integer.MAX_VALUE;
        if(newInterval[0]>=intervals[0][0]){
            for(int i=0;i<intervals.length;++i){
                if((newInterval[0]>=intervals[i][0]&&newInterval[0]<=intervals[i][1])|| //within interval
                        (i!=intervals.length-1&&newInterval[0]>intervals[i][1]&&newInterval[0]<intervals[i+1][0])){ //between intervals
                    l=i;
                    break;
                }
            }
        }

        if(newInterval[1]<=intervals[intervals.length-1][1]){
            for(int i=intervals.length-1;i>=0;--i){
                if((newInterval[1]>=intervals[i][0]&&newInterval[1]<=intervals[i][1])|| //within interval
                        (i!=intervals.length-1&&newInterval[1]>intervals[i][1]&&newInterval[1]<intervals[i+1][0])){ //between intervals
                    r=i;
                    break;
                }
            }
        }
        if(l==-1&&r==Integer.MAX_VALUE){
            ans=new int[1][2];
            ans[0]=newInterval;
        }else if(l==-1&&r<Integer.MAX_VALUE){
//            if(newInterval[1]>intervals[r][1]){
//                ans=new int[intervals.length-r][2];
//                ans[0][0]=newInterval[0];
//                ans[0][1]=newInterval[1];
//                for(int i=r+1;i<intervals.length;++i){
//                    ans[i-r][0]=intervals[i][0];
//                    ans[i-r][1]=intervals[i][1];
//                }
//            }else{
//                ans=new int[intervals.length-r][2];
//                ans[0][0]=newInterval[0];
//                ans[0][1]=intervals[r][1];
//                for(int i=r+1;i<intervals.length;++i){
//                    ans[i-r][0]=intervals[i][0];
//                    ans[i-r][1]=intervals[i][1];
//                }
//            }
            ans=new int[intervals.length-r][2];
            ans[0][0]=newInterval[0];
            ans[0][1]=Math.max(intervals[r][1],newInterval[1]);

            for(int i=r+1;i<intervals.length;++i){
                ans[i-r][0]=intervals[i][0];
                ans[i-r][1]=intervals[i][1];
            }
        }else if(l>=0&&r==Integer.MAX_VALUE){
            if(newInterval[0]>intervals[l][1]){//between intervals
                ans=new int[l+2][2];
                for(int i=0;i<=l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l+1][0]=newInterval[0];
                ans[l+1][1]=newInterval[1];
            }else{//within interval
                ans=new int[l+1][2];
                for(int i=0;i<l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l][0]=intervals[l][0];
                ans[l][1]=newInterval[1];
            }
        }else{
            if(newInterval[0]>intervals[l][1]){//between intervals
                ans=new int[intervals.length-(r-l)+1][2];
                for(int i=0;i<=l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l+1][0]=newInterval[0];
                ans[l+1][1]=Math.max(intervals[r][1],newInterval[1]);
                for(int i=1;i<intervals.length-r;++i){
                    ans[l+1+i][0]=intervals[r+i][0];
                    ans[l+1+i][1]=intervals[r+i][1];
                }
            }else{//within interval
                ans=new int[intervals.length-(r-l)][2];
                for(int i=0;i<l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l][0]=intervals[l][0];
                ans[l][1]=Math.max(intervals[r][1],newInterval[1]);
                for(int i=1;i<intervals.length-r;++i){
                    ans[l+i][0]=intervals[r+i][0];
                    ans[l+i][1]=intervals[r+i][1];
                }
            }

        }
        return ans;
    }

    @Test
    public void testInsert(){
        int[][] a={{1,3},{6,9},{10,13}};
        int[] b={14,16};
        int[][] ans=insert(a,b);
        for(int i=0;i<ans.length;++i){
            System.out.println(ans[i][0]+" "+ans[i][1]);
        }
    }

    // Definition for a Node.
    class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {}

        public Node(int _val,List<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    Set<Integer> s=new HashSet<>();
    void dfs(Node node,Map<Integer,List<Integer> > mp){
        for(Node tmp:node.neighbors){
            if(!mp.containsKey(node.val)){
                mp.put(node.val,new ArrayList<>());
            }
            mp.get(node.val).add(tmp.val);
//            System.out.println(node.val+" : "+tmp.val);
            if(!s.contains(tmp.val)){
                s.add(tmp.val);
                dfs(tmp,mp);
            }
        }
    }

    //133. Clone Graph
    public Node cloneGraph(Node node) {
        s.add(node.val);
        Map<Integer,List<Integer>> mp=new HashMap<>();
        dfs(node,mp);
        Map<Integer,Node> mp2=new HashMap<>();
        Node first=new Node(node.val,new ArrayList<>());
        mp2.put(first.val,first);
        for(int val:mp.keySet()){
            if(!mp2.containsKey(val)){
                Node tmp=new Node(val,new ArrayList<>());
                mp2.put(val,tmp);
            }
            for(int neighborVal:mp.get(val)){
                if(!mp2.containsKey(neighborVal)){
                    Node tmp=new Node(neighborVal,new ArrayList<>());
                    mp2.put(neighborVal,tmp);
                }
                mp2.get(val).neighbors.add(mp2.get(neighborVal));
            }

        }
        return first;
    }


    //715. Range Module
    class RangeModule {
        int[][] intervals=null;//维护不相交的区间集合
        public RangeModule() {

        }

        public void show(){
            if(intervals==null) return;
            for(int i=0;i<intervals.length;++i){
                System.out.print("["+intervals[i][0]+", "+intervals[i][1]+"] ");
            }
            System.out.println();
        }

        private int[][] insert(int[] newInterval) {
            int[][] ans;
            if(intervals==null||intervals.length==0){
                ans=new int[1][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=newInterval[1];
                return ans;
            }
            final int len=intervals.length;
            if(newInterval[1]<intervals[0][0]){
                ans=new int[1+len][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=newInterval[1];
                for(int i=1;i<=len;++i){
                    ans[i]=intervals[i-1];
                }
                return ans;
            }
            if(newInterval[0]>intervals[len-1][1]){
                ans=new int[1+len][2];
                for(int i=0;i<len;++i){
                    ans[i]=intervals[i];
                }
                ans[len][0]=newInterval[0];
                ans[len][1]=newInterval[1];
                return ans;
            }

            int l=-1,r=Integer.MAX_VALUE;
            if(newInterval[0]>=intervals[0][0]){
                for(int i=0;i<len;++i){
                    if((newInterval[0]>=intervals[i][0]&&newInterval[0]<=intervals[i][1])|| //within interval
                            (i!=len-1&&newInterval[0]>intervals[i][1]&&newInterval[0]<intervals[i+1][0])){ //between intervals
                        l=i;
                        break;
                    }
                }
            }

            if(newInterval[1]<=intervals[len-1][1]){
                for(int i=len-1;i>=0;--i){
                    if((newInterval[1]>=intervals[i][0]&&newInterval[1]<=intervals[i][1])|| //within interval
                            (i!=len-1&&newInterval[1]>intervals[i][1]&&newInterval[1]<intervals[i+1][0])){ //between intervals
                        r=i;
                        break;
                    }
                }
            }
            if(l==-1&&r==Integer.MAX_VALUE){
                ans=new int[1][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=newInterval[1];
            }else if(l==-1&&r<Integer.MAX_VALUE){
                ans=new int[len-r][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=Math.max(intervals[r][1],newInterval[1]);

                for(int i=r+1;i<len;++i){
                    ans[i-r][0]=intervals[i][0];
                    ans[i-r][1]=intervals[i][1];
                }
            }else if(l>=0&&r==Integer.MAX_VALUE){
                if(newInterval[0]>intervals[l][1]){//between intervals
                    ans=new int[l+2][2];
                    for(int i=0;i<=l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l+1][0]=newInterval[0];
                    ans[l+1][1]=newInterval[1];
                }else{//within interval
                    ans=new int[l+1][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=newInterval[1];
                }
            }else{
                if(newInterval[0]>intervals[l][1]){//between intervals
                    ans=new int[len-(r-l)+1][2];
                    for(int i=0;i<=l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l+1][0]=newInterval[0];
                    ans[l+1][1]=Math.max(intervals[r][1],newInterval[1]);
                    for(int i=1;i<len-r;++i){
                        ans[l+1+i][0]=intervals[r+i][0];
                        ans[l+1+i][1]=intervals[r+i][1];
                    }
                }else{//within interval
                    ans=new int[len-(r-l)][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=Math.max(intervals[r][1],newInterval[1]);
                    for(int i=1;i<len-r;++i){
                        ans[l+i][0]=intervals[r+i][0];
                        ans[l+i][1]=intervals[r+i][1];
                    }
                }

            }
            return ans;
        }

        private int[][] remove(int[] rmInterval){
            if(intervals==null||intervals.length==0) return intervals;
            final int len=intervals.length;
            if(rmInterval[1]<intervals[0][0]||rmInterval[0]>intervals[len-1][1]){
                return intervals;
            }
            int[][] ans;
            int l=-1,r=Integer.MAX_VALUE;
            if(rmInterval[0]>=intervals[0][0]){
                for(int i=0;i<len;++i){
                    if((rmInterval[0]>=intervals[i][0]&&rmInterval[0]<=intervals[i][1])|| //within interval
                            (i!=len-1&&rmInterval[0]>intervals[i][1]&&rmInterval[0]<intervals[i+1][0])){ //between intervals
                        l=i;
                        break;
                    }
                }
            }

            if(rmInterval[1]<=intervals[len-1][1]){
                for(int i=len-1;i>=0;--i){
                    if((rmInterval[1]>=intervals[i][0]&&rmInterval[1]<=intervals[i][1])|| //within interval
                            (i!=len-1&&rmInterval[1]>intervals[i][1]&&rmInterval[1]<intervals[i+1][0])){ //between intervals
                        r=i;
                        break;
                    }
                }
            }

            if(l==-1&&r==Integer.MAX_VALUE){
                return null;
            }else if(l==-1&&r<Integer.MAX_VALUE){
                if(rmInterval[1]>=intervals[r][1]){
                    ans=new int[len-r-1][2];
                    for(int i=r+1;i<len;++i){
                        ans[i-r-1][0]=intervals[i][0];
                        ans[i-r-1][1]=intervals[i][1];
                    }
                }else{
                    ans=new int[len-r][2];
                    ans[0][0]=rmInterval[1]+1;
                    ans[0][1]=intervals[r][1];

                    for(int i=r+1;i<len;++i){
                        ans[i-r][0]=intervals[i][0];
                        ans[i-r][1]=intervals[i][1];
                    }
                }
            }else if(l>=0&&r==Integer.MAX_VALUE){
                ans=new int[l+1][2];
                if(rmInterval[0]>intervals[l][1]){
                    for(int i=0;i<=l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                }else{
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=rmInterval[0]-1;
                }
            }else{
                if(rmInterval[1]>=intervals[r][1]){//between intervals
                    ans=new int[len-(r-l)][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=rmInterval[0]>intervals[l][1]?intervals[l][1]:rmInterval[0]-1;
                    for(int i=1;i<len-r;++i){
                        ans[l+i][0]=intervals[r+i][0];
                        ans[l+i][1]=intervals[r+i][1];
                    }
                }else{//within interval
                    ans=new int[len-(r-l)+1][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=rmInterval[0]>intervals[l][1]?intervals[l][1]:rmInterval[0]-1;
                    ans[l+1][0]=rmInterval[1]+1;
                    ans[l+1][1]=intervals[r][1];
                    for(int i=1;i<len-r;++i){
                        ans[l+1+i][0]=intervals[r+i][0];
                        ans[l+1+i][1]=intervals[r+i][1];
                    }
                }

            }
            return ans;

        }
        public void addRange(int left, int right) {
            int[] newInterval={left,right-1};
            intervals=insert(newInterval);
        }

        public boolean queryRange(int left, int right) {
            if(intervals==null) return false;
            final int len=intervals.length;
            --right;
            if(len==0||left>intervals[len-1][1]||right<intervals[0][0]) return false;
            for(int i=0;i<len;++i){
                if(left>=intervals[i][0]&&left<=intervals[i][1]){
                    if(right<=intervals[i][1]) return true;
                    if(i+1<len&&intervals[i][1]==intervals[i+1][0]-1){//相邻的两个区间可以合并
                        int pos=i;
                        while(pos+1<len){
                            if(intervals[pos][1]==intervals[pos+1][0]-1){//区间可以合并
                                if(right<=intervals[pos+1][1]) return true;//并且落在合并的区间内
                                else ++pos;//继续搜索是否有可以合并的区间
                            }else return false;
                        }
                    }else return false;
                }
            }
            return false;
        }

        public void removeRange(int left, int right) {
            int[] rmInterval={left,right-1};
            intervals=remove(rmInterval);
        }

    }

    /**
     * Your RangeModule object will be instantiated and called as such:
     * RangeModule obj = new RangeModule();
     * obj.addRange(left,right);
     * boolean param_2 = obj.queryRange(left,right);
     * obj.removeRange(left,right);
     */

    @Test
    public void testRangeModule(){
        RangeModule rangeModule=new RangeModule();
        rangeModule.addRange(2,6);
        rangeModule.addRange(2,8);
        rangeModule.addRange(4,7);
        rangeModule.addRange(8,9);
        rangeModule.removeRange(1,10);
        rangeModule.removeRange(3,5);
        rangeModule.removeRange(1,2);
        rangeModule.show();
//        System.out.println(rangeModule.queryRange(1, 7));
//        System.out.println(rangeModule.queryRange(2, 9));
//        System.out.println(rangeModule.queryRange(4, 6));

    }


    //567. Permutation in String
    private String sorted(String s){
        char[] c=s.toCharArray();
        Arrays.sort(c);
        return String.valueOf(c);
    }

    private boolean check(String a,int[] mCount){
        int[] aCount=new int[26];
        for(int i=0;i<a.length();++i) ++aCount[a.charAt(i)-'a'];
        for(int i=0;i<26;++i){
            if(aCount[i]!=mCount[i]) return false;
        }
        return true;
    }

    public boolean checkInclusion(String s1, String s2) {
        if(s1==null||s1.length()==0) return true;
        if(s2==null||s2.length()==0) return false;
        int[] mCount=new int[26];
        int len=s1.length();
        for(int i=0;i<len;++i) ++mCount[s1.charAt(i)-'a'];
        for(int i=0;i<s2.length()-len+1;++i){
            if(check(s2.substring(i,i+len),mCount)) return true;
        }
        return false;
    }

    @Test
    public void testCheckInclusion(){
        System.out.println(checkInclusion("adc","dcda"));
    }

    //加了缓存似乎更慢了...
//    Map<String,String> d=new HashMap<>();
    private boolean matchStr(String s1,String s2){
//        if(d.containsKey(s1)&&d.get(s1).equals(s2)) return true;
//        if(d.containsKey(s2)&&d.get(s2).equals(s1)) return true;
        int cnt=0;
        for(int i=0;i<s1.length();++i){
            if(s2.charAt(i)!=s1.charAt(i)){
                ++cnt;
                if(cnt==2) break;
            }
        }
        if(cnt==1){
//            d.put(s1,s2);
//            d.put(s2,s1);
            return true;
        }else return false;
    }

    private List<String> getMatchStrs(String tmpStr,List<String> wordList){
        List<String> result=new ArrayList<>();
        for (String s1 : wordList) {
            if(matchStr(s1,tmpStr)) result.add(s1);
        }
        return result;
    }

    //127. Word Ladder
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if(beginWord.equals(endWord)) return 1;
        if(wordList==null||wordList.size()==0) return 0;
        if(!wordList.contains(endWord)) return 0;
        Queue<String> q=new ArrayDeque<>();
        Queue<Integer> cntQ=new ArrayDeque<>();
        q.add(beginWord);
        cntQ.add(1);
        while (!q.isEmpty()){
            String tmpStr=q.poll();
            int cnt=cntQ.poll();
            if(tmpStr.equals(endWord)){
                return cnt;
            }
            for (String s1 : getMatchStrs(tmpStr,wordList)) {
                wordList.remove(s1);
                q.add(s1);
                cntQ.add(cnt+1);
            }
        }
        return 0;
    }

    @Test
    public void testLadderLength(){
        List<String> wordList=new ArrayList<>(Arrays.asList("hot","dot","dog","lot","log","cog"));
        System.out.println(ladderLength("hit","cog",wordList));
    }


    //positive==true: high low high low ....
    //positive==false: low high low high ....
    private int findWiggle(int[] nums,boolean positive){
        int pos=0;
        int ans=0;
        for(int i=0;i<nums.length-1;++i){
            if(nums[i+1]>nums[pos]) {
                if(!positive){
                    positive=true;
                    ++ans;
                    pos=i+1;
                }else{
                    ++pos;//pos后面开始的起码不劣于pos开始的
                }
            }else if(nums[i+1]<nums[pos]){
                if(positive){
                    positive=false;
                    ++ans;
                    pos=i+1;
                }else{
                    ++pos;
                }

            }
        }
        return ans+1;
    }

    //376. Wiggle Subsequence
    public int wiggleMaxLength(int[] nums) {
        if(nums==null||nums.length==0) return 0;
        if(nums.length==1) return 1;
        return Math.max(findWiggle(nums,true),findWiggle(nums,false));
    }

    @Test
    public void testWiggleMaxLength(){
        int[] array={33,53,12,64,50,41,45,21,97,35,47,92,
                39,0,93,55,40,46,69,42,6,95,51,68,72,9,32,
                84,34,64,6,2,26,98,3,43,30,60,3,68,82,9,97,
                19,27,98,99,4,30,96,37,9,78,43,64,4,65,30,
                84,90,87,64,18,50,60,1,40,32,48,50,76,100,
                57,29,63,53,46,57,93,98,42,80,82,9,41,55,
                69,84,82,79,30,79,18,97,67,23,52,38,74,15};
        System.out.println(wiggleMaxLength(array));
    }


    //215. Kth Largest Element in an Array
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> q=new PriorityQueue<>(k);
        q.add(nums[0]);
        for(int i=1;i<nums.length;++i){
            if(q.size()<k) q.add(nums[i]);
            else{
                if(q.peek()<nums[i]){
                    q.poll();
                    q.add(nums[i]);
                }
            }
        }
        return q.peek();
    }

    @Test
    public void testFindKthLargest(){
        int[] array={3,2,3,1,2,4,5,5,6};
        System.out.println(findKthLargest(array,4));
    }

    //93. Restore IP Addresses
    private void ipAddressesDfs(String s,int index,List<Integer> pre,List<String> ans){
        if(pre.size()>4) return;
        if(pre.size()==4&&index==s.length()){
            StringBuilder sb=new StringBuilder();
            sb.append(pre.get(0)).append(".");
            sb.append(pre.get(1)).append(".");
            sb.append(pre.get(2)).append(".");
            sb.append(pre.get(3));
            ans.add(sb.toString());
            return;
        }else if(index==s.length()) return;

        if(pre.isEmpty()) {
            pre.add(s.charAt(index) - '0');
            ipAddressesDfs(s, index + 1, pre,ans);
        }else if(pre.get(pre.size()-1)==0){
            pre.add(s.charAt(index)-'0');
            ipAddressesDfs(s,index+1,pre,ans);
        }else{
            int prevTmp=pre.get(pre.size()-1);
            int nextTmp=prevTmp*10+(s.charAt(index)-'0');
            List<Integer> preBK=new ArrayList<>(pre);
            if(nextTmp>0&&nextTmp<=255){
                pre.remove(pre.size()-1);
                pre.add(nextTmp);
                ipAddressesDfs(s,index+1,pre,ans);

                preBK.add((s.charAt(index)-'0'));
                ipAddressesDfs(s,index+1,preBK,ans);
            }else if(nextTmp>255){
                pre.add((s.charAt(index)-'0'));
                ipAddressesDfs(s,index+1,pre,ans);
            }
        }
    }

    public List<String> restoreIpAddresses(String s) {
        List<String> ans=new ArrayList<>();
        if(s==null||s.length()<4) return ans;
        List<Integer> pre=new ArrayList<>();
        ipAddressesDfs(s,0,pre,ans);
        return ans;
    }


    @Test
    public void testRestoreIpAddresses(){
        for(String ip:restoreIpAddresses("10001")){
            System.out.println(ip);
        }
    }


    public int nthUglyNumber(int n) {
        if(n<=0) return -1;
        if(n==1) return 1;

        int[] nums=new int[n];
        nums[0]=1;
        int p2=0,p3=0,p5=0;
        int pos=1;
        while(pos<n){
            int min=Collections.min(Arrays.asList(2*nums[p2],3*nums[p3],5*nums[p5]));
            nums[pos]=min;
            while (2*nums[p2]<=nums[pos]) ++p2;
            while (3*nums[p3]<=nums[pos]) ++p3;
            while (5*nums[p5]<=nums[pos]) ++p5;
            ++pos;
        }

        return nums[n-1];
    }

    @Test
    public void testNthUglyNumber(){
        System.out.println(nthUglyNumber(10));
    }



    @Test
    public static void test_1(){
        int[] array={2,4,6,8,10,10,12,14,16};
        System.out.println(Arrays.binarySearch(array,6));
        Stream.of(2,4,6,8,10,10,12,14,16).filter(c -> c>=10).map(c->(char)(c+'a')).forEach(System.out::println);
        System.out.println(MessageFormat.format("\n{0}-{1}","ss","dd"));

        List<Integer> l=new ArrayList<>(Arrays.asList(2,4,6,8,10,10,12,14,16));
        System.out.println(l);
        Collections.shuffle(l);
        System.out.println(l);
        BigDecimal decimal=new BigDecimal("123.456");
        System.out.println(decimal.toString());

    }

}
