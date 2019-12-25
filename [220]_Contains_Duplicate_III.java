class Solution {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        int len=nums.length;
        if(len<=1||k<=0||t<0) return false;
        TreeMap<Long,Integer> mp=new TreeMap<>();
        for(int i=0;i<len;++i){
            Map<Long,Integer> tmpMap=mp.subMap((long)nums[i]-t,(long)nums[i]+t+1);//对数时间,取出落在区间内的项
            if(tmpMap.isEmpty()){
                mp.put((long)nums[i],i);
            }else{
                Map<Long,Integer> tmp=new HashMap<>();
                int tmpIndex;
                //这里要注意,不能一边遍历一边修改容器,会导致ConcurrencyNoticationException
                for(Long key:tmpMap.keySet()){
                    tmpIndex=tmpMap.get(key);
                    if(i-tmpIndex<=k){//索引距离k以内的话
                        return true;
                    }else{
                        tmp.put((long)nums[i],i);
                    }
                }
                mp.putAll(tmp);
            }
        }
        return false;
    }
}