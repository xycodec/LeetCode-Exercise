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

    private boolean match(String s, int i, String p, int j) {
        int key = Objects.hash(i, j);

        if (dp.containsKey(key))
            return dp.get(key);

        if (i == s.length() || j == p.length()) {
            if (i == s.length() && j == p.length()) {
                return true;
            } else if (i == s.length()) {
                while (j < p.length() && p.charAt(j) == '*')
                    j++;

                if (j == p.length())
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        }

        boolean isMatch = true;

        if (p.charAt(j) == '*') {
            isMatch = match(s, i, p, j + 1);// match empty

            if (!isMatch)
                isMatch = match(s, i + 1, p, j + 1);//match last one

            if (!isMatch)
                isMatch = match(s, i + 1, p, j);//continue match
        } else if (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i)) {
            isMatch = match(s, i + 1, p, j + 1);
        } else {
            isMatch = false;
        }

        dp.put(key, isMatch);

        return isMatch;
    }

    public boolean isMatch(String s, String p) {
        return match(s, 0, p, 0);
    }

    @Test
    public void testIsMatch() {
        System.out.println(String.format("%d%d", 10, 20));
        System.out.println(10 << 16);
        System.out.println(isMatch("aaabababaaabaababbbaaaabbbbbbabbbbabbbabbaabbababab",
                "*ab***ba**b*b*aaab*b"));
    }

    //93. Restore IP Addresses
    private void ipAddressesDfs(String s, int index, List<Integer> pre, List<String> ans) {
        if (pre.size() > 4) return;
        if (pre.size() == 4 && index == s.length()) {
            StringBuilder sb = new StringBuilder();
            sb.append(pre.get(0)).append(".");
            sb.append(pre.get(1)).append(".");
            sb.append(pre.get(2)).append(".");
            sb.append(pre.get(3));
            ans.add(sb.toString());
            return;
        } else if (index == s.length()) return;

        if (pre.isEmpty()) {
            pre.add(s.charAt(index) - '0');
            ipAddressesDfs(s, index + 1, pre, ans);
        } else if (pre.get(pre.size() - 1) == 0) {
            pre.add(s.charAt(index) - '0');
            ipAddressesDfs(s, index + 1, pre, ans);
        } else {
            int prevTmp = pre.get(pre.size() - 1);
            int nextTmp = prevTmp * 10 + (s.charAt(index) - '0');
            List<Integer> preBK = new ArrayList<>(pre);
            if (nextTmp > 0 && nextTmp <= 255) {
                pre.remove(pre.size() - 1);
                pre.add(nextTmp);
                ipAddressesDfs(s, index + 1, pre, ans);

                preBK.add((s.charAt(index) - '0'));
                ipAddressesDfs(s, index + 1, preBK, ans);
            } else if (nextTmp > 255) {
                pre.add((s.charAt(index) - '0'));
                ipAddressesDfs(s, index + 1, pre, ans);
            }
        }
    }

    public List<String> restoreIpAddresses(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() < 4) return ans;
        List<Integer> pre = new ArrayList<>();
        ipAddressesDfs(s, 0, pre, ans);
        return ans;
    }


    @Test
    public void testRestoreIpAddresses() {
        for (String ip : restoreIpAddresses("10001")) {
            System.out.println(ip);
        }
    }

    //加了缓存似乎更慢了...
//    Map<String,String> d=new HashMap<>();
    private boolean matchStr(String s1, String s2) {
//        if(d.containsKey(s1)&&d.get(s1).equals(s2)) return true;
//        if(d.containsKey(s2)&&d.get(s2).equals(s1)) return true;
        int cnt = 0;
        for (int i = 0; i < s1.length(); ++i) {
            if (s2.charAt(i) != s1.charAt(i)) {
                ++cnt;
                if (cnt == 2) break;
            }
        }
        if (cnt == 1) {
//            d.put(s1,s2);
//            d.put(s2,s1);
            return true;
        } else return false;
    }

    private List<String> getMatchStrs(String tmpStr, List<String> wordList) {
        List<String> result = new ArrayList<>();
        for (String s1 : wordList) {
            if (matchStr(s1, tmpStr)) result.add(s1);
        }
        return result;
    }

    //127. Word Ladder
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if (beginWord.equals(endWord)) return 1;
        if (wordList == null || wordList.size() == 0) return 0;
        if (!wordList.contains(endWord)) return 0;
        Queue<String> q = new ArrayDeque<>();
        Queue<Integer> cntQ = new ArrayDeque<>();
        q.add(beginWord);
        cntQ.add(1);
        while (!q.isEmpty()) {
            String tmpStr = q.poll();
            int cnt = cntQ.poll();
            if (tmpStr.equals(endWord)) {
                return cnt;
            }
            for (String s1 : getMatchStrs(tmpStr, wordList)) {
                wordList.remove(s1);
                q.add(s1);
                cntQ.add(cnt + 1);
            }
        }
        return 0;
    }

    @Test
    public void testLadderLength() {
        List<String> wordList = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        System.out.println(ladderLength("hit", "cog", wordList));
    }

    int pathNum = 0;
    int gridNum = 0;
    int[][] direction = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    boolean[][] vis;

    private void uniquePathsIIIDfs(int curX, int curY, int[][] grid) {
        if (grid[curX][curY] == 2 && gridNum == 0) {
            ++pathNum;
            return;
        }
        for (int i = 0; i < 4; ++i) {
            int nextX = curX + direction[i][0], nextY = curY + direction[i][1];
            if (nextX >= 0 && nextX < grid.length && nextY >= 0 && nextY < grid[0].length) {
                if (!vis[nextX][nextY] && (grid[nextX][nextY] == 0 || grid[nextX][nextY] == 2)) {
                    vis[nextX][nextY] = true;
                    --gridNum;
                    uniquePathsIIIDfs(nextX, nextY, grid);
                    ++gridNum;
                    vis[nextX][nextY] = false;
                }
            }
        }
    }

    //980. Unique Paths III
    public int uniquePathsIII(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int m = grid.length, n = grid[0].length;
        vis = new boolean[m][n];
        int startX = -1, startY = -1;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    startX = i;
                    startY = j;
                }
                if (grid[i][j] != -1) ++gridNum;
            }
        }
        vis[startX][startY] = true;
        --gridNum;//starting point's grid
        uniquePathsIIIDfs(startX, startY, grid);
        return pathNum;
    }

    @Test
    public void testUniquePathsIII() {
        int[][] array = {{1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 2}};
        System.out.println(uniquePathsIII(array));
    }

    private void numIslandsDfs(char[][] grid, int x, int y, boolean[][] vis) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) return;
        if (grid[x][y] == '1' && !vis[x][y]) {
            vis[x][y] = true;
            numIslandsDfs(grid, x + 1, y, vis);
            numIslandsDfs(grid, x - 1, y, vis);
            numIslandsDfs(grid, x, y + 1, vis);
            numIslandsDfs(grid, x, y - 1, vis);
        }
    }

    //200. Number of Islands
    //使用染色的方式, 计算有多少独立的颜色块
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        boolean[][] vis = new boolean[grid.length][grid[0].length];
        int result = 0;
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                if (grid[i][j] == '1' && !vis[i][j]) {
                    ++result;
                    numIslandsDfs(grid, i, j, vis);
                }
            }
        }
        return result;
    }

    @Test
    public void testNumsIslands() {
        System.out.println(numIslands(new char[][]{
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        }));
    }

    /**
     * <p>726. Number of Atoms</p>
     * tip 描述: 给定一个化学分子式的字符串,求各元素的个数, eg: 输入 "Mg(OH)2", 输出为 "H2MgO2"
     *
     * @param formula
     * @return
     */
    public String countOfAtoms(String formula) {
        Stack<TreeMap<String, Integer>> stack = new Stack<>();
        //curMap对字符串进行遍历
        TreeMap<String, Integer> curMap = new TreeMap<>();
        int len = formula.length();
        for (int i = 0; i < len; ) {
            char c = formula.charAt(i);
            if (c == '(') {
                stack.push(curMap);
                //新建map对括号里面字符串进行操作
                curMap = new TreeMap<>();
                i++;
            } else if (c == ')') {
                //两个treemap合并
                //当前treemap保存下来
                TreeMap<String, Integer> temp = curMap;
                curMap = stack.pop();
                i++;
                int istart = i;
                int quantity = 1;
                while (i < len && Character.isDigit(formula.charAt(i))) i++;
                if (istart < i) {
                    quantity = Integer.parseInt(formula.substring(istart, i));
                }
                for (String name : temp.keySet()) {
                    curMap.put(name, curMap.getOrDefault(name, 0) + temp.get(name) * quantity);
                }
            } else {
                int istart = i;
                i++;
                while (i < len && Character.isLowerCase(formula.charAt(i))) i++;
                String name = formula.substring(istart, i);
                istart = i;
                int quantity = 1;
                while (i < len && Character.isDigit(formula.charAt(i))) i++;
                if (istart < i) {
                    quantity = Integer.parseInt(formula.substring(istart, i));
                }
                curMap.put(name, curMap.getOrDefault(name, 0) + quantity);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String name : curMap.keySet()) {
            int quantity = curMap.get(name);
            sb.append(name);
            if (quantity > 1) {
                sb.append(quantity);
            }
        }
        return sb.toString();
    }


    /**
     * <p>37. Sudoku Solver</p>
     * 给定一个9*9的数独矩阵,一件填了一些数字,'.'代表空的格子,请问填写完空格,使得其满足数独的规则
     * 每行/每列 1~9各出现一次,3*3的九宫格1~9只能出现一次
     *
     * @param board 数独矩阵.
     * @param x,y 搜索到的位置.
     * @param grid 标记九宫格的使用数字.
     * @param rows,cols 标记行,列的使用数字.
     */
    boolean ok = false;//标识是否已找到解

    private void sudokuDfs(char[][] board, int x, int y, boolean[][][] grids, boolean[][] rows, boolean[][] cols) {
        if (ok) return;
        if (x == 8 && y == 9) {
            ok = true;
            return;
        }
        if (y == 9) {
            sudokuDfs(board, x + 1, 0, grids, rows, cols);
        } else {
            if (board[x][y] != '.') {
                sudokuDfs(board, x, y + 1, grids, rows, cols);
            } else {
                for (int num = 9; num >= 1; --num) {
                    if (!rows[x][num] && !cols[y][num] && !grids[x / 3][y / 3][num]) {
                        rows[x][num] = true;
                        cols[y][num] = true;
                        grids[x / 3][y / 3][num] = true;
                        board[x][y] = (char) (num + '0');

                        sudokuDfs(board, x, y + 1, grids, rows, cols);
                        if (ok) return;

                        rows[x][num] = false;
                        cols[y][num] = false;
                        grids[x / 3][y / 3][num] = false;
                        board[x][y] = '.';
                    }
                }
            }
        }
    }

    public void solveSudoku(char[][] board) {
        boolean[][][] grids = new boolean[3][3][10];
        boolean[][] rows = new boolean[9][10];
        boolean[][] cols = new boolean[9][10];
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] != '.') {
                    grids[i / 3][j / 3][board[i][j] - '0'] = true;
                    rows[i][board[i][j] - '0'] = true;
                    cols[j][board[i][j] - '0'] = true;
                }
            }
        }
        sudokuDfs(board, 0, 0, grids, rows, cols);
    }

    @Test
    public void testSolveSudoku() {
        char[][] board = new char[][]{
                "53..7....".toCharArray(),
                "6..195...".toCharArray(),
                ".98....6.".toCharArray(),
                "8...6...3".toCharArray(),
                "4..8.3..1".toCharArray(),
                "7...2...6".toCharArray(),
                ".6....28.".toCharArray(),
                "...419..5".toCharArray(),
                "....8..79".toCharArray()
        };
        solveSudoku(board);
        for (char[] chars : board) {
            System.out.println(Arrays.toString(chars));
        }
    }

    /**
     * <p>1162. As Far from Land as Possible</p>
     * 描述: 给定一个仅包含值0和1的N x N网格，其中0表示水，1表示土地，找到一个水单元，使其到最近的土地单元的距离最大，并返回该距离.
     * 距离的计算采用曼哈顿距离
     *
     * @param grid 地图
     * @return
     */
    public int maxDistance(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int ans = -1;
        Queue<int[]> queue = new LinkedList<>();
        int[][] d = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                if (grid[i][j] == 0) {
                    //bfs
                    queue.add(new int[]{i, j});
                    boolean[][] vis = new boolean[grid.length][grid[0].length];
                    vis[i][j] = true;
                    while (!queue.isEmpty()) {
                        int[] tmp = queue.poll();
                        if (grid[tmp[0]][tmp[1]] == 1) {
                            ans = Math.max(ans, Math.abs(i - tmp[0]) + Math.abs(j - tmp[1]));
                            break;
                        }
                        for (int k = 0; k < 4; ++k) {
                            int nextX = d[k][0] + tmp[0], nextY = d[k][1] + tmp[1];
                            if (nextX >= 0 && nextX < grid.length && nextY >= 0 && nextY < grid[0].length && !vis[nextX][nextY]) {
                                vis[nextX][nextY] = true;
                                queue.add(new int[]{nextX, nextY});
                            }
                        }
                    }
                    queue.clear();
                }
            }
        }
        return ans;
    }

    @Test
    public void testMaxDistance() {
        System.out.println(maxDistance(new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        }));
    }


    private int findTargetSumWaysDfs(int[] nums, int i, int sum, int S, int[][] memo) {//记忆化搜索
        if (sum == S && i == nums.length) return 1;
        if (i == nums.length) return 0;
        if (memo[i][sum + 1000] != -1) return memo[i][sum + 1000];//加1000的偏移量,主要是为了防止越界,因为可能取负,最坏情况可能到-1000
        return memo[i][sum + 1000] = (findTargetSumWaysDfs(nums, i + 1, sum + nums[i], S, memo) +
                findTargetSumWaysDfs(nums, i + 1, sum - nums[i], S, memo));
    }

    /**
     * <p>494. Target Sum</p>
     * 描述: 给定一个正整数数组nums, 对每个数字可以不变或变为相反数,再给定一个数字S,问有多少种方案能使得数组的和等于S;
     * 题目保证原始nums的和不超过1000.
     *
     * @param nums
     * @param S
     * @return
     */
    public int findTargetSumWays(int[] nums, int S) {
        int[][] memo = new int[nums.length][2001];//memo[i][sum]表示从第i个元素开始,sum为初始值,求得和为S的方案数
        for (int[] tmp : memo) {
            Arrays.fill(tmp, -1);
        }
        return findTargetSumWaysDfs(nums, 0, 0, S, memo);
    }

    @Test
    public void testFindTargetSumWays() {
        System.out.println(findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
    }

}
