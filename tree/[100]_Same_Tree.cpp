//https://leetcode.com/problems/same-tree/
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
    bool ans=true;
    void judge(TreeNode* a,TreeNode* b) {
        if(!ans) return;
        if(a==NULL&&b==NULL) return;
        else if((a==NULL&&b!=NULL)||(a!=NULL&&b==NULL)){
            ans=false;
            return;
        }
        judge(a->left,b->left);
        if(a->val!=b->val){
            ans=false;
            return;
        }
        judge(a->right,b->right);
    }
    
    //利用中序遍历的过程来判断
    bool isSameTree(TreeNode* p, TreeNode* q) {
        judge(p,q);
        return ans;
    }
};