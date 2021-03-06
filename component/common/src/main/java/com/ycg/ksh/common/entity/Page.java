package com.ycg.ksh.common.entity;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/6 0006
 */

import java.util.Collection;
import java.util.Collections;

/**
 * 报表分页
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/6 0006
 */
public class Page<T> extends BaseEntity {

    private Collection<T> results;

    private int num;
    private int size;
    private long total;
    private int pages;

    public Page(int num, int size) {
        this.num = num;
        this.size = size;
    }

    public Page(int num, int size, long total, Collection<T> results) {
        this.num = num;
        this.size = size;
        this.total = total;
        this.results  = results;
        this.pages = (int) (total / size);
        if(total % size != 0) {
            pages = pages + 1;
        }
    }

    public Page<T> refresh(){
        this.pages = (int) (total / size);
        if(total % size != 0) {
            pages = pages + 1;
        }
        return this;
    }

    public boolean isEmpty(){
        return results == null || results.isEmpty();
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
