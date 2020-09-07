package com.xycode.springinterview;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: HuaweiInterview
 *
 * @Author: xycode
 * @Date: 2020/4/15
 * @Description: this is description of the HuaweiInterview class
 **/
public class HuaweiInterview {
    private static boolean check(String s) {
        if (!Character.isUpperCase(s.charAt(0))) {
            return false;
        } else {
            for (int i = 1; i < s.length(); ++i) {
                if (!Character.isLowerCase(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    private static void huaweiSolution1() {
        Scanner scanner = new Scanner(System.in);
        String[] tmp = scanner.next().split(",");
        if (tmp.length == 0) {
            System.out.println("error.0001");
            return;
        }
        Map<String, Integer> treeMap = new HashMap<>();
        for (int i = 0; i < tmp.length; ++i) {
            treeMap.put(tmp[i], treeMap.getOrDefault(tmp[i], 0) + 1);
            if (!check(tmp[i])) {
                System.out.println("error.0001");
                return;
            }
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(treeMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o1.getValue().equals(o2.getValue())) {
                    if (o1.getKey().contains(o2.getKey()) || o2.getKey().contains(o1.getKey())) {
                        return Integer.compare(o1.getKey().length(), o2.getKey().length());
                    } else {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                } else {
                    return -Integer.compare(o1.getValue(), o2.getValue());
                }
            }
        });
        System.out.println(list.get(0).getKey());
    }

    private static void huaweiSolution2() {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();
        String[] tmp = scanner.next().split("],");
        for (int i = 0; i < tmp.length; ++i) {
            String[] tmp2 = tmp[i].split("\\[");
            if (word.equals(tmp2[0])) {
                String[] register = tmp2[1].split(",");
                String addr = register[0].split("=")[1];
                String mask = register[1].split("=")[1];
                String val = register[2].split("=")[1];
                if (i == tmp.length - 1) {
                    val = val.substring(0, val.length() - 1);
                }
                if (!addr.substring(0, 2).toLowerCase().equals("0x")
                        || !mask.substring(0, 2).toLowerCase().equals("0x")
                        || !val.substring(0, 2).toLowerCase().equals("0x")) {
                    System.out.print("FAIL\r\n");
                    continue;
                }
                try {
                    Long.parseLong(addr.substring(2), 16);
                    Long.parseLong(mask.substring(2), 16);
                    Long.parseLong(val.substring(2), 16);
                } catch (Exception e) {
                    System.out.print("FAIL\r\n");
                    continue;
                }
                System.out.print(addr + " " + mask + " " + val + "\r\n");
            }
        }
    }

    static Map<Integer, Integer> stackSizeMp = new HashMap<>();
    static long maxSum = 0;
    static int errorCode = 0;

    //    static Map<Integer,Long> memo=new HashMap<>();
    private static void dfs(Map<Integer, List<Integer>> mp, int id, int pathSum, int depth, int maxDepth) {
        if (errorCode != 0) return;
        if (depth > maxDepth) {
            errorCode = 1;
            return;
        }
        boolean flag = false;
        for (Map.Entry<Integer, List<Integer>> entry : mp.entrySet()) {
            if (entry.getKey() == id) {
                for (int nextId : entry.getValue()) {
                    dfs(mp, nextId, pathSum + stackSizeMp.get(nextId), depth + 1, maxDepth);
                }
            }
        }
        if (!flag) {
            maxSum = Math.max(maxSum, pathSum);
        }
    }

    private static void huaweiSolution3() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Map<Integer, List<Integer>> mp = new HashMap<>();
        int[] tmp = new int[n];
        for (int i = 0; i < n; ++i) {
            tmp[i] = scanner.nextInt();
        }
        for (int i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            if (!scanner.hasNext()) {
                System.out.println("NA");
                return;
            }
            int stackSize = scanner.nextInt();
            stackSizeMp.put(id, stackSize);
            List<Integer> list = new LinkedList<>();
            for (int j = 0; j < tmp[i]; ++j) {
                if (!scanner.hasNext()) {
                    System.out.println("NA");
                    return;
                }
                list.add(scanner.nextInt());
            }
            mp.put(id, list);
        }

        long ans = 0;
        for (int id : stackSizeMp.keySet()) {
            maxSum = 0;
            errorCode = 0;
            dfs(mp, id, stackSizeMp.get(id), 1, n);
            if (errorCode == 1) {
                System.out.println("R");
                return;
            }
//            memo.put(id,maxSum);
            ans = Math.max(ans, maxSum);
        }
        System.out.println(ans);
    }


    public static void main(String[] args) {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));

//        Scanner scanner=new Scanner(System.in);

//        huaweiSolution3();

        Pattern p = Pattern.compile("^([A-Z])([a-z])+$");
        Matcher matcher = p.matcher("TOm");
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

    }
}
