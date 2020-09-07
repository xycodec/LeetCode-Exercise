package com.xycode.autumninterview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/*
 *
 * @author: xycode
 * @date: 2020/9/6
 */
public class TencentInterview {

    public static void longestCommonSubsequence(List<Integer> s1, List<Integer> s2) {
        int len1 = s1.size(), len2 = s2.size();
        int[][] dp = new int[len1][len2];
        String[][] str = new String[len1][len2];
        dp[0][0] = s1.get(0).equals(s2.get(0)) ? 1 : 0;
        int ansLen = dp[0][0];//ans的初始值和第一 行/列 的初始化有关,只要有非空的子序列,ans就应该置为1
        for (int i = 1; i < len1; ++i) {
            if (s2.get(0).equals(s1.get(0))) {
                dp[i][0] = 1;
                str[i][0] = "" + s1.get(0);
                ansLen = 1;
            } else {
                dp[i][0] = dp[i - 1][0];//子序列未必连续,所以序列长度取前面那个值
                str[i][0] = str[i - 1][0];
            }
        }
        for (int j = 1; j < len2; ++j) {
            if (s1.get(0).equals(s2.get(j))) {
                dp[0][j] = 1;
                str[0][j] = "" + s1.get(0);
                ansLen = 1;
            } else {
                dp[0][j] = dp[0][j - 1];
                str[0][j] = str[0][j - 1];
            }
        }

        String ans = "";
        if (s1.get(0).equals(s2.get(0))) {
            str[0][0] = "" + s1.get(0);
        }
        for (int i = 1; i < len1; ++i) {
            for (int j = 1; j < len2; ++j) {
                if (s1.get(i).equals(s2.get(j))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    str[i][j] = str[i - 1][j - 1] + " " + s1.get(i);
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);//这个转移方程实际上决定了求出的是子序列(未必连续),s1,s2都可以跳过一些字符
                    if (dp[i - 1][j] > dp[i][j - 1]) {
                        dp[i][j] = dp[i - 1][j];
                        str[i][j] = str[i - 1][j];
                    } else {
                        dp[i][j] = dp[i][j - 1];
                        str[i][j] = str[i][j - 1];
                    }
                }

                if (dp[i][j] > ansLen) {
                    ansLen = dp[i][j];
                    ans = str[i][j];
                }
            }
        }
        System.out.println(ans.trim());
    }

    private static void tencentSolution1() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        List<Integer> list1 = Arrays.stream(bufferedReader.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        int m = Integer.parseInt(bufferedReader.readLine());
        List<Integer> list2 = Arrays.stream(bufferedReader.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());

        longestCommonSubsequence(list1, list2);
    }


    static List<Set<Integer>> list = new ArrayList<>();
    static Set<Integer> vis = new HashSet<>();
    static Set<Integer> visTeam = new HashSet<>();

    private static void dfs(int index) {
        if (visTeam.contains(index)) {
            return;
        }
        visTeam.add(index);
        Set<Integer> s = list.get(index);
        vis.addAll(s);
        for (int num : s) {
            for (int i = 0; i < list.size(); ++i) {
                if (i != index && list.get(i).contains(num)) {
                    dfs(i);
                }
            }
        }
    }

    private static void tencentSolution2() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int startIndex = -1;
        for (int i = 0; i < m; ++i) {
            int x = scanner.nextInt();
            Set<Integer> s = new HashSet<>();
            for (int j = 0; j < x; ++j) {
                int tmp = scanner.nextInt();
                if (tmp == 0) {
                    startIndex = i;
                }
                s.add(tmp);
            }
            list.add(s);
        }
        dfs(startIndex);
        System.out.println(vis.size());
    }

    static class WordNode {
        String word;
        int freq;

        public WordNode(String word, int freq) {
            this.word = word;
            this.freq = freq;
        }

        @Override
        public String toString() {
            return word + " " + freq;
        }
    }

    public static void topKFrequent(String[] words, int k, boolean flag) {
        List<WordNode> ans = new LinkedList<>();
        if (words == null || words.length == 0 || k <= 0) return;
        Map<String, Integer> mp = new HashMap<>();
        for (int i = 0; i < words.length; ++i) {
            mp.put(words[i], mp.getOrDefault(words[i], 0) + 1);
        }


        PriorityQueue<WordNode> queue = new PriorityQueue<>(k, (x, y) -> (x.freq == y.freq) ? -x.word.compareTo(y.word) :
                (flag ? Integer.compare(x.freq, y.freq) : -Integer.compare(x.freq, y.freq)));//注意这是按照频次的最小堆,最后会reverse,所以这里比较字典序应该反过来
        for (String word : mp.keySet()) {
            int freq = mp.get(word);
            if (queue.size() < k) {
                queue.add(new WordNode(word, freq));
            } else {
                boolean b = flag ? queue.peek().freq < freq : queue.peek().freq > freq;
                if (b) {
                    queue.poll();
                    queue.add(new WordNode(word, freq));
                } else if (queue.peek().freq == freq) {//频次相同則按字典序
                    if (queue.peek().word.compareTo(word) > 0) {
                        queue.poll();
                        queue.add(new WordNode(word, freq));
                    }
                }
            }
        }
        while (!queue.isEmpty()) {
            ans.add(queue.poll());
        }
        Collections.reverse(ans);
        for (WordNode wordNode : ans) {
            System.out.println(wordNode);
        }
    }

    private static void tencentSolution3() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        String[] words = new String[n];
        for (int i = 0; i < n; ++i) {
            words[i] = scanner.next();
        }
        topKFrequent(words, k, true);
        topKFrequent(words, k, false);
    }

    private static void tencentSolution4() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> a = new LinkedList<>();
        List<Integer> tmpList = new ArrayList<>();//原始顺序表
        for (int i = 0; i < n; ++i) {
            int tmp = scanner.nextInt();
            a.add(tmp);
            tmpList.add(tmp);
        }
        Collections.sort(a);//排序表
        for (int i = 0; i < n; ++i) {
            int tmpValue = tmpList.get(i);//原始数据表取值
            int tmpIndex = Collections.binarySearch(a, tmpValue);
            a.remove(tmpIndex);
            System.out.println(a.get((n - 1) / 2));
            a.add(tmpIndex, tmpValue);
        }
    }

    public static void main(String[] args) throws IOException {
        tencentSolution1();
    }
}
