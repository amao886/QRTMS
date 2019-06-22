package com.ycg.ksh.service.core.entity.service.adventive;

import com.ycg.ksh.common.entity.BaseEntity;
import com.ycg.ksh.entity.persistent.adventive.AdventiveNote;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MediaObject extends BaseEntity implements Delayed {

    private int frequency;//推送频率(秒/次)
    private int frequencyType;//频率类型(1:正常,2:递增)
    private int maxCount;//同一条记录最大推送次数

    private long waitTime;//要等待的时间
    private long expireTime;//开始推送的时间

    private int errors;//异常次数


    private AdventiveNote target;


    public MediaObject() { }


    public MediaObject(AdventiveNote target, int frequency, int frequencyType, int maxCount) {
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.maxCount = maxCount;
        this.target = target;
        modifyNextTimes();
    }

    public void immediately(){
        this.waitTime = TimeUnit.SECONDS.toNanos(1L);
        this.expireTime = System.nanoTime() + this.waitTime;
    }


    public boolean modifyCount(){
        target.setCount(target.getCount() + 1);
        if(target.getCount() >= maxCount){
            return false;
        }
        return true;
    }

    public MediaObject modifyNextTimes(){
        long frequency = Math.max(this.frequency, 1);
        if(frequencyType - 2 == 0 && target.getCount() > 3){
            frequency = frequency * ((target.getCount() - 3) + 1);
        }
        this.waitTime = TimeUnit.SECONDS.toNanos(frequency);
        this.expireTime = System.nanoTime() + this.waitTime;
        return this;
    }


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expireTime - System.nanoTime(),  TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if(o == null || ! (o instanceof MediaObject)) return 1;
        if(o == this) return 0;
        MediaObject s = (MediaObject) o;
        if (this.expireTime > s.expireTime) {
            return 1;
        }else if (this.expireTime == s.expireTime) {
            return 0;
        }else {
            return -1;
        }
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(int frequencyType) {
        this.frequencyType = frequencyType;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public AdventiveNote getTarget() {
        return target;
    }

    public void setTarget(AdventiveNote target) {
        this.target = target;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }
}
