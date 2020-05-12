package com.xycode.leetcode;

import com.google.common.hash.BloomFilter;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * ClassName: LeetCodeTree
 *
 * @Author: xycode
 * @Date: 2019/12/24
 * @Description: this is description of the LeetCodeTree class
 **/
public class LeetCodeTree {
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    TreeNode build(List<Integer> preorder,
                   List<Integer> inorder){
        if(preorder.isEmpty()) return null;
        int rootValue=preorder.get(0);
        TreeNode root=new TreeNode(rootValue);
        if(preorder.size()==1){
            if(inorder.size()==1){
                return root;
            }
//                else{
//                    System.err.println("illegal input!");
//                    return null;
//                }
        }

        //找到中序序列中的根节点位置,左边就是左子树序列,右边就是右子树序列
        int rootIndex=0;
        while(rootIndex<inorder.size()&&rootValue!=inorder.get(rootIndex)){
            ++rootIndex;
        }

//            if(rootIndex==inorder.size()){
//                System.err.println("illegal input!");
//                return null;
//            }

        if(rootIndex>0){
            root.left=build(preorder.subList(1,1+rootIndex),inorder.subList(0,rootIndex));
        }

        if(rootIndex<inorder.size()){
            root.right=build(preorder.subList(rootIndex+1,preorder.size()),inorder.subList(rootIndex+1,inorder.size()));
        }
        return root;
    }


    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if(preorder==null||inorder==null||preorder.length==0||inorder.length==0) return null;
        List<Integer> pre=new ArrayList<>();
        List<Integer> in=new ArrayList<>();
        for(int i=0;i<preorder.length;++i){
            pre.add(preorder[i]);
            in.add(inorder[i]);
        }

        return build(pre,in);
    }


    void inorderTraversal(TreeNode root,List<Integer> ans) {
        if(root==null) return;
        inorderTraversal(root.left,ans);
        ans.add(root.val);
        inorderTraversal(root.right,ans);
    }

    void preorderTraversal(TreeNode root,List<Integer> ans) {
        if(root==null) return;
        ans.add(root.val);
        preorderTraversal(root.left,ans);
        preorderTraversal(root.right,ans);
    }

    TreeNode build2(List<Integer> postorder,
                    List<Integer> inorder){
        if(postorder.isEmpty()) return null;
        int rootValue=postorder.get(postorder.size()-1);
        TreeNode root=new TreeNode(rootValue);
        if(postorder.size()==1){
            if(inorder.size()==1){
                return root;
            }
//            else{
//                System.err.println("illegal input!");
//                return null;
//            }
        }

        //找到中序序列中的根节点位置,左边就是左子树序列,右边就是右子树序列
        int rootIndex=0;
        while(rootIndex<inorder.size()&&rootValue!=inorder.get(rootIndex)){
            ++rootIndex;
        }

//        if(rootIndex==inorder.size()){
//            System.err.println("illegal input!");
//            return null;
//        }

        if(rootIndex>0){
            root.left=build2(postorder.subList(0,rootIndex),inorder.subList(0,rootIndex));
        }

        if(rootIndex<inorder.size()){
            root.right=build2(postorder.subList(rootIndex,postorder.size()-1),inorder.subList(rootIndex+1,inorder.size()));
        }

        return root;
    }

    public TreeNode buildTree2(int[] inorder, int[] postorder) {
        if(inorder==null||postorder==null||inorder.length==0||postorder.length==0) return null;
        List<Integer> in=new ArrayList<>();
        List<Integer> post=new ArrayList<>();
        for(int i=0;i<inorder.length;++i){
            post.add(postorder[i]);
            in.add(inorder[i]);
        }

        return build2(post,in);
    }

     //572. Subtree of Another Tree
    public boolean matchTreeCore(TreeNode s,TreeNode t){
        if(s==null&&t!=null) return false;
        if(s!=null&&t==null) return false;
        if(s==null&&t==null) return true;
        if(s.val==t.val)
            return matchTreeCore(s.left,t.left)&&matchTreeCore(s.right,t.right);
        else
            return false;
    }

    public boolean matchTree(TreeNode s, TreeNode t){
        if(s!=null&&t!=null){
            if(s.val==t.val){
                if(matchTreeCore(s,t)) return true;
            }
            return matchTree(s.left,t)||matchTree(s.right,t);
        }
        return false;
    }

    public boolean isSubtree(TreeNode s, TreeNode t) {
        return matchTree(s,t);
    }



    //297. Serialize and Deserialize Binary Tree
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
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
        q.add(root);
        sb.append(root.val).append(" ");
        --treeSize;
        while(!q.isEmpty()&&treeSize>0){//当到达树的节点个数时就退出,避免添加多余的null
            TreeNode tmpNode=q.poll();
            if(tmpNode.left!=null) {
                q.add(tmpNode.left);
                sb.append(tmpNode.left.val).append(" ");
                --treeSize;
            } else sb.append("null ");
            if(tmpNode.right!=null) {
                q.add(tmpNode.right);
                sb.append(tmpNode.right.val).append(" ");
                --treeSize;
            } else sb.append("null ");
        }
        return sb.toString().trim();
    }


    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if(data==null) return null;
        String[] tmp=data.split(" ");
        if(tmp.length==1){
            if(!tmp[0].equals("null")) return new TreeNode(Integer.parseInt(tmp[0]));
            else return null;
        }

        Queue<TreeNode> q=new LinkedList<>();
        TreeNode root=new TreeNode(Integer.parseInt(tmp[0]));
        q.add(root);
        int index=1;
        while(index<tmp.length){
            TreeNode fatherNode=q.poll();
            if(!tmp[index].equals("null")){
                TreeNode childNode=new TreeNode(Integer.parseInt(tmp[index]));
                fatherNode.left=childNode;
                q.add(childNode);
            }else{
                fatherNode.left=null;
            }
            ++index;
            if(index<tmp.length&&!tmp[index].equals("null")){
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

    @Test
    public void testSerialize(){
        TreeNode root=new TreeNode(1);
        root.left=new TreeNode(2);
        root.right=new TreeNode(3);

        root.left.left=new TreeNode(4);
        root.right.right=new TreeNode(5);

        System.out.println(serialize(root));
        System.out.println(serialize(deserialize("1 2 3 4 null null 5")));
    }

    public void levelDfs(TreeNode root,int depth,List<Integer> level,List<Integer> val){
        if(root==null) return;
//        System.out.println(depth+" : "+root.val);
        level.add(depth);
        val.add(root.val);
        levelDfs(root.left,depth+1,level,val);
        levelDfs(root.right,depth+1,level,val);

    }

    //102. Binary Tree Level Order Traversal
    //tip: 基于这样一个事实: 前序遍历序列中,对于同一depth的节点来说,必定是从左到右顺序的(只是可能混进了一些更深depth的节点遍历)
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans=new ArrayList<>();
        if(root==null) return ans;
//        List<Integer> order=new ArrayList<>();
//        Queue<TreeNode> q=new LinkedList<>();
//        q.add(root);
//        while(!q.isEmpty()){
//            TreeNode tmpNode=q.poll();
//            order.add(tmpNode.val);
//            if(tmpNode.left!=null) {
//                q.add(tmpNode.left);
//            }
//            if(tmpNode.right!=null) {
//                q.add(tmpNode.right);
//            }
//        }

        List<Integer> level=new ArrayList<>();
        List<Integer> val=new ArrayList<>();
        levelDfs(root,1,level,val);

        Set<Integer> s=new HashSet<>();
        for(int i=0;i<level.size();++i){
            s.add(level.get(i));
        }

        for(int i=0;i<s.size();++i) ans.add(new ArrayList<>());
        for(int i=0;i<val.size();++i){
            ans.get(level.get(i)-1).add(val.get(i));
        }
        for(int i=0;i<ans.size();++i){
            for(int j=0;j<ans.get(i).size();++j){
                System.out.print(ans.get(i).get(j)+" ");
            }
            System.out.println();
        }
        return ans;
    }

    //103. Binary Tree Zigzag Level Order Traversal
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> ans=new ArrayList<>();
        if(root==null) return ans;
        ans=levelOrder(root);
        for(int i=0;i<ans.size();++i){
            if(i%2==1){
                Collections.reverse(ans.get(i));
            }
        }
        System.out.println("----zigzag----");
        for(int i=0;i<ans.size();++i){
            for(int j=0;j<ans.get(i).size();++j){
                System.out.print(ans.get(i).get(j)+" ");
            }
            System.out.println();
        }
        return ans;
    }

    @Test
    public void testLevelOrder(){
        levelOrder(deserialize("1 2 3 4 null null 5"));
        zigzagLevelOrder(deserialize("1 2 3 4 null null 5"));
    }

    void bstTraversal(TreeNode root,List<Integer> ans) {
        if(root==null) return;
        bstTraversal(root.left,ans);
        ans.add(root.val);
        bstTraversal(root.right,ans);
    }

    public int kthSmallest(TreeNode root, int k) {
        if(root==null) return -1;
        List<Integer> tmp=new ArrayList<>();
        bstTraversal(root,tmp);
        if(k>tmp.size()) return -1;
        return tmp.get(k-1);
    }

    @Test
    public void testKthSmallest(){
        System.out.println(kthSmallest(deserialize("3 1 4 null 2"),1));
    }


    //450. Delete Node in a BST
    //tip 描述: 在一颗二叉搜索树中删除一个节点
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root==null) return null;
        TreeNode deletedNode=root;
        TreeNode parentNode=null;//记录待删除节点的父节点
        while (deletedNode!=null&&key!=deletedNode.val){
            parentNode=deletedNode;
            if(key>deletedNode.val){
                deletedNode=deletedNode.right;
            }else{
                deletedNode=deletedNode.left;
            }
        }
        if(deletedNode==null) return root;
        if(deletedNode.left==null&&deletedNode.right==null){//待删除的是叶子节点
            if(deletedNode==root) return null;//树中只有一个节点,并且是要删除根节点
            if(parentNode.left==deletedNode) parentNode.left=null;
            else parentNode.right=null;
        }else if(deletedNode.left!=null&&deletedNode.right!=null){//待删除的节点的左右子节点都不为null
            TreeNode tmp=deletedNode.right;//找到右子树的最左节点(也就是后继节点)
            TreeNode successorParentNode=null;//记录后继节点的父节点
            while(tmp.left!=null){
                successorParentNode=tmp;
                tmp=tmp.left;
            }
            if(deletedNode.right==tmp){//找到的后继节点是待删除节点的右子节点
                if(parentNode==null) root=tmp;//要删除的是根节点
                else if(parentNode.left==deletedNode) parentNode.left=tmp;
                else parentNode.right=tmp;
                tmp.left=deletedNode.left;
            }else{//找到的后继节点是更深的子孙节点
                successorParentNode.left=tmp.right;

                tmp.left=deletedNode.left;
                tmp.right=deletedNode.right;
                if(parentNode==null) root=tmp;//要删除的是根节点
                else if(parentNode.left==deletedNode) parentNode.left=tmp;
                else parentNode.right=tmp;
            }
            deletedNode.left=null;//help GC
            deletedNode.right=null;//help GC
        }else{//待删除的节点的子节点有一个为null,此时直接替换子树即可
            if(deletedNode.left!=null){
                if(parentNode==null) root=deletedNode.left;//要删除的是根节点
                else if(parentNode.left==deletedNode) parentNode.left=deletedNode.left;
                else parentNode.right=deletedNode.left;
                deletedNode.left=null;//help GC
            }else {
                if(parentNode==null) root=deletedNode.right;//要删除的是根节点
                else if(parentNode.left==deletedNode) parentNode.left=deletedNode.right;
                else parentNode.right=deletedNode.right;
                deletedNode.right=null;//help GC
            }
        }
        return root;
    }

    @Test
    public void testDeleteNode(){
        System.out.println(serialize(deleteNode(deserialize("1 null 2"),1)));
    }

    int ans=0;
    void rangeSumBSTTraversal(TreeNode root,int L,int R) {
        if(root==null) return;
        rangeSumBSTTraversal(root.left,L,R);
        if(root.val>=L&&root.val<=R) ans+=root.val;
        rangeSumBSTTraversal(root.right,L,R);
    }

    public int rangeSumBST(TreeNode root, int L, int R) {
        rangeSumBSTTraversal(root,L,R);
        return ans;
    }

    @Test
    public void testRangeSumBST(){
        System.out.println(rangeSumBST(deserialize("18 9 27 6 15 24 30 3 null 12 null 21"),18,24));
    }



    void swap(TreeNode t1,TreeNode t2){
        int tmp=t1.val;
        t1.val=t2.val;
        t2.val=tmp;
    }
    void recoverTreeDfs(TreeNode root,List<TreeNode> ans) {
        if(root==null) return;
        recoverTreeDfs(root.left,ans);
        ans.add(root);
        recoverTreeDfs(root.right,ans);
    }

    //99. Recover Binary Search Tree
    public void recoverTree(TreeNode root) {
        if(root==null) return;
        List<TreeNode> order=new ArrayList<>();
        recoverTreeDfs(root,order);
        int pos=-1;
        for(int i=0;i<order.size()-1;++i){
            if(order.get(i).val>order.get(i+1).val){
                pos=i;
                break;
            }
        }
        if(pos==-1) return;
        for(int i=pos+1;i<order.size();++i){
            swap(order.get(pos),order.get(i));
            if(order.get(pos).val<order.get(pos+1).val){
                if(order.get(i).val>order.get(i-1).val){
                    if(i==order.size()-1||order.get(i).val<order.get(i+1).val){
                        return;
                    }
                }
            }
            swap(order.get(pos),order.get(i));
        }
    }


    //根据一个有序数组建立一个平衡BST
    TreeNode buildBST(List<Integer> inorder){
        if(inorder.isEmpty()) return null;
        int rootIndex=inorder.size()/2;
        int rootValue=inorder.get(rootIndex);//每次取中位数,将其作为根节点,其左边就是左子树序列,右边就是右子树序列
        TreeNode root=new TreeNode(rootValue);
        if(inorder.size()==1){
            return root;
        }

        root.left=buildBST(inorder.subList(0,rootIndex));
        root.right=buildBST(inorder.subList(rootIndex+1,inorder.size()));

        return root;
    }

    //108. Convert Sorted Array to Binary Search Tree
    public TreeNode sortedArrayToBST(int[] nums) {
        if(nums==null||nums.length==0) return null;
        List<Integer> inorder=new ArrayList<>();
        for(int i=0;i<nums.length;++i){
            inorder.add(nums[i]);
        }

        return buildBST(inorder);
    }

    @Test
    public void testSortedArrayToBST(){
        int[] array={-10,-3,0,5,9};
        System.out.println(serialize(sortedArrayToBST(array)));
    }


    void recoverDfs(TreeNode root,int depth,List<List<Integer>> lists) {
        if(depth<lists.size()&&!lists.get(depth).isEmpty()){
            root.left=new TreeNode(lists.get(depth).get(0));
            lists.get(depth).remove(0);
            recoverDfs(root.left,depth+1,lists);
            if(lists.get(depth).isEmpty()) {
                root.right=new TreeNode(lists.get(depth).get(lists.get(depth).size()-1));
                lists.get(depth).remove(lists.get(depth).size()-1);
                recoverDfs(root.right,depth+1,lists);
            }
        }
    }


    //1028. Recover a Tree From Preorder Traversal
    //tip: 基于这样一个事实: 前序遍历序列中,对于同一depth的节点来说,必定是从左到右顺序的(只是可能混进了一些更深depth的节点遍历)
    public TreeNode recoverFromPreorder(String S) {
        if(S==null||S.length()==0) return null;
        int len=S.length();
        Map<Integer, TreeNode> map = new HashMap<>();
        int pos = 0;
        while(pos < len && S.charAt(pos) != '-') pos++;
        TreeNode root = new TreeNode(Integer.parseInt(S.substring(0,pos)));
        map.put(0, root);
        while(pos<len) {
            int prev = pos;
            while(S.charAt(pos) == '-') pos++;
            int depth = pos - prev - 1;
            TreeNode parent = map.get(depth);//先序遍历保证深度从小到大,所以这里不可能是null
            prev = pos;
            while(pos < len && S.charAt(pos) != '-') pos++;
            TreeNode child = new TreeNode(Integer.parseInt(S.substring(prev,pos)));
            if(parent.left == null) parent.left = child;//如果只有一个节点,那么左子节点优先,根据先序遍历的规律: 根->左->右,并且这里的depth将序列划分好了,同一depth一定都是 根->左->右,
            else parent.right = child;
            map.put(depth+1,child);//实际上随着前序遍历,同一depth上的节点会被不断覆盖
        }
        return root;
    }


    //N元树
    class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    @Test
    public void testRecoverFromPreorder(){
        System.out.println(serialize(recoverFromPreorder("1-401--349---90--88")));
    }

    //429. N-ary Tree Level Order Traversal
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> ans=new ArrayList<>();
        if(root==null) return ans;
        Queue<Node> q=new LinkedList<>();
        Queue<Integer> levelQ=new LinkedList<>();
        q.add(root);
        levelQ.add(0);
        while(!q.isEmpty()){
            Node tmpNode=q.poll();
            int level=levelQ.poll();
            if(level==ans.size()) ans.add(new ArrayList<>());
            ans.get(level).add(tmpNode.val);
            for (Node child : tmpNode.children) {
                q.add(child);
                levelQ.add(level+1);
            }
        }
        return ans;
    }


    @Test
    public void testFindRedundantConnection(){
        int[][] array= {{5,2},{5,1},{3,1},{3,4},{3,5}};
        int[] ans=findRedundantConnection(array);
        System.out.println(ans[0]+", "+ans[1]);
    }

    //684. Redundant Connection
    public int[] findRedundantConnection(int[][] edges) {
        if(edges==null||edges.length==0) return null;
        //record all edges that are not inside the ring
        Set<Integer> s=new HashSet<>();
        int[] degree=new int[edges.length+1];
        while(true){
            boolean flag=false;
            for(int i=0;i<edges.length;++i){
                if(!s.contains(edges[i][0])&&!s.contains(edges[i][1])){
                    degree[edges[i][0]]+=1;
                    degree[edges[i][1]]+=1;
                }
            }
            for(int i=1;i<=edges.length;++i){
                if(degree[i]==1){
                    s.add(i);
                    flag=true;
                }
                degree[i]=0;
            }
            if(!flag) break;
        }

        int[] ans=new int[2];
        for(int i=edges.length-1;i>=0;--i){
            if(!s.contains(edges[i][0])&&!s.contains(edges[i][1])){
                ans[0]=edges[i][0];
                ans[1]=edges[i][1];
                return ans;
            }
        }
        return null;
    }


    //685. Redundant Connection II
    public int[] findRedundantDirectedConnection(int[][] edges) {
        if(edges==null||edges.length==0) return null;
        //将非环内的边全部记录下来
        Set<Integer> s=new HashSet<>();
        int[] degree=new int[edges.length+1];
        while(true){
            boolean flag=false;
            for (int[] edge : edges) {
                if (!s.contains(edge[0]) && !s.contains(edge[1])) {
                    degree[edge[0]] += 1;
                    degree[edge[1]] += 1;
                }
            }
            for(int i=1;i<=edges.length;++i){
                if(degree[i]==1){
                    s.add(i);
                    flag=true;
                }
                degree[i]=0;//清空
            }
            if(!flag) break;
        }

        for (int[] edge : edges) {
            degree[edge[1]] += 1;//入度
        }
        int[] ans=new int[2];
        for(int i=edges.length-1;i>=0;--i){
            if(!s.contains(edges[i][0])&&!s.contains(edges[i][1])){//一个节点有多个parent
                if(degree[edges[i][1]]>1){
                    ans[0]=edges[i][0];
                    ans[1]=edges[i][1];
                    return ans;
                }
            }
        }

        //执行到这儿,说明图中parent仅有一个环,那就从后往前删掉第一条符合的边即可
        for(int i=edges.length-1;i>=0;--i){
            if(!s.contains(edges[i][0])&&!s.contains(edges[i][1])){
                ans[0]=edges[i][0];
                ans[1]=edges[i][1];
                return ans;
            }
        }
        return null;
    }



    //114. Flatten Binary Tree to Linked List
    void flatDfs(TreeNode root,List<Integer> ans) {
        if(root==null) return;
        ans.add(root.val);
        flatDfs(root.left,ans);
        flatDfs(root.right,ans);
    }


    //左子树的最右节点与rootNode连接,并且还与右子树的根节点(所组成的链表)相连接
    TreeNode flat(TreeNode root) {
        if(root==null) return null;
        TreeNode tmpLeft=flat(root.left),tmpRight=flat(root.right);
        if(tmpLeft!=null){
            root.right=tmpLeft;//root的right(next)指针指向左子树的根节点所组成的链表

            TreeNode tmpNode=tmpLeft;
            while (tmpNode.right!=null) tmpNode=tmpNode.right;//找到左子树的最右节点
            tmpNode.right=tmpRight;
        }else{//root没有左子树
            root.right=tmpRight;
        }
        root.left=null;//这里是单向链表,right充当next指针,所以left置为null
        return root;
    }

    public void flatten(TreeNode root) {
        if(root==null) return;
        //tip: 该方法space complexity: O(N), time complexity: O(N)
//        List<Integer> list=new ArrayList<>();
//        flatDfs(root,list);
//        TreeNode tmpNode=root;
//        tmpNode.left=null;
//        for (int i=1;i<list.size();++i) {
//            TreeNode next=new TreeNode(list.get(i));
//            tmpNode.right=next;
//            tmpNode=next;
//        }

        //tip: 更好的方法,space complexity: O(1), time complexity: O(N)
        flat(root);
    }


    public void dfs(int node, int parent,List<Set<Integer>> neighbors,int[] ans,int[] count) {
        for (int child: neighbors.get(node))
            if (child != parent) {
                dfs(child, node,neighbors,ans,count);
                count[node] += count[child];
                ans[node] += ans[child] + count[child];
            }
    }

    public void dfs2(int node, int parent,List<Set<Integer>> neighbors,int[] ans,int[] count,int N) {
        for (int child: neighbors.get(node))
            if (child != parent) {
                ans[child] = ans[node] - count[child] + N - count[child];
                dfs2(child, node,neighbors,ans,count,N);
            }
    }

    //834. Sum of Distances in Tree
    public int[] sumOfDistancesInTree(int N, int[][] edges) {
        int[] ans=new int[N];
        if(edges==null||edges.length==0) return ans;
//        Map<Integer,List<Integer>> neighbors=new HashMap<>();//适合N较大的情况,更省空间
        List<Set<Integer>> neighbors=new ArrayList<>();//适合N较小的情况,访问速度略快
        int[] count=new int[N];
        Arrays.fill(count,1);
        for(int i=0;i<N;++i) neighbors.add(new HashSet<>());
        for(int[] edge:edges){
            neighbors.get(edge[0]).add(edge[1]);
            neighbors.get(edge[1]).add(edge[0]);
        }
        dfs(0, -1,neighbors,ans,count);
        dfs2(0, -1,neighbors,ans,count,N);
        return ans;
    }

    @Test
    public void testFindRedundantDirectedConnection(){
        int[][] array= {{5,2},{5,1},{3,1},{3,4},{3,5}};
        int[] ans=findRedundantDirectedConnection(array);
        System.out.println(ans[0]+", "+ans[1]);
    }


    //208. Implement Trie (Prefix Tree)
    //tip: 字典树
    class Trie {
        private TrieNode root;

        class TrieNode{
            char val;
            boolean isEnd;
            TrieNode[] children=new TrieNode[26];

            public TrieNode(char val) {
                this.val = val;
            }
        }

        /** Initialize your data structure here. */
        public Trie() {
            this.root = new TrieNode(' ');
        }

        /** Inserts a word into the trie. */
        public void insert(String word) {
            if(word==null||word.length()==0) return;
            int len=word.length();
            TrieNode tmpNode=root;
            for(int i=0;i<len;++i){
                char c=word.charAt(i);
                if(tmpNode.children[c-'a']==null){
                    tmpNode.children[c-'a']=new TrieNode(c);
                }
                tmpNode=tmpNode.children[c-'a'];
            }
            tmpNode.isEnd=true;
        }

        /** Returns if the word is in the trie. */
        public boolean search(String word) {
            if(word==null||word.length()==0) return false;
            int len=word.length();
            TrieNode tmpNode=root;
            for(int i=0;i<len;++i){
                char c=word.charAt(i);
                if(tmpNode.children[c-'a']==null){
                    return false;
                }
                tmpNode=tmpNode.children[c-'a'];
            }
            return tmpNode.isEnd;
        }

        /** Returns if there is any word in the trie that starts with the given prefix. */
        public boolean startsWith(String prefix) {
            if(prefix==null||prefix.length()==0) return false;
            int len=prefix.length();
            TrieNode tmpNode=root;
            for(int i=0;i<len;++i){
                char c=prefix.charAt(i);
                if(tmpNode.children[c-'a']==null){
                    return false;
                }
                tmpNode=tmpNode.children[c-'a'];
            }
            return true;
        }
    }

    @Test
    public void testTrie() {
        Trie trie=new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));
        System.out.println(trie.search("app"));
        System.out.println(trie.startsWith("app"));
        trie.insert("app");
        System.out.println(trie.search("app"));
    }

    private void findWordsDfs(char[][] board,int x,int y,String curStr,boolean[][] vis,Trie trie,Set<String> result){
        if(x<0||x>=board.length||y<0||y>=board[0].length) return;
        if(vis[x][y]) return;
        curStr+=board[x][y];
        //tip: 使用字典树进行快速判断
        if(!trie.startsWith(curStr)) return;
        if(trie.search(curStr))
            result.add(curStr);//这里不return的原因是可能有这样的单词: app,apple,匹配到app的话就return,可能会错过匹配apple

        vis[x][y]=true;
        findWordsDfs(board,x+1,y,curStr,vis,trie,result);
        findWordsDfs(board,x-1,y,curStr,vis,trie,result);
        findWordsDfs(board,x,y+1,curStr,vis,trie,result);
        findWordsDfs(board,x,y-1,curStr,vis,trie,result);
        vis[x][y]=false;
    }

    //212. Word Search II (使用字典树)
    //tip 描述: 给定一个2维字母列表和一个单词列表,在二维列表中找单词,每个单词都必须由连续相邻单元格的字母构成，方向为上下左右。
    // 同一个字母单元格在一个单词中不能使用多次。
    public List<String> findWords(char[][] board, String[] words) {
        if(board==null||words==null||board.length==0||words.length==0) return new ArrayList<>();
        Trie trie=new Trie();
        for(int i=0;i<words.length;++i){
            trie.insert(words[i]);
        }
        int m=board.length,n=board[0].length;
        boolean[][] vis=new boolean[m][n];
        Set<String> result=new HashSet<>();//使用HashSet,防止重复添加,因为可能有多条路径匹配一个单词
        for(int i=0;i<m;++i){
            for(int j=0;j<n;++j){
                findWordsDfs(board,i,j,"",vis,trie,result);
            }
        }
        return new ArrayList<>(result);
    }

    @Test
    public void testFindWords() {
        System.out.println(findWords(new char[][]{
                {'o','a','a','n'},
                {'e','t','a','e'},
                {'i','h','k','r'},
                {'i','f','l','v'}
        },new String[]{"oath","pea","eat","rain"}));
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

}
