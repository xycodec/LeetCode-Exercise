package com.xycode.springinterview;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * ClassName: AlibabaInterview
 *
 * @Author: xycode
 * @Date: 2020/3/23
 * @Description: this is description of the AlibabaInterview class
 **/
public class AlibabaInterview {
    final static int M = 1000000007;

    private static long fastPow2(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        long ans = 1;
        long base = 2;
        while (n != 0) {
            if ((n & 1) == 1) ans = ((ans % M) * (base % M)) % M;
            base = ((base % M) * (base % M)) % M;
            n >>= 1;
        }
        return ans % M;
    }

    //n*2^(n-1) % 1000000007
    private static int func1(int n) {
        return (int) (((n % M) * (fastPow2(n - 1) % M)) % M);
    }


    static int endX, endY;
    static boolean[][] vis;
    static int[][] dir = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    static int ans = Integer.MAX_VALUE;
    static int[][] memo;//记忆化搜索

    private static void dfs(char[][] map, int x, int y, int step, int k) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) return;
        if (memo[x][y] != Integer.MAX_VALUE) {//已缓存
            if (memo[x][y] <= step) return;
        } else {
            memo[x][y] = Math.min(memo[x][y], step);
        }
        if (x == endX && y == endY) {
            ans = Math.min(ans, step);
            return;
        }
        //上下左右走
        for (int i = 0; i < 4; ++i) {
            int tmpX = x + dir[i][0], tmpY = y + dir[i][1];
            if (tmpX < 0 || tmpX >= map.length || tmpY < 0 || tmpY >= map[0].length) continue;
            if (!vis[tmpX][tmpY] && map[tmpX][tmpY] != '#') {
                vis[tmpX][tmpY] = true;
                dfs(map, tmpX, tmpY, step + 1, k);
                vis[tmpX][tmpY] = false;
            }
        }
        //飞行
        if (k > 0) {
            int flyX = map.length - 1 - x, flyY = map[0].length - 1 - y;
            if (flyX >= 0 && flyX < map.length && flyY >= 0 && flyY < map[0].length) {
                if (!vis[flyX][flyY] && map[flyX][flyY] != '#') {
                    vis[flyX][flyY] = true;
                    dfs(map, flyX, flyY, step + 1, k - 1);
                    vis[flyX][flyY] = false;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
//        int n=scanner.nextInt();
//        System.out.println(func1(n));

        int n = scanner.nextInt(), m = scanner.nextInt();
        char[][] map = new char[n][m];
        vis = new boolean[n][m];
        memo = new int[n][m];
        for (int i = 0; i < n; ++i) {
            map[i] = scanner.next().toCharArray();
            Arrays.fill(memo[i], Integer.MAX_VALUE);
        }

        int startX = 0, startY = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (map[i][j] == 'S') {
                    startX = i;
                    startY = j;
                } else if (map[i][j] == 'E') {
                    endX = i;
                    endY = j;
                }
            }
        }
        vis[startX][startY] = true;
        dfs(map, startX, startY, 0, 5);
        System.out.println(ans == Integer.MAX_VALUE ? -1 : ans);
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
//        String input=reader.readLine();
//        String[] tmp=input.split(" ");

//        StreamTokenizer tokenizer=new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//        while(tokenizer.nextToken() != StreamTokenizer.TT_EOF){
//            System.out.println((int)tokenizer.nval);
//        }

    }
}
