package com.ycg.ksh.common.extend.mybatis;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.ExampleProvider;

import java.util.Set;

/**
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */
public class CustomProvider extends ExampleProvider {

    public CustomProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
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

    /**
     * 批量插入
     *
     * @param ms
     */
    public String inserts(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            sql.append(column.getColumnHolder("record") + ",");
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

    /**
     * 根据主键批量更新,一次更新数量不宜过多
     *
     * @param ms
     */
    public String updates(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        String entityName = "ucn";
        sql.append("<foreach collection=\"list\" item=\""+ entityName +"\" separator=\";\" >");
        sql.append(CustomSqlHelper.updateTable(entityClass, tableName(entityClass), entityName));
        sql.append(CustomSqlHelper.updateSetColumns(entityClass, entityName, true, true));
        sql.append(CustomSqlHelper.wherePKColumns(entityClass, entityName));
        sql.append("</foreach>");
        return sql.toString();
    }


    /**
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     *
     * @param ms
     * @return
     */
    public String selectByIdentities(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if(columnList.size() != 1){
            throw new MapperException("继承 selectByIdentities 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
        EntityColumn column = columnList.iterator().next();
        sql.append(" where ");
        sql.append(column.getColumn());
        sql.append(" in ");
        sql.append("<foreach collection=\"list\" item=\"key\" separator=\",\" open=\"(\" close=\")\">");
        sql.append("#{key}");
        sql.append("</foreach>");
        return sql.toString();
    }
    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param ms
     * @return
     */
    public String deleteByIdentities(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, this.tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append(" where ");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("<foreach collection=\"list\" item=\"key\" separator=\",\" open=\"(\" close=\")\">");
            sql.append("#{key}");
            sql.append("</foreach>");
            return sql.toString();
        } else {
            throw new MapperException("继承 deleteByIdentities 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
    }

}
