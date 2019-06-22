package com.ycg.ksh.entity.collecter;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/6 0006
 */

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;

/**
 * 报表分页
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/6 0006
 */
public class ReportPage<T> extends BaseEntity {

    private Collection<T> results;

    private int num;
    private int size;
    private long total;
    private int pages;

    public ReportPage(int num, int size) {
        this.num = num;
        this.size = size;
    }

    public ReportPage(int num, int size, long total, Collection<T> results) {
        this.num = num;
        this.size = size;
        this.total = total;
        this.results  = results;
        this.pages = (int) (total / size);
        if(total % size != 0) {
            pages = pages + 1;
        }
    }

    public ReportPage<T> refresh(){
        this.pages = (int) (total / size);
        if(total % size != 0) {
            pages = pages + 1;
        }
        return this;
    }


    public Collection<T> getResults() {
        return results;
    }

    public int getNum() {
        return num;
    }

    public int getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public int getPages() {
        return pages;
    }

    public void setResults(Collection<T> results) {
        this.results = results;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
