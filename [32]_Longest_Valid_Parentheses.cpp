//https://leetcode.com/problems/longest-valid-parentheses/
class Solution {
public:
    int longestValidParentheses(string s){
        int len = s.size();
        if(len==1) return 0;
        stack<int> sk;
        //先记录合法括号的每个索引
        for (int i = 0; i < len; ++i){
            if (s[i] == '('){
                sk.push(i);
            }
            else if (s[i] == ')'){
                if (sk.empty()){
                    sk.push(i);
                    continue;
                }
                if (s[sk.top()] == '('){
                    sk.pop();
                }else{
                    sk.push(i);
                }
            }
        }

        //接着在这些索引中寻找区间长度最大的即可
        if (sk.empty()){//栈为空说明完全匹配
            return len;
        }else{
            vector<int> v;
            if(sk.top()!=len-1) v.push_back(len-1);
            while (!sk.empty()){
                v.push_back(sk.top());
                sk.pop();
            }
            if(v[v.size()-1]!=0) v.push_back(0);
            vector<int> v2;
            for(int i=0;i<v.size()-1;++i){
                v2.push_back(v[i]-v[i+1]-1);
            }
            int ans=*std::max_element(v2.begin(), v2.end());
            return ans%2==0?ans:ans+1;
        }
    }
};