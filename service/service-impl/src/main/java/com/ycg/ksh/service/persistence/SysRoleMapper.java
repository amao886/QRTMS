package com.ycg.ksh.service.persistence;

import com.ycg.ksh.entity.persistent.SysRole;
import tk.mybatis.mapper.common.Mapper;

public interface SysRoleMapper extends Mapper<SysRole> {

    SysRole getRole(Integer userKey);
}