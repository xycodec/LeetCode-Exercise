package com.xycode.utils.cache;


import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: LRUCache
 *
 * @Author: xycode
 * @Date: 2020/4/21
 * @Description: this is description of the LRUCache class
 **/
public class LRUCache {
    LRULinkedList list;

    public LRUCache() {
    }

    public LRUCache(int capacity) {
        list=new LRULinkedList(capacity);
    }

    public int get(int key) {
        return list.get(key);
    }

    public void put(int key, int value) {
        list.put(key,value);
    }

    private class LRULinkedList{
        private class ListNode{
            int key,val;
            ListNode prev;
            ListNode next;
            private ListNode(int key,int val) {
                this.key=key;
                this.val = val;
            }
        }
        Map<Integer,ListNode> data;
        ListNode head,tail;
        int size,capacity;
        private LRULinkedList(int capacity) {
            this.capacity = capacity;
            this.data=new HashMap<>();
        }

        //notice: 头部是旧数据,尾部是新数据
        private int get(int key){
            if(!data.containsKey(key)) return -1;
            else{
                ListNode tmp=data.get(key);
                if(size==1||tmp==tail) return tmp.val;
                if(tmp==head){
                    //更新head
                    head=tmp.next;
                    head.prev=null;
                }else{
                    //获取tmp的前向节点,并解除tmp的连接关系
                    ListNode prevTmp=tmp.prev;
                    prevTmp.next=tmp.next;
                    tmp.next.prev=prevTmp;
                }

                //将tmp放到尾结点(最新)
                tail.next=tmp;
                tmp.prev=tail;
                tmp.next=null;
                tail=tmp;//更新tail
                return tail.val;
            }
        }

        //notice: 头部是旧数据,尾部是新数据
        private void put(int key,int val){
            if(data.containsKey(key)){//重复put,将更新的key移到链表末尾,size不用变
                get(key);
                tail.val=val;
            }else{
                if(size==capacity){
                    data.remove(head.key);//移除旧数据
                    head=head.next;//更新head指针
                    if(head!=null) head.prev=null;
                    else tail=null;//head==null说明capacity==1,此时head==null,没节点了,将tail也置为null
                    --size;
                }
                ListNode tmp=new ListNode(key,val);
                if(size==0){
                    head=tmp;
                }else{
                    tail.next=tmp;
                    tmp.prev=tail;
                }
                tail=tmp;//更新tail指针
                data.put(key,tmp);//存储数据
                ++size;
            }
        }
    }

    @Test
    public void testLRUCache(){
        LRUCache cache = new LRUCache(2);
        System.out.println(cache.get(2));
        cache.put(2, 6);
        System.out.println(cache.get(1));
        cache.put(1, 5);
        cache.put(1, 2);
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));
    }
}


