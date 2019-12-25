//https://leetcode.com/problems/sudoku-solver/
#include <vector>
#include <iostream>
using namespace std;
class Solution {
public:
    //map<int,bool> mp_row[9],mp_col[9],mp_9[3][3];
    bool mp_row[9][9+1],mp_col[9][9+1],mp_9[3][3][9+1];
    bool ok=false;
    void init(vector<vector<char>>& bd){
        for(int i=0;i<9;++i){
            for(int j=1;j<=9;++j){
                mp_row[i][j]=false;
                mp_col[i][j]=false;
            }
        }
        for(int i=0;i<3;++i){
            for(int j=0;j<3;++j){
                for(int k=1;k<=9;++k){
                    mp_9[i][j][k]=false;
                }
            }
        }
        int tmp;
        for(int i=0;i<9;++i){
            for(int j=0;j<9;++j){
                if(bd[i][j]!='.'){
                    tmp=(int)(bd[i][j]-'0');
                    mp_row[i][tmp]=true;
                    mp_col[j][tmp]=true;
                    mp_9[i/3][j/3][tmp]=true;
                }
            }
        }
    }
    //喜闻乐见的数独游戏, dfs搜索+剪枝
    void dfs(vector<vector<char>>& bd,int m,int n){
        if(ok) return;
        if(m==8&&n==9){
            ok=true;
            // for(int i=0;i<9;++i){
            //     for(int j=0;j<9;++j){
            //         cout<<bd[i][j]<<" ";
            //     }
            //     cout<<endl;
            // }
            return;
        }
        if(n==9){
            dfs(bd,m+1,0);
            return;
        }
        if(bd[m][n]=='.'){
            for(int i=9;i>=1;--i){
                if(!mp_row[m][i]&&!mp_col[n][i]&&!mp_9[m/3][n/3][i]){
                    mp_row[m][i]=true;
                    mp_col[n][i]=true;
                    mp_9[m/3][n/3][i]=true;

                    bd[m][n]=(char)(i+'0');
                    dfs(bd,m,n+1);
                    if(ok) return;
                    bd[m][n]='.';

                    mp_row[m][i]=false;
                    mp_col[n][i]=false;
                    mp_9[m/3][n/3][i]=false;
                }
            }               
        }else{
            dfs(bd,m,n+1);
        }
    }
    
    void solveSudoku(vector<vector<char>>& board) {
        init(board);
        // for(int i=1;i<=9;++i){
        //     cout<<(mp_9[2][2][i]==true?i:-1)<<endl;
        // }
        dfs(board,0,0);
    }
};