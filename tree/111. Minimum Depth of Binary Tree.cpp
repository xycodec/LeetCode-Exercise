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
    int bfs(TreeNode* root) {
        queue<TreeNode*> q;
        root->val=1;
        q.push(root);
        while(!q.empty()){
            TreeNode* tmp=q.front();
            q.pop();
            if(tmp->left==NULL&&tmp->right==NULL){
                return tmp->val;
            }
            
            if(tmp->left!=NULL){
                (tmp->left)->val=tmp->val+1;
                q.push(tmp->left);
            }
                
            if(tmp->right!=NULL){
                (tmp->right)->val=tmp->val+1;
                q.push(tmp->right);
            }

        }
        return -1;
    }
    
    int minDepth(TreeNode* root) {
        if(root==NULL) return 0;
        return bfs(root);
    }
};

int main(){
    cout<<"..."<<endl;
    return 0;
}