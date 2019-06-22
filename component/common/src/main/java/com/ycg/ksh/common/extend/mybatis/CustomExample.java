package com.ycg.ksh.common.extend.mybatis;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */

import com.ycg.ksh.common.util.StringUtils;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义Example
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/5/16
 */
public class CustomExample extends Example {

    //分组字段
    protected Set<String> groupColumns;

    public String maxColumn(String property) {
        if (StringUtils.isNotBlank(property) && propertyMap.containsKey(property)) {
            String column = column(property);
            String expression = "MAX("+ column +")";
            if (this.selectColumns == null) {
                this.selectColumns = new LinkedHashSet<String>();
            }
            this.selectColumns.add(expression +" AS "+ column);
            return expression;
        }
        return null;
    }

    public String selectColumn(String property) {
        if (StringUtils.isNotBlank(property) && propertyMap.containsKey(property)) {
            String column = column(property);
            if (this.selectColumns == null) {
                this.selectColumns = new LinkedHashSet<String>();
            }
            this.selectColumns.add(column);
            return column;
        }
        return null;
    }

    public String dateFormatColumn(String property, String format) {
        if (StringUtils.isNotBlank(property) && propertyMap.containsKey(property)) {
            String column = column(property);
            String expression = "DATE_FORMAT("+ column +", '"+ format +"')";
            if (this.selectColumns == null) {
                this.selectColumns = new LinkedHashSet<String>();
            }
            this.selectColumns.add(expression +" AS "+ column);
            return expression;
        }
        return null;
    }

    public CustomExample setGroupColumns(String... columns) {
        if (columns != null && columns.length > 0) {
            if (this.groupColumns == null) {
                this.groupColumns = new LinkedHashSet<String>();
            }
            for (String column : columns) {
                this.groupColumns.add(column);
            }
        }
        return this;
    }

    public Set<String> getGroupColumns() {
        return groupColumns;
    }

    /**
     * 默认exists为true
     *
     * @param entityClass
     */
    public CustomExample(Class<?> entityClass) {
        super(entityClass);
    }

    /**
     * 带exists参数的构造方法，默认notNull为false，允许为空
     *
     * @param entityClass
     * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
     */
    public CustomExample(Class<?> entityClass, boolean exists) {
        super(entityClass, exists);
    }

    /**
     * 带exists参数的构造方法
     *
     * @param entityClass
     * @param exists      - true时，如果字段不存在就抛出异常，false时，如果不存在就不使用该字段的条件
     * @param notNull     - true时，如果值为空，就会抛出异常，false时，如果为空就不使用该字段的条件
     */
    public CustomExample(Class<?> entityClass, boolean exists, boolean notNull) {
        super(entityClass, exists, notNull);
    }

    @Override
    protected Criteria createCriteriaInternal() {
        return new CustomCriteria(propertyMap, exists, notNull);
    }

    public String column(String property) {
        if (propertyMap.containsKey(property)) {
            return propertyMap.get(property).getColumn();
        } else if (exists) {
            throw new MapperException("当前实体类不包含名为" + property + "的属性!");
        } else {
            return null;
        }
    }

    public String property(String property) {
        if (propertyMap.containsKey(property)) {
            return property;
        } else if (exists) {
            throw new MapperException("当前实体类不包含名为" + property + "的属性!");
        } else {
            return null;
        }
    }

    public class CustomCriteria extends Criteria {

        protected CustomCriteria(Map<String, EntityColumn> propertyMap, boolean exists, boolean notNull) {
            super(propertyMap, exists, notNull);
        }

        @Override
        public Criteria andLike(String property, String value) {
//            addCriterion("LOCATE('"+ value +"', "+ column(property) +") >", 0, property(property));
            addCriterion(column(property) + " like", "%"+ value +"%", property(property));
            return this;
        }

        @Override
        public Criteria andNotLike(String property, String value) {
//            addCriterion("LOCATE('"+ value +"', "+ column(property) +") <=", 0, property(property));
            addCriterion(column(property) + " not like", "%"+ value +"%", property(property));
            return this;
        }

        @Override
        public Criteria orLike(String property, String value) {
//            addOrCriterion("LOCATE('"+ value +"', "+ column(property) +") >", 0, property(property));
            addOrCriterion(column(property) + " like", "%"+ value +"%", property(property));
            return this;
        }

        @Override
        public Criteria orNotLike(String property, String value) {
//            addOrCriterion("LOCATE('"+ value +"', "+ column(property) +") <=", 0, property(property));
            addOrCriterion(column(property) + " not like", "%"+ value +"%", property(property));
            return this;
        }
    }
}
