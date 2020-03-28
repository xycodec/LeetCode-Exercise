package com.xycode.nowcoderEX;

import org.testng.annotations.Test;
import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: CaptureOffer
 *
 * @Author: xycode
 * @Date: 2020/2/26
 * @Description: this is description of the CaptureOffer class
 **/
public class CaptureOffer {
    //链表节点定义
    class ListNode {
        int val;
        ListNode next = null;
        ListNode(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }

    //二叉树节点定义
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    //二叉树节点定义(带父节点指针)
    class TreeLinkNode {
        int val;
        TreeLinkNode left = null;
        TreeLinkNode right = null;
        TreeLinkNode next = null;

        TreeLinkNode(int val) {
            this.val = val;
        }
    }

    //复杂链表的定义
    class RandomListNode {
        int label;
        RandomListNode next = null;
        RandomListNode random = null;

        RandomListNode(int label) {
            this.label = label;
        }
    }


    // 1.二维数组中的查找
    public boolean Find(int target, int [][] array) {
        if(array==null||array[0].length==0) return false;
        int m=0,n=array[0].length-1;
        while (m<array.length&&n>=0){
            if(array[m][n]==target) return true;
            else if(array[m][n]>target) --n;
            else if(array[m][n]<target) ++m;
        }
        return false;
    }

    @Test
    public void testFind(){
        int[][] array={
                {1,2,3,4},
                {7,8,9,10},
                {17,18,19,20},
                {27,28,29,30}
        };
        System.out.println(Find(31,array));

    }

    //2. 替换空格
    public String replaceSpace(StringBuffer str) {
        //tip: 自实现的replace
        char[] tmp=str.toString().toCharArray();
        int cnt=0;
        for(int i=0;i<tmp.length;++i){
            if(tmp[i]==' ') ++cnt;
        }
        char[] result=new char[cnt*2+tmp.length];
        int j=result.length-1;
        for(int i=tmp.length-1;i>=0;--i){
            if(tmp[i]==' '){
                result[j--]='0';
                result[j--]='2';
                result[j--]='%';
            }else{
                result[j--]=tmp[i];
            }
        }
        return new String(result);

        //tip: 调用系统的replaceAll()
////        System.out.println(str);
//        return tmp.replaceAll(" ","%20");
    }

    @Test
    public void testReplaceSpace(){
        System.out.println(replaceSpace(new StringBuffer("hello world")));
    }


    // 3.从尾到头打印链表
    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        ArrayList<Integer> result=new ArrayList<>();
        ListNode tmpNode=listNode;
        while(tmpNode!=null){
            result.add(tmpNode.val);
            tmpNode=tmpNode.next;
        }
        Collections.reverse(result);
        return result;
    }


    private TreeNode buildTree(List<Integer> pre, List<Integer> in){
        if(pre==null||pre.size()==0) return null;
        int rootValue=pre.get(0);
        if(pre.size()==1&&in.size()==1){
            if(!pre.get(0).equals(in.get(0))) throw new IllegalArgumentException("前序与中序序列不匹配");
            return new TreeNode(rootValue);
        }
        //前序遍历的第一个元素是根节点元素, 以此找到中序遍历中根节点的索引
        int rootIndex=0;
        while (rootIndex<in.size()&&in.get(rootIndex)!=rootValue) ++rootIndex;
        //中序遍历中根节点索引的左边就是左子树,右边就是右子树,两边子树是与前序遍历一一对应的
        TreeNode rootNode=new TreeNode(rootValue);
        //rootIndex==0,说明没有左子树
        if(rootIndex>0) rootNode.left=buildTree(pre.subList(1,rootIndex+1),in.subList(0,rootIndex));
        //rootIndex==in.size(),说明没有右子树
        if(rootIndex<in.size()) rootNode.right=buildTree(pre.subList(rootIndex+1,pre.size()),in.subList(rootIndex+1,in.size()));
        return rootNode;
    }

    // 4.重建二叉树
    public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        if(pre.length==0||in.length==0||pre.length!=in.length) return null;
        List<Integer> preList=new ArrayList<>();
        List<Integer> inList=new ArrayList<>();
        for(int i=0;i<pre.length;++i){
            preList.add(pre[i]);
            inList.add(in[i]);
        }
        return buildTree(preList,inList);
    }

    // 5.用两个栈实现队列
    Stack<Integer> stack1 = new Stack<>();
    Stack<Integer> stack2 = new Stack<>();

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() throws Exception {
        if(stack1.empty()) throw new Exception("queue is empty");
        while(!stack1.empty()){
            stack2.push(stack1.pop());
        }
        int result=stack2.pop();
        while(!stack2.empty()){
            stack1.push(stack2.pop());
        }
        return result;
    }

    @Test
    public void testStackQueue() throws Exception {
        push(1);
        push(2);
        push(3);
        push(4);
        System.out.println(pop());
        System.out.println(pop());
        System.out.println(pop());
        System.out.println(pop());
    }

    // 6.旋转数组的最小数字
    public int minNumberInRotateArray(int [] array) {
        if(array==null||array.length==0) throw new IllegalArgumentException("IllegalArgument: array");
        if(array.length==1||array[0]<array[array.length-1]) return array[0];
        int l=0,r=array.length-1;
        int mid=(l+r)/2;
        while(array[l]>=array[r]){
            if(r-l==1){//只有两个值了
                mid=r;// array[l]>=array[r]
                break;
            }
            if(array[l]==array[mid]&&array[mid]==array[r]) {//eg: 1,1,1,0,1
                int tmp=array[l];
                for(int i=l+1;i<r;++i){//eg: 1,1,1,1,1
                    tmp=Math.min(tmp, array[i]);
                }
                return tmp;
            }
            if(array[mid]>=array[l]){//左半边有序(l ~ mid)
                l=mid;
            }else{//右半边有序(mid ~ r)
                r=mid;
            }
            mid=(l+r)/2;
        }
        return array[mid];
    }

    @Test
    public void testMinNumberInRotateArray(){
        int[] array={6501,6828,6963,7036,7422,7674,8146,8468,8704,8717,9170,9359,9719,9895,9896,9913,9962,154,293,334,492,1323,1479,1539,1727,1870,1943,2383,2392,2996,3282,3812,3903,4465,4605,4665,4772,4828,5142,5437,5448,5668,5706,5725,6300,6335};
        System.out.println(minNumberInRotateArray(array));
    }

    // 7.斐波那契数列
    public int Fibonacci(int n) {
        if(n<=1) return n;
        int[] ans=new int[n+1];
        ans[0]=0;
        ans[1]=1;
        for(int i=2;i<=n;++i){
            ans[i]=ans[i-1]+ans[i-2];
        }
        return ans[n];
    }

    // 8.跳台阶
    public int JumpFloor(int target) {
        if(target<=0) return 0;
        int[] dp=new int[target+2];
        dp[1]=1;
        dp[2]=2;
        for(int i=3;i<=target;++i){
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[target];
    }

    // 9.变态跳台阶
    public class Solution9 {
        public int JumpFloorII(int target) {
            if(target<=0) return 0;
            int[] dp=new int[target+2];
            dp[1]=1;
            dp[2]=2;
            for(int i=3;i<=target;++i){
                dp[i]=1;//直接跳N个台阶上来,只有一种,这里需要加上,别忘了
                for(int j=1;j<=i;++j){
                    dp[i]+=dp[i-j];
                }
            }
            return dp[target];
        }
    }

    // 10.矩形覆盖
    public int RectCover(int target) {
        if(target<=2) return target;
        int[] dp=new int[target+1];
        dp[1]=1;
        dp[2]=2;
        for(int i=3;i<=target;++i){
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[target];
    }

    // 11.二进制中1的个数
    public int NumberOf1(int n) {
        if(n==0) return 0;
        int result=0;
        while(n!=0){
            n&=n-1;//trick: 将n最低位的1变为0
            ++result;
        }
        return result;
    }

    // 12.数值的整数次方
    //tip: 快速幂
    public double Power(double base, int exponent) {
        if(base==0||base==1||exponent==1) return base;
        if(exponent==0) return 1;
        if(exponent>0){
            double result=1;
            while (exponent!=0){
                if((exponent&1)==1) result*=base;
                base*=base;
                exponent>>=1;
            }
            return result;
        }else{
            return 1/Power(base,-exponent);
        }
    }

    // 13.调整数组顺序使奇数位于偶数前面
    public void reOrderArray(int [] array) {
        if(array==null||array.length<=1) return;
        int len=array.length;
        int[] tmp=Arrays.copyOf(array,len);
        int cnt=0;
        for(int i=0;i<len;++i){
            if(tmp[i]%2==1) array[cnt++]=tmp[i];
        }
        for(int i=0;i<len;++i){
            if(tmp[i]%2==0) array[cnt++]=tmp[i];
        }
    }

    @Test
    public void testReOrderArray() {
        int[] array={6,1,2,3,4,5};
        reOrderArray(array);
        System.out.println(Arrays.toString(array));
    }

    // 14.链表中倒数第k个结点
    public ListNode FindKthToTail(ListNode head,int k) {
        if(head==null||k<=0) return null;
        int len=0;
        ListNode tmpNode=head;
        while(tmpNode!=null){
            ++len;
            tmpNode=tmpNode.next;
        }
        if(k>len) return null;
        tmpNode=head;
        int cnt=len-k;
        while (tmpNode!=null&&cnt!=0){
            --cnt;
            tmpNode=tmpNode.next;
        }
        return tmpNode;
    }

    // 15.反转链表
    public ListNode ReverseList(ListNode head) {
        if(head==null) return null;
        if(head.next==null) return head;
        ListNode pNode=head,qNode=head.next;
        ListNode tmpNode;
        while(qNode!=null){
            tmpNode=qNode.next;//暂存
            if(pNode==head) pNode.next=null;//若是头结点就需要将next置为null
            qNode.next=pNode;//反转

            //迭代
            pNode=qNode;
            qNode=tmpNode;
        }
        return pNode;
    }

    // 16.合并两个排序的链表
    //tip: 不修改传入的参数,空间复杂度O(N)
//    public ListNode Merge(ListNode list1,ListNode list2) {
//        if(list1==null&&list2==null) return null;
//        if(list1==null) return list2;
//        if(list2==null) return list1;
//
//        ListNode pNode=list1,qNode=list2;
//        ListNode tmpNode;
//        if(list1.val<list2.val){
//            tmpNode=new ListNode(list1.val);
//            pNode=pNode.next;
//        }else{
//            tmpNode=new ListNode(list2.val);
//            qNode=qNode.next;
//        }
//        ListNode result=tmpNode;//暂存
//        while(pNode!=null||qNode!=null){
//            if(pNode==null&&qNode!=null){//只剩qNode有值
//                tmpNode.next=new ListNode(qNode.val);
//                qNode=qNode.next;
//            }else if(pNode!=null&&qNode==null){//只剩pNode有值
//                tmpNode.next=new ListNode(pNode.val);
//                pNode=pNode.next;
//            }else{//pNode与qNode都有值
//                if(pNode.val<qNode.val){
//                    tmpNode.next=new ListNode(pNode.val);
//                    pNode=pNode.next;
//                }else{
//                    tmpNode.next=new ListNode(qNode.val);
//                    qNode=qNode.next;
//                }
//            }
////            System.out.println(tmpNode.val);
//            tmpNode=tmpNode.next;
//        }
//        return result;
//    }
    //tip: 修改传入的参数,空间复杂度O(1)
    public ListNode Merge(ListNode list1,ListNode list2) {
        ListNode newHead = new ListNode(-1);
        ListNode current = newHead;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        if (list1 != null) current.next = list1;
        if (list2 != null) current.next = list2;
        return newHead.next;
    }
    @Test
    public void testMerge() {
        ListNode list1=new ListNode(1),list2=new ListNode(2);
        list1.next=new ListNode(3);list1.next.next=new ListNode(5);
        list2.next=new ListNode(4);list2.next.next=new ListNode(6);
        Merge(list1,list2);
    }


    private boolean subtreeMatch(TreeNode root1, TreeNode root2){
        if(root1==null&&root2==null) return true;
        if(root1!=null&&root2==null) return true;//root1代表的子树结构较大,nowCoder OJ允许这种情况(LeetCode上不允许)
        if(root1==null&&root2!=null) return false;//root2代表的子树结构过大
        if(root1.val==root2.val){
            return subtreeMatch(root1.left,root2.left)&&subtreeMatch(root1.right,root2.right);
        }else return false;
    }

    private boolean subtreeTraversal(TreeNode root1,TreeNode root2){
        if(root1==null||root2==null) return false;
        if(root1.val==root2.val){//值相等,則开始比对
            if(subtreeMatch(root1,root2)) return true;
            //return subtreeMatch(root1,root2);
            // warn: 这样写是不对的,因为若一开始值匹配,但是子树不匹配,结果会直接返回false,
            //      但实际上子树的子树可能存在匹配的结构, 所以只能在完全匹配的时候才返回true
        }
        //值不相等,就继续比较左右子节点
        return subtreeTraversal(root1.left,root2)||subtreeTraversal(root1.right,root2);
    }
    // 17.树的子结构
    public boolean HasSubtree(TreeNode root1,TreeNode root2) {
        if(root1==null||root2==null) return false;
        return subtreeTraversal(root1,root2);
    }

    private void MirrorDfs(TreeNode root){
        if(root==null) return;
        if(root.left!=null&&root.right!=null){//交换左右子节点指针
            TreeNode tmpNode=root.left;
            root.left=root.right;
            root.right=tmpNode;
        }else if(root.left==null&&root.right!=null){//只有右子节点
            root.left=root.right;
            root.right=null;
        }else if(root.left!=null&&root.right==null){//只有左子节点
            root.right=root.left;
            root.left=null;
        }else return;//左右子节点都为null
        //继续镜像左右子树
        MirrorDfs(root.left);
        MirrorDfs(root.right);
    }
    // 18.二叉树的镜像
    public void Mirror(TreeNode root) {
        MirrorDfs(root);
    }


    enum Direction{
        up,down,left,right
    }
    // 19.顺时针打印矩阵
    public ArrayList<Integer> printMatrix(int [][] matrix) {
        ArrayList<Integer> ans=new ArrayList<>();
        if(matrix==null||matrix.length==0||matrix[0].length==0) return ans;
        int m=matrix.length,n=matrix[0].length;
        int i=0,j=0;
        boolean[][] vis=new boolean[m][n];
        Direction d=Direction.right;
        //状态机方法,注意顺时针的状态转换: right -> down -> left -> up -> right
        while(ans.size()!=m*n){
//            System.out.println(i+", "+j);
            if(d.equals(Direction.right)){
                if(j<n&&!vis[i][j]){
                    vis[i][j]=true;
                    ans.add(matrix[i][j++]);
                }else{//状态转移并回退(因为越界了或者重复访问)
                    --j;
                    ++i;
                    d=Direction.down;
                }
            }else if(d.equals(Direction.down)){
                if(i<m&&!vis[i][j]){
                    vis[i][j]=true;
                    ans.add(matrix[i++][j]);
                }else{
                    --i;
                    --j;
                    d=Direction.left;
                }
            }else if(d.equals(Direction.left)){
                if(j>=0&&!vis[i][j]){
                    vis[i][j]=true;
                    ans.add(matrix[i][j--]);
                }else{
                    ++j;
                    --i;
                    d=Direction.up;
                }
            }else if(d.equals(Direction.up)){
                if(i>=0&&!vis[i][j]){
                    vis[i][j]=true;
                    ans.add(matrix[i--][j]);
                }else{
                    ++i;
                    ++j;
                    d=Direction.right;
                }
            }
        }
        return ans;
    }

    @Test
    public void testPrintMatrix() {
        System.out.println(printMatrix(new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}
        }));
    }


    // 20.包含min函数的栈
    class MinStack {
        //min栈用于记录data栈中的最小值,其size与data栈保持同步,栈顶的元素就是当前data栈中的最小值
        private Stack<Integer> data=new Stack<>();
        private Stack<Integer> min=new Stack<>();

        public void push(int node) {
            if(data.empty()){
                min.push(node);
            }else{
                if(node<min.peek()){
                    min.push(node);
                }else{
                    min.push(min.peek());
                }
            }
            data.push(node);
        }

        public void pop() {
            data.pop();
            min.pop();
        }

        public int top() {
            return data.peek();
        }

        public int min() {
            return min.peek();
        }

        public boolean empty(){
            return data.empty();
        }
    }

    @Test
    public void testMinStack() {
        MinStack minStack=new MinStack();
        minStack.push(5);
        minStack.push(2);
        minStack.push(6);
        minStack.push(3);
        while(!minStack.empty()){
            System.out.println(minStack.min());
            minStack.pop();
        }
    }


    // 21.栈的压入、弹出序列
    public boolean IsPopOrder(int [] pushA,int [] popA) {
        if(pushA==null||popA==null||pushA.length!=popA.length) return false;
        int len=pushA.length;
        Stack<Integer> stack=new Stack<>();
        int i=0,j=0;//i是popA数组的指针(popA是需要匹配的), j是pushA数组的指针
        while(i<len){
            if(stack.empty()) stack.push(pushA[j++]);//处理栈为空的情况,这时简单地添加一个元素,方便下面的比较
            while(popA[i]!=stack.peek()){
                if(j>=len) return false;
                stack.push(pushA[j++]);//没找到匹配的,就把当前的值放到栈中
            }
            stack.pop();//找到匹配的了,并且上面已经j++,所以这里只需把栈头匹配到的元素弹出即可
            ++i;//继续匹配popA数组的下一个值
        }
        return true;
    }

    @Test
    public void testIsPopOrder() {
        System.out.println(IsPopOrder(new int[]{1,2,3,4,5},new int[]{4,5,3,2,1}));
        System.out.println(IsPopOrder(new int[]{1,2,3,4,5},new int[]{4,3,5,1,2}));
    }

    // 22.从上往下打印二叉树
    public ArrayList<Integer> PrintFromTopToBottom(TreeNode root) {
        ArrayList<Integer> ans=new ArrayList<>();
        if(root==null) return ans;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            TreeNode tmpNode=queue.poll();
            if(tmpNode.left!=null) queue.add(tmpNode.left);
            if(tmpNode.right!=null) queue.add(tmpNode.right);
            ans.add(tmpNode.val);
        }
        return ans;
    }

    private boolean squenceDfs(List<Integer> list){
        int len=list.size();
        if(len<=1) return true;
        int rootValue=list.get(len-1);
        int i=0;
        for(;i<len-1;++i){
            if(list.get(i)>rootValue){//找到第一个大于rootValue的索引
                break;
            }
        }
        for(int j=i;j<len-1;++j){//判断后面的是否都大于rootValue(BST中,右子树的节点值都大于根节点)
            if(list.get(j)<rootValue) return false;
        }
        return squenceDfs(list.subList(0,i))&&squenceDfs(list.subList(i,len-1));//看左右序列(子树)是否符合BST的结构
    }
    // 23.二叉搜索树的后序遍历序列
    public boolean VerifySquenceOfBST(int [] sequence) {
        if(sequence==null||sequence.length==0) return false;
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<sequence.length;++i){
            list.add(sequence[i]);
        }
        return squenceDfs(list);
    }


    private void FindPathDfs(TreeNode root,int target,ArrayList<Integer> path,ArrayList<ArrayList<Integer>> paths){
        if(root==null) return;
        //不能入口处在这儿判断target,因为当前的root.val还没有算进去
        int tmp=target-root.val;
        path.add(root.val);
        if(tmp==0&&root.left==null&&root.right==null){
//            System.out.println(path);
            paths.add(new ArrayList<>(path));//不能直接add(path),path是一个引用,后续可能还会改变,所以这里使用构造函数copy一份
        }else{
            FindPathDfs(root.left,tmp,path,paths);
            FindPathDfs(root.right,tmp,path,paths);
        }
        //回溯
        path.remove(path.size()-1);
    }
    // 24.二叉树中和为某一值的路径
    public ArrayList<ArrayList<Integer>> FindPath(TreeNode root,int target) {
        ArrayList<ArrayList<Integer>> paths=new ArrayList<>();
        FindPathDfs(root,target,new ArrayList<>(),paths);
        return paths;
    }

    @Test
    public void testFindPath() {
        TreeNode root=new TreeNode(10);
        root.left=new TreeNode(5);
        root.right=new TreeNode(12);
        root.left.left=new TreeNode(4);
        root.left.right=new TreeNode(7);
        System.out.println(FindPath(root,22));
    }


    // 25.复杂链表的复制
    //tip: O(N^2)
//    public RandomListNode Clone(RandomListNode pHead){
//        if(pHead==null) return null;
//        List<RandomListNode> list1=new LinkedList<>();
//        List<RandomListNode> list2=new LinkedList<>();
//        RandomListNode ans=new RandomListNode(pHead.label);
//        RandomListNode tmpNode=pHead,ansNode=ans;
//        list1.add(pHead);
//        list2.add(ans);
//        //next指针的复制
//        while(tmpNode.next!=null){
//            tmpNode=tmpNode.next;
//            ansNode.next=new RandomListNode(tmpNode.label);
//            ansNode=ansNode.next;
//
//            //记录
//            list1.add(tmpNode);
//            list2.add(ansNode);
//        }
//
//        //random指针的复制
//        tmpNode=pHead;
//        int cnt=0;
//        while(tmpNode!=null){
//            if(tmpNode.random!=null){
//                list2.get(cnt).random=list2.get(list1.indexOf(tmpNode.random));
//            }
//            tmpNode=tmpNode.next;
//            ++cnt;
//        }
//        return ans;
//    }

    //tip: O(N)
    public RandomListNode Clone(RandomListNode pHead){
        if(pHead==null) return null;
        Map<RandomListNode,RandomListNode> mp=new HashMap<>();//存储对应位置的原始节点与新建节点之间的映射
        RandomListNode ans=new RandomListNode(pHead.label);
        RandomListNode tmpNode=pHead,ansNode=ans;
        mp.put(pHead,ans);
        //next指针的复制
        while(tmpNode.next!=null){
            tmpNode=tmpNode.next;
            ansNode.next=new RandomListNode(tmpNode.label);
            ansNode=ansNode.next;

            //记录
            mp.put(tmpNode,ansNode);
        }

        //random指针的复制
        tmpNode=pHead;
        while(tmpNode!=null){
            if(tmpNode.random!=null){
                mp.get(tmpNode).random=mp.get(tmpNode.random);
            }
            tmpNode=tmpNode.next;
        }
        return ans;
    }


    TreeNode lastNode=null;
    //lastNode指向已经转成链表的最后一个节点
    //warn: 把TreeNode lastNode作为递归参数就错了,
    //     因为在函数中修改lastNode的引用而不是内部的值,这样是无法修改成功的(无法反应到后续的参数中),
    //     使用TreeNode数组就ok了
    private void ConvertDfs(TreeNode root,TreeNode[] lastNode){
        if(root==null) return;
        ConvertDfs(root.left,lastNode);
//        System.out.println(lastNode==null?"null":lastNode.val);
        //以中序遍历的顺序
        root.left=lastNode[0];//链接后向节点
        if(lastNode[0]!=null) lastNode[0].right= root;//链接前向节点
        lastNode[0]= root;//更新lastNode

        ConvertDfs(root.right,lastNode);
    }
    // 26.二叉搜索树与双向链表
    public TreeNode Convert(TreeNode pRootOfTree) {
        if(pRootOfTree==null) return null;
        ConvertDfs(pRootOfTree,new TreeNode[]{null});

        TreeNode head=pRootOfTree;
        while (head.left!=null){
            head=head.left;
        }
        return head;
    }

    @Test
    public void testConvert() {
        TreeNode root=new TreeNode(10);
        root.left=new TreeNode(5);
        root.right=new TreeNode(12);
        root.left.left=new TreeNode(4);
        root.left.right=new TreeNode(7);
        TreeNode head=Convert(root);
//        System.out.println(head);
        while(head!=null){
            System.out.println(head.val);
            head=head.right;
        }
    }


    Set<String> duplicatedWords =new HashSet<>();
    private void swap(char[] str,int i,int j){
        if(i==j) return;
        char tmp=str[i];
        str[i]=str[j];
        str[j]=tmp;
    }
    private void PermutationDfs(char[] str,int pos,ArrayList<String> ans){
        if(pos==str.length-1){
            String tmp=String.valueOf(str);
            if(!duplicatedWords.contains(tmp)) {
                ans.add(new String(str));
                duplicatedWords.add(tmp);
            }
            return;
        }
        for(int i=pos;i<str.length;++i){//i从pos开始,而不是pos+1,因为当i==pos时就是不交换当前索引的情况,这种情况需要考虑在内
            swap(str,pos,i);
            PermutationDfs(str,pos+1,ans);
            swap(str,pos,i);
        }
    }
    // 27.字符串的排列
    public ArrayList<String> Permutation(String str) {
        ArrayList<String> ans=new ArrayList<>();
        if(str==null||str.length()==0) return ans;
        PermutationDfs(str.toCharArray(),0,ans);
        Collections.sort(ans);//要求字典序输出
        return ans;
    }

    @Test
    public void testPermutation() {
        System.out.println(Permutation("aa"));
    }

    // 28.数组中出现次数超过一半的数字
    public int MoreThanHalfNum_Solution(int [] array) {
        if(array==null||array.length==0) throw new IllegalArgumentException();
        int len=array.length;
        int cnt=1,ans=array[0];
        for(int i=1;i<len;++i){
            if(ans==array[i]){
                ++cnt;
            }else{
                --cnt;
                if(cnt==0) {
                    ans=array[i];
                    cnt=1;
                }
            }
        }

        //检查次数是否超过一半
        cnt=0;
        for(int i=0;i<len;++i){
            if(ans==array[i]) ++cnt;
        }
        return cnt>len/2?ans:0;
    }

    @Test
    public void testMoreThanHalfNum_Solution() {
        System.out.println(MoreThanHalfNum_Solution(new int[]{
                2,2,2,2,2,1,3,4,5,1,2
        }));
    }

    // 29.最小的K个数
    public ArrayList<Integer> GetLeastNumbers_Solution(int[] input, int k) {
        ArrayList<Integer> ans=new ArrayList<>();
        if(input==null||input.length==0||k<=0||k>input.length) return ans;
        PriorityQueue<Integer> queue=new PriorityQueue<>(k,(x,y)->-Integer.compare(x,y));//最小堆
        int len=input.length;
        for(int i=0;i<len;++i){
            if(queue.size()<k){
                queue.add(input[i]);
            }else{
                if(input[i]<queue.peek()){
                    queue.poll();
                    queue.add(input[i]);
                }
            }
        }
        ans.addAll(queue);
        return ans;
    }

    @Test
    public void testGetLeastNumbers_Solution() {
        System.out.println(GetLeastNumbers_Solution(new int[]{
                4,5,1,6,2,7,3,8
        },4));
    }

    // 30.连续子数组的最大和
    //tip: 贪心法
//    public int FindGreatestSumOfSubArray(int[] array) {
//        if(array==null||array.length==0) return 0;
//        int tmp=array[0];
//        int ans=tmp;
//        for(int i=1;i<array.length;++i){
//            if(tmp<0){
//                tmp=array[i];
//            }else{
//                tmp+=array[i];
//            }
//            ans=Math.max(ans,tmp);
//        }
//        return ans;
//    }
    //tip: 动态规划
    //dp[i]=max{dp[i-1]+array[i],array[i]};
    //dp[i]表示以array[i]为结尾的连续子数组的最大和
    public int FindGreatestSumOfSubArray(int[] array) {
        if(array==null||array.length==0) return 0;
        int ans=array[0];
        int[] dp=Arrays.copyOf(array,array.length);
        for(int i=1;i<array.length;++i){
            dp[i]=Math.max(dp[i-1]+array[i],array[i]);
            ans=Math.max(ans,dp[i]);
        }
        return ans;
    }

    @Test
    public void testFindGreatestSumOfSubArray() {
        System.out.println(FindGreatestSumOfSubArray(new int[]{
                -2,-8,-1,-5,-9
        }));
    }

    // 31.整数中1出现的次数（从1到n整数中1出现的次数）
    public int NumberOf1Between1AndN_Solution(int n){
        if(n<1) return 0;
        int base=1;
        int sum=0;
        while(n/base!=0){
            int lowNum=n-(n/base)*base;//获取低位
            int curNum=n/base%10;//获取当前位的数字
            int heightNum=n/(base*10);//获取高位数字

            if(curNum==0)
                sum+=heightNum*base;
            else if(curNum==1)
                sum+=heightNum*base+lowNum+1;
            else
                sum+=(heightNum+1)*base;

            base*=10;
        }
        return sum;
    }

    @Test
    public void testNumberOf1Between1AndN_Solution() {
        System.out.println(NumberOf1Between1AndN_Solution(123));
    }


    // 32.把数组排成最小的数
    public String PrintMinNumber(int[] numbers) {
        if(numbers==null||numbers.length==0) return "";
        String[] tmp=new String[numbers.length];
        for(int i=0;i<numbers.length;++i){
            tmp[i]=String.valueOf(numbers[i]);
        }

        Arrays.sort(tmp,(x,y)-> (x+y).compareTo(y+x));//notice: 关键在于这个比较器,比较字符串连接起来的大小
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<tmp.length;++i){
            sb.append(tmp[i]);
        }
        return sb.toString();
    }

    @Test
    public void testPrintMinNumber() {
        System.out.println(PrintMinNumber(new int[]{
                3,32,321
        }));
    }


    // 33.丑数
    public int GetUglyNumber_Solution(int n) {
        if(n<=0) return -1;
        if(n==1) return 1;
        int[] nums=new int[n];
        nums[0]=1;
        int p2=0,p3=0,p5=0;
        int pos=1;
        while(pos<n){
            int min=Collections.min(Arrays.asList(2*nums[p2],3*nums[p3],5*nums[p5]));
            nums[pos]=min;
            while (2*nums[p2]<=nums[pos]) ++p2;
            while (3*nums[p3]<=nums[pos]) ++p3;
            while (5*nums[p5]<=nums[pos]) ++p5;
            ++pos;
        }
        return nums[n-1];
    }

    // 34.第一个只出现一次的字符
    public int FirstNotRepeatingChar(String str) {
        if(str==null||str.length()==0) return -1;
        char[] dict=new char[52];//记录字符出现的次数
        int[] firstPos=new int[52];//记录各字符第一次出现的位置
        Arrays.fill(firstPos,-1);
        int len=str.length();
        for(int i=0;i<len;++i){
            char c=str.charAt(i);
            if(Character.isLowerCase(c)){
                ++dict[c-'a'];
                if(firstPos[c-'a']<0) firstPos[c-'a']=i;
            }else{
                ++dict[c-'A'+26];
                if(firstPos[c-'A'+26]<0) firstPos[c-'A'+26]=i;
            }
        }
        List<Integer> tmp=new ArrayList<>();
        for (int i=0;i<52;++i){
            if(dict[i]==1){
                tmp.add(firstPos[i]);
//                System.out.println((char)(i>=26?'A'+i:'a'+i));
//                System.out.println(firstPos[i]);
            }
        }
        if(tmp.size()==0) return -1;
        else return Collections.min(tmp);//索引最小的那个,即第一个只出现一次的字符的索引
    }

    @Test
    public void testFirstNotRepeatingChar() {
        System.out.println(FirstNotRepeatingChar("google"));
    }


    int pairCnt=0;//统计逆序对数目
    private void mergeSort(int[] array,int s,int t,int[] tmp){
        if(s<t){
            int mid=(s+t)/2;
            mergeSort(array,s,mid,tmp);
            mergeSort(array,mid+1,t,tmp);
            //合并两个有序子数组
            int i=s,j=mid+1,cnt=0;
            while(i<=mid||j<=t){
                if(j>t){
//                    pairCnt=(pairCnt+t-mid+i)%1000000007;//这里就不用算了(会导致重复计算),因为在之前比较的时候已经算过了
                    tmp[cnt++]=array[i++];
                }else if(i>mid){
                    tmp[cnt++]=array[j++];
                }else if(array[i]<array[j]){//真正的比较,从小到大
                    tmp[cnt++]=array[i++];
                }else{
                    pairCnt=(pairCnt+mid-i+1)%1000000007;//逆序对数目增加len(mid->i)=mid-i+1
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

    // 35.数组中的逆序对
    public int InversePairs(int[] array) {
        if(array==null||array.length==0) return 0;
        mergeSort(array,0,array.length-1,new int[array.length]);
        return pairCnt;
    }

    @Test
    public void testInversePairs() {
        int[] array={364,637,341,406,747,995,234,971,571,219,993,407,416,366,315,301,601,650,418,355,460,505,360,965,
                516,648,727,667,465,849,455,181,486,149,588,233,144,174,557,67,746,550,474,162,268,142,463,221,882,576,
                604,739,288,569,256,936,275,401,497,82,935,983,583,523,697,478,147,795,380,973,958,115,773,870,259,655,
                446,863,735,784,3,671,433,630,425,930,64,266,235,187,284,665,874,80,45,848,38,811,267,575};
        int ans=InversePairs(array);
        System.out.println(Arrays.toString(array));
        System.out.println(ans);
    }

    // 36.两个链表的第一个公共结点
    public ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        if(pHead1==null||pHead2==null) return null;
        if(pHead1==pHead2) return pHead1;//tip: 首节点就相同,这种情况需要单独考虑
        int len1=0,len2=0;
        ListNode tmpNode1=pHead1,tmpNode2=pHead2;
        //统计两个链表的长度
        while (tmpNode1!=null){
            ++len1;
            tmpNode1=tmpNode1.next;
        }
        while(tmpNode2!=null){
            ++len2;
            tmpNode2=tmpNode2.next;
        }

        tmpNode1=pHead1;
        tmpNode2=pHead2;
        int d=len1-len2;
        //较长的链表先走,确保两个链表的剩余长度相同
        if(d>0){
            for(int i=0;i<d;++i){
                tmpNode1=tmpNode1.next;
            }
        }else{
            for(int i=0;i<-d;++i){
                tmpNode2=tmpNode2.next;
            }
        }
        while (tmpNode1!=null&&tmpNode2!=null){
            if(tmpNode1==tmpNode2) return tmpNode1;
            tmpNode1=tmpNode1.next;
            tmpNode2=tmpNode2.next;
        }
        return null;
    }

    // 37.数字在排序数组中出现的次数
    //tip: 极端情况下时间复杂度是O(N)
//    public int GetNumberOfK(int[] array, int k) {
//        if(array==null||array.length==0) return 0;
//        int index=Arrays.binarySearch(array,k);
//        if(index<0) return 0;
//        else{
//            int ans=1;
//            for(int i=index-1;i>=0;--i){
//                if(array[i]==k) ++ans;
//                else break;
//            }
//
//            for(int i=index+1;i<array.length;++i){
//                if(array[i]==k) ++ans;
//                else break;
//            }
//            return ans;
//        }
//    }
    //tip: 时间复杂度O(logN)
    private int lowerBound(int[] array,int val){
        int l=0,r=array.length-1;
        int mid=(l+r)>>1;
        while (l<=r){
            if(array[mid]<val){
                l=mid+1;
            }else if(array[mid]>val){
                r=mid-1;
            }else if(mid-1>=0&&array[mid-1]==val){
                r=mid-1;//逼近下界
            }else{
                return mid;
            }
            mid=(l+r)>>1;
        }
        return -1;
    }
    private int upperBound(int[] array,int val){
        int l=0,r=array.length-1;
        int mid=(l+r)>>1;
        while (l<=r){
            if(array[mid]<val){
                l=mid+1;
            }else if(array[mid]>val){
                r=mid-1;
            }else if(mid+1<array.length&&array[mid+1]==val){
                l=mid+1;//逼近上界
            }else{
                return mid;
            }
            mid=(l+r)>>1;
        }
        return -1;
    }
    public int GetNumberOfK(int[] array, int k) {
        if(array==null||array.length==0) return 0;
        int l=lowerBound(array,k),r=upperBound(array,k);
        if(l==-1||r==-1) return 0;
        return r-l+1;
    }

    @Test
    public void testGetNumberOfK() {
        int[] array={1,2,3,4,4,4,4,5,6,7};
        System.out.println(lowerBound(array,4));
        System.out.println(upperBound(array,4));
        System.out.println(GetNumberOfK(array,4));
    }

    int maxDepth=0;
    int minDepth=Integer.MAX_VALUE;
    private void TreeDepthDfs(TreeNode root,int depth){
        if(root==null) return;
        if(root.left==null&&root.right==null){
            maxDepth=Math.max(maxDepth,depth);
            minDepth=Math.min(minDepth,depth);
        }
        TreeDepthDfs(root.left,depth+1);
        TreeDepthDfs(root.right,depth+1);
    }
    // 38.二叉树的深度
    public int TreeDepth(TreeNode root) {
        if(root==null) return 0;
        TreeDepthDfs(root,1);
        return maxDepth;
    }


    //depth[]用于记录各节点的深度,因为要获取计算好的参数,所以使用数组类型
    private boolean BalanceDfs(TreeNode root,int[] depth){
        if(root==null) {
            depth[0]=0;
            return true;
        }
        int[] left={0},right={0};
        if(BalanceDfs(root.left,left)&&BalanceDfs(root.right,right)){//左右子树都平衡
            int diff=Math.abs(left[0]-right[0]);
            if(diff<=1){//平衡
                depth[0]=1+Math.max(left[0],right[0]);//更新节点深度
                return true;
            }
        }
        return false;
    }
    // 39.平衡二叉树
    public boolean IsBalanced_Solution(TreeNode root) {
        if(root==null) return true;
        return BalanceDfs(root,new int[]{0});
    }


    // 40.数组中只出现一次的(2个)数字
    public void FindNumsAppearOnce(int []array,int num1[] , int num2[]) {
        if(array==null||array.length<=1) return;
        int tmp=0;
        for(int i=0;i<array.length;++i){
            tmp^=array[i];
        }
        int mark=1;
        while((tmp&mark)!=mark){//找到最低位的bit==1,按照这个bit的值将数组分为两部分
            mark<<=1;
        }
        num1[0]=0;
        num2[0]=0;
        for(int i=0;i<array.length;++i){
            if((array[i]&mark)==mark) {//bit==1,按照这个bit的值将数组分为两部分
                num1[0]^=array[i];
            }else num2[0]^=array[i];
        }
    }

    @Test
    public void testFindNumsAppearOnce() {
        int[] num1=new int[]{0},num2=new int[]{0};
        FindNumsAppearOnce(new int[]{2,4,3,6,3,2,5,5},num1,num2);
        System.out.println(num1[0]+", "+num2[0]);
    }

    // 41.和为S的连续正数序列
    public ArrayList<ArrayList<Integer> > FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> ans=new ArrayList<>();
        if(sum<=0) return ans;
        int l=1,r=2;
        int tmp=l+r;
        while(r<sum){
            if(tmp==sum){
                ans.add(new ArrayList<>());
                for(int i=l;i<=r;++i){
                    ans.get(ans.size()-1).add(i);
                }
                tmp-=l;
                ++l;
            }else if(tmp>sum){
                tmp-=l;
                ++l;
            }else{
                ++r;
                tmp+=r;
            }
        }
        return ans;
    }

    @Test
    public void testFindContinuousSequence() {
        System.out.println(FindContinuousSequence(15));
    }


    // 42.和为S的两个数字
    public ArrayList<Integer> FindNumbersWithSum(int[] array,int sum) {
        ArrayList<Integer> ans=new ArrayList<>();
        if(array==null||array.length<=1) return ans;
        Arrays.sort(array);
        for(int i=0;i<array.length;++i){
            int val=sum-array[i];
            if(Arrays.binarySearch(array,i+1,array.length,val)>0){//找到值了
                if(ans.isEmpty()){
                    ans.add(array[i]);
                    ans.add(val);
                }else{
                    if(ans.get(0)*ans.get(1)>array[i]*val){
                        ans.clear();
                        ans.add(array[i]);
                        ans.add(val);
                    }
                }
            }
        }
        return ans;
    }

    @Test
    public void testFindNumbersWithSum() {
        System.out.println(FindNumbersWithSum(new int[]{1, 2, 3, 4, 5},6));
    }

    private void reverse(char[] str,int s,int e){
        if(s<0||e<0) return;
        int l=s,r=e;
        char tmp;
        while(l<r){
            tmp= str[l];
            str[l]=str[r];
            str[r]=tmp;
            ++l;
            --r;
        }
    }

    // 43.左旋转字符串
    public String LeftRotateString(String str,int n) {
        if(str==null||str.length()==0) return "";
        char[] ch=str.toCharArray();
        reverse(ch,0,n-1);
        reverse(ch,n,ch.length-1);
        reverse(ch,0,ch.length-1);
        return String.valueOf(ch);
    }

    @Test
    public void testLeftRotateString() {
        System.out.println(LeftRotateString("abcXYZdef",3));
    }

    // 44.翻转单词顺序列
    public String ReverseSentence(String str) {
        if(str==null||str.length()==0) return "";
        char[] ch=str.toCharArray();
        int p=0,q=str.indexOf(' ',p);
        while (q!=-1){
            reverse(ch,p,q-1);
            p=q+1;
            q=str.indexOf(' ',p);
        }
        reverse(ch,p,ch.length-1);//翻转最后一个单词
        reverse(ch,0,ch.length-1);
        return String.valueOf(ch);
    }

    @Test
    public void testReverseSentence() {
        System.out.println(ReverseSentence("student. a am I"));
    }

    // 45.扑克牌顺子
    public boolean isContinuous(int[] numbers) {
        if(numbers==null||numbers.length==0) return false;
        if(numbers.length==1) return true;
        int len=numbers.length;
        int[] cnt=new int[14];
        int minCard=Integer.MAX_VALUE,maxCard=-1;
        for(int i=0;i<len;++i){
            ++cnt[numbers[i]];
            if(numbers[i]!=0) minCard=Math.min(minCard,numbers[i]);
            maxCard=Math.max(maxCard,numbers[i]);
        }

        for(int i=minCard;i<=maxCard;++i){
            if(cnt[i]>1) return false;
            else if(cnt[i]==0){
                --cnt[0];
                if(cnt[0]<0) return false;
            }
        }
        return cnt[0]<=13-maxCard+minCard-1;//大小王的数目如果过多,也认为不是顺子
    }

    @Test
    public void testIsContinuous() {
        System.out.println(isContinuous(new int[]{1,3,2,5,4}));
    }

    // 46.孩子们的游戏(圆圈中最后剩下的数)
    public int LastRemaining_Solution(int n, int m) {
        if(n<=0) return -1;
        List<Integer> list=new LinkedList<>();
        for(int i=0;i<n;++i){//0 ~ n-1
            list.add(i);
        }
        int tmp=0;
        Iterator<Integer> iterator=list.iterator();//此时得到的迭代器类似是一个dummy节点,需要不断.next()才能遍历元素
        while(list.size()>1){
            if(!iterator.hasNext()) iterator=list.iterator();
            iterator.next();
            if(tmp==m-1){
                iterator.remove();//remove()之前需要调用过next(),删除的就是上次next()返回的那个元素
                tmp=0;
            }else{
                ++tmp;
            }
        }
        return list.get(0);
    }

    @Test
    public void testLastRemaining_Solution() {
//        List<Integer> list=new LinkedList<>();
//        for(int i=0;i<5;++i) list.add(i);
//        Iterator<Integer> iterator=list.iterator();
//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
////            if(!iterator.hasNext()) iterator=list.iterator();
//            iterator.remove();
//        }
//        System.out.println(list);

        System.out.println(LastRemaining_Solution(5,3));
    }

    // 47.求1+2+3+...+n
    public int Sum_Solution(int n) {
        int sum = n;
        boolean flag = (sum > 0) && ((sum += Sum_Solution(--n)) > 0);
        return sum;
    }

    // 48.不用加减乘除做加法
    //eg: 3 + 2 --> 0011 + 0010 --> 0011 ^ 0010 + ((0011 & 0010) << 1)
    public int Add(int num1,int num2) {
        if(num1==0) return num2;
        if(num2==0) return num1;
        int sum = 0, carry;
        while(num2!=0) {//num2==0表示已经没有进位了
            //对应位的和(不含进位)
            sum = num1^num2;
            //对应位和的进位,既然是进位,就要整体左移一位
            carry = (num1&num2)<<1;
            num1=sum;
            num2=carry;
        }
        return sum;
    }

    // 49.把字符串转换成整数
    public int StrToInt(String str) {
        if(str==null||str.length()==0) return 0;
        if(str.equals("-2147483648")) return Integer.MIN_VALUE;//tip: 特例,int正数部分的边界是2147483647,所以这里单独考虑
        boolean flag=true;//表示是否为负数
        char first=str.charAt(0);
        int start=0;
        if(!Character.isDigit(first)){
            start=1;
            if(first=='+') flag=true;
            else if(first=='-') flag=false;
            else return 0;
        }
        int ans=0,base=1;
        for(int i=str.length()-1;i>=start;--i){
            if(Character.isDigit(str.charAt(i))){
                ans+=(str.charAt(i)-'0')*base;
                if(ans<0) return 0;//判断溢出
                base*=10;
            }else return 0;
        }
        if(!flag) ans*=-1;
        return ans;
    }

    @Test
    public void testStrToInt() {
        System.out.println(StrToInt("-2147483648"));
    }

    // 50.数组中重复的数字
    //tip: time complexity=O(N); space complexity=O(N)
//    public boolean duplicate(int numbers[],int length,int[] duplication) {
//        if(numbers==null||length<=0) return false;
//        int[] d=new int[length];
//        for(int i=0;i<length;++i){
//            ++d[numbers[i]];
//            if(d[numbers[i]]>1){
//                duplication[0]=numbers[i];
//                return true;
//            }
//        }
//        return false;
//    }
    //tip: time complexity=O(N); space complexity=O(1)
    public boolean duplicate(int numbers[],int length,int[] duplication) {
        if(numbers==null||length<=0) return false;
        //归档法,因为number[i]的范围是0~n-1,这里尽量令numbers[i]==i
        for(int i=0;i<length;++i){
            while (i!=numbers[i]){
                if(numbers[i]==numbers[numbers[i]]){//当发现numbers[i]的位置已经被占了时,表明numbers[i]重复
                    duplication[0]=numbers[i];
                    return true;
                }
                int tmp=numbers[i];
                numbers[i]=numbers[tmp];
                numbers[tmp]=tmp;
            }
        }
        return false;
    }

    // 51.构建乘积数组
    public int[] multiply(int[] A) {
        if(A==null||A.length==0) return null;
        int n=A.length;
        int[] b1=new int[n],b2=new int[n];
        //b1[i]=A[0 ~ i-1]的连乘
        b1[0]=1;
        int tmp=1;
        for(int i=1;i<=n-1;++i){
            tmp*=A[i-1];
            b1[i]=tmp;
        }
        //b2[i]=A[i+1~n-1]的连乘
        b2[n-1]=1;
        tmp=1;
        for(int i=n-2;i>=0;--i){
            tmp*=A[i+1];
            b2[i]=tmp;
        }

        //合并计算
        int[] B=new int[n];
        for(int i=0;i<n;++i){
            B[i]=b1[i]*b2[i];
        }
        return B;
    }

    // 52.正则表达式匹配
    //tip: 使用正则表达式
//    public boolean match(char[] str, char[] pattern) {
//        Pattern p=Pattern.compile(String.valueOf(pattern));
//        Matcher m=p.matcher(String.valueOf(str));
//        if(m.find()){
//            if(m.group().equals(String.valueOf(str))) return true;
//        }
//        return false;
//    }

    //使用dfs
    private boolean matchDfs(char[] str,int i,char[] pattern,int j){
        if(i==str.length&&j==pattern.length) return true;//刚好匹配
        if(j==pattern.length) return i==str.length;//pattern已经用完了,这时看str是否用完了
        if(i==str.length){//str用完了
            if(j==pattern.length-1) return false;//如果pattern只剩一个字符了,那是不可能匹配上的
            if(pattern[j+1]=='*') return matchDfs(str,i,pattern,j+2);//pattern的j+1个字符是'*',这是可以使'*'匹配第j个字符0次
        }else{//str没用完
            if(j==pattern.length-1){//如果pattern只剩当前一个字符了(潜台词是后面没有'*')
                if(pattern[j]=='.'||pattern[j]==str[i]) return matchDfs(str,i+1,pattern,j+1);//str的当前字符可以和pattern的当前字符匹配
                else return false;//不能匹配那就返回false了
            }else{//pattern后续还有字符,也就是说可能存在'*' (之前的边界判断都是为了在这里方便的判断'*'是否存在)
                if(pattern[j+1]=='*'){//pattern后面一个字符是'*'
                    if(str[i]!=pattern[j]&&pattern[j]!='.') return matchDfs(str,i,pattern,j+2);//str的当前字符和pattern的当前字符不匹配,这时可以使'*'匹配0次
                    else return matchDfs(str,i,pattern,j+2)||matchDfs(str,i+1,pattern,j);//当前字符匹配,那么可以继续匹配0次或大于等于1次
                }else{//后面一个字符不是'*'
                    if(str[i]!=pattern[j]&&pattern[j]!='.') return false;//当前字符不匹配,返回false
                    else return matchDfs(str,i+1,pattern,j+1);//当前字符匹配,那么继续匹配下一个
                }
            }
        }
        return false;
    }
    public boolean match(char[] str, char[] pattern) {
        if(str==null&&pattern==null) return true;
        if(str==null||pattern==null) return false;
        if(pattern.length==0) return str.length==0;
        return matchDfs(str,0,pattern,0);
    }

    @Test
    public void testMatch() {
        System.out.println(match(new char[]{'a','a','a'},new char[]{'a','b','*','a','c','*','a'}));
        System.out.println(match(new char[]{'a','a','a'},new char[]{'a','b','*','a'}));
    }

    //判断字符串是否是合法的整数(signed表示可不可以带正负号)
    private boolean validInteger(String s,boolean signed){
        if(s==null||s.length()==0) return false;
        if(!signed){
            for(int i=0;i<s.length();++i){
                if(!Character.isDigit(s.charAt(i))) return false;
            }
            return true;
        }else{//signed==true: can contain '+','-'
            if(s.charAt(0)=='+'||s.charAt(0)=='-'){
                return validInteger(s.substring(1),false);
            }else return validInteger(s,false);//没有正负号的情况
        }
    }

    //exclude 'e',即判断一个浮点数
    private boolean validNumber(String s){
        if(s==null||s.length()==0) return false;
        char first=s.charAt(0);
        if(first=='.'){//eg: .1234
            return validInteger(s.substring(1),false);
        }else{
            if(first=='+'||first=='-'){
                if(s.length()==1) return false;
                if(s.charAt(1)=='.') return validInteger(s.substring(2),false);//eg: +.1234
                int cnt=0;
                for(int i=1;i<s.length();++i){
                    if(s.charAt(i)=='.') ++cnt;
                }
                if(cnt==0) return validInteger(s.substring(1),false);
                if(cnt!=1) return false;//multiple '.', is illegal
                int pos=s.indexOf('.');
                if(pos==s.length()-1) return validInteger(s.substring(1,pos),false);//防止产生空字符串, eg: +3.
                return validInteger(s.substring(1,pos),false)&&validInteger(s.substring(pos+1),false);
            }else{
                int cnt=0;
                for(int i=1;i<s.length();++i){
                    if(s.charAt(i)=='.') ++cnt;
                }
                if(cnt==0) return validInteger(s,false);
                if(cnt!=1) return false;//multiple '.', is illegal
                int pos=s.indexOf('.');
                if(pos==s.length()-1) return validInteger(s.substring(0,pos),false);//防止产生空字符串, eg: 3.
                return validInteger(s.substring(0,pos),false)&&validInteger(s.substring(pos+1),false);
            }
        }
    }
    // 53.表示数值的字符串
    public boolean isNumeric(char[] ch){
        if(ch==null) return false;
        String s=String.valueOf(ch).toLowerCase().trim();// E->e,去除空格
        if(s.length()==0) return false;
        int cnt=0;
        for(int i=0;i<s.length();++i){
            if(s.charAt(i)=='e') ++cnt;
        }
        if(cnt==0) return validNumber(s);
        if(cnt!=1) return false;
        int pos=s.indexOf('e');//实际上只能存在一个e
        //指数部分不允许有.号(即不允许是浮点数),所以使用validInteger; 指数部分允许有正负号,所以其参数signed=true
        return validNumber(s.substring(0,pos))&&validInteger(s.substring(pos+1),true);
    }

    // 54.字符流中第一个不重复的字符
    class Solution54{
        int[] cnt=new int[256];//记录次数
        int[] firstPos=new int[256];//记录第一次出现的索引
        int index=0;
        public Solution54() {
            Arrays.fill(firstPos,-1);
        }

        //Insert one char from stringStream
        public void Insert(char ch) {
            ++cnt[ch];
            if(firstPos[ch]==-1) firstPos[ch]=index;
            ++index;
        }
        //return the first appearance once char in current stringStream
        public char FirstAppearingOnce() {
            List<int[]> tmp=new ArrayList<>();
            for (int i=0;i<256;++i){
                if(cnt[i]==1){
                    tmp.add(new int[]{firstPos[i],i});//记录第一次出现的索引 以及 字符数值
                }
            }
            if(tmp.size()==0) return '#';
            else return (char) Collections.min(tmp, Comparator.comparingInt(x -> x[0]))[1];//索引最小的那个就是第一个只出现一次的字符
        }
    }

    @Test
    public void testSolution54() {
        Solution54 solution54=new Solution54();
        solution54.Insert('g');
        solution54.Insert('o');
        solution54.Insert('o');
        solution54.Insert('g');
        solution54.Insert('l');
        solution54.Insert('e');
        System.out.println(solution54.FirstAppearingOnce());
    }

    // 55.链表中环的入口结点
    public ListNode EntryNodeOfLoop(ListNode pHead) {
        if(pHead==null) return null;
        Set<ListNode> set=new HashSet<>();
        ListNode tmpNode=pHead;
        set.add(tmpNode);
        while(tmpNode!=null){
            tmpNode=tmpNode.next;
            if(set.contains(tmpNode)) return tmpNode;
            else set.add(tmpNode);
        }
        return null;
    }

    // 56.删除链表中重复的结点
    public ListNode deleteDuplication(ListNode pHead) {
        if(pHead==null) return null;
        ListNode tmpNode=pHead;
        Set<Integer> set=new HashSet<>();//记录重复节点的val
        //使重复节点都变成单个
        while (tmpNode!=null){
            ListNode tmpNext=tmpNode.next;
            if(tmpNext==null) break;
            if(tmpNode.val==tmpNext.val){
                set.add(tmpNode.val);
                tmpNode.next=tmpNext.next;//删除后面的重复节点
                tmpNext.next=null;//help gc
            }else{
                tmpNode=tmpNode.next;
            }
        }
        //去掉前面的重复节点,直至遇到非重复节点为止
        while(pHead!=null&&set.contains(pHead.val)){
            pHead=pHead.next;
        }
        tmpNode=pHead;
        ListNode prevNode=pHead;
        while(tmpNode!=null){
            if(set.contains(tmpNode.val)){
                //删除遗留的重复节点,这时不能更新prevNode,因为删除节点后,对于下一个节点来说,当前的prevNode仍是前一个节点
                prevNode.next=tmpNode.next;
            }else{
                prevNode=tmpNode;
            }
            tmpNode=tmpNode.next;
        }
        return pHead;
    }

    @Test
    public void testDeleteDuplication() {
        ListNode node=new ListNode(1);
        node.next=new ListNode(2);
        node.next.next=new ListNode(3);
        node.next.next.next=new ListNode(3);
        node.next.next.next.next=new ListNode(4);
        node.next.next.next.next.next=new ListNode(4);
        node.next.next.next.next.next.next=new ListNode(5);
        System.out.println(deleteDuplication(node));
    }

    // 57.二叉树的下一个结点
    //中序遍历(左-根-右)
    public TreeLinkNode GetNext(TreeLinkNode pNode) {
        if(pNode==null) return null;
        if(pNode.right!=null){//pNode存在右子树,那么下一个节点就是左子树的最左节点
            TreeLinkNode tmpNode=pNode.right;
            while(tmpNode.left!=null){
                tmpNode=tmpNode.left;
            }
            return tmpNode;
        }else if(pNode.next==null||pNode.next.left==pNode) {
            //pNode是其父节点的左子结点,并且没有右子树,那么下一个节点就是其父节点
            //或者没有右子树并且pNode是根节点,这时就返回null (pNode.next==null)
            return pNode.next;
        }else{//没有右子树,并且pNode是其父节点的右子节点,那么找到一个祖先节点是祖先的祖先节点的左子结点时,此时祖先的祖先节点就是下一个节点
            TreeLinkNode fatherNode=pNode.next;
            while(fatherNode!=null){
                if(fatherNode.next==null) return null;//找到根节点了,这时就没有下一个节点了
                if(fatherNode.next.left==fatherNode) return fatherNode.next;
                fatherNode=fatherNode.next;
            }
            return null;
        }
    }


    boolean isSymmetricalDfs(TreeNode left,TreeNode right){
        if(left==null&&right==null) return true;
        if(left==null||right==null) return false;
        if(left.val!=right.val) return false;
        //若左子树的左孩子等于右子树的右孩子 且 左子树的右孩子等于右子树的左孩子,并且左右子树节点的值相等,则是对称的。
        return isSymmetricalDfs(left.left,right.right)&&isSymmetricalDfs(left.right,right.left);
    }
    // 58.对称的二叉树
    boolean isSymmetrical(TreeNode pRoot) {
        if(pRoot==null) return true;
        return isSymmetricalDfs(pRoot.left,pRoot.right);
    }


    // 59.按之字形顺序打印二叉树
    public ArrayList<ArrayList<Integer> > Print(TreeNode pRoot) {
        ArrayList<ArrayList<Integer>> ans=new ArrayList<>();
        if(pRoot==null) return ans;
        Queue<TreeNode> queue=new LinkedList<>();
        Queue<Integer> depthQueue=new LinkedList<>();
        queue.add(pRoot);
        depthQueue.add(0);
        while (!queue.isEmpty()){
            TreeNode tmpNode= queue.poll();
            int depth=depthQueue.poll();
            if(ans.size()-1<depth) ans.add(new ArrayList<>());

            ans.get(depth).add(tmpNode.val);

            if(tmpNode.left!=null) {
                queue.add(tmpNode.left);
                depthQueue.add(depth+1);
            }
            if(tmpNode.right!=null) {
                queue.add(tmpNode.right);
                depthQueue.add(depth+1);
            }
        }

        //这时ans都是从左到右的,所以这里隔行反转
        for(int i=0;i<ans.size();++i){
            if(i%2==1){
                Collections.reverse(ans.get(i));
            }
        }
        return ans;
    }
    // 60.把二叉树打印成多行; (将59题的反转部分去掉就ok了)


    // 61.序列化二叉树
    class Solution61 {
        final String sepStr;//分割字符串
        final String nilStr;//代表空指针的字符串

        public Solution61() {
            sepStr=",";
            nilStr="null";
        }
        public Solution61(String sepStr, String nilStr) {
            this.sepStr = sepStr;
            this.nilStr = nilStr;
        }

        // Encodes a tree to a single string.
        public String Serialize(TreeNode root) {
            if(root==null) return null;
            StringBuilder sb=new StringBuilder();
            Queue<TreeNode> q=new LinkedList<>();
            q.add(root);
            //先得到树的节点个数
            int treeSize=0;
            while(!q.isEmpty()){
                TreeNode tmpNode=q.poll();
                ++treeSize;
                if(tmpNode.left!=null) q.add(tmpNode.left);
                if(tmpNode.right!=null) q.add(tmpNode.right);
            }

            //开始序列化
            q.add(root);
            sb.append(root.val).append(sepStr);
            --treeSize;
            while(!q.isEmpty()&&treeSize>0){//当到达树的节点个数时就退出,避免添加多余的null
                TreeNode tmpNode=q.poll();
                if(tmpNode.left!=null) {
                    q.add(tmpNode.left);
                    sb.append(tmpNode.left.val).append(sepStr);
                    --treeSize;
                } else sb.append(nilStr).append(sepStr);
                if(tmpNode.right!=null) {
                    q.add(tmpNode.right);
                    sb.append(tmpNode.right.val).append(sepStr);
                    --treeSize;
                } else sb.append(nilStr).append(sepStr);
            }
            String result=sb.toString();
            return result.substring(0,result.length()-1);
        }


        // Decodes your encoded data to tree.
        public TreeNode Deserialize(String data) {
            if(data==null) return null;
            String[] tmp=data.split(sepStr);
            if(tmp.length==1){
                if(!tmp[0].equals(nilStr)) return new TreeNode(Integer.parseInt(tmp[0]));
                else return null;//只有一个nilStr,那就返回null
            }

            Queue<TreeNode> q=new LinkedList<>();
            TreeNode root=new TreeNode(Integer.parseInt(tmp[0]));
            q.add(root);
            int index=1;
            while(index<tmp.length){
                TreeNode fatherNode=q.poll();
                if(!tmp[index].equals(nilStr)){
                    TreeNode childNode=new TreeNode(Integer.parseInt(tmp[index]));
                    fatherNode.left=childNode;
                    q.add(childNode);
                }else{
                    fatherNode.left=null;
                }
                ++index;
                if(index<tmp.length&&!tmp[index].equals(nilStr)){//注意index的范围检查
                    TreeNode childNode=new TreeNode(Integer.parseInt(tmp[index]));
                    fatherNode.right=childNode;
                    q.add(childNode);
                }else{
                    fatherNode.right=null;
                }
                ++index;
            }
            return root;
        }
    }


    int K;
    TreeNode ans;
    private void KthNodeDfs(TreeNode root){
        if(root==null) return;
        KthNodeDfs(root.left);
        --K;
        if(K==0){
            ans=root;
            return;
        }
        KthNodeDfs(root.right);
    }

    // 62.二叉搜索树的第k个结点
    TreeNode KthNode(TreeNode pRoot, int k) {
        if(pRoot==null||k<=0) return null;
        K=k;
        KthNodeDfs(pRoot);
        return ans;
    }


    // 63.数据流中的中位数
    class Solution63 {
        PriorityQueue<Integer> lessQ;
        PriorityQueue<Integer> greaterQ;
        int cnt;
        public Solution63() {
            //保证lessQ中的所有元素小于等于greaterQ中的元素
            lessQ =new PriorityQueue<>((x, y)->-Integer.compare(x,y));//最大堆
            greaterQ =new PriorityQueue<>();//最小堆
            cnt=0;
        }

        public void Insert(Integer num) {
            ++cnt;
            //lessQ,greaterQ的size相差不超过1,所以根据奇偶情况添加元素,造成的情况就是: 0<=lessQ.size-greaterQ.size<=1
            if((cnt&1)==1){//往最大堆放元素
                //要保证最大堆lessQ中元素都小于等于最小堆greaterQ中的元素
                greaterQ.add(num);//所以先把元素放到最小堆greaterQ中
                lessQ.add(greaterQ.poll());//再从最小堆greaterQ中拿出最小的那个元素,并放入最大堆lessQ
            }else{//往最小堆中放元素
                //要保证最小堆greaterQ中元素都大于等于最大堆lessQ中的元素
                lessQ.add(num);//所以先把元素放到最大堆lessQ中
                greaterQ.add(lessQ.poll());//再从最大堆lessQ拿出最大的那个元素,并放入最小堆greaterQ
            }
        }

        public Double GetMedian() {
            if(cnt==0) return null;
            if((cnt&1)==1) return Double.valueOf(lessQ.peek());
            else return (lessQ.peek()+ greaterQ.peek())/2.0;
        }
    }

    @Test
    public void testSolution63() {
        Solution63 solution63=new Solution63();
        solution63.Insert(5);
        solution63.Insert(2);
        solution63.Insert(3);
        solution63.Insert(4);
        solution63.Insert(1);
        solution63.Insert(6);
        solution63.Insert(7);
        solution63.Insert(0);
        solution63.Insert(8);
        System.out.println(solution63.GetMedian());
    }


    // 64.滑动窗口的最大值 (剑指offer p288)
    public ArrayList<Integer> maxInWindows(int[] num, int size) {
        ArrayList<Integer> ans=new ArrayList<>();
        if(num==null||num.length==0||size<=0||size>num.length) return ans;
        int len=num.length;
        Deque<Integer> q=new LinkedList<>();
        for(int i=0;i<len;++i){
            while(!q.isEmpty()&&num[i]>num[q.getLast()]){//新扫描到的元素比队尾的元素大,則更新
                q.removeLast();
            }
            q.addLast(i);
            if(i-q.getFirst()==size) q.removeFirst();//队首的元素索引超过窗口的范围了
            if(i>=size-1){//窗口形成了,并滑动中...
                ans.add(num[q.getFirst()]);
            }
        }
        return ans;
    }

    @Test
    public void testMaxInWindows() {
        System.out.println(maxInWindows(new int[]{2,3,4,2,6,2,5,1},3));
    }

    int[][] d={{1,0},{0,1},{-1,0},{0,-1}};
    private boolean hasPathDfs(char[][] m,int x,int y,char[] str,int pos,boolean[][] vis){
        if(pos==str.length) return true;//匹配完了
        if(x<0||x>=m.length||y<0||y>=m[0].length) return false;//检查范围
        if(!vis[x][y]&&m[x][y]==str[pos]){
            vis[x][y]=true;
            for(int i=0;i<4;++i){
                if(hasPathDfs(m,x+d[i][0],y+d[i][1],str,pos+1,vis)){
                    return true;
                }
            }
            vis[x][y]=false;//回溯
        }
        return false;
    }

    // 65.矩阵中的路径
    public boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        if(matrix==null||str==null||matrix.length==0||str.length==0) return false;
        if(rows*cols!=matrix.length) return false;
        char[][] m=new char[rows][cols];
        for(int i=0;i<rows;++i){
            for(int j=0;j<cols;++j){
                m[i][j]=matrix[i*cols+j];
            }
        }
        boolean[][] vis=new boolean[rows][cols];
        for(int i=0;i<rows;++i){
            for(int j=0;j<cols;++j){
                if(m[i][j]==str[0]){
                    if(hasPathDfs(m,i,j,str,0,vis)) return true;
                }
            }
        }
        return false;
    }

    @Test
    public void testHasPath() {
        System.out.println(hasPath(new char[]{'a','b','c','e','s','f','c','s','a','d','e','e'},
                3,4,new char[]{'b','c','c','e','d'}));
        System.out.println(hasPath(new char[]{'a','b','c','e','s','f','c','s','a','d','e','e'},
                3,4,new char[]{'a','b','c','b'}));
        System.out.println(hasPath(new char[]{'a'},
                1,1,new char[]{'a'}));
    }


    int gridNum=0;
    private boolean check(int x,int y,int threshold){
        int tmp=0;
        while (x!=0){
            tmp+=x%10;
            x/=10;
        }
        while(y!=0){
            tmp+=y%10;
            y/=10;
        }
        return tmp<=threshold;
    }
    private void movingCountDfs(int threshold, int x,int y,int rows,int cols,boolean[][] vis){
        if(x<0||x>=rows||y<0||y>=cols) return;
        if(!vis[x][y]&&check(x,y,threshold)){
            ++gridNum;
            vis[x][y]=true;
            for(int i=0;i<4;++i){
                movingCountDfs(threshold,x+d[i][0],y+d[i][1],rows,cols,vis);
            }
        }
    }
    // 66.机器人的运动范围
    public int movingCount(int threshold, int rows, int cols) {
        if(threshold<0||rows<0||cols<0) return 0;
        movingCountDfs(threshold,0,0,rows,cols,new boolean[rows][cols]);
        return gridNum;
    }

    @Test
    public void testMovingCount() {
        System.out.println(movingCount(15,20,20));// ->359
    }

    // 67.剪绳子
    public int cutRope(int target) {
        if(target<2) return 0;
        if(target==2) return 1;
        if(target==3) return 2;

        int[] dp=new int[target+1];
        dp[0]=0;
        dp[1]=1;
        dp[2]=2;
        dp[3]=3;

        //dp[i]=max{dp[j]*dp[i-j]}, j=1...i/2
        int max;
        for(int i=4;i<=target;++i){
            max=0;
            for(int j=1;j<=i/2;++j){
                max=Math.max(max,dp[j]*dp[i-j]);
            }
            dp[i]=max;
        }
        return dp[target];
    }

    //LeetCode 236. Lowest Common Ancestor of a Binary Tree
    //面试题68.树中两个节点的公共祖先
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root==null||p==root||q==root) return root;//root==null,返回null代表没有找到p,q,否则找到了就返回p,q中的任意一个
        TreeNode left=lowestCommonAncestor(root.left,p,q);//p,q是否在左子树里
        TreeNode right=lowestCommonAncestor(root.right,p,q);//p,q是否在右子树里
        if(left==null&&right!=null){//左子树中没有p,q,那么就去右子树中找
            return right;
        }else if(left!=null&&right==null) {//右子树中没有p,q,那么就去左子树中找
            return left;
        }else if(left==null&&right==null){
//            System.out.println(root.val);
            return null;
        }else{//left!=null&&right!=null
            return root;//左右子树中各存在p或q,此时root就是其最低公共祖先
        }
    }


    // 1.牛牛的消消乐
    //能够消除的方块数量=b×cnt[b,a)+a×cnt[a,a+b)+(a+b)×cnt[a+b,∞)
    public long minimumValueAfterDispel (int[] nums) {
        if(nums==null||nums.length==0) return 0;
        long sum=0;
        int len=nums.length;
        Arrays.sort(nums);
        long[] tmpNums=new long[len];
        for(int i=0;i<len;++i){
            sum+=nums[i];
            tmpNums[i]=nums[i];
        }
        long dec=0;
        //枚举a+b
        for (int a = 0; a < len;++a) {
            int ab = a;
            for (int b = 0; b <= a; ++b) {
                while (ab < len && tmpNums[ab] < tmpNums[a] + tmpNums[b]) ++ab;//找到大于等于a+b的起始元素
                long tmp = tmpNums[b] * (a - b)
                        + tmpNums[a] * (ab - a)
                        + (tmpNums[a] + tmpNums[b]) * (len - ab);
                dec = Math.max(dec, tmp);
            }
        }

        //枚举b
        for (int a = 0; a < len; a++) {
            int b = 0;
            for (int ab = a; ab < len; ab++) {
                while (b < a && tmpNums[b] < tmpNums[ab] - tmpNums[a]) ++b;
                long tmp = (tmpNums[ab] - tmpNums[a]) * (a - b)
                        + tmpNums[a] * (ab - a)
                        + tmpNums[ab] * (len - ab);
                dec = Math.max(dec, tmp);
            }
        }

        //枚举a
        for (int b = 0; b < len; b++) {
            int a = b;
            for (int ab = b; ab < len; ab++) {
                while (a < ab && tmpNums[a] < tmpNums[ab] - tmpNums[b]) ++a;
                long tmp = tmpNums[b] * (a - b)
                        + (tmpNums[ab] - tmpNums[b]) * (ab - a)
                        + tmpNums[ab] * (len - ab);
                dec = Math.max(dec, tmp);
            }
        }
        return sum - dec;
    }

    // 2.数组求和统计
    public int countLR(int[] a, int[] b) {
        if(a==null||b==null||a.length==0||b.length==0||a.length!=b.length) return 0;
        int n=a.length;
        int ans=0;
        long[] sumL=new long[n],sumR=new long[n];
        sumL[0]=a[0];
        for(int l=1;l<n;++l){
            sumL[l]=sumL[l-1]+a[l];
        }
        sumR[n-1]=a[n-1];
        for(int r=n-2;r>=0;--r){
            sumR[r]=sumR[r+1]+a[r];
        }
        long sum=0;
        for(int i=0;i<n;++i){
            sum+=a[i];
        }
        for(int l=0;l<n;++l){
            for(int r=l;r<n;++r){
                long tmp=sum-(l==0?0:sumL[l-1])-(r==n-1?0:sumR[r+1]);
                if(b[l]+b[r]==tmp){
                    ++ans;
                }
            }
        }
        return ans;
    }

    @Test
    public void testCountLR() {
        System.out.println(countLR(new int[]{1,2,3,4},new int[]{2,1,4,5}));
    }

    // 牛妹的蛋糕
    //状态转移方程: do[n]=dp[n-1]/3+1
    public int cakeNumber (int n) {
        if(n<0) throw new IllegalArgumentException();
        if(n==1) return 1;
        int[] dp=new int[n+1];
        dp[n]=1;
        for(int i=n;i>=2;--i){
            dp[i-1]=(dp[i]+1)*3/2;
        }
        return dp[1];
    }

    // 最大数
    public int solve(String s) {
        String[] list=s.split("[G-Z]");
        System.out.println(Arrays.toString(list));
        if(list.length==1) {
            return Integer.parseInt(list[0],16);
        } else{
            for(int i=0;i<list.length;++i){
                if(list[i].length()==0) continue;
                if(list[i].charAt(0)=='0'){//去除前面的0,以免影响长度
                    int pos=1;
                    for(int j=1;j<list[i].length();++j){
                        if(list[i].charAt(j)=='0') ++pos;
                        else break;
                    }
                    list[i]=list[i].substring(pos);
                }
            }
            Arrays.sort(list,(x,y)->x.length()==y.length()?-x.compareTo(y):-(x.length()-y.length()));
            return Integer.parseInt(list[0],16);
        }
    }
    @Test
    public void testSolve() {
        System.out.println(solve("0961237Z"));
    }

    // 牛妹的礼物
    public int selectPresent(int[][] presentVolumn) {
        if(presentVolumn==null||presentVolumn.length==0||presentVolumn[0].length==0) return 0;
        int n=presentVolumn.length,m=presentVolumn[0].length;
        int[][] dp=new int[n+1][m+1];
        for(int i=1;i<=n;++i) dp[i][1]=dp[i-1][1]+presentVolumn[i-1][0];
        for(int j=1;j<=m;++j) dp[1][j]=dp[1][j-1]+presentVolumn[0][j-1];
        for(int i=2;i<=n;++i){
            for(int j=2;j<=m;++j){
                dp[i][j]=Math.min(Math.min(dp[i-1][j],dp[i][j-1]),dp[i-1][j-1])+presentVolumn[i-1][j-1];
            }
        }
        return dp[n][m];
    }

    @Test
    public void testSelectPresent() {
        System.out.println(selectPresent(new int[][]{
                {1,2,3},
                {2,3,4}
        }));
    }

    // 字符串距离计算
    public int GetMinDistance (String S1, String S2) {
        if(S1==null||S2==null||S1.length()!=S2.length()) throw new IllegalArgumentException();
        char[] s1=S1.toCharArray(),s2=S2.toCharArray();
        int ans=Integer.MAX_VALUE;
        for(char x1='a';x1<='z';++x1){
            for(char x2='a';x2<='z';++x2){
                int tmp=0;
                for(int i=0;i<s1.length;++i){
                    if(s1[i]==x1){
                        s1[i]=x2;//替换
                        if(s1[i]!=s2[i]) ++tmp;//计算距离
                        s1[i]=x1;//恢复
                    }else{
                        if(s1[i]!=s2[i]) ++tmp;//计算距离
                    }
                }
                ans=Math.min(ans,tmp);
            }
        }
        return ans;
    }

    @Test
    public void testGetMinDistance() {
        System.out.println(GetMinDistance("aabb","cdef"));
    }

    // 牛能和牛可乐的礼物
    public int maxPresent (int[] presentVec) {
        if(presentVec==null||presentVec.length==0) return 0;
        if(presentVec.length==1) return presentVec[0];
        int len=presentVec.length;
        int sum=0;
        for(int i=0;i<len;++i){
            sum+=presentVec[i];
        }
        int V=(sum+1)/2;//设定容量为一半
        int[] dp=new int[V+1];
        //这里的容量和presentVec的价值相同,容量(体积)和价值都是题目中给的"礼物价值"
        for(int i=0;i<len;++i){
            for(int v=V;v>0;--v){
                if(v>=presentVec[i])//容量还够
                    dp[v]=Math.max(dp[v],dp[v-presentVec[i]]+presentVec[i]);
            }
        }
        return Math.abs(sum-2*dp[V]);
    }

    @Test
    public void testMaxPresent() {
        System.out.println(maxPresent(new int[]{1,3,5}));
    }

    // 牛妹的面试
    //正反各一次最长递增子序列,然后拼接起来计算即可
    public int mountainSequence(int[] numberList) {
        if(numberList==null||numberList.length==0) return 0;
        int len=numberList.length;
        int[] dp1=new int[len],dp2=new int[len];
        Arrays.fill(dp1,1);
        Arrays.fill(dp2,1);
        for(int i=1;i<len;++i){
            for(int j=0;j<i;++j){
                if(numberList[i]>numberList[j]){
                    dp1[i]=Math.max(dp1[i],dp1[j]+1);
                }
            }
        }
        for(int i=len-2;i>=0;--i){
            for(int j=len-1;j>i;--j){
                if(numberList[i]>numberList[j]){
                    dp2[i]=Math.max(dp2[i],dp2[j]+1);
                }
            }
        }
        int ans=0;
        for(int i=0;i<len;++i){
            ans=Math.max(ans,dp1[i]+dp2[i]-1);//将以i为结尾的两个自增序列拼接起来,i处的数字重复计数了一次,所以要减一
        }
        return ans;
    }

    @Test
    public void testMountainSequence() {
        System.out.println(mountainSequence(new int[]{1,2,3,6,1}));
    }

    // 递增数组
    public long IncreasingArray (int[] array) {
        if(array==null||array.length==0) return 0;
        int len=array.length;
        long ans=0;
        for(int i=0;i<len-1;++i){
            if(array[i+1]<=array[i]){
                ans+=(array[i]-array[i+1]+1);
            }
        }
        return ans;
    }

    @Test
    public void testIncreasingArray() {
        System.out.println(IncreasingArray(new int[]{1,2,1}));
    }

    // 远亲不如近邻
    public int[] solve(int n, int m, int[] a, int[] x) {
        if(n!=a.length||m!=x.length) throw new IllegalArgumentException();
        int[] ans=new int[m];
        Arrays.sort(a);
        for(int i=0;i<m;++i){
            if(x[i]<a[0]) ans[i]=a[0]-x[i];
            else if(x[i]>a[n-1]) ans[i]=x[i]-a[n-1];
            else{
                int pos=Arrays.binarySearch(a,x[i]);
                if(pos<0){
                    pos=-1*pos-1;
                    ans[i]=Math.min(x[i]-a[pos-1],a[pos]-x[i]);
                }
            }
        }
        return ans;
    }


    // 牛牛找工作
    public static void findWork() throws IOException {
        StreamTokenizer tokenizer=new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        tokenizer.nextToken();
        int N= (int) tokenizer.nval;
        tokenizer.nextToken();
        int M= (int) tokenizer.nval;
        TreeMap<Integer,Integer> mp=new TreeMap<>();
        int[][] array=new int[N][2];
        for(int i=0;i<N;++i){
            tokenizer.nextToken();
            array[i][0]= (int) tokenizer.nval;
            tokenizer.nextToken();
            array[i][1]= (int) tokenizer.nval;
        }
        Arrays.sort(array, Comparator.comparingInt(x -> x[0]));
        for(int i=1;i<N;++i){
            array[i][1]=Math.max(array[i-1][1],array[i][1]);//计算难度不大于array[i][0]的报酬的最大值(因为已经排过序,所以可以递推)
        }
        for(int i=0;i<N;++i){
            mp.put(array[i][0],array[i][1]);//存储: 难度 -> 最大的报酬
        }
        for(int i=0;i<M;++i){
            tokenizer.nextToken();
            int ability= (int) tokenizer.nval;
            Integer index=mp.floorKey(ability);
            if(index==null) System.out.println(0);
            else System.out.println(mp.get(index));
        }
    }


    //序列和
    //给出一个正整数N和长度L，找出一段长度大于等于L的连续非负整数，他们的和恰好为N。答案可能有多个，我们需要找出长度最小的那个。
    //tip: 时间复杂度: O(N)
//    public static void seqSum(int N,int L){
//        int l=1,r=2;
//        int sum=l+r;
//        int minLen=Integer.MAX_VALUE;
//        int ansL=l,ansR=r;
//        while(r<N){
//            if(sum==N){
//                int len=r-l+1;
//                if(len>=L&&len<100){
//                    minLen=Math.min(minLen,len);
//                    ansL=l;
//                    ansR=r;
//                }
//                sum-=l;
//                ++l;
//            }else if(sum<N){
//                ++r;
//                sum+=r;
//            }else if(sum>N){
//                sum-=l;
//                ++l;
//            }
//        }
//        if(minLen!=Integer.MAX_VALUE){
//            for(int i=ansL;i<=ansR-1;++i){
//                System.out.print(i+" ");
//            }
//            System.out.println(ansR);
//        }else{
//            System.out.println("No");
//        }
//    }

    //tip: 时间复杂度O(K)
    public static void seqSum(int N,int L){
        int minLen=Integer.MAX_VALUE;
        int ansL=0,ansR=0;
        for(int len=L;len<=100;++len){
            int tmp=2*N-len*(len-1);
            if(tmp<0) continue;
            if(tmp%(2*len)==0){
                minLen=Math.min(minLen,len);
                ansL=tmp/(2*len);
                ansR=ansL+len-1;
                break;
            }
        }

        if(minLen!=Integer.MAX_VALUE){
            for(int i=ansL;i<=ansR-1;++i){
                System.out.print(i+" ");
            }
            System.out.println(ansR);
        }else{
            System.out.println("No");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String input=reader.readLine();
        String[] tmp=input.split(" ");
        seqSum(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]));

//        StreamTokenizer tokenizer=new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//        while(tokenizer.nextToken() != StreamTokenizer.TT_EOF){
//            System.out.println((int)tokenizer.nval);
//        }
    }
}
