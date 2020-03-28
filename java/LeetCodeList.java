package com.xycode.leetcode;

import org.testng.annotations.Test;

import java.util.*;

/**
 * ClassName: LeetCodeList
 *
 * @Author: xycode
 * @Date: 2019/12/30
 * @Description: this is description of the LeetCodeList class
 **/
public class LeetCodeList {
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    }

    //tip: 方法一: 不太优雅
//    private Node flat(Node head){
//        if(head.next==null&&head.child!=null){
//            head.next=head.child;
//            head.child.prev=head;
//
//            Node tmpChildTail=flat(head.child);
//            tmpChildTail.next=null;
//            head.child=null;
//        }
//        while(head.next!=null){
//            System.out.println(head.val);
//            if(head.child!=null){
//                Node tmpNext=head.next;
//                head.next=head.child;
//                head.child.prev=head;
//
//                Node tmpChildTail=flat(head.child);
//                tmpChildTail.next=tmpNext;
//                tmpNext.prev=tmpChildTail;
//                head.child=null;
//            }
//            head=head.next;
//        }
//        return head;
//    }

    //方法2
    private Node flat(Node head){
        Node prevNode=null;
        while(head!=null){
//            System.out.println(head.val);
            if(head.child!=null){
                Node tmpNext=head.next;
                head.next=head.child;
                head.child.prev=head;

                Node tmpChildTail=flat(head.child);//return childList's tailNode
                tmpChildTail.next=tmpNext;
                if(tmpNext!=null) tmpNext.prev=tmpChildTail;
                head.child=null;
            }
            prevNode=head;
            head=head.next;
        }
        return prevNode;
    }

    //430. Flatten a Multilevel Doubly Linked List
    public Node flatten(Node head) {
        if(head==null) return null;
        flat(head);
        return head;
    }



    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    //根据一个有序数组建立一个平衡BST
    TreeNode buildBST(List<Integer> inorder){
        if(inorder.isEmpty()) return null;
        int rootIndex=inorder.size()/2;//每次取中位数,将其作为根节点,其左边就是左子树序列,右边就是右子树序列
        TreeNode root=new TreeNode(inorder.get(rootIndex));
        if(inorder.size()==1){
            return root;
        }

        root.left=buildBST(inorder.subList(0,rootIndex));
        root.right=buildBST(inorder.subList(rootIndex+1,inorder.size()));
        return root;
    }

    //109. Convert Sorted List to Binary Search Tree
    public TreeNode sortedListToBST(ListNode head){
        if(head==null) return null;
        List<Integer> inorder=new ArrayList<>();
        ListNode tmpNode=head;
        while(tmpNode!=null){
            inorder.add(tmpNode.val);
            tmpNode=tmpNode.next;
        }
        return buildBST(inorder);
    }


    //146. LRU Cache
    class LRUCache {
        LRULinkedList list;
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


    //460. LFU Cache
    class LFUCache {
        private class LFUNode{
            int key;
            int val;
            int count=0;
            long version;
            private LFUNode(int key,int val) {
                this.key=key;
                this.val = val;
            }
        }
        Map<Integer,LFUNode> data=new HashMap<>();
        PriorityQueue<LFUNode> q;
        int capacity;
        long opVersion=0;//global operation version
        public LFUCache(int capacity) {
            if(capacity<=0) return;
            this.capacity=capacity;
            q=new PriorityQueue<>((x,y)->
                x.count==y.count?Long.compare(x.version,y.version):x.count-y.count
            );
        }

        public int get(int key) {
            if(capacity<=0) return -1;
            else{
                LFUNode tmp=data.get(key);
                if(tmp==null) return -1;
                update(tmp,false);
                return tmp.val;
            }
        }

        public void put(int key, int value) {
            if(capacity==0) return;
            if(data.containsKey(key)){
                LFUNode tmp=data.get(key);
                tmp.val=value;
                update(tmp,false);
            }else{
                if(data.size()==capacity){
                    LFUNode tmp=q.poll();
                    data.remove(tmp.key);
                }
                LFUNode node=new LFUNode(key,value);
                data.put(key,node);
                update(node,true);
            }
        }

        //方法私有化有助于提高性能, JIT更方便优化
        private void update(LFUNode node,boolean put){
            ++opVersion;
            ++node.count;
            node.version=opVersion;
            if(!put) q.remove(node);//因为频次增加了,所以需要调整优先队列;仅当队列中没有该node时,才不需要调整而直接添加即可
            q.add(node);
        }
    }

    class LFUCacheBasedTreeSet {
        private class LFUNode{
            int key;
            int val;
            int count=0;
            long version;
            private LFUNode(int key,int val) {
                this.key=key;
                this.val = val;
            }
        }
        Map<Integer,LFUNode> data=new HashMap<>();
        TreeSet<LFUNode> treeSet;
        int capacity;
        long opVersion=0;//global operation version
        public LFUCacheBasedTreeSet(int capacity) {
            if(capacity<=0) return;
            this.capacity=capacity;
            treeSet=new TreeSet<>((x,y)->
                    x.count==y.count?Long.compare(x.version,y.version):x.count-y.count);
        }

        public int get(int key) {
            if(capacity<=0) return -1;
            else{
                LFUNode tmp=data.get(key);
                if(tmp==null) return -1;
                update(tmp,false);
                return tmp.val;
            }
        }

        public void put(int key,int value) {
            if(capacity==0) return;
            if(data.containsKey(key)){
                LFUNode tmp=data.get(key);
                tmp.val=value;
                update(tmp,false);
            }else{
                if(data.size()==capacity){
                    LFUNode tmp=treeSet.first();
                    treeSet.pollFirst();
                    data.remove(tmp.key);
                }
                LFUNode node=new LFUNode(key,value);
                data.put(key,node);
                update(node,true);
            }
        }

        //方法私有化有助于提高性能, JIT更方便优化
        private void update(LFUNode node,boolean put){
            ++opVersion;
            ++node.count;
            node.version=opVersion;
            if(!put) treeSet.remove(node);//get or update operation
            treeSet.add(node);
        }
    }


    @Test
    public void testLFUCache(){
//        LFUCache cache = new LFUCache(3);
//        cache.put(2, 2);
//        cache.put(1, 1);
//        System.out.println(cache.get(2));
//        System.out.println(cache.get(1));
//        System.out.println(cache.get(2));
//        cache.put(3, 3);
//        cache.put(4, 4);
//        System.out.println(cache.get(3));
//        System.out.println(cache.get(2));
//        System.out.println(cache.get(1));
//        System.out.println(cache.get(4));

        LFUCacheBasedTreeSet cacheBasedTreeSet = new LFUCacheBasedTreeSet(3);
        cacheBasedTreeSet.put(2, 2);
        cacheBasedTreeSet.put(1, 1);
        System.out.println(cacheBasedTreeSet.get(2));
        System.out.println(cacheBasedTreeSet.get(1));
        System.out.println(cacheBasedTreeSet.get(2));
        cacheBasedTreeSet.put(3, 3);
        cacheBasedTreeSet.put(4, 4);
        System.out.println(cacheBasedTreeSet.get(3));
        System.out.println(cacheBasedTreeSet.get(2));
        System.out.println(cacheBasedTreeSet.get(1));
        System.out.println(cacheBasedTreeSet.get(4));
    }

    //61. Rotate List
    public ListNode rotateRight(ListNode head, int k) {
        if(head==null) return null;
        int len=0;
        ListNode tmpNode=head;
        ListNode tail=head;
        while(tmpNode!=null){
            ++len;
            tail=tmpNode;
            tmpNode=tmpNode.next;
        }
        if(len==1) return head;

        //从尾部转到首部不好在O(1)空间条件下实现,那么就反过来利用链表的next特性,反过来转
        //当k>len时,就相当于转k%len次(k<len时有k==k%len),因此就需要反过来转len-k%len次
        int cnt=len-k%len;
        for(int i=0;i<cnt;++i){
            tmpNode=head;
            head=head.next;
            tmpNode.next=null;

            tail.next=tmpNode;
            tail=tail.next;
        }
        return head;
    }

}
