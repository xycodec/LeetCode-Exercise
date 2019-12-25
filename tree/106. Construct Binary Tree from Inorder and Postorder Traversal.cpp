#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <queue>
using namespace std;
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(NULL), right(NULL) {}
};
 
class Solution {
public:
    TreeNode* build(vector<int>::iterator postorder_s,vector<int>::iterator postorder_e,
    vector<int>::iterator inorder_s,vector<int>::iterator inorder_e){
        int rootValue=*postorder_e;
        TreeNode *root=new TreeNode(rootValue);
        if(postorder_s==postorder_e){
            if(inorder_s==inorder_e){
                return root;
            }else{
                cerr<<"illegal input!"<<endl;
                return NULL;
            }
        }

        vector<int>::iterator rootInorder=inorder_s;
        while(rootInorder<=inorder_e&&*rootInorder!=rootValue){
            ++rootInorder;
        }

        int leftInoderLength=rootInorder-inorder_s;;
        vector<int>::iterator leftPostorderEnd=postorder_s+leftInoderLength;
        if(leftInoderLength>0){
            root->left=build(postorder_s,leftPostorderEnd-1,inorder_s,rootInorder-1);
        }

        if(inorder_e-rootInorder>0){
            root->right=build(leftPostorderEnd,postorder_e-1,rootInorder+1,inorder_e);
        }
    
        return root;
    }
    
    TreeNode* buildTree(vector<int>& inorder, vector<int>& postorder) {
        if(postorder.empty()||inorder.empty()) return NULL;
        return build(postorder.begin(),postorder.end()-1,inorder.begin(),inorder.end()-1);

    }
    
};