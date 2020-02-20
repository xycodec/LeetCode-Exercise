#include <iostream>
#include <vector>
using namespace std;
//https://leetcode.com/problems/path-sum-ii/
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(NULL), right(NULL) {}
};
 
class Solution {
public:
    vector<vector<int>> ans;
    void dfs(TreeNode* root,vector<int> &v,int sum,int target){
        if(root==NULL) return;
        if(target==sum&&root->left==NULL&&root->right==NULL){
            ans.push_back(v);
        }
        sum+=root->val;
        v.push_back(root->val);
        dfs(root->left,v,sum,target);
        dfs(root->right,v,sum,target);
        v.pop_back();
    }
    vector<vector<int>> pathSum(TreeNode* root, int sum) {
        vector<int> v;
        dfs(root,v,0,sum);
        return ans;
    }

};

void .asmf(vector<int> v){
    v.push_back(4);
}
int main(){
    vector<int> v={1,2,3};
    f(v);
    for(int item:v){
        cout<<item<<" ";
    }
    return 0;
}