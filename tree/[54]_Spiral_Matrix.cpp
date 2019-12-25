#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <cstring>
using namespace std;
//https://leetcode.com/problems/spiral-matrix/
class Solution {
public:
    vector<int> spiralOrder(vector<vector<int>>& matrix) {
        vector<int> ans;
        if(matrix.empty()) return ans;
        int m=matrix.size(),n=matrix[0].size();
        bool vis[m][n];
        memset(vis, false, sizeof(vis));
        int size=m*n;
        int i=0,j=0;
        //R,D,L,U
        enum Direction{R,D,L,U};
        Direction d=R;
        while(ans.size()<size){
            switch(d){
                case R:
                    if(j<n&&!vis[i][j]){
                        vis[i][j]=true;
                        ans.push_back(matrix[i][j++]);
                    }else{
                        d=D;
                        --j;
                        ++i;
                    } 
                    break;
                case D:
                    if(i<m&&!vis[i][j]){
                        vis[i][j]=true;
                        ans.push_back(matrix[i++][j]);
                    }else{
                        d=L;
                        --i;
                        --j;
                    }                    
                    break;
                case L:
                    if(j>=0&&!vis[i][j]){
                        vis[i][j]=true;
                        ans.push_back(matrix[i][j--]);
                    }else{
                        d=U;
                        ++j;
                        --i;
                    }                    
                    break;
                    
                case U:
                    if(i>=0&&!vis[i][j]){
                        vis[i][j]=true;
                        ans.push_back(matrix[i--][j]);
                    }else{
                        d=R;
                        ++i;
                        ++j;
                    }
                    break;
            }
        }
       
        return ans;
    }
};

int main(){
    vector<int> v={1,2,3,4};
    vector<int> v2=v;
    swap(*v.begin(),*(v.begin()+1));
    
    for(int item:v2){
        cout<<item<<" ";
    }
    cout<<endl;

}