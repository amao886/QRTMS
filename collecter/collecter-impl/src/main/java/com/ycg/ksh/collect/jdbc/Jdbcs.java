package com.ycg.ksh.collect.jdbc;

import com.ycg.ksh.common.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * jdbc工具
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/11/1 0001
 */
public abstract class Jdbcs {

    private static final String INTEGER = "java.lang.Integer";
    private static final String _INTEGER = "int";
    private static final String DOUBLE = "java.lang.Double";
    private static final String _DOUBLE = "double";
    private static final String FLOAT = "java.lang.Float";
    private static final String _FLOAT = "float";
    private static final String LONG = "java.lang.Long";
    private static final String _LONG = "long";
    private static final String SHORT = "java.lang.Short";
    private static final String _SHORT = "short";
    private static final String BYTE = "java.lang.Byte";
    private static final String _BYTE = "byte";
    private static final String BOOLEAN = "java.lang.Boolean";
    private static final String _BOOLEAN = "boolean";
    private static final String CHARACTER = "java.lang.Character";
    private static final String _CHARACTER = "char";

    private static final String LOCALTIME = "java.time.LocalTime";
    private static final String LOCALDATE = "java.time.LocalDate";



    private static final String[] TYPES = {INTEGER, LONG, BOOLEAN, DOUBLE, FLOAT, SHORT, BYTE, CHARACTER, LOCALTIME, LOCALDATE};




    private static String CRERATE_TABLE_STRING = "create table %s (%s) ENGINE=InnoDB default charset=utf8;";
    private static String CRERATE_COLUMN_STRING = "%s %s,";

    public static String clazz2ddl(Class<?> clazz, String tableName){
        if(StringUtils.isBlank(tableName)){
            Table table = clazz.getAnnotation(Table.class);
            if(table != null && StringUtils.isNotBlank(table.name())){
                tableName = table.name();
            }else{
                tableName = name4HumpString(clazz.getSimpleName());
            }
        }
        return String.format(CRERATE_TABLE_STRING, tableName, tableContext(clazz));
    }


    private static boolean validate(Field field){
        Class<?> type = field.getType();
        if(!type.isPrimitive() && Stream.of(TYPES).noneMatch(t -> t.equalsIgnoreCase(type.getName()))){
            return false;
        }
        return field.getAnnotation(Transient.class) != null;
    }


    private static String tableContext(Class<?> clazz){
        Collection<Field> fields = Stream.of(clazz.getDeclaredFields()).filter(Jdbcs::validate).collect(Collectors.toList());


        /*String[] columns = ; builder = new StringBuilder(), constraint = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            String columnName = columnName(field, column);
            builder.append(String.format(CRERATE_COLUMN_STRING, columnName, typeAndDefault(field, column)));
            if(column != null && column.unique()){
                "UNIQUE KEY UNIQUE_EKEY_CKEY (`EMPLOYEE_KEY`, `CUSTOMER_KEY`)"
                constraint.append("PRIMARY KEY (").append(columnName(field, column)).append(")");
            }
            Id id = field.getAnnotation(Id.class);
            if(id != null){
                constraint.append("PRIMARY KEY (").append(columnName).append(")");
            }
        }
        if(builder.length() > 1){
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();*/
        return null;
    }

    private static String columnName(Field field, Column column){
        if(column != null && StringUtils.isNotBlank(column.name())){
            return column.name();
        }
        return name4HumpString(field.getName());
    }

    private static String typeAndDefault(Field field, Column column){
        return null;
    }

    private static String name4HumpString(String sourceName){
        char[] chars = sourceName.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            if(Character.isUpperCase(c)){
                builder.append("_").append(Character.toLowerCase(c));
            }else{
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
