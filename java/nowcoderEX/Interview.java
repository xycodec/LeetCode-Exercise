package com.xycode.nowcoderEX;

import org.testng.annotations.Test;
import sun.awt.image.ImageWatched;
import sun.reflect.generics.tree.Tree;

import javax.swing.plaf.metal.MetalTheme;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: Interview
 *
 * @Author: xycode
 * @Date: 2020/3/20
 * @Description: this is description of the Interview class
 **/
public class Interview {
    //找到所有的顺子
    private List<int[]> selectFive(int[] cardCount){
        List<int[]> result=new LinkedList<>();
        for(int i=0;i<=cardCount.length-5;++i){
            if(cardCount[i]>0){
                int cnt=1;
                for(int j=i+1;j<cardCount.length;++j){
                    if(cnt==5) break;
                    if(cardCount[j]>0) ++cnt;
                    else break;
                }
                if(cnt==5){
                    int[] tmp=Arrays.copyOf(cardCount,cardCount.length);
                    for(int k=i;k<i+5;++k){
                        --tmp[k];
                    }
                    result.add(tmp);
                }
            }
        }
        return result;
    }
    //找出所有的三连对
    private List<int[]> selectSix(int[] cardCount){
        List<int[]> result=new LinkedList<>();
        for(int i=0;i<=cardCount.length-3;++i){
            if(cardCount[i]>=2){
                int cnt=1;
                for(int j=i+1;j<cardCount.length;++j){
                    if(cnt==3) break;
                    if(cardCount[j]>=2) ++cnt;
                    else break;
                }
                if(cnt==3){
                    int[] tmp=Arrays.copyOf(cardCount,cardCount.length);
                    for(int k=i;k<i+3;++k){
                        tmp[k]-=2;
                    }
                    result.add(tmp);
                }
            }
        }
        return result;
    }
    //打完扑克牌的最少步数
//    tip: bfs,时间复杂度略高
    public int playingCard(int[] cardCount){
        Queue<int[]> queue=new LinkedList<>();
        Queue<Integer> cntQ=new LinkedList<>();//记录每种选择对应的步数
//        Set<String> states=new HashSet<>();//记录状态,用于剪枝,貌似更慢了....
        queue.add(cardCount);
        cntQ.add(0);
        int ans=Integer.MAX_VALUE;
        int max=0;
        for(int i=0;i<cardCount.length;++i) {//只出单张和对子,所需的操作数,最优解不会超过这个,用于剪枝
            if(cardCount[i]==1){
                max+=cardCount[i];
            }else if(cardCount[i]%2==0) {
                max+=cardCount[i]/2;
            }else{
                max+=cardCount[i]/2+1;
            }
        }
        while (!queue.isEmpty()){
            int[] tmp=queue.poll();
            int cnt=cntQ.poll();
//            states.add(Arrays.toString(tmp));

            List<int[]> tmp5s=selectFive(tmp),tmp6s=selectSix(tmp);
            //出连续的三个对
            for(int[] tmp6:tmp6s){
                if(cnt>=max) break;
//                if(states.contains(Arrays.toString(tmp6))) continue;
                queue.add(tmp6);
                cntQ.add(cnt+1);
            }
            //出连续的五张
            for(int[] tmp5:tmp5s){
                if(cnt>=max) break;
//                if(states.contains(Arrays.toString(tmp5))) continue;
                queue.add(tmp5);
                cntQ.add(cnt+1);
            }

            if(tmp5s.isEmpty()&&tmp6s.isEmpty()){//没有顺子和连续的三个对子,这时可以算出一种方案了
                for(int i=0;i<tmp.length;++i) {
                    if(tmp[i]==1){//单张
                        cnt+=tmp[i];
                    }else if(tmp[i]%2==0) {//能取对子
                        cnt+=tmp[i]/2;
                    }else{
                        cnt+=tmp[i]/2+1;
                    }
                }
                ans=Math.min(ans,cnt);
            }
        }
        return ans;
    }

    @Test
    public void testPlayingCard() {
        System.out.println(playingCard(new int[]{4,4,4,4,4,4,4,4,4,4}));
//        for(int[] tmp5:selectFive(new int[]{1,1,1,2,2,2,2,2,1,1})){
//            System.out.println(Arrays.toString(tmp5));
//        }
//        System.out.println();
//        for(int[] tmp6:selectSix(new int[]{1,1,1,2,2,2,2,2,1,1})){
//            System.out.println(Arrays.toString(tmp6));
//        }
    }


    static class getPoker {
        int min = Integer.MAX_VALUE;
        int[] poker;

        public int getCount(int[] arr) {
            poker = arr;
            backtrace(0, 0);
            return min;
        }

        public void backtrace(int n, int count) {
            if (n >= 10) {
                min = Math.min(min, count);
                return;
            }
            if (poker[n] == 0) {
                backtrace(n + 1, count);
                return;
            }
            int one = getContinue(n);
            int two = getTwoContinue(n);
            if (one > 0) {     //可以打顺子
                divide(n, 1, 5);
                backtrace(n, count + 1);
                add(n, 1, 5);
            }
            if (two > 0) {     //可以打连对
                divide(n, 2, 3);
                backtrace(n, count + 1);
                add(n, 2, 3);
            }
            if (poker[n] >= 2) {  //可以打对子
                poker[n] -= 2;
                backtrace(n, count + 1);
                poker[n] += 2;
                return;   //因为能打对子就不会打单张，此时return
            }
            {
                poker[n]--;
                backtrace(n, count + 1);
                poker[n]++;
            }
        }

        public int getContinue(int n) {     //判断顺子
            if (n + 1 > 6)
                return 0;
            int min = 5;
            for (int i = n; i < n + 5; i++) {
                min = Math.min(min, poker[i]);
            }
            return min;
        }

        public int getTwoContinue(int n) {   //判断连对
            if (n + 1 > 8)
                return 0;
            int min = 5;
            for (int i = n; i < n + 3; i++) {
                min = Math.min(min, poker[i] / 2);
            }
            return min;

        }

        public void divide(int n, int count, int time) {
            for (int i = n; i < n + time; i++) {
                poker[i] = poker[i] - count;
            }
        }

        public void add(int n, int count, int time) {
            for (int i = n; i < n + time; i++) {
                poker[i] = poker[i] + count;
            }
        }

        public static void main(String[] args) {
            int[] arr = {4,4,4,4,4,4,4,4,4,4};
            System.out.println(new getPoker().getCount(arr));
        }
    }


    //描述: 给一系列字符串,要求拼接成一个最长不递减的字符串,返回其长度
    public static int music(String[] s){
        Arrays.sort(s, Comparator.comparingInt(x -> x.charAt(x.length()-1)));
        int ans = s[0].length();
        int dp[] = new int[s.length];   //dp[i]表示包含当前第i个字符串的最大长度
        dp[0] = s[0].length();
        for (int i = 1; i < s.length; i++) {
            int curMax = s[i].length();
            char curStart = s[i].charAt(0);
            for (int k = 0; k < i; k++) {
                char prevEnd = s[k].charAt(s[k].length() - 1);
                if(curStart >= prevEnd){ //若能连接
                    curMax = Math.max(dp[k] + s[i].length(), curMax);  //寻找可以连接的最大长度
                }
            }
            dp[i] = curMax;
            ans = Math.max(ans,curMax);
        }
        return ans;
    }

    @Test
    public void testMusic() {
        System.out.println(music(new String[]{"aaa","bcd","bc","zzz"}));
    }

    //头条面试题,课程安排;relatedCourseMp存储课程ID -> 该课程依赖的(先修)那些课程的Id
    public List<List<Integer>> courseSchedule(Map<Integer,List<Integer>> relatedCourseMp) {
        List<List<Integer>> ans=new LinkedList<>();
        if(relatedCourseMp.isEmpty()) return ans;
        Set<Integer> hasScheduled=new HashSet<>();//记录已经安排过的课程
        Set<Integer> related=new HashSet<>();
        boolean flag=true;
        while(flag){
            ans.add(new LinkedList<>());
            related.clear();//记录那些依赖当前课程的后续课程,每轮循环需要clear()
            for(int courseId:relatedCourseMp.keySet()){
                if(!hasScheduled.contains(courseId)&&!related.contains(courseId)&&relatedCourseMp.get(courseId).isEmpty()){
                    ans.get(ans.size()-1).add(courseId);
                    hasScheduled.add(courseId);
                    for(int tmpId:relatedCourseMp.keySet()){
                        if(tmpId!=courseId){
                            if(!relatedCourseMp.get(tmpId).isEmpty()){
                                related.add(tmpId);//tmpId课程依赖courseId课程,这里记录是为了防止remove后为空了的课程被提前安排
                                relatedCourseMp.get(tmpId).remove((Integer) courseId);
                            }
                        }
                    }
                }
            }

            if(ans.get(ans.size()-1).isEmpty()) {
                ans.remove(ans.size()-1);
                flag=false;
            }
        }
        return ans;
    }

    @Test
    public void testCourseSchedule() {
        System.out.println(courseSchedule(new HashMap<Integer,List<Integer>>(){
            {
                put(1,new LinkedList<Integer>(){
                    {

                    }
                });
                put(2,new LinkedList<Integer>(){
                    {
                        add(1);
                    }
                });
                put(3,new LinkedList<Integer>(){
                    {
                        add(1);
                        add(2);
                    }
                });
                put(4,new LinkedList<Integer>(){
                    {
                        add(2);
                    }
                });
                put(5,new LinkedList<Integer>(){
                    {
                        add(3);
                    }
                });
                put(6,new LinkedList<Integer>(){
                    {
                        add(1);
                    }
                });
            }
        }));
    }

    private String reverse(char[] str){
        int l=0,r=str.length-1;
        char tmp;
        while(l<r){
            tmp= str[l];
            str[l]=str[r];
            str[r]=tmp;
            ++l;
            --r;
        }
        return String.valueOf(str);
    }

    //压缩算法
    //[n|str]表示将str重复n次,给定一个包含这样形式的字符串(可能有递归的形式,例如: HG[3|B[2|CA]]F),输出其解压缩的形式
    public String unzipStr(String s){
        Stack<String> stack=new Stack<>();
        for(int i=0;i<s.length();++i){
            String c= String.valueOf(s.charAt(i));
            if(!c.equals("]")){
                stack.push(c);
            }else{
                StringBuilder sb=new StringBuilder();//待解析的字符串
                //此时遇到了']', 需要出栈，直到遇到第一个匹配的'['
                while(!stack.isEmpty()&& !stack.peek().equals("[")){
                    sb.append(stack.pop());
                }
                stack.pop();//弹出无用的'['
                String tmp=sb.reverse().toString();//因为是从栈中弹出的,所以需要反转,并且此时不包含'['与']'了
                int cnt=Integer.parseInt(tmp.substring(0,tmp.indexOf('|')));//解析出重复的次数
                String repeatedStr=reverse(tmp.substring(tmp.indexOf('|')+1).toCharArray());//解析出重复的字符串(放入栈中,需要反转)
                //把局部解析出的字符串放入栈中,以便后续解析
                for(int j=0;j<cnt;++j){
                    stack.push(repeatedStr);
                }
            }
        }
        StringBuilder ans = new StringBuilder();
        while(!stack.isEmpty()){
            ans.append(stack.pop());
        }
        return ans.reverse().toString();
    }

    @Test
    public void testUnzipStr() {
        System.out.println(unzipStr("HG[3|B[2|CA]]F"));
    }

    //逛街
    //给定一个数组,表示一排楼的高度,站在某个楼上,往前或往后看(包含所站的楼),输出站在每个楼所能看到的楼数量,注意较高(或高度相同的楼)的楼会挡住后面较矮的楼
    public static void street(int[] arr){
        int len=arr.length;
        // stack中要保存的是 能看见的楼的 index
        int[] rightLook = new int[len];
        Stack<Integer> stack = new Stack<>();//保存下标,notice: 单调栈
        for(int i = len - 1 ; i >= 0 ; i--){
            rightLook[i] = stack.size();
            while((!stack.isEmpty()) && (arr[i] >= arr[stack.peek()])){
                stack.pop();
            }
            stack.push(i);
        }
        stack.clear();
        int[] leftLook=new int[len];
        for(int i = 0 ; i < len ; i++){
            leftLook[i]=stack.size();
            while((!stack.isEmpty()) && (arr[i] >= arr[stack.peek()])){
                stack.pop();
            }
            stack.push(i);
        }
        for(int i=0;i<len;++i){
            System.out.print(leftLook[i]+rightLook[i]+1+" ");//+1,自己所处的楼也要算上去
        }
    }

    @Test
    public void testStreet() {
        street(new int[]{5,3,8,3,2,5});
    }

    static long pairCnt=0;//统计逆序对数目
    private static void mergeSortPair(int[] array, int s, int t, int[] tmp){
        if(s<t){
            int mid=s+(t-s)/2;
            mergeSortPair(array,s,mid,tmp);
            mergeSortPair(array,mid+1,t,tmp);
            //合并两个有序子数组
            int i=s,j=mid+1,cnt=0;
            while(i<=mid||j<=t){
                if(j>t){
                    tmp[cnt++]=array[i++];
                }else if(i>mid){
                    tmp[cnt++]=array[j++];

                }else if(array[i]<array[j]){//真正的比较,从小到大
                    tmp[cnt++]=array[i++];
                }else{
                    pairCnt+=mid-i+1;//逆序对数目增加len(mid->i)=mid-i+1
                    tmp[cnt++]=array[j++];
                }
            }

            //复制排序好的数组到原始数组array中
            cnt=0;
            while (s<=t){
                array[s++]=tmp[cnt++];
            }
        }
    }

    private static void reverse(int[] array,int i,int j){
        if(i>=j) return;
        int tmp;
        while(i<j){
            tmp=array[i];
            array[i]=array[j];
            array[j]=tmp;
            ++i;
            --j;
        }
    }

    //逆序对
    //输出每次反转操作后的逆序对个数
    public static void reversePair() {
        Scanner scanner=new Scanner(System.in);
        int n= scanner.nextInt();
        int[] array=new int[(int) Math.pow(2,n)];
//        System.out.println(array.length);
        for(int i=0;i<array.length;++i){
            array[i]=scanner.nextInt();
        }
        int m=scanner.nextInt();
        int[] mergeTmp=new int[array.length];

        for(int i=0;i<m;++i){
            int q=scanner.nextInt();
            int sep=(int) Math.pow(2,q);
//            System.out.println(Arrays.toString(array)+", "+sep);
            if(sep>1){
                for(int j=0;j<array.length;){
                    reverse(array,j,j+sep-1);
                    j+=sep;
                }
            }
            int[] arrayCpy=Arrays.copyOf(array,array.length);
            mergeSortPair(arrayCpy,0,array.length-1,mergeTmp);
            System.out.println(pairCnt);
            pairCnt=0;
        }
    }

    @Test
    public void testReversePair() {
        reversePair();
    }

    //假期
    //dp[i][0], dp[i][1], dp[i][2] 分别记录第i天 休息/工作/健身 累计的最小休息天数
    public static int minHolidays(int[] workDays,int[] fitnessDays){
        int n=workDays.length;
        int[][] dp=new int[n+1][3];//0: 放假, 1: 工作, 2: 健身
        for(int i=1;i<=n;++i){
            dp[i][0]=dp[i][1]=dp[i][2]=Integer.MAX_VALUE>>1;//因为是求最小值,所以默认初始化一个较大的值
        }
        for(int i=1;i<=n;++i){
            dp[i][0]=Math.min(dp[i-1][0],Math.min(dp[i-1][1],dp[i-1][2]))+1;
            if(fitnessDays[i-1]==1){//前一天可以健身
                dp[i][1]=Math.min(dp[i-1][0],dp[i-1][2]);//不能连续两天健身
            }
            if(workDays[i-1]==1){//前一天可以工作
                dp[i][2]=Math.min(dp[i-1][0],dp[i-1][1]);//不能连续两天工作
            }

        }
        return Math.min(dp[n][0],Math.min(dp[n][1],dp[n][2]));
    }

    @Test
    public void testMinHolidays() {
        System.out.println(minHolidays(new int[]{1,1,0,0},new int[]{0,1,1,0}));
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int[] workDays=new int[n],fitnessDays=new int[n];
        for(int i=0;i<n;++i){
            workDays[i]=scanner.nextInt();
        }
        for(int i=0;i<n;++i){
            fitnessDays[i]=scanner.nextInt();
        }
        System.out.println(minHolidays(workDays,fitnessDays));
    }


    //现给定任意正整数 n，请寻找并输出最小的正整数 m（m>9），使得 m 的各位（个位、十位、百位 ... ...）之乘积等于n，若不存在则输出 -1。
    public static int solution2(int n) {
        int[] digits=new int[10];
        for(int i=9;i>=2;){
            if(n>=i&&n%i==0){
                n/=i;
                ++digits[i];
            }else{
                --i;
            }
        }
        if(n!=1) return -1;//凑不出因子
        int base=1;
        int ans=0;
        for(int i=9;i>=2;){
            if(digits[i]>0){
                ans+=base*i;
                base*=10;
                --digits[i];
            }else{
                --i;
            }
        }
        return ans;
    }


    //在vivo产线上，每位职工随着对手机加工流程认识的熟悉和经验的增加，日产量也会不断攀升。
    //假设第一天量产1台，接下来2天(即第二、三天)每天量产2件，接下来3天(即第四、五、六天)每天量产3件 ... ...
    //以此类推，请编程计算出第n天总共可以量产的手机数量。
    public static int solution3(int n) {
        int sum=0;
        int k=0;
        for(int i=1;i<=n;++i){
            sum+=i;
            if(sum>=n){
                k=i;
                break;
            }
        }
        int ans=0;
        for(int i=1;i<=k-1;++i){
            ans+=i*i;
        }
        while(sum>=n) sum-=k;
        ans+=(n-sum)*k;
        return ans;
    }

//    public Interview() {
//        System.out.println("construct");//证明testNG测试时会创建类的实例
//    }

    @Test
    public void testVivo() {
        System.out.println(solution2(123456789));
        System.out.println(solution3(15));
    }


    //快手
    //小明最近在做病毒自动检测，他发现，在某些library 的代码段的二进制表示中，如果包含子串并且恰好有k个1，就有可能有潜在的病毒。library的二进制表示可能很大，并且子串可能很多，人工分析不可能，于是他想写个程序来先算算到底有多少个子串满足条件。如果子串内容相同，但是开始或者结束位置不一样，则被认为是不同的子串。
    //注：子串一定是连续的。例如"010"有6个子串，分别是 "0, "1", "0", "01", "10", "010"
    //输出一个整数，所有满足只包含k个1的子串的个数。
    public static int kuaishouSolution1(String library,int k){
        int cnt=0;
        for(int i=0;i<library.length();++i){
            if(library.charAt(i)=='1') ++cnt;
        }
        if(k>cnt) return 0;
        int p=0,q=0;
        cnt=library.charAt(0)==0?0:1;
        int ans=0;
        while (q<library.length()){
            if(cnt>=k){
                ++ans;
                if(library.charAt(q)=='1') ++cnt;
                ++q;
            }else{
                ++q;
                if(q<library.length()&&library.charAt(q)=='1'){
                    ++cnt;
                }
            }
        }
        return ans;
    }

    static class Node{
        int score;
        int[] count=new int[5];
        int maxCount;
        public Node() {
        }
    }
    public static void kuaishouSolution2(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt(),m=scanner.nextInt();
        Node[] nodes=new Node[m];
        for(int i=0;i<m;++i) nodes[i]=new Node();
        for(int i=0;i<n;++i){
            String s=scanner.next();
            for(int j=0;j<m;++j){
                nodes[j].count[s.charAt(j)-'A']++;
            }
        }
        long ans=0;
        for(int i=0;i<m;++i){
            nodes[i].score=scanner.nextInt();
            nodes[i].maxCount=nodes[i].count[0];
            for(int j=1;j<5;++j){
                if(nodes[i].count[j]>nodes[i].maxCount){
                    nodes[i].maxCount=nodes[i].count[j];
                }
            }
            ans+=nodes[i].maxCount*nodes[i].score;
        }
        System.out.println(ans);
    }


    //给定一组石头，每个石头有一个正数的重量。每一轮开始的时候，选择两个石头一起碰撞，假定两个石头的重量为x，y，x<=y,碰撞结果为
    //1. 如果x==y，碰撞结果为两个石头消失
    //2. 如果x != y，碰撞结果两个石头消失，生成一个新的石头，新石头重量为y-x
    //
    //最终最多剩下一个石头为结束。求解最小的剩余石头质量的可能性是多少。
    public static int kuaishouSolution3(int[] stones){
        PriorityQueue<Integer> queue=new PriorityQueue<>((x,y)->-Integer.compare(x,y));
        for (int stone : stones) {
            queue.add(stone);
        }
        int ans=0;
        while(!queue.isEmpty()){
            int x=queue.poll();
            if(queue.isEmpty()){
                ans=x;
                break;
            }
            int y=queue.poll();
            queue.add(Math.abs(x-y));
        }
        return ans;
    }

    @Test
    public void testKuaishou() {
        System.out.println(kuaishouSolution1("1010",1));

        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int[] stones=new int[n];
        for(int i=0;i<n;++i){
            stones[i]=scanner.nextInt();
        }
        System.out.println(kuaishouSolution3(stones));
    }

    //美团点评
    //给出一个布尔表达式的字符串，比如：true or false and false，表达式只包含true，false，and和or，
    //现在要对这个表达式进行布尔求值，计算结果为真时输出true、为假时输出false，不合法的表达时输出error（比如：true true）。
    //表达式求值是注意and 的优先级比 or 要高，比如：true or false and false，等价于 true or (false and false)，计算结果是 true。
    public static String meituanSolution1(String s){
        String[] tmp=s.split(" ");
        Stack<String> stack= new Stack<>();
        //先算优先级比较高的"and"
        for(int i=0;i<tmp.length;++i){
            if(tmp[i].equals("and")){
                if(stack.isEmpty()||i+1>=tmp.length||
                        (!stack.peek().equals("true")&&!stack.peek().equals("false"))||
                        (!tmp[i+1].equals("true")&&!tmp[i+1].equals("false"))) return "error";
                boolean item1=Boolean.parseBoolean(stack.pop());
                boolean item2=Boolean.parseBoolean(tmp[++i]);
                stack.push(item1&&item2?"true":"false");
            }else{
                if(!tmp[i].equals("false")&&!tmp[i].equals("true")&&!tmp[i].equals("or")) return "error";
                stack.push(tmp[i]);
            }
        }
        //计算优先级较低的"or"
        while(stack.size()>1){
            if(stack.size()<3||(!stack.peek().equals("true")&&!stack.peek().equals("false"))) return "error";
            boolean item1= Boolean.parseBoolean(stack.pop());
            if(!stack.peek().equals("or")) return "error";
            stack.pop();//弹出无用的or
            if(!stack.peek().equals("true")&&!stack.peek().equals("false")) return "error";
            boolean item2=Boolean.parseBoolean(stack.pop());
            stack.push(item1||item2?"true":"false");
        }
        String ans=stack.pop();
        if(ans.equals("true")||ans.equals("false")){
            return ans;
        }else{
            return "error";
        }
    }

    //给出两个字符串，分别是模式串P和目标串T，判断模式串和目标串是否匹配，匹配输出 1，不匹配输出 0。
    //模式串中‘？’可以匹配目标串中的任何字符，模式串中的 ’*’可以匹配目标串中的任何长度的串，
    //模式串的其它字符必须和目标串的字符匹配。例如P=a?b，T=acb，则P 和 T 匹配。
    public static int meituanSolution2(String s,String p){
        Pattern pattern=Pattern.compile(p.replaceAll("\\*",".*").replaceAll("\\?","."));
        Matcher matcher=pattern.matcher(s);
        if(matcher.find()){
            if(s.equals(matcher.group())){
                return 1;
            }
        }
        return 0;
    }

    /**
     * 用户喜好
     * 为了不断优化推荐效果，今日头条每天要存储和处理海量数据。
     * 假设有这样一种场景：我们对用户按照它们的注册时间先后来标号，对于一类文章，每个用户都有不同的喜好值，
     * 我们会想知道某一段时间内注册的用户（标号相连的一批用户）中，有多少用户对这类文章喜好值为k。因为一些特殊的原因，
     * 不会出现一个查询的用户区间完全覆盖另一个查询的用户区间(不存在L1<=L2<=R2<=R1)。
     */
    public static void byteDanceSolution1(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int[] array=new int[n+1];
        Map<Integer,TreeSet<Integer>> mp=new HashMap<>();
        for(int i=1;i<=n;++i){
            array[i]=scanner.nextInt();
            TreeSet<Integer> treeSet=mp.getOrDefault(array[i],new TreeSet<>());
            treeSet.add(i);
            mp.put(array[i],treeSet);
        }
//        System.out.println(mp);
        int q=scanner.nextInt(),l,r,k;
        List<Integer> ans=new LinkedList<>();
        for(int i=0;i<q;++i){
            l=scanner.nextInt();
            r=scanner.nextInt();
            k=scanner.nextInt();
            if(mp.containsKey(k)){
                ans.add(mp.get(k).subSet(l,r+1).size());
            }else{
                ans.add(0);
            }
        }
        ans.forEach(System.out::println);
    }

    /**
     * 手串
     * 作为一个手串艺人，有金主向你订购了一条包含n个杂色串珠的手串——每个串珠要么无色，要么涂了若干种颜色。
     * 为了使手串的色彩看起来不那么单调，金主要求，手串上的任意一种颜色（不包含无色），
     * 在任意连续的m个串珠里至多出现一次（注意这里手串是一个环形）。手串上的颜色一共有c种。
     * 现在按顺时针序告诉你n个串珠的手串上，每个串珠用所包含的颜色分别有哪些。请你判断该手串上有多少种颜色不符合要求。
     * 即询问有多少种颜色在任意连续m个串珠中出现了至少两次。
     */
    public static void byteDanceSolution2(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt(),m=scanner.nextInt(),c=scanner.nextInt();
        List<List<Integer>> list=new ArrayList<>();
        for(int i=0;i<n;++i){
            int numI=scanner.nextInt();
            List<Integer> tmpList=new ArrayList<>();
            for(int j=0;j<numI;++j){
                tmpList.add(scanner.nextInt());
            }
            list.add(tmpList);
        }

        //化环为序列
        for(int i=0;i<m-1;++i){
            list.add(list.get(i));
        }
//        System.out.println(list);
        int ans=0;
        Set<Integer> duplicateColor=new HashSet<>();
        for(int i=0;i<list.size()-(m-1);++i){//首尾相接
            Set<Integer> set=new HashSet<>();
            for(int j=i;j<i+m;++j){//串珠的距离最大为m
                for(int color:list.get(j)){
                    if(ans==c){
                        System.out.println(c);
                        return;
                    }
                    if(set.contains(color)){//判断串珠颜色是否重复
                        if(!duplicateColor.contains(color)){//记录已被标记为重复的颜色
                            ++ans;
                            duplicateColor.add(color);
                        }
                    }else{
                        set.add(color);
                    }
                }
            }

        }
        System.out.println(ans);
    }

    /**
     * 1. 三个同样的字母连在一起，一定是拼写错误，去掉一个的就好啦：比如 helllo -> hello
     * 2. 两对一样的字母（AABB型）连在一起，一定是拼写错误，去掉第二对的一个字母就好啦：比如 helloo -> hello
     * 3. 上面的规则优先“从左到右”匹配，即如果是AABBCC，虽然AABB和BBCC都是错误拼写，应该优先考虑修复AABB，结果为AABCC
     */
    public static void byteDance2019Solution1(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        Pattern p1=Pattern.compile("(.)\\1{2}");
        Pattern p2=Pattern.compile("(.)\\1{1}(.)\\2{1}");
        for(int i=0;i<n;++i){
            String word=scanner.next();
            while(true){
                Matcher matcher=p1.matcher(word);
                if(matcher.find()){
                    String replaced=matcher.group().substring(0,2);
                    word=word.replaceFirst("(.)\\1{2}",replaced);
                }else{
                    break;
                }
            }
            while (true){
                Matcher matcher=p2.matcher(word);
                if(matcher.find()){
                    String replaced=matcher.group().substring(0,3);
                    word=word.replaceFirst("(.)\\1{1}(.)\\2{1}",replaced);
                }else{
                    break;
                }
            }
            System.out.println(word);
        }
    }

    private static boolean checkArrive(int[] h,int e,int maxE){
        for(int i=0;i<h.length;++i){
            if(e>maxE) return true;//e已经超过最大值了,那么一定满足要求
            if(h[i]>e){
                e-=h[i]-e;
            }else{
                e+=e-h[i];
            }
            if(e<0) return false;
        }
        return true;
    }

    public static void byteDance2019Solution6(){
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            int n = 1024-scanner.nextInt();
            int[]coins={1,4,16,64};
            int max=n+1;
            int[]dp=new int[max+1];
            Arrays.fill(dp,max);
            dp[0]=0;
            for(int i=1;i<=n;i++){//完全背包
                for(int coin:coins){
                    if(i>=coin)dp[i]=Math.min(dp[i],dp[i-coin]+1);
                }
            }
            System.out.println(dp[n]);
        }
    }

    public static void byteDance2019Solution7(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int maxE=-1;
        int[] h=new int[n];
        for(int i=0;i<n;++i){
            h[i]=scanner.nextInt();
            maxE=Math.max(maxE,h[i]);
        }
        int l=0,r=maxE,mid;
        while (l<r){
            mid=l+(r-l)/2;
            if(checkArrive(h,mid,maxE)){
                r=mid;//找到满足要求的最小值,这时满足要求,r=mid表明暂时保留此时的结果,若r=mid-1可能会错过解
            }else{
                l=mid+1;//左半边区间不满足,肯定要在右区间内找了,l=mid+1
            }
        }
        System.out.println(l);
    }

    public static void byteDance2017Solution1(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        String[] nums=new String[n];
        Map<Character,Long> mp=new HashMap<>();
        Set<Character> head=new HashSet<>();
        for(int i=0;i<n;++i){
            nums[i]=scanner.next();
            head.add(nums[i].charAt(0));
            long base=1;
            for(int j=nums[i].length()-1;j>=0;--j){
                mp.put(nums[i].charAt(j),mp.getOrDefault(nums[i].charAt(j),0L)+base);
                base*=10;
            }
        }
        List<HashMap.Entry<Character,Long>> list=new ArrayList<>();
        list.addAll(mp.entrySet());
        Collections.sort(list, (x,y)->-Long.compare(x.getValue(),y.getValue()));
        if(list.size()==10){
            if(head.contains(list.get(0).getKey())){//产生前导0的冲突
                for(int i=9;i>=1;--i){
                    if(!head.contains(list.get(i).getKey())){//那就找到一个最小权重的字符(不冲突的),让它映射到0,因为是0,不对结果产生影响,所以这里直接删除该映射即可
//                        System.out.println(list.get(i));
                        list.remove(i);
                        break;
                    }
                }
            }
        }

        long sum=0;
        long index=9;
        for(int i=0;i<list.size();++i){
            sum+=index*list.get(i).getValue();
            --index;
        }
        System.out.println(sum);
    }

    public static void huaweiSolution1(){
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            int n=scanner.nextInt();
            if(n==0) break;
            int ans=0;
            while (true){
                if(n<2) break;
                if(n==2){
                    ++ans;
                    n=0;
                }else{
                    n-=2;
                    ++ans;
                }
            }
            System.out.println(ans);
        }
    }

    public static void huaweiSolution2(){
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()) {
            int n = scanner.nextInt();
            TreeSet<Integer> treeSet = new TreeSet<>();
            for (int i = 0; i < n; ++i) {
                treeSet.add(scanner.nextInt());
            }
            while (!treeSet.isEmpty()){
                System.out.println(treeSet.pollFirst());
            }
        }
    }

    public static void huaweiSolution3(){
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()) {
            String s=scanner.next().substring(2);
            System.out.println(Long.parseLong(s,16));

        }
    }

    public static void huawei2016Solution1(){
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNextInt()) {
            int n=scanner.nextInt(),m=scanner.nextInt();
            TreeMap<Integer,Integer> mp=new TreeMap<>();
            for(int i=0;i<n;++i){
                mp.put(i+1,scanner.nextInt());
            }
            for(int i=0;i<m;++i){
                char op=scanner.next().charAt(0);
                int a=scanner.nextInt(),b=scanner.nextInt();
                if(op=='Q'){
                    System.out.println(Collections.max(mp.subMap(Math.min(a,b),Math.max(a,b)+1).values()));
                }else if(op=='U'){
                    mp.put(a,b);
                }
            }
        }
    }

    public static void huawei2016SolutionOne(){
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNextInt()){
            int n=scanner.nextInt();
            n=Math.min(n,1000);
            List<Integer> list=new LinkedList<>();
            for(int i=0;i<n;++i){
                list.add(i);
            }
            Iterator<Integer> iterator=list.iterator();
            int cnt=0;
            while(list.size()>1){
                while(iterator.hasNext()&&cnt<=2){
                    iterator.next();
                    ++cnt;
                }
                if(cnt==3){
                    iterator.remove();
                    cnt=0;
                }else{
                    iterator=list.iterator();
                }
            }
            System.out.println(list.get(0));
        }
    }

    public static void huawei2016SolutionTwo(){
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            String s=scanner.next();
            Set<Character> set=new HashSet<>();
            for(char c:s.toCharArray()){
                if(!set.contains(c)){
                    System.out.print(c);
                    set.add(c);
                }
            }
            System.out.println();
        }
    }

    /**
     * <p>37. Sudoku Solver</p>
     * 给定一个9*9的数独矩阵,一件填了一些数字,'.'代表空的格子,请问填写完空格,使得其满足数独的规则
     * 每行/每列 1~9各出现一次,3*3的九宫格1~9只能出现一次
     * @param board 数独矩阵.
     * @param x,y 搜索到的位置.
     * @param grid 标记九宫格的使用数字.
     * @param rows,cols 标记行,列的使用数字.
     */
    static boolean ok=false;//标识是否已找到解
    private static void sudokuDfs(int[][] board,int x,int y,boolean[][][] grids,boolean[][] rows,boolean[][] cols){
        if(ok) return;
        if(x==8&&y==9) {
            ok=true;
            return;
        }
        if(y==9){
            sudokuDfs(board,x+1,0,grids,rows,cols);
        }else{
            if(board[x][y]!=0){
                sudokuDfs(board,x,y+1,grids,rows,cols);
            }else{
                for(int num=1;num<=9;++num){
                    if(!rows[x][num]&&!cols[y][num]&&!grids[x/3][y/3][num]){
                        rows[x][num]=true;
                        cols[y][num]=true;
                        grids[x/3][y/3][num]=true;
                        board[x][y]= num;

                        sudokuDfs(board,x,y+1,grids,rows,cols);
                        if(ok) return;

                        rows[x][num]=false;
                        cols[y][num]=false;
                        grids[x/3][y/3][num]=false;
                        board[x][y]=0;
                    }
                }
            }
        }
    }

    public static void solveSudoku(int[][] board) {
        boolean[][][] grids=new boolean[3][3][10];
        boolean[][] rows=new boolean[9][10];
        boolean[][] cols=new boolean[9][10];
        for(int i=0;i<board.length;++i){
            for(int j=0;j<board[i].length;++j){
                if(board[i][j]!=0){
                    grids[i/3][j/3][board[i][j]]=true;
                    rows[i][board[i][j]]=true;
                    cols[j][board[i][j]]=true;
                }
            }
        }
        sudokuDfs(board,0,0,grids,rows,cols);
    }

    public static void huawei2016SolutionThree(){
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNextInt()){
            int[][] board=new int[9][9];
            for(int i=0;i<9;++i){
                for(int j=0;j<9;++j){
                    board[i][j]=scanner.nextInt();
                }
            }
            solveSudoku(board);
            for(int i=0;i<9;++i){
                for(int j=0;j<8;++j){
                    System.out.print(board[i][j]+" ");
                }
                System.out.println(board[i][8]);
            }
        }
    }


    /**
     * 找到字符串中出现频次最大的那个字符,若不止一个最大频次的,則返回第一个出现的(最大频次的字符)
     * @param str
     */
    private static void huaweiInterview1(char[] str){
        int[] count=new int[256];
        int maxCount=0;
        for(char c:str){
            ++count[c];
            maxCount=Math.max(maxCount,count[c]);
        }

        for(char c:str){
            if(count[c]==maxCount){
                System.out.println(c);
                break;
            }
        }

    }

    public static void main(String[] args) throws IOException {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
//        String p=reader.readLine(),s=reader.readLine();
//        System.out.println(meituanSolution2(s,p));
        kuaishouSolution2();
    }

}
