/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 11:34:34
 */
package com.ycg.ksh.entity.service;

import com.ycg.ksh.entity.persistent.ProjectGroup;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * 项目组信息,包含条码总数和已使用数量
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 11:34:34
 */
public class MergeProjectGroup extends ProjectGroup {

	private static final long serialVersionUID = -1375770156328035743L;

    private int number = 0; // 可用条码数量
    private int useCount = 0; // 已使用条码数量
    private int totalNum = 0; // 全部条码数量
	
    private List<MergeProjectGroupMember> list;
  
	public MergeProjectGroup() {
		super();
	}
	public MergeProjectGroup(ProjectGroup projectGroup) throws Exception {
		super();
		BeanUtils.copyProperties(this, projectGroup);
	}
	
	/**
	 * getter method for list
	 * @return the list
	 */
	public List<MergeProjectGroupMember> getList() {
		return list;
	}
	/**
	 * setter method for list
	 * @param list the list to set
	 */
	public void setList(List<MergeProjectGroupMember> list) {
		this.list = list;
	}
	/**
	 * getter method for number
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * setter method for number
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * getter method for totalNum
	 * @return the totalNum
	 */
	public int getTotalNum() {
		return totalNum;
	}
	/**
	 * setter method for totalNum
	 * @param totalNum the totalNum to set
	 */
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	/**
	 * getter method for useCount
	 * @return the useCount
	 */
	public int getUseCount() {
		return useCount;
	}
	/**
	 * setter method for useCount
	 * @param useCount the useCount to set
	 */
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
}
