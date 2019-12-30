package com.xycode.leetcode;

import org.testng.annotations.Test;

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
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root==null) return null;
        TreeNode dNode=root;
        TreeNode parentNode=null;
        while (dNode!=null&&key!=dNode.val){
            parentNode=dNode;
            if(key>dNode.val){
                dNode=dNode.right;
            }else{
                dNode=dNode.left;
            }
        }
        if(dNode==null) return root;
        if(dNode.left==null&&dNode.right==null){//待删除的是叶子节点
            if(dNode==root) return null;//树中只有一个节点,并且是要删除根节点
            if(parentNode.left==dNode) parentNode.left=null;
            else parentNode.right=null;
        }else if(dNode.left!=null&&dNode.right!=null){//待删除的节点的左右子节点都不为null
            TreeNode tmp=dNode.right;//找到右子树的最左节点
            parentNode=null;
            while(tmp.left!=null){
                parentNode=tmp;
                tmp=tmp.left;
            }
            if(dNode.right==tmp){//找到的是待删除节点的右子节点
                dNode.val=tmp.val;
                dNode.right=tmp.right;
                tmp.right=null;
            }else{//找到的是更深的子孙节点
                parentNode.left=tmp.right;
                dNode.val=tmp.val;
                tmp.right=null;
            }
        }else{//待删除的节点的子节点有一个为null,此时直接替换子树即可
            if(dNode.left!=null){
                TreeNode tmp=dNode.left;
                dNode.val=tmp.val;
                dNode.left=tmp.left;
                dNode.right=tmp.right;
            }else {
                TreeNode tmp=dNode.right;
                dNode.val=tmp.val;
                dNode.left=tmp.left;
                dNode.right=tmp.right;
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

    @Test
    public void testFindRedundantDirectedConnection(){
        int[][] array= {{5,2},{5,1},{3,1},{3,4},{3,5}};
        int[] ans=findRedundantDirectedConnection(array);
        System.out.println(ans[0]+", "+ans[1]);
    }

}
