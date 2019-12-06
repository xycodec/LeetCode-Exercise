https://leetcode.com/problems/integer-to-roman/
class Solution {
public:
    string intToRoman(int num) {
        string symbol[13]={"I","IV","V","IX","X","XL","L","XC","C","CD","D","CM","M"};
        int value[13]={1,4,5,9,10,40,50,90,100,400,500,900,1000};
        string ans="";
        int cnt;
        for(int i=12;i>=0;--i){
            while(num>=value[i]){
                cnt=num/value[i];
                num-=cnt*value[i];
                while(cnt--) ans+=symbol[i];
            }
        }
        return ans;
    }
};