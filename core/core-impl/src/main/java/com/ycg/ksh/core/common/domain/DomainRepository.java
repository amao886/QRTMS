package com.ycg.ksh.core.common.domain;

/**
 * 领域模型资源库接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/12/17 0017
 */
public interface DomainRepository<T, K> {

    /**
     * 生成标识
     * @return  生成的标识
     */
    K nextIdentify();
    /**
     *  获取聚合
     * @param k
     * @return
     */
    T findByKey(K k);
    
    /**
     *  持久化聚合
     * @param t
     */
    void save(T t);


    /**
     *  修改聚合
     * @param t
     */
    void modify(T t);

}
