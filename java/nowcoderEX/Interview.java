package com.xycode.nowcoderEX;

import org.testng.annotations.Test;

import java.util.*;

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



}
