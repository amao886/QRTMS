/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 13:25:13
 */
package com.ycg.ksh.service.support.excel;

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-19 13:25:13
 */
public class ExceptionObject extends BaseEntity{
    
    private static final long serialVersionUID = -5165203720458688371L;
    
    
    private Object[] objects;
    private String message;
    
    public ExceptionObject(Object[] objects, String message) {
        super();
        this.objects = objects;
        this.message = message;
    }
    /**
     * getter method for objects
     * @return the objects
     */
    public Object[] getObjects() {
        return objects;
    }
    /**
     * getter method for message
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
