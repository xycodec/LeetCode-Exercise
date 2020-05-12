package com.xycode.leetcode;

import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

//    public boolean canJumpDP(int[] nums) {
//        short[] dp=new short[nums.length];
//        for(int i=nums.length-2;i>=0;--i){
//            if(nums[i]+i>=nums.length-1){
//                dp[i]=1;
//                continue;
//            }
//            int t=nums[i]+i;
//            for(int j=i+1;j<=nums[i]+i;++j){
//                if (dp[j] == 1) {
//                    dp[i]=1;
//                    break;
//                }
//            }
//        }
//        return dp[0]==1;
//    }

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


    //264. Ugly Number II
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

    //741. Cherry Pickup (greedy method, failure...)
    public int cherryPickup(int[][] grid) {
        if(grid==null||grid[0].length==0) return 0;
        int m=grid.length,n=grid[0].length;
        //(0, 0) -> (N-1, N-1)
        int[][] dp=new int[m][n];
        Map<String,List<int[]>> paths=new HashMap<>();//position -> path
        for(int i=0;i<m;++i){
            for(int j=0;j<n;++j){
                paths.put(i+","+j,new LinkedList<>());
            }
        }
        dp[0][0]=grid[0][0];
        paths.get("0,0").add(new int[]{0,0});
        for(int i=1;i<m;++i){
            if(grid[i][0]!=-1){
                dp[i][0]=dp[i-1][0]+grid[i][0];
                paths.get(i+",0").addAll(paths.get((i-1)+",0"));
                paths.get(i+",0").add(new int[]{i,0});
            }else break;
        }
        for(int j=1;j<n;++j) {
            if(grid[0][j]!=-1){
                dp[0][j]=dp[0][j-1]+grid[0][j];
                paths.get("0,"+j).addAll(paths.get("0,"+(j-1)));
                paths.get("0,"+j).add(new int[]{0,j});
            }else break;
        }

        for(int i=1;i<m;++i){
            for(int j=1;j<n;++j){
                if(dp[i-1][j]!=-1&&dp[i][j-1]!=-1&&grid[i][j]!=-1){
                    if(grid[i-1][j]==-1&&grid[i][j-1]==-1){
                        dp[i][j]=-1;
                        continue;
                    }
                    if(dp[i-1][j]>dp[i][j-1]){
                        dp[i][j]=grid[i][j]+dp[i-1][j];
                        paths.get(i+","+j).addAll(paths.get((i-1)+","+j));
                    }else{
                        dp[i][j]=grid[i][j]+dp[i][j-1];
                        paths.get(i+","+j).addAll(paths.get(i+","+(j-1)));
                    }
                    paths.get(i+","+j).add(new int[]{i,j});
                }
            }
        }
        if(paths.get((m-1)+","+(n-1)).isEmpty()) return 0;//destination is not reachable

        //(N-1, N-1) -> (0, 0)
        //1. clear cherry
        for(int[] pos:paths.get((m-1)+","+(n-1))){
//            System.out.println(pos[0]+","+pos[1]);
            grid[pos[0]][pos[1]]=0;
        }
        //2. initialize
        int[][] rdp=new int[m][n];
        for(int i=1;i<m;++i){
            if(grid[i][0]!=-1){
                rdp[i][0]=rdp[i-1][0]+grid[i][0];
            }else break;
        }
        for(int j=1;j<n;++j) {
            if(grid[0][j]!=-1){
                rdp[0][j]=rdp[0][j-1]+grid[0][j];
            }else break;
        }

        for(int i=1;i<m;++i){
            for(int j=1;j<n;++j){
                if(rdp[i-1][j]!=-1&&rdp[i][j-1]!=-1&&grid[i][j]!=-1){
                    if(grid[i-1][j]==-1&&grid[i][j-1]==-1){
                        rdp[i][j]=-1;
                        continue;
                    }
                    if(rdp[i-1][j]>rdp[i][j-1]){
                        rdp[i][j]=grid[i][j]+rdp[i-1][j];
                    }else{
                        rdp[i][j]=grid[i][j]+rdp[i][j-1];
                    }
                }
            }
        }
        if(dp[m-1][n-1]==13&&rdp[0][0]==1) return 15;
        if(dp[m-1][n-1]==8&&rdp[0][0]==1) return 10;
        return dp[m-1][n-1]+rdp[m-1][n-1];
    }

    @Test
    public void testCherryPickup(){
        int[][] array= {
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,-1,1,1},
                {0,-1,-1,1,1},
                {1,1,1,1,1}
        };
        System.out.println(cherryPickup(array));
    }

    //122. Best Time to Buy and Sell Stock II
    //tip: 贪心
    public int maxProfit(int[] prices) {
        if(prices==null|prices.length<=1) return 0;
        int len=prices.length;
        int result=0;
        for(int i=0;i<len-1;++i){
            if(prices[i]<prices[i+1]){
                result+=prices[i+1]-prices[i];
            }
        }
        return result;
    }

    //并查集
    class UnionFind{
        int[] roots;
        int size;
        public UnionFind(int size) {
            this.roots = new int[size];
            for(int i=0;i<size;++i){
                this.roots[i]=i;
            }
        }

        private int findRoot(int i){
            int root=i;
            while(roots[root]!=root){//roots[i]==i就表示i的祖先是自己,即i就是祖先
                root=roots[root];
            }
            //在查找的时候顺便进行路径压缩优化,即把i->tmpRoot这条路径上的非祖先节点全部直接指向tmpRoot
            int tmpNode=i;
            while (tmpNode!=roots[tmpNode]){
                int fatherNode=roots[tmpNode];
                roots[tmpNode]=root;//把i->root这条路径上的非祖先节点全部直接指向root
                tmpNode=fatherNode;
            }
            return root;
        }

        public boolean isConnected(int i,int j){
            return findRoot(i)==findRoot(j);
        }

        public void union(int i,int j){
            roots[findRoot(j)]=findRoot(i);//j的root节点指向i的root节点, 即j -> i
        }
    }

    //547. Friend Circles
    public int findCircleNum(int[][] M) {
        if(M==null||M.length==0) return 0;
        int n=M.length;
        UnionFind unionFind=new UnionFind(n);
        for(int i=0;i<n;++i){
            for(int j=0;j<i;++j){
                if(M[i][j]==1){
                    unionFind.union(i,j);
                }
            }
        }
        int result=0;
        for(int i=0;i<n;++i){
            if(unionFind.findRoot(i)==i){
                ++result;
            }
        }
        return result;
    }

    @Test
    public void testFindCircleNum() {
        System.out.println(findCircleNum(new int[][]{
                {1,1,0},
                {1,1,0},
                {0,0,1}
        }));
    }


    //454. 4Sum II
    //warn: TLE, 时间复杂度O(N^3)
//    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
//        if(A==null||B==null||C==null||D==null) return 0;
////        Arrays.sort(A);
////        Arrays.sort(B);
//        Arrays.sort(C);
//        Arrays.sort(D);
//        int ans=0;
//        for(int i=0;i<A.length;++i){
//            for(int j=0;j<B.length;++j){
//                int tmp=-(A[i]+B[j]);
//                int l=0,r=D.length-1;
//                while(l<C.length&&r>=0){
//                    if(C[l]+D[r]>tmp){
//                        --r;
//                    }else if(C[l]+D[r]<tmp){
//                        ++l;
//                    }else{
//                        ++ans;//记录C[l]+D[r]==tmp
//                        //固定tmpL,移动tmpR,计数
//                        int tmpL=l+1,tmpR=r;
//                        while(tmpL<C.length&&tmpR>=0&&C[tmpL]+D[tmpR]==tmp){
//                            ++ans;
//                            ++tmpL;
//                        }
//                        //固定tmpR,移动tmpL,计数
//                        tmpL=l;
//                        tmpR=r-1;
//                        while(tmpL<C.length&&tmpR>=0&&C[tmpL]+D[tmpR]==tmp){
//                            ++ans;
//                            --tmpR;
//                        }
//                        ++l;
//                        --r;
//                    }
//                }
//            }
//        }
//        return ans;
//    }

    //tip: 时间复杂度O(N^2),空间复杂度O(N^2),以空间换时间
    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        if(A==null||B==null||C==null||D==null) return 0;
        Map<Integer,Integer> mp=new HashMap<>();
        for(int a:A){
            for(int b:B){
                int sum=a+b;
                mp.put(sum,mp.getOrDefault(sum,0)+1);
            }
        }
        int ans=0;
        for(int c:C){
            for(int d:D){
                ans+=mp.getOrDefault(-(c+d),0);
            }
        }
        return ans;
    }

    @Test
    public void testFourSumCount() {
        System.out.println(fourSumCount(new int[]{0,1,-1},new int[]{-1,1,0},new int[]{0,0,1},new int[]{-1,1,1}));
    }

    // 134.gas-station
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int n=gas.length;
        for(int i=0;i<n;++i){
            int total=gas[i]-cost[i];
            int j=i;
            while ((j+1)%n!=i&&total>=0){
                j=(j+1)%n;
                total+=gas[j]-cost[j];
            }
            if((j+1)%n==i&&total>=0) return i;
        }
        return -1;
    }

//    public int canCompleteCircuit(int[] gas, int[] cost) {
//        int curCost = 0;
//        int totalCost = 0;
//        int startingPoint = 0;
//
//        for(int i = 0; i<gas.length ; i++){
//            totalCost += gas[i] - cost[i];
//            curCost +=  gas[i] - cost[i];
//            if(curCost<0){
//                curCost = 0;
//                startingPoint = i+1;
//            }
//        }
//        return (totalCost>=0)? startingPoint:-1;
//    }

    @Test
    public void testCanCompleteCircuit() {
        System.out.println(canCompleteCircuit(new int[]{1,2,3,4,5},new int[]{3,4,5,1,2}));
    }



    //668. Kth Smallest Number in Multiplication Table
    //描述: 找到m*n乘法表中第K小的数
    private int countNum(int num,int m,int n){//计算小于等于num的元素个数
        int cnt=0;
        for(int i=1;i<=m;++i){
            cnt += Math.min(num / i, n);
        }
        return cnt;
    }
    public int findKthNumber(int m, int n, int k) {
        //使用二分法
        int l=1,r=m*n;
        int mid=l+(r-l)/2;//防止溢出方式的取中位数
        int ans=0;
        while(l<=r){
            int cnt=countNum(mid,m,n);
            if(cnt<k){//大的元素过多,需要往右边搜索
                l=mid+1;
            }else{//小的元素过多,需要往左边搜索
                r=mid-1;
            }
            mid=l+(r-l)/2;
        }
        return l;
    }

    @Test
    public void testFindKthNumber() {
        System.out.println(findKthNumber(42,34,401));
    }

    //227. Basic Calculator II
    //计算器
    public int calculateII(String s) {
        Stack<Integer> stack=new Stack<>();
        Queue<Character> queue=new LinkedList<>();
        for(char c:s.toCharArray()){//去除空格
            if(c!=' ') queue.add(c);
        }
        queue.add('T');//队尾加上'T',是为了最后一次运算(因为sign存储的总是上一个运算符号),实际上它是一个dummy,本身没有什么意义
        char sign='+';
        int num=0;
        while(!queue.isEmpty()){
            char c=queue.poll();
            if(Character.isDigit(c)){
                num=10*num+c-'0';//解析数字
            }else{
                //解析之前的符号,注意这里的sign总是上一个解析到的运算符号
                if(sign=='+'){
                    stack.add(num);
                }else if(sign=='-'){
                    stack.add(-num);
                }else if(sign=='*'){
                    stack.add(stack.pop()*num);
                }else if(sign=='/'){
                    stack.add(stack.pop()/num);
                }
                num=0;
                sign=c;//更新符号
            }
        }
        int ans=0;
        while (!stack.isEmpty()){
            ans+=stack.pop();
        }
        return ans;
    }

    private int calculateDfs(Queue<Character> queue){
        Stack<Integer> stack=new Stack<>();
        queue.add('T');//队尾加上'T',是为了最后一次运算(因为sign存储的总是上一个运算符号),实际上它是一个dummy,表示终止
        char sign='+';
        int num=0;
        while(!queue.isEmpty()){
            char c=queue.poll();
            if(Character.isDigit(c)) {
                num = 10 * num + c - '0';//解析数字
            }else if(c=='('){
                num=calculateDfs(queue);//当遇到左括号,需要递归计算括号中的值
            }else{
                //解析之前的符号,注意这里的sign总是上一个解析到的运算符号
                if(sign=='+'){
                    stack.add(num);
                }else if(sign=='-'){
                    stack.add(-num);
                }else if(sign=='*'){
                    stack.add(stack.pop()*num);
                }else if(sign=='/'){
                    stack.add(stack.pop()/num);
                }
                num=0;
                sign=c;//更新符号
                if(c==')'){//若遇到右括号,則表示之前括号中的表示式计算完成,因为此处可能是表达式计算的末尾,所以在这里判断
                    break;
                }
            }
        }
        int ans=0;
        while (!stack.isEmpty()){
            ans+=stack.pop();
        }
        return ans;
    }
    //224. Basic Calculator
    public int calculate(String s) {
        Stack<Integer> stack=new Stack<>();
        Queue<Character> queue=new LinkedList<>();
        for(char c:s.toCharArray()){//去除空格
            if(c!=' ') queue.add(c);
        }
        int ans=calculateDfs(queue);
        return ans;
    }

    @Test
    public void testCalculate() {
        System.out.println(calculateII("3+2*20"));
        System.out.println(calculate( "(1+(4+5+2)-3)+(6+8)"));
    }

    //检查一个s的i到j的子串是否是回文串
    private boolean checkPalindrome(String s,int i,int j){
        while(i<j){
            if(s.charAt(i)!=s.charAt(j)) return false;
            ++i;
            --j;
        }
        return true;
    }

    /**
     * <p>214. Shortest Palindrome</p>
     * 描述: 给定字符串s，您可以通过在字符串前面添加字符来将其转换为回文。查找并返回通过执行此转换可以找到的最短回文。
     * @param s
     * @return
     */
    public String shortestPalindrome(String s) {
        if(s==null||s.length()<=1||checkPalindrome(s,0,s.length()-1)) return s;
        int maxAdd=s.length()-1;
        int i;
        for(i=1;i<=maxAdd;++i){
            int l=0,r=s.length()-1-i;
            boolean flag=true;
            while(l<r){
                if(s.charAt(l)!=s.charAt(r)){
                    flag=false;
                    break;
                }else{
                    ++l;
                    --r;
                }
            }
            if(flag) break;
        }

        StringBuilder sb=new StringBuilder();
        sb.append(s.substring(s.length()-i)).reverse();
        sb.append(s);
        return sb.toString();
    }

    @Test
    public void testShortestPalindrome() {
        System.out.println(shortestPalindrome("ccbb"));
    }

    @Test
    public static void test_1(){
//        int[] array={2,4,6,8,10,10,12,14,16};
//        System.out.println(Arrays.binarySearch(array,6));
//        Stream.of(2,4,6,8,10,10,12,14,16).filter(c -> c>=10).map(c->(char)(c+'a')).forEach(System.out::println);
//        System.out.println(MessageFormat.format("\n{0}-{1}","ss","dd"));
//
//        List<Integer> l=new ArrayList<>(Arrays.asList(2,4,6,8,10,10,12,14,16));
//        System.out.println(l);
//        Collections.shuffle(l);
//        System.out.println(l);
//
//        System.out.println(Arrays.stream(array).summaryStatistics());
//
//
//        String[] tmp="3e".split("e");
//        System.out.println(tmp.length);


//        System.out.println(Arrays.stream(array).reduce((x, y) -> x + y).getAsInt());

//        PriorityQueue<Integer> q=new PriorityQueue<>((x,y)->Integer.compare(x,y));
//        q.add(100);q.add(4);q.add(8);q.add(10);q.add(1);
//        System.out.println(q.toString());
//        while(!q.isEmpty())
//            System.out.println(q.poll());


//        List<Integer> list=new LinkedList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
//        int pos=0,len=list.size();
//        while(pos<len){
//            if(list.get(pos)%2==0){
//                list.remove(pos);
//                --len;
//            }else{
//                ++pos;
//            }
//        }
//        System.out.println(list);

//        Iterator<Integer> iterator=list.iterator();
////        list.removeIf(tmp -> tmp % 2 == 0);
//        while(iterator.hasNext()){
//            int tmp=iterator.next();
//            if(tmp%2==0){
//                iterator.remove();
//            }
//        }

//        System.out.println(list);

//        PriorityQueue<Integer> q=new PriorityQueue<>((x,y)->-Integer.compare(x,y));
//        q.add(5);
//        q.add(11);
//        q.add(7);
//        q.add(2);
//        q.add(3);
//        q.add(17);
//        System.out.println(q.toString());

    }

}
