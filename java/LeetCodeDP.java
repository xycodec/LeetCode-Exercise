package com.xycode.leetcode;

import com.sun.prism.PresentableState;
import org.testng.annotations.Test;
import sun.nio.cs.ext.IBM037;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ClassName: LeetCodeDP
 *
 * @Author: xycode
 * @Date: 2020/3/2
 * @Description: this is description of the LeetCodeDP class
 **/
public class LeetCodeDP {
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

    //62. Unique Paths
    public int uniquePaths(int m, int n) {
//        if(m<=0||n<=0) return 0;
//        if(m==1||n==1) return 1;
//        return uniquePaths(m-1,n)+uniquePaths(m,n-1);
        if(m<=0||n<=0) return 0;
        int[][] dp=new int[m+1][n+1];
        for(int i=1;i<=m;++i) dp[i][1]=1;
        for(int i=1;i<=n;++i) dp[1][i]=1;
        for(int i=2;i<=m;++i){
            for(int j=2;j<=n;++j){
                dp[i][j]=dp[i-1][j]+dp[i][j-1];
            }
        }
        return dp[m][n];
    }

    @Test
    public void testUniquePaths(){
        System.out.println(uniquePaths(51,9));
    }

    //63. Unique Paths II
    //tip: 较繁琐的写法
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m=obstacleGrid.length,n=obstacleGrid[0].length;
        if(n <= 0 || obstacleGrid[m - 1][n - 1] == 1 || obstacleGrid[0][0] == 1) return 0;
        obstacleGrid[0][0]=1;
        for(int i=1;i<m;++i){
            if(obstacleGrid[i][0]==0){
                obstacleGrid[i][0]=obstacleGrid[i-1][0];
            }else obstacleGrid[i][0]=0;
        }

        for(int i=1;i<n;++i){
            if(obstacleGrid[0][i]==0){
                obstacleGrid[0][i]=obstacleGrid[0][i-1];
            }else obstacleGrid[0][i]=0;
        }

        for(int i=1;i<m;++i){
            for(int j=1;j<n;++j){
                if(obstacleGrid[i][j]==0){
                    obstacleGrid[i][j]=obstacleGrid[i-1][j]+obstacleGrid[i][j-1];
                }else obstacleGrid[i][j]=0;
            }
        }

        return obstacleGrid[m-1][n-1];
    }

    //tip: 较简洁的写法
//    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
//        int m=obstacleGrid.length,n=obstacleGrid[0].length;
//        if(n<=0||obstacleGrid[m-1][n-1]==1||obstacleGrid[0][0]==1) return 0;
//        int[][] dp=new int[m+1][n+1];
//        for(int i=1;i<=m;++i){
//            for(int j=1;j<=n;++j){
//                if(i==1&&j==1)
//                    dp[i][j]=1;
//                else if(obstacleGrid[i-1][j-1]==0)
//                    dp[i][j]=dp[i-1][j]+dp[i][j-1];
//                else
//                    dp[i][j]=0;
//            }
//        }
//        return dp[m][n];
//    }

    @Test
    public void testUniquePathsWithObstacles(){
        int[][] array={{0,0,0},{1,0,1},{0,0,0}};
        System.out.println(uniquePathsWithObstacles(array));
    }

    //64. Minimum Path Sum
    public int minPathSum(int[][] grid) {
        if(grid==null||grid[0].length==0) return 0;
        int m=grid.length,n=grid[0].length;
        int[][] dp=new int[m][n];
        dp[0][0]=grid[0][0];
        for(int i=1;i<m;++i) dp[i][0]=dp[i-1][0]+grid[i][0];
        for(int j=1;j<n;++j) dp[0][j]=dp[0][j-1]+grid[0][j];
        for(int i=1;i<m;++i){
            for(int j=1;j<n;++j){
                dp[i][j]=grid[i][j]+Math.min(dp[i-1][j],dp[i][j-1]);//state transition
            }
        }
        return dp[m-1][n-1];
    }

    @Test
    public void testMinPathSum(){
        int[][] array= {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };
        System.out.println(minPathSum(array));
    }

    //120. Triangle
    //从下往上递推(状态转移方程): dp[i][j]=min{dp[i+1][j],dp[i+1][j+1]} + triangle[i][j]
    public int minimumTotal(List<List<Integer>> triangle) {
        if(triangle==null||triangle.size()==0) return 0;
        int m=triangle.size(),n=triangle.get(m-1).size();
        int[][] dp=new int[m][n];
        for(int i=0;i<n;++i)//初始化
            dp[m-1][i]=triangle.get(m-1).get(i);

        for(int i=m-2;i>=0;--i){
            for(int j=0;j<triangle.get(i).size();++j){//dp状态转移方程
                dp[i][j]=Math.min(dp[i+1][j],dp[i+1][j+1])+triangle.get(i).get(j);
            }
        }
        return dp[0][0];
    }

    //tip: 优化空间复杂度,因为每轮需要存储的状态只有一行而已
//    public int minimumTotal(List<List<Integer>> triangle) {
//        if(triangle==null||triangle.size()==0) return 0;
//        int[] dp=new int[triangle.get(triangle.size()-1).size()];
//        for(int i=0;i<triangle.get(triangle.size()-1).size();++i)//初始化
//            dp[i]=triangle.get(triangle.size()-1).get(i);
//
//        for(int i=triangle.size()-2;i>=0;--i){
//            for(int j=0;j<triangle.get(i).size();++j){//dp状态转移方程
//                dp[j]=Math.min(dp[j],dp[j+1])+triangle.get(i).get(j);
//            }
//        }
//        return dp[0];
//    }

    //152. Maximum Product Subarray
    //定义maxDP[i]为到i个数字时,最大的连续子数组乘积,minDP[i]为到i个数字时最小的连续子数组乘积
    //初始情况: maxDP[0]=minDP[0]=nums[0]
    //状态转移方程: maxDP[i]=max(maxDP[i-1]*nums[i],minDP[i-1]*nums[i],nums[i]);
    //             minDP[i]=min(maxDP[i-1]*nums[i],minDP[i-1]*nums[i],nums[i]);
    public int maxProduct(int[] nums) {
        if(nums==null||nums.length==0) return 0;
        int len=nums.length;
        int[] maxDP=new int[len],minDP=new int[len];
        maxDP[0]=minDP[0]=nums[0];
        int result=nums[0];
        for (int i = 1; i < len; i++) {
            maxDP[i]=Math.max(Math.max(maxDP[i-1]*nums[i],minDP[i-1]*nums[i]),nums[i]);
            minDP[i]=Math.min(Math.min(maxDP[i-1]*nums[i],minDP[i-1]*nums[i]),nums[i]);
            result=Math.max(result,maxDP[i]);
        }
        return result;
    }

    @Test
    public void testMaxProduct() {
        System.out.println(maxProduct(new int[]{-2,-3,-2,4}));
    }

    //188. Best Time to Buy and Sell Stock IV
    public int maxProfit(int[] prices) {
        if(prices==null||prices.length<=1) return 0;
        int[][][] dp=new int[prices.length][2+1][2];
        //[days][transactions][hold]

        dp[0][0][1]=-prices[0];//第一天买入
        //非法状态
        dp[0][1][0]=-1000000;
        dp[0][1][1]=-1000000;
        dp[0][2][0]=-1000000;
        dp[0][2][1]=-1000000;
        for(int i=1;i<prices.length;++i){//days
            dp[i][0][0]=dp[i-1][0][0];//不动
            dp[i][0][1]=Math.max(dp[i-1][0][1],dp[i-1][0][0]-prices[i]);//不动/买入

            dp[i][1][0]=Math.max(dp[i-1][1][0],dp[i-1][0][1]+prices[i]);//不动/卖出
            dp[i][1][1]=Math.max(dp[i-1][1][1],dp[i-1][1][0]-prices[i]);//不动/买入

            dp[i][2][0]=Math.max(dp[i-1][2][0],dp[i-1][1][1]+prices[i]);//不动/卖出
        }
        return Math.max(Math.max(dp[prices.length-1][0][0],dp[prices.length-1][1][0]),dp[prices.length-1][2][0]);
    }

    //188. Best Time to Buy and Sell Stock IV
    public int maxProfit(int k, int[] prices) {
        if(prices==null||prices.length<=1) return 0;
        if(k>=prices.length/2){
            int ans=0;
            for(int i=1;i<prices.length;++i){
                ans=Math.max(ans,ans+prices[i]-prices[i-1]);
            }
            return ans;
        }
        int[][] dp=new int[prices.length][k+1];
        //[days][transactions]

        int result=0;
        for(int kk=1;kk<=k;++kk){
            int maxDiff=dp[0][kk-1]-prices[0];
            for(int i=1;i<prices.length;++i){//days
                dp[i][kk]=Math.max(dp[i-1][kk],prices[i]+maxDiff);
                maxDiff=Math.max(maxDiff,dp[i][kk-1]-prices[i]);
            }
            result=Math.max(result,dp[prices.length-1][kk]);

        }
        return result;
    }

    @Test
    public void testMaxProfit() {
        System.out.println(maxProfit(2,new int[]{3,2,6,5,0,3}));
    }

    //300. Longest Increasing Subsequence
    //定义dp[i]为以nums[i]为结束的最长上升子序列
    //初始情况: dp[i]=1, i=0...len(nums)-1; 即每个序列都只有当前字符
    //状态转移方程: 当nums[i]>nums[j], 这时可以更新前面的最大长度,并且dp[i]需要和更新后的值进行比较
    //              即转义方程为: dp[i]=max{dp[i],dp[j]+1}, j=0...i-1
    public int lengthOfLIS(int[] nums) {
        if(nums==null||nums.length==0) return 0;
        int[] dp=new int[nums.length];
        Arrays.fill(dp,1);
        int result=1;
        for(int i=1;i<nums.length;++i){
            for(int j=0;j<i;++j){
                if(nums[j]<nums[i])
                    dp[i]=Math.max(dp[i],dp[j]+1);
            }
            result=Math.max(result,dp[i]);
        }
        return result;
    }

    //322. Coin Change
    //想象成青蛙跳台阶,跳的合法步数就是coins指定的一系列值
    //定义dp[i]为跳到i台阶用的最少的步数;
    //初始状态: dp[0]=0
    //状态转移方程: dp[i]=min{dp[j-coins[i]]},j=0...len(coins)-1, 并且j-coins[i]>=0
    public int coinChange(int[] coins, int amount) {
        if(coins==null||coins.length==0||amount<0) return -1;
        int[] dp=new int[amount+1];
        Arrays.fill(dp,Integer.MAX_VALUE-1);//因为是求最小的硬币数(跳数),所以这里需要初始化一个很大的值
        dp[0]=0;
        for(int i=1;i<=amount;++i){
            for(int j=0;j<coins.length;++j){
                if(coins[j]<=i){
                    dp[i]=Math.min(dp[i],dp[i-coins[j]]+1);
                }
            }
        }
        return dp[amount]==Integer.MAX_VALUE-1?-1:dp[amount];
    }

    //72. Edit Distance
    //定义dp[i][j]为word1的前i个字符,word2的前j个字符匹配时,所用的最少操作数
    //初始情况: dp[i][0]=i,dp[0][j]=j, i=0...len(word1), j=0...len(word2),
    //当一方只有0个字符中,那么另一个只能把当前索引之前的字符全部删除掉,即需要进行i/j次delete操作
    //状态转移方程: 当word1[i]==word2[j]时, dp[i][j]=dp[i-1][j-1], 因为这是不需要进行任何操作
    //             当word1[i]!=word2[j]时, dp[i][j]=min{dp[i-1][j],d[i][j-1],dp[i-1][j-1]} + 1
    //                                                  insert操作,delete操作,replace操作(因为替换掉以后,i,j都不用看了)
    public int minDistance(String word1, String word2) {
        if(word1==null||word2==null) throw new NullPointerException();
        int[][] dp=new int[word1.length()+1][word2.length()+1];
        for(int i=1;i<=word1.length();++i) dp[i][0]=i;
        for(int j=1;j<=word2.length();++j) dp[0][j]=j;

        for(int i=1;i<=word1.length();++i){
            for(int j=1;j<=word2.length();++j){
                if(word1.charAt(i-1)==word2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1];
                }else{
                    dp[i][j]=Math.min(Math.min(dp[i-1][j],dp[i][j-1]),dp[i-1][j-1])+1;
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }

}
