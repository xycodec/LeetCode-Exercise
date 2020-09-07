package com.xycode.leetcode;

import com.xycode.utils.heap.MinHeap;
import org.testng.annotations.Test;

import java.util.*;

/**
 * ClassName: LeetCodeHeap
 *
 * @Author: xycode
 * @Date: 2020/3/21
 * @Description: this is description of the LeetCodeHeap class
 **/
public class LeetCodeHeap {

    //215. Kth Largest Element in an Array
    //描述: 找出一个数组中第K大的元素
//    public int findKthLargest(int[] nums, int k) {
//        PriorityQueue<Integer> q=new PriorityQueue<>(k);
//        q.add(nums[0]);
//        for(int i=1;i<nums.length;++i){
//            if(q.size()<k) q.add(nums[i]);
//            else{
//                if(q.peek()<nums[i]){
//                    q.poll();
//                    q.add(nums[i]);
//                }
//            }
//        }
//        return q.peek();
//    }

    //使用自实现的最小堆
    public int findKthLargest(int[] nums, int k) {
        MinHeap q = new MinHeap(k);
        q.push(nums[0]);
        for (int i = 1; i < nums.length; ++i) {
            if (q.size() < k) q.push(nums[i]);
            else {
                if (q.peek() < nums[i]) {
                    q.poll();
                    q.push(nums[i]);
                }
            }
        }
        return q.peek();
    }

    @Test
    public void testFindKthLargest() {
        int[] array = {3, 2, 3, 1, 2, 4, 5, 5, 6, 7, 7, 8, 2, 3, 1, 1, 1, 10, 11, 5, 6, 2, 4, 7, 8, 5, 6};
        System.out.println(findKthLargest(array, 20));
    }

    //347. Top K Frequent Elements
    //描述,在一个数组中找出频次前K多的元素
    public List<Integer> topKFrequent(int[] nums, int k) {
        List<Integer> ans = new LinkedList<>();
        if (nums == null || nums.length == 0 || k <= 0) return ans;
        Map<Integer, Integer> mp = new HashMap<>();
        for (int i = 0; i < nums.length; ++i) {
            mp.put(nums[i], mp.getOrDefault(nums[i], 1) + 1);
        }
        PriorityQueue<int[]> queue = new PriorityQueue<>(k, Comparator.comparingInt(x -> x[1]));
        for (int num : mp.keySet()) {
            int freq = mp.get(num);
            if (queue.size() < k) {
                queue.add(new int[]{num, freq});
            } else {
                if (queue.peek()[1] < freq) {
                    queue.poll();
                    queue.add(new int[]{num, freq});
                }
            }
        }
        while (!queue.isEmpty()) {
            ans.add(queue.poll()[0]);
        }
        Collections.reverse(ans);
        return ans;
    }

    class WordNode {
        String word;
        int freq;

        public WordNode(String word, int freq) {
            this.word = word;
            this.freq = freq;
        }
    }

    //692. Top K Frequent Words
    //描述: 找出单词数组中出现频次前K多的单词,若频次相同則按字典序
    public List<String> topKFrequent(String[] words, int k) {
        List<String> ans = new LinkedList<>();
        if (words == null || words.length == 0 || k <= 0) return ans;
        Map<String, Integer> mp = new HashMap<>();
        for (int i = 0; i < words.length; ++i) {
            mp.put(words[i], mp.getOrDefault(words[i], 0) + 1);
        }
        PriorityQueue<WordNode> queue = new PriorityQueue<>(k, (x, y) -> (x.freq == y.freq) ? -x.word.compareTo(y.word) :
                Integer.compare(x.freq, y.freq));//注意这是按照频次的最小堆,最后会reverse,所以这里比较字典序应该反过来
        for (String word : mp.keySet()) {
            int freq = mp.get(word);
            if (queue.size() < k) {
                queue.add(new WordNode(word, freq));
            } else {
                if (queue.peek().freq < freq) {
                    queue.poll();
                    queue.add(new WordNode(word, freq));
                } else if (queue.peek().freq == freq) {//频次相同則按字典序
                    if (queue.peek().word.compareTo(word) > 0) {
                        queue.poll();
                        queue.add(new WordNode(word, freq));
                    }
                }
            }
        }
        while (!queue.isEmpty()) {
            ans.add(queue.poll().word);
        }
        Collections.reverse(ans);
        return ans;
    }

    @Test
    public void testTopKFrequent() {
        System.out.println(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));
        System.out.println(topKFrequent(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 3));
    }
}
