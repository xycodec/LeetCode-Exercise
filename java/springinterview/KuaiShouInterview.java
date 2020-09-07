package com.xycode.springinterview;


import java.util.*;

/**
 * ClassName: KuaiShouInterview
 *
 * @Author: xycode
 * @Date: 2020/4/26
 * @Description: this is description of the KuaiShouInterview class
 **/
public class KuaiShouInterview {
    private static boolean check(TreeSet<String> s1, TreeSet<String> s2) {
        if (s1.size() != s2.size()) return false;
        Iterator<String> it1 = s1.iterator(), it2 = s2.iterator();
        while (it1.hasNext()) {
            if (!it1.next().equals(it2.next())) return false;
        }
        return true;
    }

    private static void kuaishouSolution1() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Map<String, TreeSet<String>> mp = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next().split("//")[1];
            int pos = s.indexOf("/");
            if (pos < 0) {
                if (!mp.containsKey(s)) {
                    mp.put(s, new TreeSet<>());
                }
                mp.get(s).add("NUL");
            } else {
                String name = s.substring(0, pos), req = s.substring(pos);
                if (!mp.containsKey(name)) {
                    mp.put(name, new TreeSet<>());
                }
                mp.get(name).add(req);
            }
        }
        Map<Integer, List<String>> mp2 = new HashMap<>();
        for (Map.Entry<String, TreeSet<String>> entry : mp.entrySet()) {
            if (mp2.containsKey(entry.getValue().size())) {
                mp2.get(entry.getValue().size()).add(entry.getKey());
            } else {
                mp2.put(entry.getValue().size(), new LinkedList<>());
                mp2.get(entry.getValue().size()).add(entry.getKey());
            }
        }

        Map<String, List<String>> ans = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : mp2.entrySet()) {
            if (entry.getValue().size() >= 2) {
                List<String> list = entry.getValue();
                for (int i = 0; i < list.size(); ++i) {
                    ans.put(list.get(i), new LinkedList<>());
                    ans.get(list.get(i)).add(list.get(i));
                    for (int j = 0; j < list.size(); ++j) {
                        if (i != j && check(mp.get(list.get(i)), mp.get(list.get(j)))) {
                            ans.get(list.get(i)).add(list.get(j));
                        }
                    }
                }
            }
        }

        Set<List<String>> set = new HashSet<>();
        for (List<String> list : ans.values()) {
            Collections.sort(list);
            if (list.size() > 1) {
                set.add(list);
            }
        }
        System.out.println(set.size());
        for (List<String> list : set) {
            for (int i = 0; i < list.size(); ++i) {
                System.out.print("http://" + list.get(i) + " ");
            }
            System.out.println();
        }
    }


    private static void kuaishouSolution2() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Map<Map.Entry<Long, Long>, Long> costs = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            int op = scanner.nextInt();
            if (op == 1) {
                long u = scanner.nextLong(), v = scanner.nextLong(), w = scanner.nextLong();
                Map.Entry<Long, Long> entry1 = new AbstractMap.SimpleEntry<>(u, v);
                Map.Entry<Long, Long> entry2 = new AbstractMap.SimpleEntry<>(v, u);
                costs.put(entry1, costs.getOrDefault(entry1, 0L) + w);
                costs.put(entry2, costs.getOrDefault(entry2, 0L) + w);
            } else {
                long u = scanner.nextLong(), v = scanner.nextLong();
                if (costs.size() == 0) {
                    System.out.println(0);
                } else {
                    if (u == v) {
                        System.out.println(0);
                        continue;
                    }
                    long tmpU = u, tmpV = v;
                    int levelU = (int) (Math.log(u) / Math.log(2)) + 1;
                    int levelV = (int) (Math.log(v) / Math.log(2)) + 1;
                    if (levelU > levelV) {
                        tmpU >>= (levelU - levelV);
                    } else if (levelU < levelV) {
                        tmpV >>= (levelV - levelU);
                    }
                    while (tmpU != tmpV) {
                        tmpU >>= 1;
                        tmpV >>= 1;
                    }
                    long ans = 0;
                    long lca = tmpU;
                    while (u != lca) {
                        Map.Entry<Long, Long> entry = new AbstractMap.SimpleEntry<>(u, u >> 1);
                        if (costs.containsKey(entry)) {
                            ans += costs.get(entry);
                        }
                        u >>= 1;
                    }
                    while (v != lca) {
                        Map.Entry<Long, Long> entry = new AbstractMap.SimpleEntry<>(v, v >> 1);
                        if (costs.containsKey(entry)) {
                            ans += costs.get(entry);
                        }
                        v >>= 1;
                    }
                    System.out.println(ans);
                }
            }
        }
    }


    static int[][] d = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    static boolean flag = true;

    private static void dfs(int[][] map, int x, int y) {
        if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y] == 1) {
            map[x][y] = 0;
            if (x == 0 || y == 0 || x == map.length - 1 || y == map[0].length - 1) {
                flag = false;
            }
            for (int i = 0; i < 4; ++i) {
                int nextX = x + d[i][0], nextY = y + d[i][1];
                dfs(map, nextX, nextY);
            }
        }
    }

    private static void kuaishouSolution4() {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt(), y = scanner.nextInt();
        int[][] map = new int[x][y];
        for (int i = 0; i < x; ++i) {
            for (int j = 0; j < y; ++j) {
                map[i][j] = scanner.nextInt();
            }
        }
        int ans = 0;
        for (int i = 1; i < x - 1; ++i) {
            for (int j = 1; j < y - 1; ++j) {
                if (map[i][j] == 1) {
                    flag = true;
                    dfs(map, i, j);
                    if (flag) {
                        ++ans;
                    }
                }
            }
        }
        System.out.println(ans);
    }

    public static void main(String[] args) {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
//        Scanner scanner=new Scanner(System.in);
        kuaishouSolution2();
    }
}
