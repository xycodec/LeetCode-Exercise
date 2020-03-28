package com.xycode.nowcoderEX;

import org.testng.annotations.Test;

import java.util.Arrays;
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
     * 在数组中选择一个基数pivot,以这个pivot为界限,将数组分为两部分,左边的元素都小于pivot,右边的元素都大于pivot
     * 这个函数式快速排序的基础
     * @param array
     * @param s
     * @param t
     * @return
     */
    private int partition(int[] array,int s,int t){
        int l=s,r=t,pos= ThreadLocalRandom.current().nextInt(t-s+1)+s;
        int pivot=array[pos];
        swap(array,l,pos);//swap(s[l], s[pos]); //将pos位置的数和第一个数交换,就相当于每次pos位置的数为基准,因为每次必须从l开始
        while (l<r){
            while(l<r&&array[r]>pivot) --r;// 从右向左找第一个小于等于x的数 (这个必须先执行,否则会导致元素被覆盖)
            if(l<r){
                array[l]=array[r];
                ++l;//speed up
            }
            while(l<r&&array[l]<pivot) ++l;// 从左向右找第一个大于等于x的数
            if(l<r) {
                array[r]=array[l];
                --r;
            }
        }
        array[l]=pivot;
        return l;
    }
    //快速排序
    public void quickSort(int array[], int l, int r) {
        if (l < r) {
            int pos=partition(array,l,r);
            quickSort(array, l, pos - 1); // 递归调用
            quickSort(array, pos + 1, r);
        }
    }

    @Test
    public void testQuickSort() {
        int[] array = {48, 6, 57, 42, 60, 72, 83, 88, 85};
//        System.out.println(partition(array,0,array.length-1));
        quickSort(array,0,array.length-1);
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

    public void func2(Object[] array){
        for(Object o:array){
            System.out.println(o);
        }
    }
    public <T> void func1(T[] array){
        func2(array);
    }
    @Test
    public void testMergeSort() {
        int[] array = {48, 6, 57, 42, 60, 72, 83, 88, 85};
        mergeSort(array);
        for(int i=0;i<array.length;++i){
            System.out.print(array[i]+" ");
        }
        System.out.println();
        "123".intern();
//        func1(array);
    }



}
