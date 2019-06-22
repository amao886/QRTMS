package com.ycg.ksh.entity.adapter.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/6
 */

import com.ycg.ksh.common.entity.BaseEntity;

/**
 * 电子签收--合同
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/6
 */
public class Contract extends BaseEntity {

    enum Step{
        TEMPLATE, FILE;
    }

    private String requestKey;//请求编号

    private String title;//合同标题",//合同标题
    private String contractNo;//合同自定义编号",//可选参数，不传时默认与合同id相 同。

    private String templateId;//模版id，在开放平台上，上传模版后获得
    private String file;//合同文件

    private Step step;

    public boolean stepTemplate(){
        return Step.TEMPLATE == step;
    }
    public boolean stepFile(){
        return Step.FILE == step;
    }

    public Contract() {
    }

    public static Contract template(String title, String contractNo, String templateId){
        Contract contract = new Contract();
        contract.setTitle(title);
        contract.setContractNo(contractNo);
        contract.setTemplateId(templateId);

        contract.step = Step.TEMPLATE;
        return contract;
    }

    public static Contract file(String title, String contractNo, String file){
        Contract contract = new Contract();
        contract.setTitle(title);
        contract.setContractNo(contractNo);
        contract.setFile(file);

        contract.step = Step.FILE;
        return contract;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }
}
