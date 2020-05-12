package com.xycode.utils.tree;

/**
 * ClassName: BSTree
 *
 * @Author: xycode
 * @Date: 2020/4/23
 * @Description: this is description of the BSTree class
 **/
public class BSTree {
    protected class TreeNode {
        int val;
        TreeNode lChild,rChild;
        TreeNode parent;
        TreeNode(int x) { val = x; }

        public TreeNode(int val, TreeNode parent) {
            this.val = val;
            this.parent = parent;
        }
    }

    private TreeNode root;

    public BSTree() {
    }

    private void insertCore(TreeNode root, int val){
        if(val>root.val){
            if(root.rChild==null){
                root.rChild=new TreeNode(val,root);
            }else{
                insertCore(root.rChild,val);
            }
        }else{
            if(root.lChild==null){
                root.lChild=new TreeNode(val,root);
            }else{
                insertCore(root.lChild,val);
            }
        }
    }

    public void insert(int val){
        if(root==null){
            root=new TreeNode(val,null);
            return;
        }
        insertCore(root,val);
    }

    protected TreeNode findCore(TreeNode root,int val){
        TreeNode tmpNode=root;
        while (tmpNode!=null&&val!=tmpNode.val){
            if(val>tmpNode.val){
                tmpNode=tmpNode.rChild;
            }else{
                tmpNode=tmpNode.lChild;
            }
        }
        return tmpNode;
    }

    public boolean find(int val){
        return findCore(root,val)!=null;
    }

    private TreeNode deleteCore(int val) {
        TreeNode deletedNode=findCore(root,val);//找到待删除的节点
        if(deletedNode==null) return null;
        if(deletedNode.lChild==null&&deletedNode.rChild==null){//待删除的是叶子节点
            if(deletedNode.parent==null) root=null;//树中只有一个节点,并且是要删除根节点
            else if(deletedNode.parent.lChild==deletedNode) deletedNode.parent.lChild=null;//左叶子节点
            else deletedNode.parent.rChild=null;
        }else if(deletedNode.lChild!=null&&deletedNode.rChild!=null){//待删除的节点的左右子节点都不为null
            TreeNode tmp=deletedNode.rChild;//找到右子树的最左节点(也就是后继节点)
            while(tmp.lChild!=null){
                tmp=tmp.lChild;
            }
            if(deletedNode.rChild==tmp){//找到的后继节点是待删除节点的右子节点
                //将后继节点的右子树提上来
                if(deletedNode.parent==null) root=tmp;//要删除的是根节点
                else if(deletedNode.parent.lChild==deletedNode) deletedNode.parent.lChild=tmp;
                else deletedNode.parent.rChild=tmp;
                tmp.lChild=deletedNode.lChild;
            }else{//找到的后继节点是更深的子孙节点
                //子树提升
                tmp.parent.lChild=tmp.rChild;
                //替换被删除的节点
                tmp.lChild=deletedNode.lChild;
                tmp.rChild=deletedNode.rChild;
                if(deletedNode.parent==null) root=tmp;//要删除的是根节点
                else if(deletedNode.parent.lChild==deletedNode) deletedNode.parent.lChild=tmp;
                else deletedNode.parent.rChild=tmp;
            }
            deletedNode.rChild=null;//help GC
            deletedNode.lChild=null;//help GC
        }else{//待删除的节点的子节点有一个为null,此时直接替换子树即可
            if(deletedNode.lChild!=null){
                if(deletedNode.parent==null) root=deletedNode.lChild;//要删除的是根节点
                else if(deletedNode.parent.lChild==deletedNode) deletedNode.parent.lChild=deletedNode.lChild;
                else deletedNode.parent.rChild=deletedNode.lChild;
                deletedNode.lChild=null;//help GC
            }else {
                if(deletedNode.parent==null) root=deletedNode.rChild;//要删除的是根节点
                else if(deletedNode.parent.lChild==deletedNode) deletedNode.parent.lChild=deletedNode.rChild;
                else deletedNode.parent.rChild=deletedNode.rChild;
                deletedNode.rChild=null;//help GC
            }
        }
        return deletedNode;
    }

    public boolean delete(int val){
        return deleteCore(val)!=null;
    }

    /**
     * 中序遍历(输出有序)
     * @param root
     */
    private void showCore(TreeNode root){
        if(root==null) return;
        showCore(root.lChild);
        System.out.print(root.val+" ");
        showCore(root.rChild);
    }

    public void show(){
        showCore(root);
        System.out.println();
    }

    public static void main(String[] args) {
        int[] array={1,3,2,7,9,2,5,4,11,6,8};
        BSTree bsTree=new BSTree();
        for(int num:array){
            bsTree.insert(num);
        }
        System.out.println(bsTree.find(10));
        System.out.println(bsTree.find(7));
        bsTree.show();
        bsTree.delete(1);
        bsTree.delete(11);
        bsTree.delete(2);
        bsTree.delete(5);
        bsTree.show();
    }

}
