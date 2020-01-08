#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
#include <queue>
using namespace std;
//59. Spiral Matrix II
class Solution {
public:
    vector<vector<int>> generateMatrix(int n) {
        vector<vector<int>> ans;
        if(n==0) return ans;
        for(int i=0;i<n;++i){
            ans.push_back(vector<int>(n));
        }
        if(n==1){
            ans[0][0]=1;
            return ans;
        }
        bool vis[n][n];
        memset(vis, false, sizeof(vis));
        int size=n*n;
        int i=0,j=0;
        //direction: R,D,L,U
        enum Direction{R,D,L,U};
        Direction d=R;
        int cnt=1;
        while(cnt<=size){
            switch(d){
                case R:
                    if(j<n&&!vis[i][j]){
                        vis[i][j]=true;
                        ans[i][j++]=cnt++;
                    }else{
                        d=D;
                        --j;
                        ++i;
                    } 
                    break;
                case D:
                    if(i<n&&!vis[i][j]){
                        vis[i][j]=true;
                        ans[i++][j]=cnt++;
                    }else{
                        d=L;
                        --i;
                        --j;
                    }                    
                    break;
                case L:
                    if(j>=0&&!vis[i][j]){
                        vis[i][j]=true;
                        ans[i][j--]=cnt++;
                    }else{
                        d=U;
                        ++j;
                        --i;
                    }                    
                    break;
                    
                case U:
                    if(i>=0&&!vis[i][j]){
                        vis[i][j]=true;
                        ans[i--][j]=cnt++;
                    }else{
                        d=R;
                        ++i;
                        ++j;
                    }
                    break;
                default:
                    break;
            }
        }
       
        return ans;
    }
};