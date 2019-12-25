#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
class Solution {
public:
    int searchInsert(vector<int>& nums, int target) {
        if(nums.empty()) return 0;
        vector<int>::iterator iter=lower_bound(nums.begin(),nums.end(),target);
        if(iter==nums.begin()) return 0;
        else if(iter==nums.end()) return nums.size();
        else return iter-nums.begin();
    }
};

int main(){
    cout<<"sss"<<endl;
    return 0;
}