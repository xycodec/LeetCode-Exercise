/**
 * Definition for a binary tree node.
 * struct TreeNode {
 *     int val;
 *     TreeNode *left;
 *     TreeNode *right;
 *     TreeNode(int x) : val(x), left(NULL), right(NULL) {}
 * };
 */

#include <vector>
#include <exception>
using namespace std;
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(NULL), right(NULL) {}
};

class Solution {
public:
    TreeNode* build(vector<int>::iterator preorder_s,vector<int>::iterator preorder_e,
    vector<int>::iterator inorder_s,vector<int>::iterator inorder_e){
        int rootValue=*preorder_s;//前序遍历的第一个数字就是根节点的值
        TreeNode *root=new TreeNode(rootValue);
        if(preorder_s==preorder_e){//区间只有一个元素,这时左/右节点就完全确定了
            if(inorder_s==inorder_e&&*preorder_s==*preorder_e){
                return root;
            }else{//实际上两个区间不匹配了,也就是illegal input
                return nullptr;
            }
        }

        //找到中序序列中的根节点位置
        vector<int>::iterator rootInorder=inorder_s;
        while(rootInorder<=inorder_e&&*rootInorder!=rootValue){
            ++rootInorder;
        }

        if(rootInorder==inorder_e&&*rootInorder!=rootValue){//实际上两个区间不匹配了,也就是illegal input
            return nullptr;
        }

        int leftInoderLength=rootInorder-inorder_s;//中序序列中左子树对应的区间长度
        vector<int>::iterator leftPreorderEnd=preorder_s+leftInoderLength;//前序序列中左子树的末端指针索引
        if(leftInoderLength>0){//左子树区间长度大于0
            root->left=build(preorder_s+1,leftPreorderEnd,inorder_s,rootInorder-1);//创建左子树,填好对应的区间
        }

        if(inorder_e-rootInorder>0){//右子树区间长度大于0
            root->right=build(leftPreorderEnd+1,preorder_e,rootInorder+1,inorder_e);//创建右子树,填好对应的区间
        }
        return root;
    }
    
    TreeNode* buildTree(vector<int>& preorder, vector<int>& inorder) {
        if(preorder.empty()||inorder.empty()) return nullptr;
        return build(preorder.begin(),preorder.end()-1,inorder.begin(),inorder.end()-1);
    }
};