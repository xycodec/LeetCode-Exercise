package com.xycode.utils.tree;

/**
 * ClassName: RedBlackTree
 *
 * @Author: xycode
 * @Date: 2020/4/23
 * @Description: this is description of the RedBlackTree class
 **/
public class RedBlackTree {
    private static final int RED = 0, BLACK = 1;

    private class TreeNode {
        int val;
        int color;
        TreeNode lChild, rChild;
        TreeNode parent;

        public TreeNode(int val, TreeNode parent) {
            this.color = BLACK;
            this.val = val;
            this.parent = parent;
        }

        public TreeNode(int val, int color, TreeNode parent) {
            this.val = val;
            this.color = color;
            this.parent = parent;
        }
    }

    private TreeNode root;

    private void insertCore(TreeNode root, int val) {
        if (val > root.val) {
            if (root.rChild == null) {
                root.rChild = new TreeNode(val, RED, root);
            } else {
                insertCore(root.rChild, val);
            }
        } else {
            if (root.lChild == null) {
                root.lChild = new TreeNode(val, RED, root);
            } else {
                insertCore(root.lChild, val);
            }
        }
    }

    public void insert(int val) {
        if (root == null) {
            root = new TreeNode(val, null);
            return;
        }
        insertCore(root, val);
    }

    private void leftRotate() {

    }

    public static void main(String[] args) {

    }
}
