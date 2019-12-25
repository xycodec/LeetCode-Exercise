#pragma warning(disable:4996)
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <map>
using namespace std;
//https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
class Solution {
public:
    int ans=0;
    int m,n;
    int dir[4][2]={{1,0},{0,1},{-1,0},{0,-1}};
    int depths[200][200];
    void dfs(int x,int y,int depth,vector<vector<int>>& matrix){
        for(int i=0;i<4;++i){
            int tmp_x=x+dir[i][0],tmp_y=y+dir[i][1];
            if(tmp_x<0||tmp_x>=m||tmp_y<0||tmp_y>=n){
                continue;
            }
            if(depths[tmp_x][tmp_y]>depth) continue;
            if(matrix[tmp_x][tmp_y]>matrix[x][y]){
                dfs(tmp_x,tmp_y,depth+1,matrix);
            }else{
                ans=max(ans,depth);
                depths[x][y]=max(depths[x][y],depth);
            }
        }
    }
    int longestIncreasingPath(vector<vector<int>>& matrix) {
        if(matrix.empty()) return 0;
        if(matrix[0].empty()) return 0;
        m=matrix.size(),n=matrix[0].size();
        if(m==1&&n==1) return 1;
        for(int i=0;i<m;++i){
            for(int j=0;j<n;++j){
                dfs(i,j,1,matrix);
            }
        }
        return ans;
    }
};

int main(){
    int a[2][2]={{1,2},{3,4}};
    cout<<*std::max_element(a[1],a[1]+2)<<endl;
    multimap<int,int> mp;
    mp.insert(make_pair(1,2));
    mp.insert(make_pair(1,3));
    
    return 0;
}