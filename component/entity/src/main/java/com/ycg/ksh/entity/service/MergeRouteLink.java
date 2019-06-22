package com.ycg.ksh.entity.service;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/26
 */

/**
 * 路由链
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/1/26
 */
public class MergeRouteLink {

    private MergeRouteLine self;

    private MergeRouteLink prevRoute;//前一个

    private MergeRouteLink nextRoute;//后一个

    public boolean hasPrev(){
        return prevRoute != null;
    }
    public boolean hasNext(){
        return nextRoute != null;
    }

    public MergeRouteLink(MergeRouteLine self) {
        this.self = self;
    }

    public MergeRouteLine self() {
        return self;
    }

    public MergeRouteLine prevRoute() {
        return prevRoute.self;
    }
    public MergeRouteLink prevLink() {
        return prevRoute;
    }

    public void setPrevRoute(MergeRouteLink prevRoute) {
        this.prevRoute = prevRoute;
    }

    public MergeRouteLine nextRoute() {
        return nextRoute.self;
    }
    public MergeRouteLink nextLink() {
        return nextRoute;
    }

    public void setNextRoute(MergeRouteLink nextRoute) {
        this.nextRoute = nextRoute;
    }
}
