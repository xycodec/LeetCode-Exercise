package com.xycode.interview;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: QihuInterview
 *
 * @Author: xycode
 * @Date: 2020/4/16
 * @Description: this is description of the QihuInterview class
 **/
public class QiHuInterview {

    private static void qihuSolution1(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt(),m=scanner.nextInt();
        int maxAbility=n;
        List<Integer> list=new LinkedList<>();
        int[] cnt=new int[n+1];
        for(int i=1;i<=n;++i){
            list.add(scanner.nextInt());
        }
        long ans=0;
        int pos=list.get(0);
        while(cnt[pos]<m){
            if(list.get(0)!=maxAbility){
                if(list.get(0)>list.get(1)){
                    int tmp=list.remove(1);
                    cnt[tmp]=0;
                    list.add(tmp);
                }else{
                    int tmp=list.remove(0);
                    cnt[tmp]=0;
                    list.add(tmp);
                }
                pos=list.get(0);
                ++cnt[pos];
                ++ans;
            }else{
                ans+=m-cnt[maxAbility];
                break;
            }
        }
        System.out.println(ans);
    }

    static long count=0;
    static long total=0;
    private static void dfs(int ai,int i,int n){
        if(ai==0){
            ++count;
            ++total;
            return;
        }
        if(i==n){
            ++total;
            return;
        }
        for(int k0=0;k0<=ai;++k0){
            dfs(ai-k0,i+1,n);
        }
    }
    private static void qihuSolution2(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt(),a0=scanner.nextInt();
        if(n==1){
            System.out.printf("%.5f\n",1.0/(a0+1));
            return;
        }else if(a0==1){
            System.out.printf("%.5f\n",1-1.0/(n+1));
            return;
        }
        dfs(a0,0,n);
        System.out.printf("%.5f\n",count/(double)total);
    }

    public static void main(String[] args) {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));

//        Scanner scanner=new Scanner(System.in);

        System.out.println(C.x);
    }

    static class A{
        static{
            System.out.println("static A");
        }
    }

    static class B extends A{
        public static int x=10;
        static {
            System.out.println("static B");
        }
    }

    static class C extends B{
        static{
            System.out.println("static C");
        }
    }
}
