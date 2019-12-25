#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
class Solution {
public:
    vector<vector<int>> ans;
    vector<int> nums;
    void dfs(int index,vector<int>& v,int target){
        if(target==0){
            ans.push_back(v);
            return;
        }else if(target<0) return;

        for(int i=index;i<nums.size();++i){
            v.push_back(nums[i]);
            dfs(i,v,target-nums[i]);
            v.pop_back();
        }

    }
    
    vector<vector<int>> combinationSum(vector<int>& candidates, int target) {
        if(candidates.empty()) return ans;
        sort(candidates.begin(),candidates.end());
        if(candidates[0]>target) return ans;

        while(candidates.back()>target) candidates.pop_back();
        nums=candidates;
        vector<int> v;
        dfs(0,v,target);
        return ans;
    }
};

int main(){
    vector<int> v={1,2,3,4,5};
    while(v.back()>3) v.pop_back();
    for(int item:v) cout<<item<<endl;
    return 0;
}