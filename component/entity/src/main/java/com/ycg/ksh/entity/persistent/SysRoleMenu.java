package com.ycg.ksh.entity.persistent;

import com.ycg.ksh.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "`T_SYS_ROLE_MENU`")
public class SysRoleMenu extends BaseEntity {
    /**
     * 角色ID
     */
    @Id
    @Column(name = "`R_ID`")
    private Integer rId;

    /**
     * 菜单ID
     */
    @Column(name = "`M_ID`")
    private Integer mId;

    /**
     * 获取角色ID
     *
     * @return R_ID - 角色ID
     */
    public Integer getrId() {
        return rId;
    }

    /**
     * 设置角色ID
     *
     * @param rId 角色ID
     */
    public void setrId(Integer rId) {
        this.rId = rId;
    }

    /**
     * 获取菜单ID
     *
     * @return M_ID - 菜单ID
     */
    public Integer getmId() {
        return mId;
    }

    /**
     * 设置菜单ID
     *
     * @param mId 菜单ID
     */
    public void setmId(Integer mId) {
        this.mId = mId;
    }
}