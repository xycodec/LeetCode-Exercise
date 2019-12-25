#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
class Solution {
public:
    vector<vector<int>> ans;
    set<vector<int>> s;
    vector<int> nums;
    void dfs(int index,vector<int>& v,int target){
        if(target==0){
            if(s.find(v)==s.end()) {
                s.insert(v);
                ans.push_back(v);
            }
            return;
        }else if(target<0) return;

        for(int i=index+1;i<nums.size();++i){
            v.push_back(nums[i]);
            dfs(i,v,target-nums[i]);
            v.pop_back();
        }

    }
    
    vector<vector<int>> combinationSum2(vector<int>& candidates, int target) {
        if(candidates.empty()) return ans;
        sort(candidates.begin(),candidates.end());
        if(candidates[0]>target) return ans;

        while(candidates.back()>target) candidates.pop_back();
        nums=candidates;
        vector<int> v;
        dfs(-1,v,target);
        return ans;
    }
};