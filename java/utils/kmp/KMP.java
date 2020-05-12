package com.xycode.utils.kmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ClassName: KMPSearch
 *
 * @Author: xycode
 * @Date: 2020/4/18
 * @Description: kmp算法实现, 通过了LeetCode 28. Implement strStr()的验证
 **/
public class KMP {
    private static int[] getNext(char[] p){
        int[] prefixTable=new int[p.length];//记录前后缀匹配长度
        int len=0;//记录当前匹配的长度
        int pos=1;//prefixTable[0]=0;
        while(pos<p.length){
            if(p[pos]==p[len]){//匹配,
                prefixTable[pos++]=++len;
            }else{//不匹配
                if(len>0){//回溯到上一个匹配
                    len=prefixTable[len-1];
                }else{//len==0,说明回溯到头了还没找到匹配的字符,这时就置0
                    prefixTable[pos++]=0;
                }
            }
        }

        //这里生成next数组,主要是为了方便kmp算法使用
        int[] next=new int[p.length];
        next[0]=-1;//这里的-1相当于一个标志位
        for(int j=1;j<p.length;++j){
            next[j]=prefixTable[j-1];
        }
        return next;
    }

    public static List<Integer> KMPSearch(char[] s,char[] p){
        List<Integer> positions=new ArrayList<>();
        if(s.length==0||p.length==0) return positions;//对于空串来说,都认为不匹配
        int[] next=getNext(p);
        int i=0,j=0;
        while(i<s.length){
            if(j==p.length-1&&s[i]==p[j]){//找到一个完全匹配了
                positions.add(i-j);
                j=next[j];//若一开始就完全匹配,j=next[0]=-1,会导致越界,所以下面需要判断j是否为-1
            }
            if(j==-1||s[i]==p[j]){//j==-1说明应该跳到最开始的字符,应该使j=0,刚好等价于++j;
                ++i;
                ++j;
            }else{
                j=next[j];//当前字符不匹配时，该字符对应的 next 值会告诉你下一步匹配中，模式串应该跳到哪个位置（跳到next [j] 的位置）
            }
        }
        return positions;
    }

    public static List<Integer> KMPSearch(String s, String p){
        return KMPSearch(s.toCharArray(),p.toCharArray());
    }


    public static void main(String[] args) {
        System.out.println(KMPSearch("aa","a"));
    }
}

