#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
class Solution {
public:
    vector<vector<int>> ans;
    set<vector<int>> unique;
    void swap(vector<int>::iterator s,vector<int>::iterator e){
        int tmp=*s;
        *s=*e;
        *e=tmp;
    }
    void dfs(vector<int>& v,vector<int>::iterator s,vector<int>::iterator e) {
        if(s == e) {
            if(unique.find(v)==unique.end()){
                unique.insert(v);
                ans.push_back(v);
            }
            return;
        }
        for(vector<int>::iterator iter=s;iter!=e;++iter) {
            if(s==iter||*s==*iter){
                dfs(v,s+1,e);
                continue;
            }
            swap(s,iter);
            dfs(v,s+1,e);
            swap(s,iter);
        }
    }
    
    vector<vector<int>> permuteUnique(vector<int>& nums) {
        sort(nums.begin(),nums.end());
        dfs(nums,nums.begin(),nums.end());
        return ans;
    }
};


// class Solution {
// public:
//     vector<vector<int>> permute(vector<int>& nums) {
//         vector<vector<int>> ans;
//         sort(nums.begin(),nums.end());
//         ans.push_back(nums);
//         while(next_permutation(nums.begin(),nums.end())){
//             ans.push_back(nums);
//         }
//         return ans;
//     }
// };