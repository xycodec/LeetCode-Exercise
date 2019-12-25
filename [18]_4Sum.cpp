#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
//https://leetcode.com/problems/4sum/
class Solution {
public:
    vector<vector<int>> fourSum(vector<int>& nums, int target) {
        set<vector<int>> ans2;
        if(nums.empty()) return {};
        sort(nums.begin(),nums.end());
        int len=nums.size();
        //Time Complexity: O(n^2*logn)
        for(int i=0;i<len;++i){
            for(int j=i+1;j<len;++j){
                int tmp=target-nums[i]-nums[j];
                int h=j+1,r=len-1;
                while(h<r){
                    if(nums[h]+nums[r]>tmp){
                        --r;
                    }else if(nums[h]+nums[r]<tmp){
                        ++h;
                    }else{
                        ans2.insert({nums[i],nums[j],nums[h],nums[r]});
                        while (h<r&&nums[h]==nums[h+1]) ++h;
                        while (h<r&&nums[r]==nums[r-1]) --r;
                        ++h;
                        --r;
                    }
                }
            }
        }
        vector<vector<int>> ans(ans2.begin(),ans2.end());
        return ans;
    }
    
};