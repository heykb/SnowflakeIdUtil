package com.zhu.mine.util;


import javax.annotation.security.RunAs;
import javax.xml.crypto.Data;
import java.util.Random;

/**
 * Twitter的分布式自增ID算法snowflake（全局时间有序Id） （36位UUID无序）
 * Long型
 * 0 - {0000000000 0000000000 0000000000 0000000000 0} - {00000 - 00000} - {000000000000}
 *
 */
public class SnowflakeIdWorker {

    //12位的有序序列,用于同一毫秒内Id计数
    private final long sequenceIdBits = 12L;
    private final long sequenceIdMask = -1L ^ (-1L << sequenceIdBits);
    private long sequencedId = 0L;

    //5位的机器表示位，用于标识不同机器
    private final long workIdBits = 5L;
    private final long maxWorkId = -1L ^ (-1L << workIdBits);
    private long workId;

    //5位的数据表示位,用于标识不同类型数据
    private final long datacenterIdBits = 5L;
    private final long maxDataCenterId = -1L ^ (-1L << datacenterIdBits);
    private long datacenterId;

    //机器Id左移12位
    private final long workIdShift = sequenceIdBits;

    //数据Id左移12 + 5位
    private final long datacenterIdShift = sequenceIdBits + workIdBits;

    //上一次时间戳Id,用于验证时间戳有序（下次生成的更大）,1545126625220L=2018/12/18/17:50开始使用此工具，41位时间戳理论可以使用69年
    private final long twepoch = 1545126625220L;
    private Long lastTimestamp = -1L;
    //41位时间戳左移 12+5+5
    private long timestampShift = sequenceIdBits + workIdBits + datacenterId;


    //构造函数首先指定机器和数据表示位,作用：Id分类
    public SnowflakeIdWorker(long workId, long datacenterId) {
        if (workId > maxDataCenterId | workId < 0) {
            throw new IllegalArgumentException("wordId必须在[0,31]之间");
        }
        if (datacenterId > maxDataCenterId | datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId[0,31]之间");
        }
        this.workId = workId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {

        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            try {
                throw new Exception(String.format(
                        "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //同一毫秒内,进行毫秒内序列
        if (timestamp == lastTimestamp) {
            //序列计数+1
            sequencedId = (sequencedId + 1) & sequenceIdMask;
            //毫秒内计数满了，等待到下一毫秒获取
            if (sequencedId == 0) {
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis();
                }
            }
        } else {
            //时间戳改变，序列重置
            sequencedId = 0L;
        }
        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampShift)
                | (datacenterId << datacenterIdShift)
                | (workId << workIdShift)
                | sequencedId;
    }


}

