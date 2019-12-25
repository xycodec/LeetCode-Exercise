//https://leetcode.com/problems/symmetric-tree/
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
    vector<int> vi;
    vector<char> vc;//record the structure information of the binary tree
    void inorderTraversal(TreeNode* root,char c) {
        if(root==NULL) return;
        inorderTraversal(root->left,'L');
        vc.push_back(c);
        vi.push_back(root->val);
        inorderTraversal(root->right,'R');
    }
    
    //对称二叉树的中序遍历一定是回文的(反过来未必成立,所以还需记录结构信息来辅助判断)
    //并且记录结构信息以应对类似下面这样的情况:
    //      1
    //     / \
    //    2   2
    //      \   \
    //       3    3
    bool isSymmetric(TreeNode* root) {
        if(root==NULL) return true;
        inorderTraversal(root,'H');
        if(vi.size()%2==0) return false;
        int len=vi.size()/2;
        for(int i=0;i<len;++i){
            if(vi[i]!=vi[vi.size()-1-i]) return false;
            if(vc[i]==vc[vc.size()-1-i]) return false;
        }
        return true;
    }
};