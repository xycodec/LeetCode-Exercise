package com.xycode.springinterview;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * ClassName: TecentInterview
 *
 * @Author: xycode
 * @Date: 2020/4/26
 * @Description: this is description of the TecentInterview class
 **/
public class TencentInterview {

    private static void tencentSolution1() {
        Queue<String> queue = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; ++i) {
            queue.clear();
            int q = Integer.parseInt(scanner.nextLine());
            for (int j = 0; j < q; ++j) {
                String[] tmp = scanner.nextLine().split(" ");
                if (tmp.length == 2) {
                    if (tmp[0].equals("PUSH")) {
                        queue.add(tmp[1]);
                    }
                } else {
                    if (tmp[0].equals("POP")) {
                        if (!queue.isEmpty())
                            queue.poll();
                        else
                            System.out.println(-1);
                    } else if (tmp[0].equals("TOP")) {
                        if (!queue.isEmpty())
                            System.out.println(queue.peek());
                        else
                            System.out.println(-1);
                    } else if (tmp[0].equals("SIZE")) {
                        System.out.println(queue.size());
                    } else if (tmp[0].equals("CLEAR")) {
                        queue.clear();
                    }
                }
            }
        }
    }


    static int cnt = 0;

    private static double divideAndConquer(List<Map.Entry<Integer, Integer>> A, List<Map.Entry<Integer, Integer>> B) {
        if (A.size() == 1) {
            ++cnt;
            int x1 = A.get(0).getKey(), y1 = A.get(0).getValue();
            int x2 = B.get(0).getKey(), y2 = B.get(0).getValue();
            return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        } else {
            int mid = A.size() / 2;
            //O(N^2)...
            return Math.min(
                    divideAndConquer(A.subList(mid, A.size()), B.subList(0, mid)),
                    Math.min(
                            divideAndConquer(A.subList(0, mid), B.subList(mid, B.size())),
                            Math.min(
                                    divideAndConquer(A.subList(0, mid), B.subList(0, mid)),
                                    divideAndConquer(A.subList(mid, A.size()), B.subList(mid, B.size()))
                            )));
        }
    }

    private static void tencentSolution2() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(reader.readLine());
        for (int k = 0; k < q; ++k) {
            int n = Integer.parseInt(reader.readLine());
            List<Map.Entry<Integer, Integer>> A = new ArrayList<>();
            List<Map.Entry<Integer, Integer>> B = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                String s = reader.readLine();
                int pos = s.indexOf(" ");
                int x = Integer.parseInt(s.substring(0, pos)), y = Integer.parseInt(s.substring(pos + 1));
                A.add(new AbstractMap.SimpleEntry<>(x, y));
            }
            double ans = Double.MAX_VALUE;
//            boolean flag=false;
            for (int i = 0; i < n; ++i) {
                String s = reader.readLine();
                int pos = s.indexOf(" ");
                int x = Integer.parseInt(s.substring(0, pos)), y = Integer.parseInt(s.substring(pos + 1));
                B.add(new AbstractMap.SimpleEntry<>(x, y));
//                if(!flag){
//                    for(Map.Entry<Integer,Integer> entry:A){
//                        ans=Math.min(ans,(x-entry.getKey())*(x-entry.getKey())+(y-entry.getValue())*(y-entry.getValue()));
//                        if(ans==0){
//                            flag=true;
//                            break;
//                        }
//                    }
//                }
            }
            ans = divideAndConquer(A, B);
            System.out.printf("%.3f\n", Math.sqrt(ans));
            System.out.println(cnt);
            cnt = 0;
        }
    }

    private static boolean check(int[] a) {
        for (int i = 1; i < a.length; ++i) {
            if (a[i] > a[i - 1]) return false;
        }
        return true;
    }

    private static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private static void swapAndReverse(int[] a, int[] b, int p) {
        swap(a, p, p + 1);
        swap(b, p, p + 1);
        int tmp = a[p];
        a[p] = b[p];
        b[p] = tmp;

        tmp = a[p + 1];
        a[p + 1] = b[p + 1];
        b[p + 1] = tmp;
    }

    private static void tencentSolution3() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n], b = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = scanner.nextInt();
        }
        for (int i = 0; i < n; ++i) {
            b[i] = scanner.nextInt();
        }
        if (check(a)) {
            System.out.println(0);
            return;
        }
        Queue<int[]> queueA = new LinkedList<>();
        Queue<int[]> queueB = new LinkedList<>();
        Queue<Integer> cntQ = new LinkedList<>();
        queueA.add(a);
        queueB.add(b);
        cntQ.add(0);
        Set<int[]> memo = new HashSet<>();
        memo.add(a);
        while (!queueA.isEmpty()) {
            int[] tmpA = Arrays.copyOf(queueA.poll(), n);
            int[] tmpB = Arrays.copyOf(queueB.poll(), n);
            int cnt = cntQ.poll();
            if (check(tmpA)) {
                System.out.println(cnt);
                return;
            }
            for (int i = 0; i < n - 1; ++i) {
                int[] tmp1 = Arrays.copyOf(tmpA, n);
                int[] tmp2 = Arrays.copyOf(tmpB, n);
                swapAndReverse(tmp1, tmp2, i);

//                if(check(tmp1)){
//                    System.out.println(cnt);
//                    return;
//                }
                if (!memo.contains(tmp1)) {
                    System.out.println(Arrays.toString(tmp1));
                    queueA.add(tmp1);
                    queueB.add(tmp2);
                    cntQ.add(cnt + 1);
                } else {
                    memo.add(tmp1);
                }
            }
        }
        System.out.println(-1);
    }


    static class MyQueue {
        Stack<Integer> stack1, stack2;

        public MyQueue() {
            stack1 = new Stack<>();
            stack2 = new Stack<>();
        }

        public void add(int x) {
            stack2.add(x);
        }

        public int poll() {
            while (!stack2.isEmpty()) {
                stack1.add(stack2.pop());
            }
            int result = stack1.pop();
            while (!stack1.isEmpty()) {
                stack2.add(stack1.pop());
            }
            return result;
        }

        public int peek() {
            while (!stack2.isEmpty()) {
                stack1.add(stack2.pop());
            }
            int result = stack1.peek();
            while (!stack1.isEmpty()) {
                stack2.add(stack1.pop());
            }
            return result;
        }

    }

    private static void tencentSolution4() throws IOException {
        MyQueue queue = new MyQueue();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(reader.readLine());
        for (int j = 0; j < q; ++j) {
            String s = reader.readLine();
            int pos = s.indexOf(" ");
            if (pos > 0) {
                queue.add(Integer.parseInt(s.substring(pos + 1)));
            } else {
                if (s.equals("poll")) {
                    queue.poll();
                } else if (s.equals("peek")) {
                    System.out.println(queue.peek());
                }
            }
        }
    }

    private static void tencentSolution5() {
        Scanner scanner = new Scanner(System.in);
        final double LOG2 = Math.log(2);
        int q = scanner.nextInt();
        for (int i = 0; i < q; ++i) {
            long x = scanner.nextLong(), k = scanner.nextInt();
            long level = (int) (Math.log(x) / LOG2) + 1;
            if (k >= level) {
                System.out.println(-1);
            } else {
                long d = level - k;
                System.out.println(x >> d);
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
//        Scanner scanner=new Scanner(System.in);
        tencentSolution2();
    }
}
