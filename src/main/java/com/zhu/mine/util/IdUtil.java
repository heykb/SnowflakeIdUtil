package com.zhu.mine.util;

public class IdUtil {

    private IdUtil(){ }

        private static class SingletonInstance{
            private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1,1);
        }

        public static  long generateId(){
            return SingletonInstance.idWorker.nextId();
    }

}
