package com.xycode.leetcode;

import com.google.common.base.Strings;
import org.testng.annotations.Test;
import sun.applet.AppletResourceLoader;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
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

    //93. Restore IP Addresses
    private void ipAddressesDfs(String s, int index, List<Integer> pre, List<String> ans){
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


    //1093. Statistics from a Large Sample
    public double[] sampleStats(int[] count) {
        int min=-1,max=-1,mode=-1;
        double mean=-1,median=-1;
        long cnt=0,sum=0;
        long modeCnt=0;
        for(int i=0;i<256;++i){
            if(count[i]!=0){
                //min
                if(min==-1) min=i;
                //mean
                cnt+=count[i];
                sum+=i*count[i];
                //mode
                if(modeCnt<count[i]){
                    modeCnt=count[i];
                    mode=i;
                }
            }
        }
        mean=sum/(double)cnt;
        int step=1000;
        if(cnt%2==1){
            long half=cnt/2;
            for(int i=255;i>=0;--i){
                if(count[i]!=0){
                    //max
                    if(max==-1) max=i;
                    //median
                    if(half>count[i]) half-=count[i];
                    else{
                        int tmp=count[i];
                        while (half>step&&tmp>step){
                            tmp-=step;
                            half-=step;
                        }
                        for(int j=1;j<=tmp;++j){
                            if(half==0) median=i;
                            --half;
                        }
                        if(half<0) break;
                    }
                }
            }
        }else{
            long halfR=cnt/2-1,halfL=cnt/2;
            int tmpMedian=0;
            for(int i=255;i>=0;--i){
                if(count[i]!=0){
                    //max
                    if(max==-1) max=i;
                    //median
                    if(halfR>count[i]) halfR-=count[i];
                    else{
                        int tmp=count[i];
                        while (halfR>step&&tmp>step){
                            tmp-=step;
                            halfR-=step;
                        }
                        for(int j=1;j<=tmp;++j){
                            if(halfR==0) tmpMedian=i;
                            --halfR;
                        }
                    }

                    if(halfL>count[i]) halfL-=count[i];
                    else{
                        int tmp=count[i];
                        while (halfL>step&&tmp>step){
                            tmp-=step;
                            halfL-=step;
                        }
                        for(int j=1;j<=tmp;++j){
                            if(halfL==0) median=(i+tmpMedian)/2.0;
                            --halfL;
                        }
                        if(halfL<0) break;
                    }
                }
            }
        }
        double[] ans={min,max,mean,median,mode};
        return ans;
    }



    //56. Merge Intervals
    public int[][] merge(int[][] intervals) {
        if(intervals==null||intervals.length<=1) return intervals;
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        LinkedList<int[]> list=new LinkedList<>();
        for(int i=0;i<intervals.length;++i){
            int[] tmp={intervals[i][0],intervals[i][1]};
            list.add(tmp);
        }
        while(true){
            int len=list.size();
            boolean flag=false;
            for(int i=0;i<len-1;++i){
                if(list.get(i)[1]>=list.get(i+1)[0]){
                    if (list.get(i)[1] < list.get(i+1)[1]) {
                        list.get(i)[1] = list.get(i+1)[1];
                    }
                    list.remove(i+1);
                    flag=true;
                    break;
                }
            }
            if(!flag) break;
        }
        int[][] ans=new int[list.size()][2];
        for(int i=0;i<list.size();++i){
            ans[i]=list.get(i);
        }
        return ans;
    }

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

    //220. Contains Duplicate III
    public static boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        int len=nums.length;
        if(len<=1||k<=0||t<0) return false;
        TreeMap<Long,Integer> mp=new TreeMap<>();
        for(int i=0;i<len;++i){
            Map<Long,Integer> tmpMap=mp.subMap((long)nums[i]-t,(long)nums[i]+t+1);
            if(tmpMap.isEmpty()){
                mp.put((long)nums[i],i);
            }else{
                Map<Long,Integer> tmp=new HashMap<>();
                for(long key:tmpMap.keySet()){
                    int tmpIndex=tmpMap.get(key);
                    if(i-tmpIndex<=k){
                        return true;
                    }else{
                        tmp.put((long)nums[i],i);
                    }
                }
                mp.putAll(tmp);
            }
        }
        return false;
    }

    class RandomFlipMatrix {
//        List<Integer> rows=new ArrayList<>();
//        List<Integer> cols=new ArrayList<>();
//        int n_rows,n_cols;
//        public RandomFlipMatrix(int n_rows, int n_cols) {
//            this.n_rows=n_rows;
//            this.n_cols=n_cols;
//            for(int i=0;i<n_rows;++i){
//                rows.add(i);
//            }
//            for(int j=0;j<n_cols;++j){
//                cols.add(j);
//            }
//            Collections.shuffle(rows);
//            Collections.shuffle(cols);
//        }
//        int rowPos=0,colPos=0;
//        public int[] flip() {
//            if(colPos==n_cols){
//                colPos=0;
//                ++rowPos;
//            }
//            int[] tmp={rows.get(rowPos),cols.get(colPos)};
//            ++colPos;
//            return tmp;
//        }
//
//        public void reset() {
//            rowPos=0;
//            colPos=0;
//            Collections.shuffle(rows);
//            Collections.shuffle(cols);
//        }


        Map<Integer, Integer> map=new HashMap<>();
        int rows, cols, total;
        Random rand=ThreadLocalRandom.current();
        public RandomFlipMatrix(int n_rows, int n_cols) {
            rows = n_rows;
            cols = n_cols;
            total = rows * cols;
        }

        /**
         * Fisher–Yates shuffle洗牌算法:
         * 1. 计算矩阵的元素个数记作n，那么[0, n-1]相当于矩阵下标对应的一维索引。
         * 2. 维护一个Map,每次采样时，key表示采样到的索引，value表示这一次采样时的末尾索引。每次采样时末尾索引都不一样，第一次采样时末尾索引为n-1，每次采样前，我们都把末尾索引减1。
         * 3. 这样如果采样到之前使用过的索引，我们就可以从dict中根据“已经采样过的索引”（key）得到value（“采样这个使用过的索引时记录下来的末尾索引”）来作为当前的采样索引
         * 4. 如果采样到还未使用过的索引，则直接使用这个索引来作为当前的采样索引。
         * @return
         */
        public int[] flip() {
            int r = rand.nextInt(total--);
            int x = map.getOrDefault(r, r);
            map.put(r, map.getOrDefault(total, total));
            return new int[]{x / cols, x % cols};
        }

        public void reset() {
            map.clear();
            total = rows * cols;
        }

    }


    private boolean validInteger(String s,boolean signed){
        if(s==null||s.length()==0) return false;
        if(!signed){
            for(int i=0;i<s.length();++i){
                if(!Character.isDigit(s.charAt(i))) return false;
            }
            return true;
        }else{//signed==true: can contain '+','-'
            if(s.charAt(0)=='+'||s.charAt(0)=='-'){
                return validInteger(s.substring(1),false);
            }else return validInteger(s,false);
        }

    }

    //exclude 'e'
    private boolean validNumber(String s){
        if(s==null||s.length()==0) return false;
        char first=s.charAt(0);
        if(first=='.'){
            return validInteger(s.substring(1),false);
        }else{
            if(first=='+'||first=='-'){
                if(s.length()==1) return false;
                if(s.charAt(1)=='.') return validInteger(s.substring(2),false);
                int cnt=0;
                for(int i=1;i<s.length();++i){
                    if(s.charAt(i)=='.') ++cnt;
                }
                if(cnt==0) return validInteger(s.substring(1),false);
                if(cnt!=1) return false;//multiple '.', is illegal
                int pos=s.indexOf('.');
                if(pos==s.length()-1) return validInteger(s.substring(1,pos),false);//eg: +3.
                return validInteger(s.substring(1,pos),false)&&validInteger(s.substring(pos+1),false);
            }else{
                int cnt=0;
                for(int i=1;i<s.length();++i){
                    if(s.charAt(i)=='.') ++cnt;
                }
                if(cnt==0) return validInteger(s,false);
                if(cnt!=1) return false;//multiple '.', is illegal
                int pos=s.indexOf('.');
                if(pos==s.length()-1) return validInteger(s.substring(0,pos),false);//eg: 3.
                return validInteger(s.substring(0,pos),false)&&validInteger(s.substring(pos+1),false);
            }
        }
    }

    //65. Valid Number
    public boolean isNumber(String s) {
        if(s==null) return false;
        s=s.trim();
        if(s.length()==0) return false;
        int cnt=0;
        for(int i=0;i<s.length();++i){
            if(s.charAt(i)=='e') ++cnt;
        }
        if(cnt==0) return validNumber(s);
        if(cnt!=1) return false;
        int pos=s.indexOf('e');
        return validNumber(s.substring(0,pos))&&validInteger(s.substring(pos+1),true);
    }

    @Test
    public void testIsNumber(){
        System.out.println(isNumber("3.e2"));
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


    int decodeCnt=0;
    private void numDecodingsDfs(String s, int index){
        if(index==s.length()){
            ++decodeCnt;
            return;
        }
        int nextTmp1=s.charAt(index)-'0';
        if(nextTmp1!=0) numDecodingsDfs(s,index+1);
        if(index+1!=s.length()&&nextTmp1!=0){
            int nextTmp2=nextTmp1*10+s.charAt(index+1)-'0';
            if(nextTmp2>=1&&nextTmp2<=26) numDecodingsDfs(s,index+2);
        }
    }

    //91. Decode Ways
    public int numDecodings(String s) {
        //dfs,指数时间复杂度
//        numDecodingsDfs(s,0);
//        return decodeCnt;

        //dp: O(N^2)
        int len=s.length();
        int[] dp=new int[len];
        int cnt=0;
        for(int i=len-1;i>=0;--i){
            cnt=0;
            int tmp1=s.charAt(i)-'0';
            if(tmp1==0) continue;
            if(i<len-1) cnt=dp[i+1];
            else cnt=1;
            if(i<len-1){
                int tmp2=s.charAt(i+1)-'0';
                int decodeTmp=tmp1*10+tmp2;
                if(decodeTmp<=26){
                    if(i<len-2) cnt+=dp[i+2];
                    else ++cnt;
                }
            }
            dp[i]=cnt;
        }
        return cnt;
    }

    @Test
    public void testNumDecodings(){
        System.out.println(numDecodings("226"));
    }


    //84. Largest Rectangle in Histogram
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
        for (int i = 0; i < len; i++) {
            if (st.empty() || heightsTmp[st.peek()] < heightsTmp[i])
                st.push(i);
            else {
                while (!st.empty() && heightsTmp[i] <= heightsTmp[st.peek()]) {
                    h = heightsTmp[st.pop()];
                    w = st.empty() ? i : i - (st.peek() + 1);
                    maxArea = Math.max(maxArea, h * w);
                }
                st.push(i);
            }
        }

        return maxArea;
    }

    @Test
    public void testLargestRectangleArea(){
        int[] array={2,1,5,6,2,3};
        System.out.println(largestRectangleArea(array));
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

        System.out.println(Arrays.stream(array).summaryStatistics());


        String[] tmp="3e".split("e");
        System.out.println(tmp.length);
    }

}
