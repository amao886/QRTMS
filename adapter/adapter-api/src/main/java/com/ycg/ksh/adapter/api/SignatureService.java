package com.ycg.ksh.adapter.api;

import java.io.File;
import java.io.Serializable;

/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/4/28
 */
public interface SignatureService {

    /**
     * 个人-认证
     * @param idCard 用户身份证号码
     * @param name  用户真实姓名
     * @param mobile 手机号
     * @param first  是否是第一次(非第一次调更新)
     * @return 用户标识
     */
    Serializable legalize(String idCard, String name, String mobile, boolean first);

    /** 企业-认证
     * @param certificate 企业营业执照编号或统一组织机构代码
     * @param name  企业名称
     * @param address 企业注册地址
     * @param contact 企业联系人
     * @param mobile 联系人电话
     * @param first  是否是第一次(非第一次调更新)
     * @return 用户标识
     */
    Serializable legalize(String certificate, String name, String address, String contact, String mobile, boolean first);

    /** 上传PDF文件创建合同
     * @param name 合同名称
     * @param file PDF文件
     * @return
     */
    Serializable createContract(String name, File file);
}



