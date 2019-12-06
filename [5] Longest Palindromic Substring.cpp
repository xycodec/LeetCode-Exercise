//https://leetcode.com/problems/longest-palindromic-substring/
class Solution {
public:
    string longestPalindrome(string s) {
        int len=s.size();
        if(len==1) return s;
        if(len==2){
            if(s[0]==s[1]) return s;
            else return s.substr(0,1);
        }
        int pos=0;
        int maxlen=1;
        string ans=s.substr(0,1);
        //检测奇数长度的回文串
        while(pos<len-1){
            int i=pos,j=pos;
            while(i>=0&&j<len){
                if(i-1>=0&&j+1<len&&s[i-1]==s[j+1]){
                    --i;
                    ++j;
                }else{
                    if(j-i+1>maxlen){
                        maxlen=j-i+1;
                        ans=s.substr(i,maxlen);
                    }
                    --i;
                    ++j;
                    break;
                }
            }
            ++pos;
        }
        pos=0;
        //检测偶数长度的回文串
        while(pos<len-1){
            int i=pos,j=pos+1;
            if(s[i]==s[j]){
                while(i>=0&&j<len){
                    if(i-1>=0&&j+1<len&&s[i-1]==s[j+1]){
                        --i;
                        ++j;
                    }else{

                        if(j-i+1>maxlen){
                            maxlen=j-i+1;
                            ans=s.substr(i,maxlen);
                        }
                        --i;
                        ++j;
                        break;
                    }
                }
            }
            ++pos;
        }
        return ans;

    }
};