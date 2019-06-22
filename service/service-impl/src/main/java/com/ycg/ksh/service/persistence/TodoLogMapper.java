package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.TodoLog;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

public interface TodoLogMapper extends Mapper<TodoLog> {

    void insertBatch(Collection<TodoLog> collection);

    Collection<TodoLog> selectByTodoKey(Integer todoKey);
}