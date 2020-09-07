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
    private Node flat(Node head) {
        Node prevNode = null;
        while (head != null) {
//            System.out.println(head.val);
            if (head.child != null) {
                Node tmpNext = head.next;
                head.next = head.child;
                head.child.prev = head;

                Node tmpChildTail = flat(head.child);//return childList's tailNode
                tmpChildTail.next = tmpNext;
                if (tmpNext != null) tmpNext.prev = tmpChildTail;
                head.child = null;
            }
            prevNode = head;
            head = head.next;
        }
        return prevNode;
    }

    //430. Flatten a Multilevel Doubly Linked List
    public Node flatten(Node head) {
        if (head == null) return null;
        flat(head);
        return head;
    }


    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    //根据一个有序数组建立一个平衡BST
    TreeNode buildBST(List<Integer> inorder) {
        if (inorder.isEmpty()) return null;
        int rootIndex = inorder.size() / 2;//每次取中位数,将其作为根节点,其左边就是左子树序列,右边就是右子树序列
        TreeNode root = new TreeNode(inorder.get(rootIndex));
        if (inorder.size() == 1) {
            return root;
        }

        root.left = buildBST(inorder.subList(0, rootIndex));
        root.right = buildBST(inorder.subList(rootIndex + 1, inorder.size()));
        return root;
    }

    /**
     * <p>109. Convert Sorted List to Binary Search Tree</p>
     * 根据一个有序数组建立一个平衡BST
     *
     * @param head
     * @return
     */
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) return null;
        List<Integer> inorder = new ArrayList<>();
        ListNode tmpNode = head;
        while (tmpNode != null) {
            inorder.add(tmpNode.val);
            tmpNode = tmpNode.next;
        }
        return buildBST(inorder);
    }


    //146. LRU Cache
    class LRUCache {
        LRULinkedList list;

        public LRUCache(int capacity) {
            list = new LRULinkedList(capacity);
        }

        public int get(int key) {
            return list.get(key);
        }

        public void put(int key, int value) {
            list.put(key, value);
        }

        private class LRULinkedList {
            private class ListNode {
                int key, val;
                ListNode prev;
                ListNode next;

                private ListNode(int key, int val) {
                    this.key = key;
                    this.val = val;
                }
            }

            Map<Integer, ListNode> data;
            ListNode head, tail;
            int size, capacity;

            private LRULinkedList(int capacity) {
                this.capacity = capacity;
                this.data = new HashMap<>();
            }

            //notice: 头部是旧数据,尾部是新数据
            private int get(int key) {
                if (!data.containsKey(key)) return -1;
                else {
                    ListNode tmp = data.get(key);
                    if (size == 1 || tmp == tail) return tmp.val;
                    if (tmp == head) {
                        //更新head
                        head = tmp.next;
                        head.prev = null;
                    } else {
                        //获取tmp的前向节点,并解除tmp的连接关系
                        ListNode prevTmp = tmp.prev;
                        prevTmp.next = tmp.next;
                        tmp.next.prev = prevTmp;
                    }

                    //将tmp放到尾结点(最新)
                    tail.next = tmp;
                    tmp.prev = tail;
                    tmp.next = null;
                    tail = tmp;//更新tail
                    return tail.val;
                }
            }

            //notice: 头部是旧数据,尾部是新数据
            private void put(int key, int val) {
                if (data.containsKey(key)) {//重复put,将更新的key移到链表末尾,size不用变
                    get(key);
                    tail.val = val;
                } else {
                    if (size == capacity) {
                        data.remove(head.key);//移除旧数据
                        head = head.next;//更新head指针
                        if (head != null) head.prev = null;
                        else tail = null;//head==null说明capacity==1,此时head==null,没节点了,将tail也置为null
                        --size;
                    }
                    ListNode tmp = new ListNode(key, val);
                    if (size == 0) {
                        head = tmp;
                    } else {
                        tail.next = tmp;
                        tmp.prev = tail;
                    }
                    tail = tmp;//更新tail指针
                    data.put(key, tmp);//存储数据
                    ++size;
                }
            }
        }
    }

    @Test
    public void testLRUCache() {
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
        private class LFUNode {
            int key;
            int val;
            int count = 0;
            long version;

            private LFUNode(int key, int val) {
                this.key = key;
                this.val = val;
            }
        }

        Map<Integer, LFUNode> data = new HashMap<>();
        PriorityQueue<LFUNode> q;
        int capacity;
        long opVersion = 0;//global operation version

        public LFUCache(int capacity) {
            if (capacity <= 0) return;
            this.capacity = capacity;
            q = new PriorityQueue<>((x, y) ->
                    x.count == y.count ? Long.compare(x.version, y.version) : x.count - y.count
            );
        }

        public int get(int key) {
            if (capacity <= 0) return -1;
            else {
                LFUNode tmp = data.get(key);
                if (tmp == null) return -1;
                update(tmp, false);
                return tmp.val;
            }
        }

        public void put(int key, int value) {
            if (capacity == 0) return;
            if (data.containsKey(key)) {
                LFUNode tmp = data.get(key);
                tmp.val = value;
                update(tmp, false);
            } else {
                if (data.size() == capacity) {
                    LFUNode tmp = q.poll();
                    data.remove(tmp.key);
                }
                LFUNode node = new LFUNode(key, value);
                data.put(key, node);
                update(node, true);
            }
        }

        //方法私有化有助于提高性能, JIT更方便优化
        private void update(LFUNode node, boolean put) {
            ++opVersion;
            ++node.count;
            node.version = opVersion;
            if (!put) q.remove(node);//因为频次增加了,所以需要调整优先队列;仅当队列中没有该node时,才不需要调整而直接添加即可
            q.add(node);
        }
    }

    class LFUCacheBasedTreeSet {
        private class LFUNode {
            int key;
            int val;
            int count = 0;
            long version;

            private LFUNode(int key, int val) {
                this.key = key;
                this.val = val;
            }
        }

        Map<Integer, LFUNode> data = new HashMap<>();
        TreeSet<LFUNode> treeSet;
        int capacity;
        long opVersion = 0;//global operation version

        public LFUCacheBasedTreeSet(int capacity) {
            if (capacity <= 0) return;
            this.capacity = capacity;
            treeSet = new TreeSet<>((x, y) ->
                    x.count == y.count ? Long.compare(x.version, y.version) : x.count - y.count);
        }

        public int get(int key) {
            if (capacity <= 0) return -1;
            else {
                LFUNode tmp = data.get(key);
                if (tmp == null) return -1;
                update(tmp, false);
                return tmp.val;
            }
        }

        public void put(int key, int value) {
            if (capacity == 0) return;
            if (data.containsKey(key)) {
                LFUNode tmp = data.get(key);
                tmp.val = value;
                update(tmp, false);
            } else {
                if (data.size() == capacity) {
                    LFUNode tmp = treeSet.first();
                    treeSet.pollFirst();
                    data.remove(tmp.key);
                }
                LFUNode node = new LFUNode(key, value);
                data.put(key, node);
                update(node, true);
            }
        }

        //方法私有化有助于提高性能, JIT更方便优化
        private void update(LFUNode node, boolean put) {
            ++opVersion;
            ++node.count;
            node.version = opVersion;
            if (!put) treeSet.remove(node);//get or update operation
            treeSet.add(node);
        }
    }


    @Test
    public void testLFUCache() {
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
        if (head == null) return null;
        int len = 0;
        ListNode tmpNode = head;
        ListNode tail = head;
        while (tmpNode != null) {
            ++len;
            tail = tmpNode;
            tmpNode = tmpNode.next;
        }
        if (len == 1) return head;

        //从尾部转到首部不好在O(1)空间条件下实现,那么就反过来利用链表的next特性,反过来转
        //当k>len时,就相当于转k%len次(k<len时有k==k%len),因此就需要反过来转len-k%len次
        int cnt = len - k % len;
        for (int i = 0; i < cnt; ++i) {
            tmpNode = head;
            head = head.next;
            tmpNode.next = null;

            tail.next = tmpNode;
            tail = tail.next;
        }
        return head;
    }

    //23. Merge k Sorted Lists
    //tip 描述: 合并k个有序的链表
    //时间复杂度:
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        int k = lists.length;
        PriorityQueue<ListNode> queue = new PriorityQueue<>(k,
                Comparator.comparingInt(x -> x.val));
        ListNode head = new ListNode(-1);//dummy node
        for (int i = 0; i < k; ++i) {
            if (lists[i] != null) {
                queue.add(lists[i]);//当前节点的值 -> 下一个节点的引用
            }
        }

        ListNode tmpNode = head;
        while (!queue.isEmpty()) {
            ListNode minNode = queue.poll();//弹出一个最小值的节点
            tmpNode.next = new ListNode(minNode.val);
            tmpNode = tmpNode.next;
            if (minNode.next != null) {
                queue.add(minNode.next);//添加那个最小值节点后面的节点
            }
        }
        return head.next;
    }


    //86. Partition List
    //tip 描述: 给定一个值x, 划分一个链表,使得小于x的所有节点都在大于或等于x的节点之前,不要改变每个分区中节点的原始相对顺序
    public ListNode partition(ListNode head, int x) {
        ListNode lessNode = new ListNode(-1), greaterNode = new ListNode(-1);
        ListNode tmpNode = head, tmpLessNode = lessNode, tmpGreaterNode = greaterNode;//设置两个dummyNode,用于链接<x和>=x的节点
        while (tmpNode != null) {
            if (tmpNode.val < x) {
                tmpLessNode.next = tmpNode;
                tmpLessNode = tmpLessNode.next;
            } else {
                tmpGreaterNode.next = tmpNode;
                tmpGreaterNode = tmpGreaterNode.next;
            }
            tmpNode = tmpNode.next;
        }
        tmpGreaterNode.next = null;//后一个链表要设置终止节点,否则可能出现环
        tmpLessNode.next = greaterNode.next;//合并两个链表
        return lessNode.next;//返回合并的链表头
    }


    /**
     * <p>386. Lexicographical Numbers</p>
     * 描述: 给定一个整数n, 按字典顺序返回1-n
     *
     * @param n
     * @return
     */
    public List<Integer> lexicalOrder(int n) {
//        List<String> ans=new ArrayList<>(n);
//        for(int i=1;i<=n;++i){
//            ans.add(String.valueOf(i));
//        }
//        Collections.sort(ans);
//        return ans.parallelStream().map(Integer::parseInt).collect(Collectors.toList());

        List<Integer> ans = new ArrayList<>(n);
        for (int i = 1; i <= n; ++i) {
            ans.add(i);
        }
        Collections.sort(ans, Comparator.comparing(String::valueOf));
        return ans;
    }

    @Test
    public void testLexicalOrder() {
        System.out.println(lexicalOrder(13));
    }


    /**
     * <p>1094. Car Pooling</p>
     * 描述: 给定一个行程列表trips, trips[i]=[num_passengers，start_location，end_location],在给出车辆容量capacity,计算是否能抵达终点
     *
     * @param trips
     * @param capacity
     * @return
     */
    public boolean carPooling(int[][] trips, int capacity) {
        if (trips == null || trips.length == 0) return true;
        if (capacity < 0) return false;
        Arrays.sort(trips, Comparator.comparingInt(x -> x[1]));
        for (int i = 0; i < trips.length; ++i) {
            int tmp = trips[i][0];
            for (int j = 0; j < i; ++j) {
                if (trips[i][1] < trips[j][2]) {
                    tmp += trips[j][0];
                    if (tmp > capacity) return false;
                }
            }
        }
        return true;
    }

    @Test
    public void testCarPooling() {
        System.out.println(carPooling(new int[][]{
                {2, 1, 5}, {3, 5, 7}
        }, 4));
    }

    /**
     * <p>327. Count of Range Sum</p>
     * 描述: 给定一个整数数组nums,返回[lower，upper]中包含的子数组和的数目,子数组和定义为索引i到j（i≤j）之间的nums元素之和.
     *
     * @param nums
     * @param lower
     * @param upper
     * @return
     */
    public int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) return 0;
        long[] sums = new long[nums.length];
        sums[0] = nums[0];
        for (int i = 1; i < nums.length; i++) sums[i] = sums[i - 1] + nums[i];
        int ans = 0;
        TreeMap<Long, Integer> treeMap = new TreeMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (lower <= sums[i] && sums[i] <= upper) ans++;
            for (Integer count : treeMap.subMap(sums[i] - upper, true, sums[i] - lower, true).values()) {
                ans += count;
            }
            treeMap.put(sums[i], treeMap.getOrDefault(sums[i], 0) + 1);
        }
        return ans;
    }

    @Test
    public void testCountRangeSum() {
        System.out.println(countRangeSum(new int[]{0, 0}, 0, 0));
    }

    /**
     * <p>1353. Maximum Number of Events That Can Be Attended</p>
     * 描述: 给定一个活动安排的列表,每个活动有开始时间与结束时间,events[i]={startDay_i,endDay_i},某天只能参与一个活动,
     * 计算最多能参加多少个活动(需完整参与整个活动)
     *
     * @param events
     * @return
     */
    public int maxEvents(int[][] events) {
        if (events == null || events.length == 0) return 0;
        Arrays.sort(events, Comparator.comparing(x -> x[1]));
        boolean[] uncheck = new boolean[100001];
        int ans = 0;
        for (int[] event : events) {
            for (int i = event[0]; i <= event[1]; ++i) {
                if (!uncheck[i]) {
                    ++ans;
                    uncheck[i] = true;
                    break;
                }
            }
        }
        return ans;
    }

    @Test
    public void testMaxEvents() {
        System.out.println(maxEvents(new int[][]{
                {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}
        }));
    }
}
