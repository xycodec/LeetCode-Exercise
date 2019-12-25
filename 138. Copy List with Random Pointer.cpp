#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
#include <queue>
using namespace std;
// Definition for a Node.
class Node {
public:
    int val;
    Node* next;
    Node* random;
    
    Node(int _val) {
        val = _val;
        next = NULL;
        random = NULL;
    }
};
class Solution {
public:
    Node* copyRandomList(Node* head) {
        if(head==NULL) return NULL;
        map<Node*,Node*> mp;
        Node *copyHead=new Node(head->val);
        Node *tmpCopyNode=copyHead;
        Node *tmpNode=head;
        int cnt=0;
        //copy next pointer
        while(tmpNode!=NULL){
            mp.insert(make_pair(tmpNode,tmpCopyNode));
            tmpNode=tmpNode->next;
            if(tmpNode==NULL) break;
            tmpCopyNode->next=new Node(tmpNode->val);
            tmpCopyNode=tmpCopyNode->next;
            
        }

        tmpNode=head;
        tmpCopyNode=copyHead;
        //copy random pointer
        while(tmpNode!=NULL){
            if(tmpNode->random!=NULL){
                tmpCopyNode->random=mp[tmpNode->random];
            }
            tmpNode=tmpNode->next;
            tmpCopyNode=tmpCopyNode->next;
        }

        return copyHead;
    }
};