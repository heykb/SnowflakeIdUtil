package com.zhu.mine.util;

public class IdUtil {
    private static SnowflakeIdWorker idWorker;
    private IdUtil(){ }
    public static  long generateId(){
        if(idWorker == null){
            idWorker = new SnowflakeIdWorker(1,1);
        }
        return idWorker.nextId();
    }
}
