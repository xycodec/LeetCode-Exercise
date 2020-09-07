package com.xycode.utils.tree;

/**
 * ClassName: AVLTree
 *
 * @Author: xycode
 * @Date: 2020/4/24
 * @Description: this is description of the AVLTree class
 **/
public class AVLTree extends BSTree {
    protected class AVLTreeNode extends TreeNode {
        int factor;

        public AVLTreeNode(int x) {
            super(x);
        }

        public AVLTreeNode(int val, TreeNode parent) {
            super(val, parent);
        }
    }

    private AVLTreeNode root;

    public AVLTree() {
    }

    @Override
    public void insert(int val) {

    }
}
