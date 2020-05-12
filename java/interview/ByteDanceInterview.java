package com.xycode.interview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.lang.reflect.Array;
import java.util.*;

/**
 * ClassName: ByteDanceInterview
 *
 * @Author: xycode
 * @Date: 2020/4/12
 * @Description: this is description of the ByteDanceInterview class
 **/
public class ByteDanceInterview {
    private static void solution1(int[] d,int n){
        int cnt=0;
        for(int j=1;j<=n;++j){
            if(d[j]<0){
                System.out.println("NO");
                return;
            }
            if(d[j]!=0){
                int tmp=d[j];
                int k=j+1;
                for(;k<=n;++k){
                    if(d[k]==0){
                        j=k;
                        ++cnt;
                        break;
                    }
                    if(tmp!=d[k]){
                        System.out.println("NO");
                        return;
                    }
                }
            }
        }
        if(cnt>1){
            System.out.println("NO");
        }else{
            System.out.println("YES");
        }
    }

    public static void byteDanceSolution1(){
        Scanner scanner=new Scanner(System.in);
        int t=scanner.nextInt();
        for(int i=0;i<t;++i){
            int n=scanner.nextInt();
            int[] a=new int[n+1],b=new int[n+1],d=new int[n+1];
            for(int j=1;j<=n;++j){
                a[j]=scanner.nextInt();
            }
            for(int j=1;j<=n;++j){
                b[j]=scanner.nextInt();
                d[j]=b[j]-a[j];
            }
//            System.out.println(Arrays.toString(d));
            solution1(d,n);
        }
    }

    public static void byteDanceSolution2(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int[] array=new int[n];
        for(int i=0;i<n;++i){
            array[i]=scanner.nextInt();
        }
        int ans=0;
        for(int i=1;i<n-1;++i){
            if(array[i]>array[i-1]&&array[i]>array[i+1]){
                if(array[i]<=2*array[i+1]){
                    ++ans;
                }else{
                    ans+=array[i]/(2*array[i+1])+1;
                }
            }
        }
        System.out.println(ans);
    }

    public static void byteDanceSolution3(){
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt(),m=scanner.nextInt();
        int[] a=new int[n],b=new int[m];//优惠券,商品
        for(int i=0;i<n;++i){
            a[i]=scanner.nextInt();
        }
        Arrays.sort(a);
        for(int i=0;i<m;++i){
            b[i]=scanner.nextInt();
        }
        Arrays.sort(b);
        long ans=0;
        int prevGood=-1,prevMoney=-1;
        for(int i=0;i<m;++i){
            if(prevGood==b[i]){
                ans+=prevMoney;
                continue;
            }else{
                prevGood=b[i];
            }
            int tmp=Arrays.binarySearch(a,b[i]);
            if(tmp<0){
                tmp=-(tmp+1);
                if(tmp==0){
                    ans+=b[i];
                    prevMoney=b[i];
                }else{
                    ans+=b[i]-a[tmp-1];
                    prevMoney=b[i]-a[tmp-1];
                }
            }else{
                prevMoney=0;
            }
        }
        System.out.println(ans);
    }

    public static void byteDanceSolution4(){
        Scanner scanner=new Scanner(System.in);
        int t=scanner.nextInt();
        for(int i=0;i<t;++i) {
            int n = scanner.nextInt();
            int[] h = new int[n];
            for (int j = 0; j < n; ++j) {
                h[j] = scanner.nextInt();
            }
            int[] ans = new int[n];
            for (int j = 0; j < n; ++j) {
                int tmp = h[j];
                for (int k = j + 1; k < n && h[k] <= tmp; ++k) ++ans[j];
            }
            for (int j = n - 1; j >= 0; --j) {
                int tmp = h[j];
                for (int k = j - 1; k >= 0 && h[k] <= tmp; --k) ++ans[j];
            }
            for (int j = 0; j < n - 1; ++j) {
                System.out.print(ans[j] + " ");
            }
            System.out.println(ans[ans.length - 1]);
        }

//        Scanner in = new Scanner(System.in);
//        int t = in.nextInt();
//        for (int i = 0; i < t; i++) {
//            int n = in.nextInt();
//            int[] height = new int[n];
//            for (int j = 0; j < n; j++) {
//                height[j] = in.nextInt();
//            }
//            int[] res = new int[n];
//            Stack<Integer> stack = new Stack<>();
//            int[] left = new int[n];
//            int[] right = new int[n];
//            for (int j = 0; j < n; j++) {
//                while (!stack.isEmpty() && height[stack.peek()] <= height[j]) {
//                    stack.pop();
//                }
//                if (stack.isEmpty()) {
//                    left[j] = 0;
//                } else {
//                    left[j] = stack.peek() + 1;
//                }
//                stack.push(j);
//            }
//            stack.clear();
//            for (int j = n - 1; j >= 0; j--) {
//                while (!stack.isEmpty() && height[stack.peek()] <= height[j]) {
//                    stack.pop();
//                }
//                if (stack.isEmpty()) {
//                    right[j] = n - 1;
//                } else {
//                    right[j] = stack.peek() - 1;
//                }
//                stack.push(j);
//            }
//            for (int j = 0; j < n; j++) {
//                res[j] = right[j] - left[j];
//            }
//            for (int j = 0; j < n; j++) {
//                System.out.print(res[j] + " ");
//            }
//            System.out.println();
//        }
    }

    public static void main(String[] args) throws IOException {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
//
//        StreamTokenizer tokenizer=new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
//        while(tokenizer.nextToken() != StreamTokenizer.TT_EOF){
//            System.out.println((int)tokenizer.nval);
//        }
//
//        Scanner scanner=new Scanner(System.in);
        byteDanceSolution4();
    }
}
