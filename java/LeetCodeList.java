package com.xycode.leetcode;

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
}
