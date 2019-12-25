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



     // Definition for a Node.
     class Node {
         public int val;
         public List<Node> neighbors;

         public Node() {}

         public Node(int _val,List<Node> _neighbors) {
             val = _val;
             neighbors = _neighbors;
         }
    }


    Set<Integer> s=new HashSet<>();
    void dfs(Node node,Map<Integer,List<Integer> > mp){
        for(Node tmp:node.neighbors){
            if(!mp.containsKey(node.val)){
                mp.put(node.val,new ArrayList<>());
            }
            mp.get(node.val).add(tmp.val);
//            System.out.println(node.val+" : "+tmp.val);
            if(!s.contains(tmp.val)){
                s.add(tmp.val);
                dfs(tmp,mp);
            }
        }
    }

    //133. Clone Graph
    public Node cloneGraph(Node node) {
        s.add(node.val);
        Map<Integer,List<Integer>> mp=new HashMap<>();
        dfs(node,mp);
        Map<Integer,Node> mp2=new HashMap<>();
        Node first=new Node(node.val,new ArrayList<>());
        mp2.put(first.val,first);
        for(int val:mp.keySet()){
            if(!mp2.containsKey(val)){
                Node tmp=new Node(val,new ArrayList<>());
                mp2.put(val,tmp);
            }
            for(int neighborVal:mp.get(val)){
                if(!mp2.containsKey(neighborVal)){
                    Node tmp=new Node(neighborVal,new ArrayList<>());
                    mp2.put(neighborVal,tmp);
                }
                mp2.get(val).neighbors.add(mp2.get(neighborVal));
            }

        }
        return first;
    }
}
