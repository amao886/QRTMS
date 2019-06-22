/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 17:55:16
 */
package com.ycg.ksh.common.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * 全局唯一ID生成器
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-02 17:55:16
 */
public class GloballyIDBuilder {
    
    final Logger logger = LoggerFactory.getLogger(GloballyIDBuilder.class);
    
    /** 开始时间截 (2018-01-01) */
    private final long twepoch = 1514736000000L;

    /** 机器id所占的位数 */
    private final long workerIdBits = 2L;//5L

    /** 数据标识id所占的位数 */
    private final long datacenterIdBits = 2L;//5L

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** 支持的最大数据标识id，结果是31 */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /** 序列在id中占的位数 */
    private final long sequenceBits = 10L;

    /** 机器ID向左移12位 */
    private final long workerIdShift = sequenceBits;

    /** 数据标识id向左移17位(12+5) */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /** 时间截向左移22位(5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** 工作机器ID(0~31) */
    private long workerId;

    /** 数据中心ID(0~31) */
    private long datacenterId;

    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;

    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    public GloballyIDBuilder(long datacenter, long worker) {
        this.workerId = worker % maxWorkerId;
        this.datacenterId = datacenter % maxDatacenterId;
        if (workerId > maxWorkerId || workerId < 0) {
            logger.warn("工作ID不能大于 {} 或者小于 {}", maxWorkerId, 0);
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            logger.warn("数据中心ID不能大于 {} 或者小于 {}", maxDatacenterId, 0);
        }
        logger.info("全局唯一ID生成器初始化成功 数据中ID:{} 工作机器ID:{}", datacenterId, workerId);
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return UniqueID
     */
    public synchronized long next() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {//如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            throw new RuntimeException(String.format("系统时间异常变化 %d", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {//如果是同一时间生成的，则进行毫秒内序列
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) { //毫秒内序列溢出
                timestamp = tilNextMillis(lastTimestamp);//阻塞到下一个毫秒,获得新的时间戳
            }
        } else {//时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        lastTimestamp = timestamp;//上次生成ID的时间截
        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
