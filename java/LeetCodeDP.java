package com.xycode.leetcode;

import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
        int nextTmp=s.charAt(index)-'0';
        if(nextTmp!=0) numDecodingsDfs(s,index+1);
        if(index+1!=s.length()&&nextTmp!=0){
            nextTmp=nextTmp*10+s.charAt(index+1)-'0';
            if(nextTmp>=1&&nextTmp<=26) numDecodingsDfs(s,index+2);
        }
    }

    //91. Decode Ways
    //tip 描述: A~Z对应1~26,给定一个字符串(由数字组成),计算出其共有多少种编码方式
    public int numDecodings(String s) {
        //dfs,指数时间复杂度
//        numDecodingsDfs(s,0);
//        return decodeCnt;

        //dp: O(N)
        if (s.isEmpty() || s.charAt(0) == '0') return 0;
        int[] dp = new int[s.length() + 1];//dp[i]表示s中前i个字符组成的子串的解码方法的个数
        //dp[i]可以分为两部分: (类似斐波那契数列)
        // 1.s[i-1]!='0',那么s[i-1]可以单独编码; (若s[i-1]=='0',那么只能组合编码,否则就不存在编码方式)
        // 2.s[i-2]s[i-1]可以组合编码(即在[10,26]之间),那么这种情况也能作为一种编码方式
        dp[0] = 1;
        int tmpPrev;
        for (int i = 1; i < dp.length; ++i) {
            if (s.charAt(i - 1) != '0') dp[i] += dp[i - 1];
            if(i >= 2){
                tmpPrev=(s.charAt(i - 2)-'0')*10+s.charAt(i-1)-'0';
                if (tmpPrev>=10 && tmpPrev<=26) {//在[10,26]之间
                    dp[i] += dp[i - 2];
                }
            }
        }
        return dp[s.length()];
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
        return maxProfit(2,prices);
    }

    //188. Best Time to Buy and Sell Stock IV
    public int maxProfit(int k, int[] prices) {
        if(prices==null||prices.length<=1) return 0;
        if(k>=prices.length/2){//相当于不限制交易次数了
            int result=0;
            for(int i=1;i<prices.length;++i){
                result=Math.max(result,result+prices[i]-prices[i-1]);
            }
            return result;
        }
        int[][][] dp=new int[prices.length][k+1][2];
        //[days][transactions][isHold]
        for(int kk=0;kk<=k;++kk){
            dp[0][kk][1]=-prices[0];//非法的状态
        }
        for(int i=1;i<prices.length;++i){
            for(int kk=k;kk>=1;--kk){
                dp[i][kk][0]=Math.max(dp[i-1][kk][0],dp[i-1][kk][1]+prices[i]);
                //今天未持有            昨天未持有      昨天持有，但是今天卖了
                dp[i][kk][1]=Math.max(dp[i-1][kk][1],dp[i-1][kk-1][0]-prices[i]);
                //今天持有              昨天持有        昨天未持有，今天买了
            }
        }
        return dp[prices.length-1][k][0];
    }

    @Test
    public void testMaxProfit() {
        System.out.println(maxProfit(2,new int[]{1,2,4,2,5,7,2,4,9,0}));
    }

    //300. Longest Increasing Subsequence
    //定义dp[i]为以nums[i]为结束的最长上升子序列
    //初始情况: dp[i]=1, i=0...len(nums)-1; 即每个序列都只有当前字符
    //状态转移方程: 当nums[i]>nums[j], 这时可以更新前面的最大长度,并且dp[i]需要和更新后的值进行比较
    //              即转移方程为: dp[i]=max{dp[i],dp[j]+1}, j=0...i-1
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



    /**
     * 322. Coin Change
     * tip 描述: 给定一系列货币的面值,给定一个目标值,问兑换到目标值的最少货币数是多少
     * 想象成青蛙跳台阶,跳的合法步数就是coins指定的一系列值
     * 定义dp[i]为跳到i台阶用的最少的步数;
     * 初始状态: dp[0]=0
     * 状态转移方程: dp[i]=min{dp[j-coins[i]]+1},j=0...len(coins)-1, 并且j-coins[i]>=0
     * @param coins
     * @param amount
     * @return
     */
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

    /**
     * <p>518. Coin Change 2</p>
     * tip 描述: 给定一系列货币的面值,给定一个目标值,问兑换到目标值的方案数有多少?
     * dp[i][j] 的含义是：在可以任意使用coins[0..i]货币的情况下,组成钱数j有多少种方法
     * 初始化: 当j==0时,即兑换0元,这时dp[i][0]=1; 当i==0时,即只使用coins[i]兑换j元的方案数,此时dp[0][j]=(j%coins[i]==0)?1:0;
     * 状态转移方程: 当j>=coins[i]时,dp[i][j]=dp[i-1][j]+dp[i][j-coins[i]]; 当j<coins[i]时,dp[i][j]=dp[i-1][j];
     * @param amount
     * @param coins
     * @return
     */
    public int change(int amount, int[] coins) {
        if(coins==null||amount<0||(coins.length==0&&amount>0)) return 0;
        if(amount == 0) return 1;
        int[][] dp=new int[coins.length][amount+1];
        for(int i=0;i<coins.length;++i){
            dp[i][0]=1;
        }
        for(int j=0;j<=amount;++j){
            if(j%coins[0]==0){
                dp[0][j]=1;
            }
        }
        for(int i=1;i<coins.length;++i){
            for(int j=1;j<=amount;++j){
                if(j>=coins[i]){
                    //使用前i-1种凑成j元的方案数 + 使用前i种凑成j元的方案数, dp[i][j-coins[i]]看起来不太直观,
                    //实际上dp[i][j-coins[i]]=dp[i-1][j-coins[i]]+dp[i-1][j-2*coins[i]]+...+dp[i-1][j-k*coins[i]], j-k*coins[i]>=0
                    dp[i][j]=dp[i-1][j]+dp[i][j-coins[i]];
                }else{
                    //使用当前货币i就会超出目标值,那么就等于使用前i-1种货币凑成j元的方案数
                    dp[i][j]=dp[i-1][j];
                }
            }
        }
        return dp[coins.length-1][amount];
    }

    @Test
    public void testChange() {
        System.out.println(change(5,new int[]{1,2,5}));
    }

    /**
     * <p>72. Edit Distance</p>
     * <p>tip 描述: 将word1变换成word2所需的最少步骤数;合法的操作有: 插入一个字符到word1,删除word1的一个字符,替换word1的某个字符</p>
     * 定义dp[i][j]为word1的前i个字符,word2的前j个字符匹配时,所用的最少操作数
     * 初始情况: dp[i][0]=i,dp[0][j]=j, i=0...len(word1), j=0...len(word2);(即空串转换所需的步骤数)
     * 当一方只有0个字符中,那么另一个只能把当前索引之前的字符全部删除掉,即需要进行i/j次delete操作
     * 状态转移方程: 当word1[i]==word2[j]时, dp[i][j]=dp[i-1][j-1], 因为这时不需要进行任何操作
     *              当word1[i]!=word2[j]时, dp[i][j]=min{dp[i-1][j],dp[i][j-1],dp[i-1][j-1]} + 1 (实际上可以给各种操作施加权重,最后求权重和的最小值)
     *                                                   insert操作,delete操作,replace操作(因为替换掉以后,i,j都不用看了)
     * @param word1
     * @param word2
     * @return
     */
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


    /**
     * <p>32. Longest Valid Parentheses</p>
     * tip 描述: 给定一个字符串,求其最长的匹配的括号字符串的长度,返回其长度
     * dp[i]表示以第i个字符结尾的最长的匹配的括号字符串的长度
     * @param s
     * @return
     */
    public int longestValidParentheses(String s) {
        if(s==null||s.length()==0) return 0;
        int len=s.length();
        int[] dp=new int[len];//dp[i]表示以第i个字符结尾的最长的匹配的括号字符串的长度
        int ans=0;
        int prev;
        for(int i=1;i<len;++i){
            if(s.charAt(i)==')') {
                prev=i-dp[i-1]-1;
                //dp[i-1]表示以第i-1个字符结尾的最长的匹配的括号字符串的长度(相当于中间隔了一段),
                // 如果与当前的')'相连的话,那么i-dp[i-1]-1就可能是能够与当前')'配对的字符
                if(prev>=0&&s.charAt(prev)=='('){//当前的')'能够与之前的一个'('配对
                    dp[i]=dp[i-1]+2+(prev>0?dp[prev-1]:0);//+2表示多了一组匹配的括号,dp[prev-1]就是上一处匹配的最长的括号字符串的长度
                }
            }
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }

    @Test
    public void testLongestValidParentheses() {
        System.out.println(longestValidParentheses(")()())"));
    }

    /**
     * <p>1143. Longest Common Subsequence</p>
     * tip 描述: 给定两个字符串,求其最长的公共子序列(子序列可以不连续)
     * dp[i][j]: s1到第i个字符,s2到第j个字符,所形成的最长公共子序列的长度
     * @param s1
     * @param s2
     * @return
     */
    public int longestCommonSubsequence(String s1, String s2) {
        if(s1==null||s2==null|| s1.length()==0||s2.length()==0) return 0;
        int len1=s1.length(),len2=s2.length();
        int[][] dp=new int[len1][len2];
        dp[0][0]=s1.charAt(0)==s2.charAt(0)?1:0;
        int ans=dp[0][0];//ans的初始值和第一 行/列 的初始化有关,只要有非空的子序列,ans就应该置为1
        for(int i=1;i<len1;++i){
            if(s2.charAt(0)==s1.charAt(i)) {
                dp[i][0]=1;
                ans=1;
            } else dp[i][0]=dp[i-1][0];//子序列未必连续,所以序列长度取前面那个值
        }
        for(int j=1;j<len2;++j){
            if(s1.charAt(0)==s2.charAt(j)) {
                dp[0][j]=1;
                ans=1;
            }
            else dp[0][j]=dp[0][j-1];
        }

        for(int i=1;i<len1;++i){
            for(int j=1;j<len2;++j){
                if(s1.charAt(i)==s2.charAt(j)){
                    dp[i][j]=dp[i-1][j-1]+1;
                }else{
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);//这个转移方程实际上决定了求出的是子序列(未必连续),s1,s2都可以跳过一些字符
                }
                ans=Math.max(ans,dp[i][j]);
            }
        }
        return ans;
    }

    @Test
    public void testLongestCommonSubsequence() {
        System.out.println(longestCommonSubsequence("a","a"));
    }

    /**
     * <p>718. Maximum Length of Repeated Subarray</p>
     * tip 描述: 给定两个数组,找到长度最长的公共子数组,返回其长度(其实就是最长公共子串问题(子串是连续的));
     * dp[i][j]: s1到第i个字符,s2到第j个字符,所形成的最长公共子数组的长度
     * @param A
     * @param B
     * @return
     */

    public int findLength(int[] A, int[] B) {
        if(A==null||B==null||A.length==0||B.length==0) return 0;
        int len1=A.length,len2=B.length;
        int[][] dp=new int[len1][len2];
        for(int i=0;i<len1;++i){
            if(A[i]==B[0]) dp[i][0]=1;
        }
        for(int j=0;j<len2;++j){
            if(A[0]==B[j]) dp[0][j]=1;
        }
        int ans=0;
        for(int i=1;i<len1;++i){
            for(int j=1;j<len2;++j){
                if(A[i]==B[j]){
                    dp[i][j]=dp[i-1][j-1]+1;//子串必须连续,所以dp[i][j]只能从dp[i-1][j-1]出转移而来
                }
                ans=Math.max(ans,dp[i][j]);
            }
        }
        return ans;
    }

    @Test
    public void testFindLength() {
        System.out.println(findLength(new int[]{1,2,3,2,1},new int[]{3,2,1,4,7}));
    }

    /**
     * <p>516. Longest Palindromic Subsequence</p>
     * tip 描述: 给定一个字符,求其最长回文子序列,返回其长度
     * 实际上可以把这个问题转化为最长公共子序列问题,做法就是求 s与s的反转字符串 的最长公共子序列(最长回文子串不能这么做)
     * @param s
     * @return
     */
    public int longestPalindromeSubseq(String s) {
        if(s==null||s.length()==0) return 0;
        StringBuilder sb=new StringBuilder();
        for(int i=s.length()-1;i>=0;--i){
            sb.append(s.charAt(i));
        }
        int ans=longestCommonSubsequence(s,sb.toString());
        return ans;
    }

    @Test
    public void testLongestPalindromeSubseq() {
        System.out.println(longestPalindromeSubseq("a"));
    }

    //返回最长公共子串的长度
    public int longestCommonSubstr(char[] A, char[] B) {
        if(A==null||B==null||A.length==0||B.length==0) return 0;
        int len1=A.length,len2=B.length;
        int[][] dp=new int[len1][len2];
        int ans=0;
        for(int i=0;i<len1;++i){
            if(A[i]==B[0]) {
                dp[i][0]=1;
                ans=1;
            }
        }
        for(int j=0;j<len2;++j){
            if(A[0]==B[j]) {
                dp[0][j]=1;
                ans=1;
            }
        }

        for(int i=1;i<len1;++i){
            for(int j=1;j<len2;++j){
                if(A[i]==B[j]){
                    dp[i][j]=dp[i-1][j-1]+1;//子串必须连续,所以dp[i][j]只能从dp[i-1][j-1]出转移而来
                }
                ans=Math.max(ans,dp[i][j]);
            }
        }
        return ans;
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
     * 给定一个字符串s,问最少添加几个字符使得字符串变成回文串
     * dp[i][j]表示从i位置到j位置构成回文串需要最少添加的字符数量
     * when s[i]==s[j]; dp[i][j]=dp[i+1][j-1]
     * when s[i]!=s[j]; dp[i][j]=min{dp[i+1][j],dp[i][j-1]}
     * 也有其它的解法,即先求出s的最长回文序列的长度L,那么答案就是s.length()-L
     * @param s
     * @return
     */
    public int minAdd(String s) {
        if (s == null || s.length() == 0) return 0;//空串也认为是回文串
        char[] str = s.toCharArray();
        int len = str.length;
        int[][] dp = new int[len][len];//dp[i][j]表示从i位置到j位置构成回文串需要最少添加的字符数量
        for(int width = 1 ; width < len ; width ++) {
            for(int i = 0; i+width < len ; i++) {
                int j = i + width ;
                if(str[i]==str[j]) {
                    dp[i][j]=dp[i+1][j-1];
                }else {
                    dp[i][j]=Math.min(dp[i+1][j],dp[i][j-1])+1;
                }
            }
        }
        return dp[0][len - 1];
    }

    @Test
    public void testMinAdd() {
        System.out.println(minAdd("abacba"));
    }

}
