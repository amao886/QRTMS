package com.ycg.ksh.common.extend.mybatis;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */
public class CustomExampleProvider extends ExampleProvider {

    public CustomExampleProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectPageByExample(MappedStatement ms) {
        return selectByExample(ms);
    }

    /**
     * 根据Example查询
     *
     * @param ms
     * @return
     */
    public String selectGroupByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        if(isCheckExampleEntityClass()){
            sql.append(SqlHelper.exampleCheck(entityClass));
        }
        sql.append("<if test=\"distinct\">distinct</if>");
        //支持查询指定列
        sql.append(SqlHelper.exampleSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.exampleWhereClause());
        sql.append(exampleGroupColumn());
        sql.append(SqlHelper.exampleOrderBy(entityClass));
        return sql.toString();
    }

    /**
     * 此处一定要使用 ${} 不能使用 #{}
     * #{}将传入的数据当成一个字符串，会对自动传入的数据加一个双引号
     * ${}将传入的数据直接显示在sql语句中
     */
    public static String exampleGroupColumn() {
        return "<if test=\"groupColumns != null\">" +
                "   GROUP BY" +
                "   <foreach collection=\"groupColumns\" item=\"item\" separator=\",\">" +
                "       ${item}" +
                "   </foreach>" +
                "</if>";
    }
}
