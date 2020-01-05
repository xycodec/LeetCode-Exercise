package com.xycode.leetcode;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * ClassName: LeetCodeGraph
 *
 * @Author: xycode
 * @Date: 2019/12/31
 * @Description: this is description of the LeetCodeGraph class
 **/
public class LeetCodeGraph {
    private final int INF=Integer.MAX_VALUE;
    private void Dijkstra(int v0,int[][] graph,int[] dist){//v0-> otherNode
        boolean[] s=new boolean[graph.length];//待扩展的节点集合,false:未扩展
        int[] prev_node=new int[graph.length];//存放前向节点
        for(int i=0;i<graph.length;++i){//初始化
            dist[i]=graph[v0][i];
            if(i!=v0&&dist[i]<INF) prev_node[i]=v0;
            else prev_node[i]=-1;
        }
        s[v0]=true;
        dist[v0]=0;
        for(int i=0;i<graph.length-1;++i){//从顶点v0确定n-1条最短路径(n-1个顶点)
            int min=INF;
            int u=v0;
            for(int j=0;j<graph.length;++j){//选择当前集合T中具有最短路径的顶点 u
                if(!s[j]&&dist[j]<min){
                    u=j;
                    min=dist[j];
                }
            }
            s[u]=true;//将顶点u加入到集合s，表示它的最短路径已求得
            for(int k=0;k<graph.length;++k){
                if(!s[k]&&graph[u][k]<INF&&dist[u]+graph[u][k]<dist[k]){
                    dist[k]=dist[u]+graph[u][k];
                    prev_node[k]=u;
                }
            }
        }
    }


    List<List<String>> ans=new ArrayList<>();
    boolean[][] vis;//列,副对角线,主对角线,以8皇后为例,其主/副对角线,各有15条,2*N-1
    private void NQueensDfs(int row,char[][] board){
        if(row==board.length){
            List<String> tmp=new ArrayList<>();
            for(int i=0;i<board.length;++i){
                tmp.add(String.valueOf(board[i]));
            }
            ans.add(tmp);
        }else{
            for(int col=0;col<board.length;++col){
                //行号+列号的值相同说明在同一条副对角线上
                //行号-列号的值相同说明在同一条主对角线上(为了便于数组存储,统一加上length-1,使其都大于0)
                if(!vis[0][col]&&!vis[1][row+col]&&!vis[2][row-col+board.length-1]){
                    board[row][col]='Q';
                    vis[0][col]=vis[1][row+col]=vis[2][row-col+board.length-1]=true;//表示列,副对角线,主对角线冲突
                    NQueensDfs(row+1,board);//递归下一行
                    board[row][col]='.';
                    vis[0][col]=vis[1][row+col]=vis[2][row-col+board.length-1]=false;//回溯
                }
            }
        }

    }

    public List<List<String>> solveNQueens(int n) {
        vis=new boolean[3][2*n-1];
        char[][] board=new char[n][n];
        for(int i=0;i<n;++i){
            for(int j=0;j<n;++j){
                board[i][j]='.';
            }
        }
        NQueensDfs(0,board);
        return ans;
    }

    @Test
    public void testSolveNQueens(){
        solveNQueens(4);
        for(int i=0;i<ans.size();++i){
            for(int j=0;j<ans.get(i).size();++j){
                System.out.println(ans.get(i).get(j));
            }
            System.out.println();
        }
        Function<Integer,String> f=(i) -> i + "";
        BiFunction<Integer,Integer,Double> bif=(x,y) -> x/(double)y;
        Supplier<String> s=() -> "123";
        Consumer<String> c= str -> System.out.println(str);
        Predicate<Integer> p= i -> i>0;
    }




}
