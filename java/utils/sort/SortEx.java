package com.xycode.utils.sort;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ClassName: SortEx
 *
 * @Author: xycode
 * @Date: 2020/3/24
 * @Description: this is description of the SortEx class
 **/
public class SortEx {
    private void swap(int[] array,int i,int j){
        int tmp=array[i];
        array[i]=array[j];
        array[j]=tmp;
    }

    /**
     * 选择排序
     * @param array
     */
    public void selectSort(int[] array){
        for(int i=0;i<array.length;++i){
            int minIndex=i;
            for(int j=i+1;j<array.length;++j){
                if(array[minIndex]>array[j]){//选择过程
                    minIndex=j;
                }
            }
            if(minIndex!=i){
                swap(array,i,minIndex);
            }
//            System.out.println(Arrays.toString(array));//tip: 展示排序过程
        }
    }

    @Test
    public void testSelectSort() {
        int[] array = {48, 6, 57, 42, 60, 72, 83, 88, 85,1,0};
        selectSort(array);
        for(int i=0;i<array.length;++i){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }

    /**
     * 冒泡排序
     * @param array
     */
    public void bubbleSort(int[] array){
        for(int i=0;i<array.length;++i){
            for(int j=0;j<array.length-i-1;++j){
                if(array[j]>array[j+1]){//冒泡过程
                    swap(array,j,j+1);
                }
            }
//            System.out.println(Arrays.toString(array));//tip: 展示排序过程
        }
    }

    @Test
    public void testBubbleSort() {
        int[] array = {48, 6, 57, 42, 60, 72, 83, 88, 85,1,0};
        bubbleSort(array);
        for(int i=0;i<array.length;++i){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }

    /**
     * 插入排序,在数组基本有序的情况下很高效
     * @param array
     */
    public void insertSort(int[] array){
        for(int i=1;i<array.length;++i){
            if(array[i-1]>array[i]){
                int tmp=array[i];
                int j=i-1;
                while(j>=0&&tmp<array[j]){//插入过程,把小的数放到前面
                    array[j+1]=array[j];
                    --j;
                }
                array[j+1]=tmp;
                System.out.println(Arrays.toString(array));//tip: 展示排序过程
            }
        }
    }

    @Test
    public void testInsertSort() {
        int[] array = {18,23,19,9,23,15};
        System.out.println(Arrays.toString(array));
        insertSort(array);
        for (int i = 0; i < array.length; ++i) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

    /**
     * 希尔排序,利用插入排序在数组基本有序时非常高效这一特点,使用增量序列使数组逐渐有序
     * 时间复杂度介于O(N*logN)与O(N^2) 之间
     * @param array
     */
    public void shellSort(int[] array){
        for(int gap=array.length>>1;gap>=1;gap>>=1){//增量序列,每次保证数组中间隔为gap的数字符合顺序,一直到gap=1,整体就有序了
            for(int start=gap;start<array.length;++start){

                if(array[start-gap]>array[start]){
                    int tmp=array[start];
                    int k=start-gap;
                    while(k>=0&&tmp<array[k]){
                        array[k+gap]=array[k];
                        k-=gap;
                    }
                    array[k+gap]=tmp;
                    System.out.println(Arrays.toString(array));//tip: 展示排序过程
                }
            }
        }
    }

    @Test
    public void testShellSort() {
        int[] array = {4, 6, 7, 2, 9, 3, 8, 5, 1, 0, 1, 1};
        shellSort(array);
        for (int i = 0; i < array.length; ++i) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

    /**
     * 在数组中选择一个基数pivot,以这个pivot为界限,将数组分为两部分,左边的元素都小于pivot,右边的元素都大于pivot
     * 这个函数是快速排序的基础
     * @param array
     * @param s
     * @param t
     * @return
     */
    private int partition(int[] array,int s,int t){
        int l=s,r=t,pos=ThreadLocalRandom.current().nextInt(t-s+1)+s;//随机选择一个轴点
        int pivot=array[pos];
        swap(array,l,pos);//swap(s[l], s[pos]); //将pos位置的数和第一个数交换,就相当于每次pos位置的数为基准,因为每次必须从l开始
        while (l<r){
            while(l<r&&array[r]>pivot) --r;// 从右向左找第一个小于等于x的数 (这个必须先执行,否则会导致元素被覆盖)
            if(l<r){
                array[l]=array[r];
                ++l;//speed up
            }
            while(l<r&&array[l]<pivot) ++l;//从左向右找第一个大于等于x的数
            if(l<r) {
                array[r]=array[l];
                --r;
            }
        }
        array[l]=pivot;
        return l;
    }

    /**
     * 快速排序
     * @param array
     * @param l
     * @param r
     */
    public void quickSortCore(int array[], int l, int r) {
        if (l < r) {
            int pos=partition(array,l,r);
            quickSortCore(array, l, pos - 1); // 递归调用
            quickSortCore(array, pos + 1, r);
        }
    }

    public void quickSort(int[] array){
        quickSortCore(array,0,array.length-1);
    }

    @Test
    public void testQuickSort() {
        int[] array = {48, 6, 57, 42, 60, 72, 83, 88, 85};
//        System.out.println(partition(array,0,array.length-1));
        quickSort(array);
        for(int i=0;i<array.length;++i){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }

    /**
     * 归并排序
     * @param array
     * @param s
     * @param t
     * @param tmp 该数组用于接收已排序数组的合并,当做参数来传递比较省空间(不用多次开辟数组空间)
     */
    private void mergeSortCore(int[] array,int s,int t,int[] tmp){
        if(s<t){
            int mid=(s+t)/2;
            mergeSortCore(array,s,mid,tmp);
            mergeSortCore(array,mid+1,t,tmp);
            //合并两个有序子数组
            int i=s,j=mid+1,cnt=0;
            while(i<=mid||j<=t){
                if(j>t){
                    tmp[cnt++]=array[i++];
                }else if(i>mid){
                    tmp[cnt++]=array[j++];
                    //tip: 以上是越界判断
                }else if(array[i]<array[j]){//真正的比较,从小到大
                    tmp[cnt++]=array[i++];
                }else{
                    tmp[cnt++]=array[j++];
                }
            }
            //复制排序好的数组到原始数组array中
            cnt=0;
            while (s<=t){
                array[s++]=tmp[cnt++];
            }
        }
    }

    public void mergeSort(int[] array){
        mergeSortCore(array,0,array.length-1,new int[array.length]);
    }

    @Test
    public void testMergeSort() {
        int[] array = {48, 6, 57, 42, 60, 72, 83, 88, 85};
        mergeSort(array);
        for(int i=0;i<array.length;++i){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }


}
