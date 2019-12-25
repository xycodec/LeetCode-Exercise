#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
using namespace std;
class Solution {
public:
    void reverse_str(string &a){
        int size=a.size();
        for(int i=0;i<size/2;++i){
            char t=a[i];
            a[i]=a[size-i-1];
            a[size-i-1]=t;
        }
    }

    char result[250]={0};
    char* large_num_mul(string &a,string &b){
        char c[250]={0};
        int a_len=a.size();
        int b_len=b.size();
        reverse_str(a);
        reverse_str(b);
        for(int i=0;i<b_len;++i){
            for(int j=0;j<a_len;++j){
                int k=i+j;
                c[k]+=(a[j]-'0')*(b[i]-'0');
                if(c[k]>9){
                    c[k+1]+=(c[k]/10);
                    c[k]=c[k]%10;
                }
            }
        }

        int j;
        for(j=a_len+b_len;j>0;--j){
            if(c[j]!=0){
                break;
            }
        }
        int pos=0;
        for(int i=j;i>=0;--i){
            result[pos++]=c[i];
            cout<<c[i];
        }
        cout<<endl;
        return result;
    }
    string multiply(string num1, string num2) {
        string result(large_num_mul(num1,num2));
        return result;
    }
};

int main(){

    Solution solution;
    string a="123",b="234";
    cout<<solution.large_num_mul(a,b)<<endl;
    return 0;
}


