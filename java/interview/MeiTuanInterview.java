package com.xycode.interview;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: MeiTuanInterview
 *
 * @Author: xycode
 * @Date: 2020/4/9
 * @Description: this is description of the MeiTuanInterview class
 **/
public class MeiTuanInterview {
    public static void meituanSolution1(){
        Scanner scanner=new Scanner(System.in);
        int x=scanner.nextInt();
        String clock=scanner.next();
        int n=scanner.nextInt();
        String[] tmp=clock.split(":");
        int hour=Integer.parseInt(tmp[0]),min=Integer.parseInt(tmp[1]);
        int totalMin=hour*60+min;
        if(totalMin>=n){
            totalMin-=n;
            System.out.println(x);
            System.out.printf("%02d:%02d",totalMin/60,totalMin%60);
        }else{
            int days=n/1440+1;
            int hasMin=1440-n%(1440)+totalMin;
            if(hasMin>=1440){
                --days;
                hasMin-=1440;
            }
            while(days!=0){
                --days;
                --x;
                if(x==0){
                    x=7;
                }
            }
            System.out.println(x);
            System.out.printf("%02d:%02d",hasMin/60,hasMin%60);
        }
    }

    public static void meituanSolution2() throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        int n=Integer.parseInt(reader.readLine());
        String[] tmp1=reader.readLine().split(" ");
        String[] tmp2=reader.readLine().split(" ");
        int[] s=new int[n],e=new int[n];
        int ans=0;
        for(int i=0;i<n;++i){
            s[i]=Integer.parseInt(tmp1[i]);
            e[i]=Integer.parseInt(tmp2[i]);
        }
        int now=e[s[0]-1];//记录前面的最大排名
        for(int i=1;i<n;++i){
            if(e[s[i]-1]>now){
                now=e[s[i]-1];
            }else{
                ++ans;
            }
        }
        System.out.println(ans);
    }

    private static boolean check(int n,int x,int k){
        int sum=0;
        for(int i=1;;i*=k){
            int bugs=x/i;
            if(bugs<=0) break;
            sum+=bugs;
        }
        return sum>=n;
    }

    public static void meituanSolution3() {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt(),k=scanner.nextInt();
        int l=2,r=n,mid=l+(r-l)/2;
        while(l<r){
            if(check(n,mid,k)){
                r=mid;
            }else{
                l=mid+1;
            }
            mid=l+(r-l)/2;
        }
        System.out.println(l);
    }

    public static void meituanSolution4() {
        Scanner scanner=new Scanner(System.in);
        int k=scanner.nextInt();
        if(k<=1) {
            System.out.println(0);
            return;
        }

    }

    public static void meituanSolution5() {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt(),k=scanner.nextInt();
        List<String> list=new ArrayList<>();
        Map<String, Pattern> mp=new HashMap<>();
        Set<String> set=new HashSet<>();
        for(int i=0;i<k;++i){
            list.add(scanner.next());
            mp.put(list.get(i),Pattern.compile(list.get(i)));
            set.add(list.get(i));
        }
        for(int i=0;i<n;++i){
            String s=scanner.next();
            if(s.charAt(0)=='+'){
                int index=Integer.parseInt(s.substring(1))-1;
                set.add(list.get(index));
            }else if(s.charAt(0)=='-'){
                int index=Integer.parseInt(s.substring(1))-1;
                set.remove(list.get(index));
            }else if(s.charAt(0)=='?'){
                String tmp=s.substring(1);
                int ans=0;
                for(String p:set){
                    Matcher matcher=mp.get(p).matcher(tmp);
                    while(matcher.find()){
                        ++ans;
                    }
                }
                System.out.println(ans);
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));

//        StreamTokenizer tokenizer=new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//        while(tokenizer.nextToken() != StreamTokenizer.TT_EOF){
//            System.out.println((int)tokenizer.nval);
//        }

//        Scanner scanner=new Scanner(System.in);
//        meituanSolution2();


    }
}
