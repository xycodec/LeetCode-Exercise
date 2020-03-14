package com.xycode.leetcode;

import org.testng.annotations.Test;

import java.util.*;

/**
 * ClassName: LeetCode
 *
 * @Author: xycode
 * @Date: 2020/3/3
 * @Description: this is description of the LeetCode class
 **/
public class LeetCodeSearch {
    //44. Wildcard Matching
    private Map<Integer, Boolean> dp = new HashMap<>();
    private boolean match(String s, int i, String p, int j){
        int key = Objects.hash(i,j);

        if(dp.containsKey(key))
            return dp.get(key);

        if (i == s.length() || j == p.length()){
            if (i == s.length() && j == p.length()){
                return true;
            }else if (i == s.length()){
                while (j < p.length() && p.charAt(j) == '*')
                    j++;

                if (j == p.length())
                    return true;
                else
                    return false;
            }else{
                return false;
            }
        }

        boolean isMatch = true;

        if (p.charAt(j) == '*'){
            isMatch = match(s, i, p, j + 1);// match empty

            if (!isMatch)
                isMatch = match(s, i + 1, p, j + 1);//match last one

            if (!isMatch)
                isMatch = match(s, i + 1, p, j);//continue match
        }else if (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i)){
            isMatch = match(s, i + 1, p, j + 1);
        }else{
            isMatch = false;
        }

        dp.put(key, isMatch);

        return isMatch;
    }

    public boolean isMatch(String s, String p){
        return match(s, 0, p, 0);
    }

    @Test
    public void testIsMatch(){
        System.out.println(String.format("%d%d",10,20));
        System.out.println(10<<16);
        System.out.println(isMatch("aaabababaaabaababbbaaaabbbbbbabbbbabbbabbaabbababab",
                "*ab***ba**b*b*aaab*b"));
    }

    //93. Restore IP Addresses
    private void ipAddressesDfs(String s, int index, List<Integer> pre, List<String> ans){
        if(pre.size()>4) return;
        if(pre.size()==4&&index==s.length()){
            StringBuilder sb=new StringBuilder();
            sb.append(pre.get(0)).append(".");
            sb.append(pre.get(1)).append(".");
            sb.append(pre.get(2)).append(".");
            sb.append(pre.get(3));
            ans.add(sb.toString());
            return;
        }else if(index==s.length()) return;

        if(pre.isEmpty()) {
            pre.add(s.charAt(index) - '0');
            ipAddressesDfs(s, index + 1, pre,ans);
        }else if(pre.get(pre.size()-1)==0){
            pre.add(s.charAt(index)-'0');
            ipAddressesDfs(s,index+1,pre,ans);
        }else{
            int prevTmp=pre.get(pre.size()-1);
            int nextTmp=prevTmp*10+(s.charAt(index)-'0');
            List<Integer> preBK=new ArrayList<>(pre);
            if(nextTmp>0&&nextTmp<=255){
                pre.remove(pre.size()-1);
                pre.add(nextTmp);
                ipAddressesDfs(s,index+1,pre,ans);

                preBK.add((s.charAt(index)-'0'));
                ipAddressesDfs(s,index+1,preBK,ans);
            }else if(nextTmp>255){
                pre.add((s.charAt(index)-'0'));
                ipAddressesDfs(s,index+1,pre,ans);
            }
        }
    }

    public List<String> restoreIpAddresses(String s) {
        List<String> ans=new ArrayList<>();
        if(s==null||s.length()<4) return ans;
        List<Integer> pre=new ArrayList<>();
        ipAddressesDfs(s,0,pre,ans);
        return ans;
    }


    @Test
    public void testRestoreIpAddresses(){
        for(String ip:restoreIpAddresses("10001")){
            System.out.println(ip);
        }
    }

    //加了缓存似乎更慢了...
//    Map<String,String> d=new HashMap<>();
    private boolean matchStr(String s1,String s2){
//        if(d.containsKey(s1)&&d.get(s1).equals(s2)) return true;
//        if(d.containsKey(s2)&&d.get(s2).equals(s1)) return true;
        int cnt=0;
        for(int i=0;i<s1.length();++i){
            if(s2.charAt(i)!=s1.charAt(i)){
                ++cnt;
                if(cnt==2) break;
            }
        }
        if(cnt==1){
//            d.put(s1,s2);
//            d.put(s2,s1);
            return true;
        }else return false;
    }

    private List<String> getMatchStrs(String tmpStr,List<String> wordList){
        List<String> result=new ArrayList<>();
        for (String s1 : wordList) {
            if(matchStr(s1,tmpStr)) result.add(s1);
        }
        return result;
    }
    //127. Word Ladder
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if(beginWord.equals(endWord)) return 1;
        if(wordList==null||wordList.size()==0) return 0;
        if(!wordList.contains(endWord)) return 0;
        Queue<String> q=new ArrayDeque<>();
        Queue<Integer> cntQ=new ArrayDeque<>();
        q.add(beginWord);
        cntQ.add(1);
        while (!q.isEmpty()){
            String tmpStr=q.poll();
            int cnt=cntQ.poll();
            if(tmpStr.equals(endWord)){
                return cnt;
            }
            for (String s1 : getMatchStrs(tmpStr,wordList)) {
                wordList.remove(s1);
                q.add(s1);
                cntQ.add(cnt+1);
            }
        }
        return 0;
    }

    @Test
    public void testLadderLength(){
        List<String> wordList=new ArrayList<>(Arrays.asList("hot","dot","dog","lot","log","cog"));
        System.out.println(ladderLength("hit","cog",wordList));
    }

    int pathNum=0;
    int gridNum=0;
    int[][] direction={{1,0},{0,1},{-1,0},{0,-1}};
    boolean[][] vis;
    private void uniquePathsIIIDfs(int curX,int curY,int[][] grid){
        if(grid[curX][curY]==2&&gridNum==0){
            ++pathNum;
            return;
        }
        for(int i=0;i<4;++i){
            int nextX=curX+direction[i][0],nextY=curY+direction[i][1];
            if(nextX>=0&&nextX<grid.length&&nextY>=0&&nextY<grid[0].length){
                if(!vis[nextX][nextY]&&(grid[nextX][nextY]==0||grid[nextX][nextY]==2)){
                    vis[nextX][nextY]=true;
                    --gridNum;
                    uniquePathsIIIDfs(nextX,nextY,grid);
                    ++gridNum;
                    vis[nextX][nextY]=false;
                }
            }
        }
    }
    //980. Unique Paths III
    public int uniquePathsIII(int[][] grid) {
        if(grid==null||grid.length==0||grid[0].length==0) return 0;
        int m=grid.length,n=grid[0].length;
        vis=new boolean[m][n];
        int startX = -1,startY=-1;
        for(int i=0;i<m;++i){
            for(int j=0;j<n;++j){
                if(grid[i][j]==1){
                    startX=i;
                    startY=j;
                }
                if(grid[i][j]!=-1) ++gridNum;
            }
        }
        vis[startX][startY]=true;
        --gridNum;//starting point's grid
        uniquePathsIIIDfs(startX,startY,grid);
        return pathNum;
    }

    @Test
    public void testUniquePathsIII(){
        int[][] array={{1,0,0,0},{0,0,0,0},{0,0,0,2}};
        System.out.println(uniquePathsIII(array));
    }

    private void numIslandsDfs(char[][] grid,int x,int y,boolean[][] vis){
        if(x<0||x>=grid.length||y<0||y>=grid[0].length) return;
        if(grid[x][y]=='1'&&!vis[x][y]){
            vis[x][y]=true;
            numIslandsDfs(grid,x+1,y,vis);
            numIslandsDfs(grid,x-1,y,vis);
            numIslandsDfs(grid,x,y+1,vis);
            numIslandsDfs(grid,x,y-1,vis);
        }
    }
    //200. Number of Islands
    //使用染色的方式, 计算有多少独立的颜色块
    public int numIslands(char[][] grid) {
        if(grid==null||grid.length==0) return 0;
        boolean[][] vis=new boolean[grid.length][grid[0].length];
        int result=0;
        for(int i=0;i<grid.length;++i){
            for(int j=0;j<grid[0].length;++j){
                if(grid[i][j]=='1'&&!vis[i][j]){
                    ++result;
                    numIslandsDfs(grid,i,j,vis);
                }
            }
        }
        return result;
    }

    @Test
    public void testNumsIslands() {
        System.out.println(numIslands(new char[][]{
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        }));
    }
}
