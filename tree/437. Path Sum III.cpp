#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <queue>
#include <winsock.h> //windows 的Socket库,里面包含select()这种多路I/O
using namespace std;
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(NULL), right(NULL) {}
};

class Solution {
public:
    int dfs(TreeNode* root,int target){
        if(root==NULL) return 0;
        return (target==root->val?1:0)+dfs(root->left,target-root->val)+dfs(root->right,target-root->val);
    }
    
    int ans=0;
    void solve(TreeNode* root, int sum){
        if(root==NULL) return;
        ans+=dfs(root,sum);
        solve(root->left,sum);
        solve(root->right,sum);
    }
    
    int pathSum(TreeNode* root, int sum) {
        solve(root,sum);
        return ans;
    }
};

int main(){
    cout<<"..."<<endl;
    string s="01234";
    cout<<(s[5]=='\0'?"true":"false")<<endl;

    

    return 0;
}
