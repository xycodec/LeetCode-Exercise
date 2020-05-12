package com.xycode.utils.heap;

import java.util.Comparator;

/**
 * ClassName: MyPriorityQueue
 *
 * @Author: xycode
 * @Date: 2020/2/29
 * @Description: this is description of the MyPriorityQueue class
 **/
public class MyPriorityQueue<T extends Comparable<T>> {
    private Object[] heap;
    private int size;
    private Comparator<T> comparator;
    public MyPriorityQueue(int capacity) {
        assert capacity>0;
        this.heap= new Object[capacity];
        this.comparator= Comparable::compareTo;//默认是最小堆
    }

    public MyPriorityQueue(int capacity, Comparator<T> comparator) {
        assert capacity>0;
        this.heap= new Object[capacity];
        this.comparator = comparator;
    }

    private void swap(int i, int j){
        T tmp= (T) heap[i];
        heap[i]=heap[j];
        heap[j]=tmp;
    }


    private void shiftUp(){
        int upIndex=size-1,parentIndex=(upIndex-1)>>1;
        while (parentIndex>=0&&comparator.compare((T)heap[parentIndex],(T)heap[upIndex])>0){
            swap(parentIndex,upIndex);
            upIndex=parentIndex;
            parentIndex=(upIndex-1)>>1;
        }
    }

    public void push(T e){
        assert size<heap.length;
        //先将数据加入到堆的末端
        heap[size++]=e;
        //尽可能把这个元素往上挪
        shiftUp();
    }

    private void shiftDown(){
        int downIndex=0;
        int mIndex=(downIndex<<1)+1;//加减法优先级高于位运算,要加括号
        while(mIndex<size){//存在左子节点
            if(mIndex+1<size){//存在右子节点
                if(comparator.compare((T)heap[mIndex],(T)heap[mIndex+1])>0) ++mIndex;//右子节点较大就选择右子节点
            }
            if(comparator.compare((T)heap[downIndex],((T)heap[mIndex]))<=0) break;
            swap(downIndex,mIndex);
            downIndex=mIndex;
            mIndex=(downIndex<<1)+1;
        }
    }

    //删除堆顶元素
    public T poll(){
        assert size>0;
        T result= (T) heap[0];
        //用末端元素覆盖堆顶元素
        heap[0]=heap[--size];
        heap[size]=null;//清空
        //尽可能把这个元素往下挪
        shiftDown();
        return result;
    }

    public T peek(){
        return (T) heap[0];
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }

    public static void main(String[] args) {
//        MyPriorityQueue<Integer> heap=new MyPriorityQueue<>(20);//最小堆,默认
        MyPriorityQueue<Integer> heap=new MyPriorityQueue<>(20,(x,y)-> -Integer.compare(x,y));//最大堆
        heap.push(4);
        heap.push(2);
        heap.push(7);
        heap.push(9);
        heap.push(1);
        heap.push(5);
        heap.push(10);
        heap.push(3);
        heap.push(2);

        while(!heap.isEmpty()){
            System.out.print(heap.poll()+" ");
        }
        System.out.println();

    }
}
