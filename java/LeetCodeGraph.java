package com.xycode.leetcode;

import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    private void dijkstra(int v0,int[][] graph,int[] dist){//v0-> otherNode
        boolean[] s=new boolean[graph.length];//待扩展的节点集合,false:未扩展
        int[] prevNode=new int[graph.length];//存放前向节点
        for(int i=0;i<graph.length;++i){//初始化
            dist[i]=graph[v0][i];
            if(i!=v0&&dist[i]<INF) prevNode[i]=v0;
            else prevNode[i]=-1;
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
                    prevNode[k]=u;
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

    // Definition for a Node.
    class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {}

        public Node(int _val,List<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    Set<Integer> s=new HashSet<>();
    void dfs(Node node, Map<Integer,List<Integer> > mp){
        for(Node tmp:node.neighbors){
            if(!mp.containsKey(node.val)){
                mp.put(node.val,new ArrayList<>());
            }
            mp.get(node.val).add(tmp.val);
//            System.out.println(node.val+" : "+tmp.val);
            if(!s.contains(tmp.val)){
                s.add(tmp.val);
                dfs(tmp,mp);
            }
        }
    }


    //133. Clone Graph
    public Node cloneGraph(Node node) {
        s.add(node.val);
        Map<Integer,List<Integer>> mp=new HashMap<>();
        dfs(node,mp);
        Map<Integer,Node> mp2=new HashMap<>();
        Node first=new Node(node.val,new ArrayList<>());
        mp2.put(first.val,first);
        for(int val:mp.keySet()){
            if(!mp2.containsKey(val)){
                Node tmp=new Node(val,new ArrayList<>());
                mp2.put(val,tmp);
            }
            for(int neighborVal:mp.get(val)){
                if(!mp2.containsKey(neighborVal)){
                    Node tmp=new Node(neighborVal,new ArrayList<>());
                    mp2.put(neighborVal,tmp);
                }
                mp2.get(val).neighbors.add(mp2.get(neighborVal));
            }

        }
        return first;
    }

    /**
     * <p>210. Course Schedule II</p>
     * 描述: 有numCourses个课程,课程id从0到n-1,prerequisites[][]记录对应课程的先修课程; 安排课程的学习顺序,
     * 若无解(即有互相依赖的情况),返回一个空数组 (其实就是拓扑排序)
     * @param numCourses
     * @param prerequisites
     * @return
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if(numCourses==0) return new int[0];
        Map<Integer,Set<Integer>> relatedCourses=new HashMap<>();
        for(int i=0;i<prerequisites.length;++i){//构建图,courseId -> relatedCourseId Set
            if(relatedCourses.containsKey(prerequisites[i][0])){
                relatedCourses.get(prerequisites[i][0]).add(prerequisites[i][1]);
            }else{
                Set<Integer> relatedCourseList=new HashSet<>();
                relatedCourseList.add(prerequisites[i][1]);
                relatedCourses.put(prerequisites[i][0],relatedCourseList);
            }
        }
//        System.out.println(relatedCourses);
        for(int i=0;i<numCourses;++i){//添加没有依赖课程的节点
            if(!relatedCourses.containsKey(i)){
                relatedCourses.put(i,new HashSet<>());
            }
        }
//        System.out.println(relatedCourses);
        int[] ans=new int[numCourses];
        int cnt=0;//记录已安排的节点个数
        Set<Integer> scheduledCourses=new HashSet<>();//记录已安排的节点
        while(cnt<numCourses){
            boolean circled=true;//记录图中是否有环,即相互依赖的情况
            for(Map.Entry<Integer,Set<Integer>> entry:relatedCourses.entrySet()){
                int courseId=entry.getKey();
                Set<Integer> relatedCourseList= entry.getValue();
                if(relatedCourseList.size()==0&&!scheduledCourses.contains(courseId)){
                    ans[cnt++]=courseId;
                    for(Map.Entry<Integer,Set<Integer>> entry2:relatedCourses.entrySet()){
                        entry2.getValue().remove(courseId);
                    }
                    scheduledCourses.add(courseId);
                    circled=false;
                }
            }
            if(circled) {//若经过一轮迭代后,没能安排课程,那说明图中有环
                return new int[0];
            }
        }
        return ans;
    }

    @Test
    public void testFindOrder() {
        System.out.println(Arrays.toString(findOrder(3, new int[][]{
                {1,0},{1,2},{0,1}
        })));
    }

}
