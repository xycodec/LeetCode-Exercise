//https://leetcode.com/problems/validate-binary-search-tree/
/**
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     TreeNode *left;
 *     TreeNode *right;
 *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
 * };
 */
class Solution {
public:
    vector<int> v;
    bool flag=true;
    void inorderTraversal(TreeNode* root) {
        if(!flag) return;
        if(root==NULL) return;
        inorderTraversal(root->left);
        if(!v.empty()&&v.back()>=root->val){
            flag=false;
            return;
        }
        v.push_back(root->val);
        inorderTraversal(root->right);
        return;
    }
    
    //基于这样一个事实: 二叉搜索树的中序遍历一定是顺序的
    bool isValidBST(TreeNode* root) {
        if(root==NULL) return true;
        inorderTraversal(root);
        return flag;
    }
};