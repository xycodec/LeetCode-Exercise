package com.xycode.utils.heap;


/**
 * ClassName: MinHeap
 *
 * @Author: xycode
 * @Date: 2020/2/28
 * @Description: this is description of the MinHeap class
 **/
public class MaxHeap {
    private int[] heap;
    private int size;

    public MaxHeap(int capacity) {
        assert capacity > 0;
        this.heap = new int[capacity];
    }

    private void swap(int i, int j) {
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }


    private void shiftUp() {
        int upIndex = size - 1, parentIndex = (upIndex - 1) >> 1;
        while (parentIndex >= 0 && heap[parentIndex] < heap[upIndex]) {
            swap(parentIndex, upIndex);
            upIndex = parentIndex;
            parentIndex = (upIndex - 1) >> 1;
        }
    }

    public void push(int e) {
        assert size < heap.length;
        //先将数据加入到堆的末端
        heap[size++] = e;
        //尽可能把这个元素往上挪
        shiftUp();
    }

    private void shiftDown() {
        int downIndex = 0;
        int mIndex = (downIndex << 1) + 1;//加减法优先级高于位运算,要加括号
        while (mIndex < size) {//存在左子节点
            if (mIndex + 1 < size) {//存在右子节点
                if (heap[mIndex] < heap[mIndex + 1]) ++mIndex;//右子节点较大就选择右子节点
            }
            if (heap[downIndex] >= heap[mIndex]) break;
            swap(downIndex, mIndex);
            downIndex = mIndex;
            mIndex = (downIndex << 1) + 1;
        }
    }

    //删除堆顶元素
    public int poll() {
        assert size > 0;
        int result = heap[0];
        //用末端元素覆盖堆顶元素
        heap[0] = heap[--size];
        heap[size] = 0;//清空
        //尽可能把这个元素往下挪
        shiftDown();
        return result;
    }

    public int peek() {
        return heap[0];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }


    public static void main(String[] args) {
        MaxHeap heap = new MaxHeap(20);
        heap.push(4);
        heap.push(2);
        heap.push(7);
        heap.push(9);
        heap.push(1);
        heap.push(5);
        heap.push(10);
        heap.push(3);
        heap.push(2);

        while (!heap.isEmpty()) {
            System.out.println(heap.poll());
        }

    }

}
