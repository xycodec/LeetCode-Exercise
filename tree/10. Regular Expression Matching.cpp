#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <queue>
using namespace std;
class Solution {
public:
    bool match(string &s,int s_index,string &p,int p_index){
        if(s_index==s.size()&&p_index==p.size()) return true;
        if(s_index<s.size()&&p_index==p.size()) return false;

        if(p_index+1<p.size()&&p[p_index+1]=='*'){
            if(p[p_index]==s[s_index]||(p[p_index]=='.'&&s_index<s.size())){
                return match(s,s_index+1,p,p_index+2)
                    ||match(s,s_index+1,p,p_index)
                    ||match(s,s_index,p,p_index+2);
            }else{
                return match(s,s_index,p,p_index+2);
            }
        }

        if(s[s_index]==p[p_index]||(p[p_index]=='.'&&s_index<s.size())){
            return match(s,s_index+1,p,p_index+1);
        }

        return false;

    }
    
    bool isMatch(string s, string p) {
        return match(s,0,p,0);
    }
};