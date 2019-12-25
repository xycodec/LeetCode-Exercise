#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
class Solution {
public:
    vector<vector<int>> ans;
    void swap(vector<int>::iterator s,vector<int>::iterator e){
        if(s==e||*s==*e) return;
        int tmp=*s;
        *s=*e;
        *e=tmp;
    }

    void dfs(vector<int>& v,vector<int>::iterator s,vector<int>::iterator e) {
        if(s == e) {
            ans.push_back(v);
            return;
        }
        for(vector<int>::iterator iter=s;iter!=e;++iter) {
            if(s==iter){
                dfs(v,s+1,e);
                continue;
            }
            swap(s,iter);
            dfs(v,s+1,e);
            swap(s,iter);
        }
    }
    
    vector<vector<int>> permute(vector<int>& nums) {
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

int main(){
    vector<int> v={1,2,3,4};
    vector<int> v2=v;
    swap(*v.begin(),*(v.begin()+1));
    
    for(int item:v2){
        cout<<item<<" ";
    }
    cout<<endl;

}


