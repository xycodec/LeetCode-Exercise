package com.xycode.leetcode;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import kotlin.collections.ArraysKt;
import org.testng.annotations.Test;
import sun.net.www.http.HttpClient;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

/**
 * ClassName: leetcode1116
 *
 * @Author: xycode
 * @Date: 2019/12/3
 * @Description: this is description of the leetcode1116 class
 **/
public class LeetCodeEx {
    //leetcode, Concurrency部分
    class ZeroEvenOdd {
        private int n;
        private Queue<Integer> queue;
        private Object o=new Object();
        public ZeroEvenOdd(int n) {
            this.n = n;
            queue=new LinkedList<>();
            for(int i=1;i<=2*n;++i){
                if(i%2==1){
                    queue.add(0);
                }else{
                    queue.add(i/2);
                }
            }
//            while(!queue.isEmpty()){
//                System.out.print(queue.poll());
//            }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public synchronized void zero(IntConsumer printNumber) throws InterruptedException {
            while(!queue.isEmpty()){
                synchronized (o){
                    while(!queue.isEmpty()&&queue.peek()==0){
                        printNumber.accept(queue.poll());
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);
            }
        }

        public void even(IntConsumer printNumber) throws InterruptedException {
            while(!queue.isEmpty()){
                synchronized (o){
                    while(!queue.isEmpty()&&queue.peek()!=0&&queue.peek()%2==0){
                        printNumber.accept(queue.poll());

                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);
            }
        }

        public void odd(IntConsumer printNumber) throws InterruptedException {
            while(!queue.isEmpty()){
                synchronized (o){
                    while(!queue.isEmpty()&&queue.peek()%2==1){
                        printNumber.accept(queue.poll());
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);
            }
        }
    }

    class H2O {
        private Semaphore h=new Semaphore(2);
        private Semaphore o=new Semaphore(1);
        private AtomicInteger count=new AtomicInteger(0);
        public H2O() {

        }

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
            h.acquire();
            count.incrementAndGet();
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            if(count.get()==2){
                o.release();
                count.addAndGet(-2);
            }

        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
            o.acquire();
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            h.release(2);
        }
    }

    class FizzBuzz {
        private int n;
        private Queue<Integer> queue;
        private Object o=new Object();
        public FizzBuzz(int n) {
            this.n = n;
            this.queue=new LinkedList<>();
            for(int i=1;i<=n;++i){
                this.queue.add(i);
            }
        }

        // printFizz.run() outputs "fizz".
        public void fizz(Runnable printFizz) throws InterruptedException {
            while(!queue.isEmpty()){
                synchronized (o){
                    while(!queue.isEmpty()&&queue.peek()%3==0&&queue.peek()%5!=0){
                        printFizz.run();
                        queue.poll();
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }

        // printBuzz.run() outputs "buzz".
        public void buzz(Runnable printBuzz) throws InterruptedException {
            while(!queue.isEmpty()){
                synchronized (o){
                    while(!queue.isEmpty()&&queue.peek()%5==0&&queue.peek()%3!=0){
                        printBuzz.run();
                        queue.poll();
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }

        // printFizzBuzz.run() outputs "fizzbuzz".
        public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
            while(!queue.isEmpty()){
                synchronized (o){
                    while(!queue.isEmpty()&&queue.peek()%5==0&&queue.peek()%3!=0){
                        printFizzBuzz.run();
                        queue.poll();
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void number(IntConsumer printNumber) throws InterruptedException {
            while(!queue.isEmpty()){
                synchronized (o){
                    while(!queue.isEmpty()&&queue.peek()%5!=0&&queue.peek()%3!=0){
                        printNumber.accept(queue.poll());
                    }
                }
                TimeUnit.MICROSECONDS.sleep(5);//Questioner's bug
            }
        }
    }

    class DiningPhilosophers {
        private Queue<Integer> queue;
        private Object o;
        public DiningPhilosophers() {
            queue=new ArrayDeque<>(300);
            o=new Object();
            List<Integer> tmp=new ArrayList<>(Arrays.asList(0,1,2,3,4));
            for(int i=0;i<60;++i){
                Collections.shuffle(tmp);
                queue.addAll(tmp);
            }
        }

        // call the run() method of any runnable to execute its code
        public void wantsToEat(int philosopher,
                               Runnable pickLeftFork,
                               Runnable pickRightFork,
                               Runnable eat,
                               Runnable putLeftFork,
                               Runnable putRightFork) throws InterruptedException {
            while (true){
                synchronized (o){
                    if(philosopher==queue.peek()){
                        pickLeftFork.run();
                        pickRightFork.run();
                        eat.run();
                        putLeftFork.run();
                        putRightFork.run();
                        queue.poll();
                        break;
                    }
                }
                TimeUnit.MICROSECONDS.sleep(10);//Questioner's bug
            }
        }
    }




    //41. First Missing Positive
    public int firstMissingPositive(int[] nums) {
        if(nums.length==0) return 1;
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<nums.length;++i){
            list.add(nums[i]);
        }
        Collections.sort(list);
        if(list.get(0)>1) return 1;
        int index=0;
        while(index<nums.length){
            if(list.get(index)<=0){
                ++index;
            }else{
                if(index>0&&list.get(index-1)<=0&&list.get(index)>1) return 1;
                if(index<nums.length-1){
                    if(list.get(index+1)-list.get(index)>1) return list.get(index)+1;
                    ++index;
                }else{
                    break;
                }
            }
        }
        return list.get(nums.length-1)+1>0?list.get(nums.length-1)+1:1;
    }


    //30. Substring with Concatenation of All Words
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> ans=new ArrayList<>();
        if(words.length==0) return ans;
        int len=words[0].length();
        if(len==0){
            for(int i=0;i<words.length;++i) ans.add(i);
            return ans;
        }
        Map<String,Integer> mp=new HashMap<>();
        for (String word : words) {
            if (!mp.containsKey(word)) mp.put(word, 1);
            else mp.put(word, mp.get(word) + 1);
        }
        int index=0;
        int threshold=s.length()-words.length*len+1;
        while(index<threshold){
            int tmpIndex=index;
            Map<String,Integer> tmpMp=new HashMap<>(mp);
            int total=words.length;
            while(tmpIndex<s.length()){
                String tmpStr=s.substring(tmpIndex,tmpIndex+len);
                if(tmpMp.containsKey(tmpStr)){
                    int cnt=tmpMp.get(tmpStr);
                    if(cnt>0) {
                        tmpMp.put(tmpStr,cnt-1);
                        if(total>0) {
                            --total;
                            if(total==0){
                                ans.add(index);
                                break;
                            }
                            tmpIndex+=len;
                        }
                    }else break;
                }else break;
            }
            ++index;
        }

        return ans;
    }

    @Test
    public void testFindSubstring(){
        System.out.println(findSubstring("barfoothefoobarman", new String[]{"foo", "bar"}));
    }


    //42. Trapping Rain Water
    //tip: time complexity: O(hMax * N), TLE!!!
//    public int trap(int[] height) {
//        if(height==null||height.length<=1) return 0;
//        int hMax=-1;
//        for(int i=0;i<height.length;++i){
//            if(height[i]>hMax){
//                hMax=height[i];
//            }
//        }
//        int ans=0;
//        while (hMax>0){
//            List<Integer> tmp=new ArrayList<>();
//            for(int i=0;i<height.length;++i){
//                if(height[i]>=hMax){
//                    tmp.add(i);
//                }
//            }
//            ans+=tmp.get(tmp.size()-1)-tmp.get(0)-tmp.size()+1;
//            --hMax;
//        }
//        return ans;
//    }

    //tip: O(N)
    public int trap(int[] height) {
        int l=0, r=height.length-1;
        int ans=0;
        int lMax=0, rMax=0;
        while (l<r) {
            if (height[l]<height[r]) {//左边比较低(右边比较高)
                if(height[l]>=lMax){
                    lMax=height[l];//更新左边最高的
                }else{
                    ans+=lMax-height[l];
                    //因为当前的高度height[l]低于前面的某个lMax,
                    // 并且height[l]又低于右边的某个height[r],所以当前height[l]所处的位置一定可以盛水
                    // (可以盛lMax-height[l]的水,注意lMax是小于height[r]的)
                }
                ++l;
            } else {
                if(height[r]>=rMax){
                    rMax = height[r];
                }else{
                    ans += rMax - height[r];
                }
                --r;
            }
        }
        return ans;
    }

    @Test
    public void testTrap(){
        int[] a={0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(trap(a));
    }


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

    //55. Jump Game
    short[] memo;
    public boolean jump(int[] nums,int pos){
        if(memo[pos]!=0) return memo[pos] == 1;
        if(pos==nums.length-1) {
            return true;
        }
        if(nums[pos]+pos>=nums.length-1){
            return true;
        }
        int t=nums[pos]+pos;
        for(int i=t;i>pos;--i){
            if(jump(nums,i)){
                memo[pos]=1;
                return true;
            }

        }
        memo[pos]=-1;
        return false;
    }

    public boolean canJump(int[] nums) {
        memo=new short[nums.length];
        return jump(nums,0);
    }

    public boolean canJumpDP(int[] nums) {
        short[] dp=new short[nums.length];
        for(int i=nums.length-2;i>=0;--i){
            if(nums[i]+i>=nums.length-1){
                dp[i]=1;
                continue;
            }
            int t=nums[i]+i;
            for(int j=i+1;j<=nums[i]+i;++j){
                if (dp[j] == 1) {
                    dp[i]=1;
                    break;
                }
            }
        }
        return dp[0]==1;
    }

    //45. Jump Game II
    public int jumpMini(int[] nums) {
        if(nums.length==1) return 0;
        int lastPos = nums.length - 1;
        int step=0;
        while(lastPos>0){
            int curPos=lastPos;
            for (int i = lastPos-1; i >= 0; --i) {
                if (i + nums[i] >= lastPos) {
                    curPos=Math.min(curPos,i);
                }
            }
            ++step;
            lastPos=curPos;

        }
        return step;
    }

    @Test
    public void testJump(){
        int[] a={2,3,1,1,4};
        System.out.println(jumpMini(a));
    }


    //57. Insert Interval
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int[][] ans;
        if(intervals.length==0){
            ans=new int[1][2];
            ans[0]=newInterval;
            return ans;
        }
        if(newInterval[1]<intervals[0][0]){
            ans=new int[1+intervals.length][2];
            ans[0][0]=newInterval[0];
            ans[0][1]=newInterval[1];
            for(int i=1;i<=intervals.length;++i){
                ans[i]=intervals[i-1];
            }
            return ans;
        }
        if(newInterval[0]>intervals[intervals.length-1][1]){
            ans=new int[1+intervals.length][2];
            for(int i=0;i<intervals.length;++i){
                ans[i]=intervals[i];
            }
            ans[intervals.length][0]=newInterval[0];
            ans[intervals.length][1]=newInterval[1];
            return ans;
        }

        int l=-1,r=Integer.MAX_VALUE;
        if(newInterval[0]>=intervals[0][0]){
            for(int i=0;i<intervals.length;++i){
                if((newInterval[0]>=intervals[i][0]&&newInterval[0]<=intervals[i][1])|| //within interval
                        (i!=intervals.length-1&&newInterval[0]>intervals[i][1]&&newInterval[0]<intervals[i+1][0])){ //between intervals
                    l=i;
                    break;
                }
            }
        }

        if(newInterval[1]<=intervals[intervals.length-1][1]){
            for(int i=intervals.length-1;i>=0;--i){
                if((newInterval[1]>=intervals[i][0]&&newInterval[1]<=intervals[i][1])|| //within interval
                        (i!=intervals.length-1&&newInterval[1]>intervals[i][1]&&newInterval[1]<intervals[i+1][0])){ //between intervals
                    r=i;
                    break;
                }
            }
        }
        if(l==-1&&r==Integer.MAX_VALUE){
            ans=new int[1][2];
            ans[0]=newInterval;
        }else if(l==-1&&r<Integer.MAX_VALUE){
//            if(newInterval[1]>intervals[r][1]){
//                ans=new int[intervals.length-r][2];
//                ans[0][0]=newInterval[0];
//                ans[0][1]=newInterval[1];
//                for(int i=r+1;i<intervals.length;++i){
//                    ans[i-r][0]=intervals[i][0];
//                    ans[i-r][1]=intervals[i][1];
//                }
//            }else{
//                ans=new int[intervals.length-r][2];
//                ans[0][0]=newInterval[0];
//                ans[0][1]=intervals[r][1];
//                for(int i=r+1;i<intervals.length;++i){
//                    ans[i-r][0]=intervals[i][0];
//                    ans[i-r][1]=intervals[i][1];
//                }
//            }
            ans=new int[intervals.length-r][2];
            ans[0][0]=newInterval[0];
            ans[0][1]=Math.max(intervals[r][1],newInterval[1]);

            for(int i=r+1;i<intervals.length;++i){
                ans[i-r][0]=intervals[i][0];
                ans[i-r][1]=intervals[i][1];
            }
        }else if(l>=0&&r==Integer.MAX_VALUE){
            if(newInterval[0]>intervals[l][1]){//between intervals
                ans=new int[l+2][2];
                for(int i=0;i<=l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l+1][0]=newInterval[0];
                ans[l+1][1]=newInterval[1];
            }else{//within interval
                ans=new int[l+1][2];
                for(int i=0;i<l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l][0]=intervals[l][0];
                ans[l][1]=newInterval[1];
            }
        }else{
            if(newInterval[0]>intervals[l][1]){//between intervals
                ans=new int[intervals.length-(r-l)+1][2];
                for(int i=0;i<=l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l+1][0]=newInterval[0];
                ans[l+1][1]=Math.max(intervals[r][1],newInterval[1]);
                for(int i=1;i<intervals.length-r;++i){
                    ans[l+1+i][0]=intervals[r+i][0];
                    ans[l+1+i][1]=intervals[r+i][1];
                }
            }else{//within interval
                ans=new int[intervals.length-(r-l)][2];
                for(int i=0;i<l;++i){
                    ans[i][0]=intervals[i][0];
                    ans[i][1]=intervals[i][1];
                }
                ans[l][0]=intervals[l][0];
                ans[l][1]=Math.max(intervals[r][1],newInterval[1]);
                for(int i=1;i<intervals.length-r;++i){
                    ans[l+i][0]=intervals[r+i][0];
                    ans[l+i][1]=intervals[r+i][1];
                }
            }

        }
        return ans;
    }

    @Test
    public void testInsert(){
        int[][] a={{1,3},{6,9},{10,13}};
        int[] b={14,16};
        int[][] ans=insert(a,b);
        for(int i=0;i<ans.length;++i){
            System.out.println(ans[i][0]+" "+ans[i][1]);
        }
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
    void dfs(Node node,Map<Integer,List<Integer> > mp){
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


    //715. Range Module
    class RangeModule {
        int[][] intervals=null;//维护不相交的区间集合
        public RangeModule() {

        }

        public void show(){
            if(intervals==null) return;
            for(int i=0;i<intervals.length;++i){
                System.out.print("["+intervals[i][0]+", "+intervals[i][1]+"] ");
            }
            System.out.println();
        }

        private int[][] insert(int[] newInterval) {
            int[][] ans;
            if(intervals==null||intervals.length==0){
                ans=new int[1][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=newInterval[1];
                return ans;
            }
            final int len=intervals.length;
            if(newInterval[1]<intervals[0][0]){
                ans=new int[1+len][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=newInterval[1];
                for(int i=1;i<=len;++i){
                    ans[i]=intervals[i-1];
                }
                return ans;
            }
            if(newInterval[0]>intervals[len-1][1]){
                ans=new int[1+len][2];
                for(int i=0;i<len;++i){
                    ans[i]=intervals[i];
                }
                ans[len][0]=newInterval[0];
                ans[len][1]=newInterval[1];
                return ans;
            }

            int l=-1,r=Integer.MAX_VALUE;
            if(newInterval[0]>=intervals[0][0]){
                for(int i=0;i<len;++i){
                    if((newInterval[0]>=intervals[i][0]&&newInterval[0]<=intervals[i][1])|| //within interval
                            (i!=len-1&&newInterval[0]>intervals[i][1]&&newInterval[0]<intervals[i+1][0])){ //between intervals
                        l=i;
                        break;
                    }
                }
            }

            if(newInterval[1]<=intervals[len-1][1]){
                for(int i=len-1;i>=0;--i){
                    if((newInterval[1]>=intervals[i][0]&&newInterval[1]<=intervals[i][1])|| //within interval
                            (i!=len-1&&newInterval[1]>intervals[i][1]&&newInterval[1]<intervals[i+1][0])){ //between intervals
                        r=i;
                        break;
                    }
                }
            }
            if(l==-1&&r==Integer.MAX_VALUE){
                ans=new int[1][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=newInterval[1];
            }else if(l==-1&&r<Integer.MAX_VALUE){
                ans=new int[len-r][2];
                ans[0][0]=newInterval[0];
                ans[0][1]=Math.max(intervals[r][1],newInterval[1]);

                for(int i=r+1;i<len;++i){
                    ans[i-r][0]=intervals[i][0];
                    ans[i-r][1]=intervals[i][1];
                }
            }else if(l>=0&&r==Integer.MAX_VALUE){
                if(newInterval[0]>intervals[l][1]){//between intervals
                    ans=new int[l+2][2];
                    for(int i=0;i<=l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l+1][0]=newInterval[0];
                    ans[l+1][1]=newInterval[1];
                }else{//within interval
                    ans=new int[l+1][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=newInterval[1];
                }
            }else{
                if(newInterval[0]>intervals[l][1]){//between intervals
                    ans=new int[len-(r-l)+1][2];
                    for(int i=0;i<=l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l+1][0]=newInterval[0];
                    ans[l+1][1]=Math.max(intervals[r][1],newInterval[1]);
                    for(int i=1;i<len-r;++i){
                        ans[l+1+i][0]=intervals[r+i][0];
                        ans[l+1+i][1]=intervals[r+i][1];
                    }
                }else{//within interval
                    ans=new int[len-(r-l)][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=Math.max(intervals[r][1],newInterval[1]);
                    for(int i=1;i<len-r;++i){
                        ans[l+i][0]=intervals[r+i][0];
                        ans[l+i][1]=intervals[r+i][1];
                    }
                }

            }
            return ans;
        }

        private int[][] remove(int[] rmInterval){
            if(intervals==null||intervals.length==0) return intervals;
            final int len=intervals.length;
            if(rmInterval[1]<intervals[0][0]||rmInterval[0]>intervals[len-1][1]){
                return intervals;
            }
            int[][] ans;
            int l=-1,r=Integer.MAX_VALUE;
            if(rmInterval[0]>=intervals[0][0]){
                for(int i=0;i<len;++i){
                    if((rmInterval[0]>=intervals[i][0]&&rmInterval[0]<=intervals[i][1])|| //within interval
                            (i!=len-1&&rmInterval[0]>intervals[i][1]&&rmInterval[0]<intervals[i+1][0])){ //between intervals
                        l=i;
                        break;
                    }
                }
            }

            if(rmInterval[1]<=intervals[len-1][1]){
                for(int i=len-1;i>=0;--i){
                    if((rmInterval[1]>=intervals[i][0]&&rmInterval[1]<=intervals[i][1])|| //within interval
                            (i!=len-1&&rmInterval[1]>intervals[i][1]&&rmInterval[1]<intervals[i+1][0])){ //between intervals
                        r=i;
                        break;
                    }
                }
            }

            if(l==-1&&r==Integer.MAX_VALUE){
                return null;
            }else if(l==-1&&r<Integer.MAX_VALUE){
                if(rmInterval[1]>=intervals[r][1]){
                    ans=new int[len-r-1][2];
                    for(int i=r+1;i<len;++i){
                        ans[i-r-1][0]=intervals[i][0];
                        ans[i-r-1][1]=intervals[i][1];
                    }
                }else{
                    ans=new int[len-r][2];
                    ans[0][0]=rmInterval[1]+1;
                    ans[0][1]=intervals[r][1];

                    for(int i=r+1;i<len;++i){
                        ans[i-r][0]=intervals[i][0];
                        ans[i-r][1]=intervals[i][1];
                    }
                }
            }else if(l>=0&&r==Integer.MAX_VALUE){
                ans=new int[l+1][2];
                if(rmInterval[0]>intervals[l][1]){
                    for(int i=0;i<=l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                }else{
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=rmInterval[0]-1;
                }
            }else{
                if(rmInterval[1]>=intervals[r][1]){//between intervals
                    ans=new int[len-(r-l)][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=rmInterval[0]>intervals[l][1]?intervals[l][1]:rmInterval[0]-1;
                    for(int i=1;i<len-r;++i){
                        ans[l+i][0]=intervals[r+i][0];
                        ans[l+i][1]=intervals[r+i][1];
                    }
                }else{//within interval
                    ans=new int[len-(r-l)+1][2];
                    for(int i=0;i<l;++i){
                        ans[i][0]=intervals[i][0];
                        ans[i][1]=intervals[i][1];
                    }
                    ans[l][0]=intervals[l][0];
                    ans[l][1]=rmInterval[0]>intervals[l][1]?intervals[l][1]:rmInterval[0]-1;
                    ans[l+1][0]=rmInterval[1]+1;
                    ans[l+1][1]=intervals[r][1];
                    for(int i=1;i<len-r;++i){
                        ans[l+1+i][0]=intervals[r+i][0];
                        ans[l+1+i][1]=intervals[r+i][1];
                    }
                }

            }
            return ans;

        }
        public void addRange(int left, int right) {
            int[] newInterval={left,right-1};
            intervals=insert(newInterval);
        }

        public boolean queryRange(int left, int right) {
            if(intervals==null) return false;
            final int len=intervals.length;
            --right;
            if(len==0||left>intervals[len-1][1]||right<intervals[0][0]) return false;
            for(int i=0;i<len;++i){
                if(left>=intervals[i][0]&&left<=intervals[i][1]){
                    if(right<=intervals[i][1]) return true;
                    if(i+1<len&&intervals[i][1]==intervals[i+1][0]-1){//相邻的两个区间可以合并
                        int pos=i;
                        while(pos+1<len){
                            if(intervals[pos][1]==intervals[pos+1][0]-1){//区间可以合并
                                if(right<=intervals[pos+1][1]) return true;//并且落在合并的区间内
                                else ++pos;//继续搜索是否有可以合并的区间
                            }else return false;
                        }
                    }else return false;
                }
            }
            return false;
        }

        public void removeRange(int left, int right) {
            int[] rmInterval={left,right-1};
            intervals=remove(rmInterval);
        }

    }

    /**
     * Your RangeModule object will be instantiated and called as such:
     * RangeModule obj = new RangeModule();
     * obj.addRange(left,right);
     * boolean param_2 = obj.queryRange(left,right);
     * obj.removeRange(left,right);
     */

    @Test
    public void testRangeModule(){
        RangeModule rangeModule=new RangeModule();
        rangeModule.addRange(2,6);
        rangeModule.addRange(2,8);
        rangeModule.addRange(4,7);
        rangeModule.addRange(8,9);
        rangeModule.removeRange(1,10);
        rangeModule.removeRange(3,5);
        rangeModule.removeRange(1,2);
        rangeModule.show();
//        System.out.println(rangeModule.queryRange(1, 7));
//        System.out.println(rangeModule.queryRange(2, 9));
//        System.out.println(rangeModule.queryRange(4, 6));

    }


    //567. Permutation in String
    private String sorted(String s){
        char[] c=s.toCharArray();
        Arrays.sort(c);
        return String.valueOf(c);
    }

    private boolean check(String a,int[] mCount){
        int[] aCount=new int[26];
        for(int i=0;i<a.length();++i) ++aCount[a.charAt(i)-'a'];
        for(int i=0;i<26;++i){
            if(aCount[i]!=mCount[i]) return false;
        }
        return true;
    }

    public boolean checkInclusion(String s1, String s2) {
        if(s1==null||s1.length()==0) return true;
        if(s2==null||s2.length()==0) return false;
        int[] mCount=new int[26];
        int len=s1.length();
        for(int i=0;i<len;++i) ++mCount[s1.charAt(i)-'a'];
        for(int i=0;i<s2.length()-len+1;++i){
            if(check(s2.substring(i,i+len),mCount)) return true;
        }
        return false;
    }

    @Test
    public void testCheckInclusion(){
        System.out.println(checkInclusion("adc","dcda"));
    }

    @Test
    public static void test_1(){
        int[] array={2,4,6,8,10,10,12,14,16};
        System.out.println(Arrays.binarySearch(array,6));
        Stream.of(2,4,6,8,10,10,12,14,16).filter(c -> c>=10).map(c->(char)(c+'a')).forEach(System.out::println);
        System.out.println(MessageFormat.format("\n{0}-{1}","ss","dd"));

        List<Integer> l=new ArrayList<>(Arrays.asList(2,4,6,8,10,10,12,14,16));
        System.out.println(l);
        Collections.shuffle(l);
        System.out.println(l);
        BigDecimal decimal=new BigDecimal("123.456");
        System.out.println(decimal.toString());

    }

}
