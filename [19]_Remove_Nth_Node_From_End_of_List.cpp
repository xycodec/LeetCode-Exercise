//https://leetcode.com/problems/remove-nth-node-from-end-of-list/
/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     ListNode *next;
 *     ListNode(int x) : val(x), next(NULL) {}
 * };
 */
class Solution {
public:
    ListNode* removeNthFromEnd(ListNode* head, int n) {
        if(head==NULL) return NULL;
        if(n==0) return head;
        //巧妙地使用两个指针来搜索(之间之间的间隔为n)
        ListNode* p=head;
        ListNode* q=head;
        int cnt=n;
        while(cnt>0&&q!=NULL){
            q=q->next;
            --cnt;
        }
        if(q==NULL){
            head=head->next;
        }else{
            q=q->next;
            while(q!=NULL){
                q=q->next;
                p=p->next;
            }
            p->next=(p->next)->next;
        }
        return head;
    }
};