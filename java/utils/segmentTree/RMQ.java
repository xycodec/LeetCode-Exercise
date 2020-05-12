package com.xycode.utils.segmentTree;

import java.util.Arrays;

/**
 * ClassName: RMQ
 *
 * @Author: xycode
 * @Date: 2020/4/20
 * @Description: 区间最值问题(Range Minimum/Maximum Query),适合Immutable data,
 **/
public class RMQ {
    private static final double LOG2=Math.log(2);
    int[][] minQuery;
    int[][] maxQuery;
    public RMQ(int[] array) {
        this.minQuery= buildMinQuery(array);
        this.maxQuery=buildMaxQuery(array);
    }

    private int[][] buildMinQuery(int[] array){
        int n=0,tmp=1;
        while(tmp<array.length){
            tmp<<=1;
            ++n;
        }
        int[][] minQuery=new int[array.length][n];
        for(int i=0;i<array.length;++i){
            Arrays.fill(minQuery[i],Integer.MAX_VALUE);
            minQuery[i][0]=array[i];
        }
        //minQuery[i][j]: 也就是以array[i]为起点连续2^j个数所形成的区间中的最小值
        // 整个区间的最大值一定是左右两部分最大值的较小值; 满足动态规划的最优化原理分析得到状态转移方程:
        //  minQuery[i][j]=min(minQuery[i][j-1],minQuery[i+2^(j-1)][j-1]),边界条件为minQuery[i][0]=array[i]
        for(int j=1;j<n;++j){
            for(int i=0;i+(1<<(j-1))<array.length;++i){
                minQuery[i][j]=Math.min(minQuery[i][j-1],minQuery[i+(1<<(j-1))][j-1]);
            }
        }
        return minQuery;
    }

    public int queryMin(int l,int r){
        int k=(int)(Math.log(r-l+1)/LOG2);
        return Math.min(minQuery[l][k],minQuery[r-(1<<k)+1][k]);
    }

    private int[][] buildMaxQuery(int[] array){
        int n=0,tmp=1;
        while(tmp<array.length){
            tmp<<=1;
            ++n;
        }
        int[][] maxQuery=new int[array.length][n];
        for(int i=0;i<array.length;++i){
            Arrays.fill(maxQuery[i],Integer.MIN_VALUE);
            maxQuery[i][0]=array[i];
        }
        //O(N*logN)的初始化复杂度,O(1)的查询复杂度
        //maxQuery[i][j]: 也就是以array[i]为起点连续2^j个数所形成的区间中的最小值
        // 整个区间的最大值一定是左右两部分最大值的较大值; 满足动态规划的最优化原理分析得到状态转移方程:
        //  maxQuery[i][j]=max(maxQuery[i][j-1],maxQuery[i+2^(j-1)][j-1]),边界条件为maxQuery[i][0]=array[i]
        for(int j=1;j<n;++j){
            for(int i=0;i+(1<<(j-1))<array.length;++i){
                maxQuery[i][j]=Math.max(maxQuery[i][j-1],maxQuery[i+(1<<(j-1))][j-1]);
            }
        }
        return maxQuery;
    }

    public int queryMax(int l,int r){
        int k=(int)(Math.log(r-l+1)/LOG2);
        return Math.max(maxQuery[l][k],maxQuery[r-(1<<k)+1][k]);
    }

    public static void main(String[] args) {
        RMQ rmq=new RMQ(new int[]{1,2,3,4,5,6,7,-1,9,0,11,12});
        for(int[] tmp:rmq.minQuery){
            System.out.println(Arrays.toString(tmp));
        }
        System.out.println(rmq.queryMin(0,11));
        for(int[] tmp:rmq.maxQuery){
            System.out.println(Arrays.toString(tmp));
        }
        System.out.println(rmq.queryMax(0,11));
    }

}
