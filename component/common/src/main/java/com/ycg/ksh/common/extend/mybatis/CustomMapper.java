/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */

package com.ycg.ksh.common.extend.mybatis;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.common.*;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Mapper抽象类
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */
public interface CustomMapper<T> extends Mapper<T> , ConditionMapper<T>, ExampleMapper<T>, MySqlMapper<T>, RowBoundsMapper<T>{

    /**
     * 根据example条件和RowBounds进行分页查询
     *
     * @param example
     * @param rowBounds
     * @return
     */
    @SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
    public Page<T> selectPageByExample(Example example, RowBounds rowBounds);

    /**
     * 分组查询
     * @param example
     * @param rowBounds
     * @return
     */
    @SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
    public Page<T> selectGroupByExample(Example example, RowBounds rowBounds);

    /**
     * 批量插入
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = CustomProvider.class, method = "dynamicSQL")
    int inserts(Collection<T> recordList);

    /**
     * 根据主键批量更新,一次更新数量不宜过多
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = CustomProvider.class, method = "dynamicSQL")
    int updates(Collection<T> recordList);

    /**
     * 查询
     *
     * @param identities
     * @return
     */
    @SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
    List<T> selectByIdentities(Collection<? extends Serializable> identities);

    /**
     * 删除
     *
     * @param identities
     * @return
     */
    @SelectProvider(type = CustomProvider.class, method = "dynamicSQL")
    void deleteByIdentities(Collection<? extends Serializable> identities);


}
