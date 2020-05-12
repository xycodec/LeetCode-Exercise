package com.xycode.utils.segmentTree;

import org.testng.annotations.Test;

import java.security.interfaces.RSAMultiPrimePrivateCrtKey;
import java.util.Arrays;

/**
 * ClassName: SegmentTree
 *
 * @Author: xycode
 * @Date: 2020/4/19
 * @Description: 线段树的实现,为了便于理解,不采用数组的方式,而是直接使用指针创建树
 * 关于sum的线段树,已经通过LeetCode [307. Range Sum Query - Mutable]
 **/
public class SegmentTree {
    private static class TreeNode{
        int l,r;
        long val;
        TreeNode parent;
        TreeNode lChild,rChild;

        public TreeNode(int l, int r, long val, TreeNode parent) {
            this.l = l;
            this.r = r;
            this.val = val;
            this.parent = parent;
        }
    }

    TreeNode sumRoot;
    TreeNode minRoot;
    TreeNode maxRoot;
    public SegmentTree(int[] array) {
        buildSumTree(array);
        buildMinTree(array);
        buildMaxTree(array);
    }

    /**
     * 递归构建线段树(和)
     * @param root
     * @param prefixSum
     */
    private void buildSumTreeCore(TreeNode root, long[] prefixSum){
        if(root.r==root.l) return;
        int mid=root.l+(root.r-root.l)/2;

        //root.l==0时,表明要取左半部分的和,那么直接就是prefix[mid],就不用减了
        root.lChild=new TreeNode(root.l,mid,prefixSum[mid]-(root.l==0?0:prefixSum[root.l-1]),root);
        buildSumTreeCore(root.lChild,prefixSum);
        root.rChild=new TreeNode(mid+1,root.r,prefixSum[root.r]-prefixSum[mid],root);
        buildSumTreeCore(root.rChild,prefixSum);
    }

    private void buildSumTree(int[] array){
        if(array==null||array.length==0) return;
        long[] prefixSum=new long[array.length];//前缀和数组
        prefixSum[0]=array[0];
        for(int i=1;i<array.length;++i){
            prefixSum[i]=prefixSum[i-1]+array[i];
        }

        sumRoot=new TreeNode(0,array.length-1,prefixSum[array.length-1],null);
        buildSumTreeCore(sumRoot,prefixSum);
    }

    private long querySumDfs(int l, int r, TreeNode root){
        if(root.l==l&&root.r==r) return root.val;
        long tmp=0;
        if(root.lChild.r<l) {//处于右区间
            tmp= querySumDfs(l, r, root.rChild);
        }else if(root.rChild.l>r){//处于左区间
            tmp= querySumDfs(l,r,root.lChild);
        }else{//既有左区间,又有右区间
            tmp= querySumDfs(l,root.lChild.r,root.lChild)+querySumDfs(root.rChild.l,r,root.rChild);
        }
        return tmp;
    }

    public long querySum(int l, int r){
        return querySumDfs(l,r,sumRoot);
    }

    private TreeNode getUpdateNode(int index,TreeNode root){
        if(root.l==index&&root.r==index){
            return root;
        }
        if(root.lChild.r<index) {//处于右区间
            return getUpdateNode(index,root.rChild);
        }else{//处于左区间
            return getUpdateNode(index,root.lChild);
        }
    }

    public void updateSumNode(int index, int val){
        TreeNode updateNode=getUpdateNode(index,sumRoot);
        updateNode.val=val;
        //更新线段树
        TreeNode tmpNode=updateNode.parent;
        while(tmpNode!=null){
            tmpNode.val=tmpNode.lChild.val+tmpNode.rChild.val;
            tmpNode=tmpNode.parent;
        }
    }

    /**
     * 递归构建线段树(最小值)
     * @param root
     * @param rmq
     */
    private void buildMinTreeCore(TreeNode root, RMQ rmq){
        if(root.r==root.l) return;
        int mid=root.l+(root.r-root.l)/2;
        root.lChild=new TreeNode(root.l,mid,rmq.queryMin(root.l,mid),root);
        buildMinTreeCore(root.lChild,rmq);
        root.rChild=new TreeNode(mid+1,root.r,rmq.queryMin(mid+1,root.r),root);
        buildMinTreeCore(root.rChild,rmq);
    }

    private void buildMinTree(int[] array){
        if(array==null||array.length==0) return;
        RMQ rmq=new RMQ(array);
        minRoot=new TreeNode(0,array.length-1,rmq.queryMin(0,array.length-1),null);
        buildMinTreeCore(minRoot,rmq);
    }

    public void updateMinNode(int index, int val){
        TreeNode updateNode=getUpdateNode(index,minRoot);
        updateNode.val=val;
        //更新线段树
        TreeNode tmpNode=updateNode.parent;
        while(tmpNode!=null){
            tmpNode.val=Math.min(tmpNode.lChild.val,tmpNode.rChild.val);
            tmpNode=tmpNode.parent;
        }
    }

    private long queryMinDfs(int l, int r, TreeNode root){
        if(root.l==l&&root.r==r) return root.val;
        long tmp=0;
        if(root.lChild.r<l) {//处于右区间
            tmp= queryMinDfs(l, r, root.rChild);
        }else if(root.rChild.l>r){//处于左区间
            tmp= queryMinDfs(l,r,root.lChild);
        }else{//既有左区间,又有右区间
            tmp= Math.min(queryMinDfs(l,root.lChild.r,root.lChild),queryMinDfs(root.rChild.l,r,root.rChild));
        }
        return tmp;
    }

    public long queryMin(int l, int r){
        return queryMinDfs(l,r,minRoot);
    }

    private void buildMaxTreeCore(TreeNode root, RMQ rmq){
        if(root.r==root.l) return;
        int mid=root.l+(root.r-root.l)/2;
        root.lChild=new TreeNode(root.l,mid,rmq.queryMax(root.l,mid),root);
        buildMaxTreeCore(root.lChild,rmq);
        root.rChild=new TreeNode(mid+1,root.r,rmq.queryMax(mid+1,root.r),root);
        buildMaxTreeCore(root.rChild,rmq);
    }

    private void buildMaxTree(int[] array){
        if(array==null||array.length==0) return;
        RMQ rmq=new RMQ(array);
        maxRoot=new TreeNode(0,array.length-1,rmq.queryMax(0,array.length-1),null);
        buildMinTreeCore(maxRoot,rmq);
    }

    public void updateMaxNode(int index, int val){
        TreeNode updateNode=getUpdateNode(index,maxRoot);
        updateNode.val=val;
        //更新线段树
        TreeNode tmpNode=updateNode.parent;
        while(tmpNode!=null){
            tmpNode.val=Math.max(tmpNode.lChild.val,tmpNode.rChild.val);
            tmpNode=tmpNode.parent;
        }
    }

    private long queryMaxDfs(int l, int r, TreeNode root){
        if(root.l==l&&root.r==r) return root.val;
        long tmp=0;
        if(root.lChild.r<l) {//处于右区间
            tmp= queryMaxDfs(l, r, root.rChild);
        }else if(root.rChild.l>r){//处于左区间
            tmp= queryMaxDfs(l,r,root.lChild);
        }else{//既有左区间,又有右区间
            tmp= Math.max(queryMaxDfs(l,root.lChild.r,root.lChild),queryMaxDfs(root.rChild.l,r,root.rChild));
        }
        return tmp;
    }

    public long queryMax(int l, int r){
        return queryMaxDfs(l,r,maxRoot);
    }


    public static void main(String[] args) {
        int[] array={1,3,5,7,9,11};
        SegmentTree segmentTree=new SegmentTree(array);
        System.out.println("sum(2,5): "+segmentTree.querySum(2,5));
        System.out.println("min(2,5): "+segmentTree.queryMin(2,5));
        System.out.println("max(2,5): "+segmentTree.queryMax(2,5));
        segmentTree.updateSumNode(4,3);
        segmentTree.updateMinNode(4,-1);
        segmentTree.updateMaxNode(4,100);
        System.out.println("sum(2,5): "+segmentTree.querySum(2,5));
        System.out.println("min(2,5): "+segmentTree.queryMin(2,5));
        System.out.println("max(2,5): "+segmentTree.queryMax(2,5));

    }

}

