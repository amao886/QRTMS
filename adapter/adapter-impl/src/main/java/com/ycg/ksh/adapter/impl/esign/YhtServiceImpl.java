package com.ycg.ksh.adapter.impl.esign;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/5
 */

import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.dubbo.ServiceMonitor;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.cache.CacheManager;
import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.adapter.esign.*;
import com.ycg.ksh.adapter.api.AuthenticateService;
import com.ycg.ksh.adapter.api.ESignService;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 电子签收云合同实现
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/5
 */
public class YhtServiceImpl implements ESignService, AuthenticateService, ServiceMonitor {

    private static final String TOKEN_KEY = "yht.token.key";
    private static final String HEADER_TOKEN_KEY = "token";


    private static final String BORDERTYPE = "B1#B2";
    private static final String FONTFAMILY = "F1#F2#F3#F3";

    private static Map<String, String> BRANKCODES;

    private String certifiedUrl;//请求地址
    private String key;//账号,由云合同提供
    private String value;//密码,由云合同提供


    private String apiUrl;//请求地址
    private String appId;//应用id，在开放平台注册应用获得
    private String appKey;///应用密码，在开放平台注册应用获得


    private String brankCodeFile;///银行编码文件

    @Resource
    CacheManager cacheManager;



    /**
     * 服务启动完成
     *
     * @param activeProfile
     */
    @Override
    public void onServerStrated(ApplicationContext context, String activeProfile) throws BusinessException {
        BufferedReader reader = null;
        try {
            BRANKCODES = new HashMap<String, String>();
            File file = new File(FileUtils.path(SystemUtils.projectPath(), brankCodeFile));
            if(!file.exists()){
                URL url = this.getClass().getResource(brankCodeFile);
                if(url != null){
                    file = new File(url.getFile());
                }
            }
            logger.info("处理云合同银行编码文件 -> {}", file.getPath());
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] items = line.split("#");
                if(items.length > 1){
                    BRANKCODES.put(StringUtils.trim(items[0]), StringUtils.trim(items[1]));
                }
            }
            logger.info("处理云合同银行编码文件 -> {}", BRANKCODES.size());
        } catch (IOException e) {
            logger.error("处理云合同银行编码文件异常", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) { }
            }
        }
    }

    private JSONObject validate(String resultString){
        if(StringUtils.isNotBlank(resultString)) {
            JSONObject jsonObject = JSONObject.parseObject(resultString);
            String errcode = jsonObject.getString("code");
            if(StringUtils.isNotBlank(errcode) && !StringUtils.equalsIgnoreCase("200", errcode)){
                logger.error("请求异常:" + resultString);
                throw new BusinessException(errcode, jsonObject.getString("msg"));
            }
            return jsonObject;
        }else {
            logger.error("请求响应解析异常:" + resultString);
            throw new BusinessException("请求响应解析异常");
        }
    }

    /**
     * 获取下载令牌
     * @param jsonObject
     * @param keyString
     * @return
     */
    private String getCiphertext(JSONObject jsonObject,String keyString){
        if(null != jsonObject){
            return jsonObject.getString(keyString);
        }
        return null;
    }

    private String dataKey(JSONObject jsonObject, String keyString){
        JSONObject object = jsonObject.getJSONObject("data");
        if(object != null){
            return object.getString(keyString);
        }
        return null;
    }

    private JSONObject request(HttpClient client, Map<String, Object> parameters) throws ParameterException, BusinessException {
        try{
            //client.property(HEADER_TOKEN_KEY, getToken());
            client.parameter(parameters);
            return validate(client.request());
        } catch (BusinessException | ParameterException e){
            throw e;
        } catch (Exception e){
            throw new BusinessException("请求异常: "+ client, e);
        }finally {
            logger.info("云合同: {}", client);
            String token = client.responseHeader(HEADER_TOKEN_KEY);
            if(StringUtils.isNotBlank(token)){
                cacheManager.set(TOKEN_KEY, token, 14L, TimeUnit.MINUTES);
            }
        }
    }
    private JSONObject apiGet(String method, Map<String, Object> parameters) throws ParameterException, BusinessException {
        HttpClient client = new HttpClient(SystemUtils.buildUrl(apiUrl, method), HttpClient.Type.GET);
        client.property(HEADER_TOKEN_KEY, getToken());
        return request(client, parameters);
    }
    private JSONObject apiJson(String method, Map<String, Object> parameters) throws ParameterException, BusinessException {
        HttpClient client = new HttpClient(SystemUtils.buildUrl(apiUrl, method), HttpClient.Type.JSON);
        client.property(HEADER_TOKEN_KEY, getToken());
        return request(client, parameters);
    }
    private JSONObject certifiedRequest(String method, Map<String, Object> parameters) throws ParameterException, BusinessException {
        HttpClient client = new HttpClient(SystemUtils.buildUrl(certifiedUrl, method), HttpClient.Type.POST);
        client.parameter("key", key);
        client.parameter("value", value);
        return request(client, parameters);
    }

    /**
     * 个人实名认证
     *
     * @param personal 个人认证信息
     * @return 此次请求的编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Personal legalize(Personal personal) throws ParameterException, BusinessException {
        //authentic/personal/bankFour
        Map<String, Object> parameters = new HashMap<String, Object>();
        //personal.setRequestKey(Globallys.nextString());
        if(personal.getRequestKey() != null){
            parameters.put("guId", personal.getRequestKey());
        }
        parameters.put("idNo", personal.getIdNo());
        parameters.put("idName", personal.getName());
        parameters.put("bankCardNo", personal.getBrankNo());
        parameters.put("mobile", personal.getMobile());
        logger.info("云合同个人实名认证: {}", personal);
        certifiedRequest("authentic/personal/bankFour", parameters);
        return personal;
    }

    /**
     * 企业认证
     * @param enterprise 企业认证信息
     * @return 此次请求的编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Enterprise legalize(Enterprise enterprise) throws ParameterException, BusinessException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        logger.info("云合同: {}", enterprise);
        if(enterprise.stepBase()){
            if(StringUtils.isNotBlank(enterprise.getCodeORG())){
                parameters.put("creditCode", enterprise.getCodeORG());//组织机构代码
            }else{
                parameters.put("creditCode", enterprise.getCodeUSC());//统一社会信用代码
            }
            parameters.put("companyName", enterprise.getCompanyName());//企业名称
            parameters.put("legalPersonName", enterprise.getLegalName());//法人名称

            certifiedRequest("authentic/company/authentic", parameters);
        }
        if(enterprise.stepBrank()){
            //enterprise.setRequestKey(Globallys.nextString());
            //parameters.put("guId", enterprise.getRequestKey());
            parameters.put("cardNo", enterprise.getCardNo());//银行卡号
            parameters.put("acctName", enterprise.getAccountName());//开户名称
            parameters.put("bankCode", toBankCode(enterprise.getBank()));//银行编号
            //parameters.put("cityCode", personal.getMobile());//城市编号
            //parameters.put("braBank", personal.getMobile());//开户支行全称
            parameters.put("prcptcd", enterprise.getPrcptcd());//大额行号
            JSONObject jsonObject = certifiedRequest("authentic/company/remittance", parameters);
            if(jsonObject != null){
                enterprise.setRequestKey(jsonObject.getString("msg"));
            }
            if(StringUtils.isBlank(enterprise.getRequestKey())){
                throw new BusinessException("打款异常");
            }
        }
        if(enterprise.stepCash()){
            parameters.put("id", enterprise.getRequestKey());//打款请求接口返回得订单号
            parameters.put("money", enterprise.getCash());//打款金额
            try{
                certifiedRequest("authentic/company/checkMoney", parameters);
            }catch (BusinessException e){
                if(StringUtils.isNotBlank(e.getCode()) && "43003".equals(e.getCode())){
                    enterprise.setErrorMsg(e.getFriendlyMessage());
                }else{ throw e; }
            }
        }
        if(enterprise.stepCheck()){
            parameters.put("id", enterprise.getRequestKey());//打款请求接口返回得订单号
            try{
                certifiedRequest("authentic/company/checkRemittance", parameters);
            }catch (BusinessException e){
                if(StringUtils.isNotBlank(e.getCode()) && e.getCode().startsWith("41")){
                    enterprise.setErrorMsg(e.getFriendlyMessage());
                }else{ throw e; }
            }
        }
        return enterprise;
    }

    private String getToken(){
        Object token = cacheManager.get(TOKEN_KEY);
        if(token == null){
            HttpClient client = new HttpClient(SystemUtils.buildUrl(apiUrl, "auth/login"));
            try{
                client.parameter("appId", appId);
                client.parameter("appKey", appKey);
                /*
                if(StringUtils.isNotBlank(signerId)){
                    client.parameter("signerId", signerId);
                }
                */
                logger.info("云合同 -> 获取TOKEN: {}", client.parameters());
                validate(client.json());
                token = client.responseHeader(HEADER_TOKEN_KEY);
                cacheManager.set(TOKEN_KEY, token, 14L, TimeUnit.MINUTES);
            }catch (Exception e){
                throw new BusinessException("获取TOKEN请求异常", e);
            }
        }
        if(token == null){
            throw new BusinessException("获取token异常");
        }
        return String.valueOf(token);
    }


    /**
     * 创建或者更新个人用户信息
     *
     * @param signer
     * @param personal
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Personal buildPersonal(Signer signer, Personal personal) throws ParameterException, BusinessException {
        logger.info("云合同: {}", personal);
        Map<String, Object> parameters = new HashMap<String, Object>();
        if(personal.stepCreate()){
            parameters.put("userName", personal.getName());//用户姓名（最长15字符）
            parameters.put("identityRegion", "0");//身份地区：0大陆，1香港，2台湾，3澳门
            parameters.put("certifyNum", personal.getIdNo());//身份证号码，应用内唯一。
            parameters.put("phoneRegion", "0");//手机号地区：0大陆，1香港、澳门，2台湾
            parameters.put("phoneNo", personal.getMobile());//手机号：1.大陆,首位为1，长度11位纯数字；2. 香港、澳门,长度为8的纯数字；3.台湾,长度为10 的纯数字
            parameters.put("caType", "B2");//证书类型：B2 长效CA证书，固定字段
            JSONObject jsonObject = apiJson("user/person", parameters);
            personal.setRequestKey(dataKey(jsonObject, "signerId"));
            if(StringUtils.isBlank(personal.getRequestKey())){
                throw new BusinessException("用户信息创建失败");
            }
        }
        if(personal.stepModify()){
            parameters.put("signerId", personal.getRequestKey());
            if(StringUtils.isNotBlank(personal.getName())){
                parameters.put("userName", personal.getName());//用户姓名（最长15字符）
            }
            if(StringUtils.isNotBlank(personal.getMobile())){
                parameters.put("phoneRegion", "0");//手机号地区：0大陆，1香港、澳门，2台湾
                parameters.put("phoneNo", personal.getMobile());//手机号：1.大陆,首位为1，长度11位纯数字；2. 香港、澳门,长度为8的纯数字；3.台湾,长度为10 的纯数字
            }
            apiJson("user/personNameAndPhone", parameters);
        }
        return personal;
    }

    /**
     * 创建或者更新企业用户
     *
     * @param signer
     * @param enterprise
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Enterprise buildEnterprise(Signer signer, Enterprise enterprise) throws ParameterException, BusinessException {
        logger.info("云合同: {}", enterprise);
        Map<String, Object> parameters = new HashMap<String, Object>();
        if(enterprise.stepCreate()){
            parameters.put("userName", enterprise.getUserName());//企业名称（最长99字符）
            if(StringUtils.isNotBlank(enterprise.getCodeORG())){
                parameters.put("certifyType", "3");//企业相关证件类型包含：1.社会信用代码，2.营 业执照注册号，3.组织机构代码
                parameters.put("certifyNum", enterprise.getCodeORG());//认证号码，应用内唯一：1.社会统一信用代码， 长度为18；2.营业执照，长度为18；3.组织机构代码，长度为9
            }else{
                parameters.put("certifyType", "1");//企业相关证件类型包含：1.社会信用代码，2.营 业执照注册号，3.组织机构代码
                parameters.put("certifyNum", enterprise.getCodeUSC());//认证号码，应用内唯一：1.社会统一信用代码， 长度为18；2.营业执照，长度为18；3.组织机构代码，长度为9
            }
            parameters.put("phoneNo", enterprise.getPhoneNo());//企业类型用户的手机号仅支持中国大陆：首位为 1，长度11位纯数字
            parameters.put("caType", "B2");//证书类型：B2 长效CA证书，固定字段
            JSONObject jsonObject = apiJson("user/company", parameters);
            enterprise.setRequestKey(dataKey(jsonObject, "signerId"));
            if(StringUtils.isBlank(enterprise.getRequestKey())){
                throw new BusinessException("企业用户创建失败");
            }
        }
        return enterprise;
    }

    /**
     * 创建印章(企业或者个人)
     *
     * @param signer
     * @param sealMoulage
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public SealMoulage buildSeal(Signer signer, SealMoulage sealMoulage) throws ParameterException, BusinessException {
        logger.info("云合同: {}", sealMoulage);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("signerId", sealMoulage.getSignerId());
        if(sealMoulage.stepPersonal()){
            //"borderType": "string",//边框样式：默认B1（B1：有边框，B2：无边 框）
            // "fontFamily": "string"//字体样式：默认F1（F1：楷体，F2：华文仿宋， F3：华文楷体，F4：微软雅黑）
            String borderType = sealMoulage.getBorderType(),
                    fontFamily = sealMoulage.getFontFamily();
            if(StringUtils.isNotBlank(borderType) && (!"B1".equals(borderType) && !"B2".equals(borderType))){
                throw new BusinessException("印章边框样式不支持");
            }
            if(StringUtils.isBlank(borderType) || StringUtils.equelsOne(borderType, "F1", "F2", "F3", "F4")){
                throw new BusinessException("字体样式样式不支持");
            }
            parameters.put("borderType", Optional.ofNullable(sealMoulage.getBorderType()).orElse("B1"));
            parameters.put("fontFamily", Optional.ofNullable(sealMoulage.getFontFamily()).orElse("F1"));
            JSONObject jsonObject = apiJson("user/personMoulage", parameters);
            sealMoulage.setRequestKey(dataKey(jsonObject, "moulageId"));
            if(StringUtils.isBlank(sealMoulage.getRequestKey())){
                throw new BusinessException("生成个人印章失败");
            }
        }
        if(sealMoulage.stepEnterprise()){
            String textContent = sealMoulage.getTextContent(),
                    keyContent = sealMoulage.getKeyContent();
            if(StringUtils.isNotBlank(textContent) && textContent.length() > 12){
                throw new BusinessException("横向文本字数太多,不能超过12个汉字");
            }
            if(StringUtils.isNotBlank(keyContent) && keyContent.length() != 13){
                throw new BusinessException("防伪码必须是13个数字");
            }
            parameters.put("styleType", Optional.ofNullable(sealMoulage.getStyleType()).orElse("1"));
            parameters.put("textContent", textContent);
            parameters.put("keyContent", keyContent);
            JSONObject jsonObject = apiJson("user/companyMoulage", parameters);
            sealMoulage.setRequestKey(dataKey(jsonObject, "moulageId"));
            if(StringUtils.isBlank(sealMoulage.getRequestKey())){
                throw new BusinessException("生成企业印章失败");
            }
        }
        return sealMoulage;
    }

    /**
     * 创建合同
     *
     * @param signer
     * @param contract
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public Contract buildContract(Signer signer, Contract contract) throws ParameterException, BusinessException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        JSONObject jsonObject = null;
        parameters.put("contractTitle", contract.getTitle());//合同标题
        parameters.put("contractNo", contract.getContractNo());//合同自定义编号,可选参数，不传时默认与合同id相
        if(contract.stepTemplate()){//按模板方式创建合同文件
            parameters.put("templateId", contract.getTemplateId());//模版id，在开放平台上，上传模版后获得
            parameters.put("contractData", "内容");
            jsonObject = apiJson("contract/templateContract", parameters);
        }
        if(contract.stepFile()){//上传文件的方式创建文件
            HttpClient client = new HttpClient(SystemUtils.buildUrl(apiUrl, "contract/fileContract"), HttpClient.Type.POST);
            client.property(HEADER_TOKEN_KEY, getToken());
            client.parameter(parameters);
            try{
                jsonObject = validate(client.upload(contract.getFile(), "contractFile"));
            }catch (Exception ioe){
                throw new BusinessException("合同文件上传异常", ioe);
            }
        }
        contract.setRequestKey(dataKey(jsonObject, "contractId"));
        if(StringUtils.isBlank(contract.getRequestKey())){
            throw new BusinessException("合同生成失败");
        }
        return contract;
    }

    /**
     * 合同下载
     *
     * @param signer
     * @param contractNo
     * @return
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public FileEntity download(Signer signer, Serializable contractNo) throws ParameterException, BusinessException {
        //String method = String.format("contract/download/%d/%s", 1, contractNo);
        //auth/download/{ciphertext}
        JSONObject jsonObject = apiGet(String.format("contract/download/%d/%s", 1, contractNo), null);//获取下载的临时令牌
        String ciphertext = getCiphertext(jsonObject, "data");
        HttpClient client = new HttpClient(SystemUtils.buildUrl(apiUrl, "auth/download/"+ ciphertext), HttpClient.Type.GET);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setSuffix(FileUtils.PDF_SUFFIX);
        fileEntity.setFileName(FileUtils.appendSuffix(String.valueOf(contractNo), FileUtils.PDF_SUFFIX));
        fileEntity.setDirectory(SystemUtils.fileRootPath());
        fileEntity.setSubDir(Directory.ERECEIPT.sub("yht"));
        try{
            File file = client.down(fileEntity.getPath());
            if(file != null){
                fileEntity.setCount(1);
                fileEntity.setSize(FileUtils.size(file.length(), FileUtils.ONE_KB));
            }
        }catch (IOException ioe){
            throw new BusinessException("合同文件下载异常", ioe);
        }
        return fileEntity;
    }

    /**
     * 添加合同签署者
     *
     * @param signer
     * @param contractNo 合同自定义编号
     * @param signers    签署者
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void addContractSigner(Signer signer, Serializable contractNo, Collection<ContractSigner> signers) throws ParameterException, BusinessException {
        logger.info("云合同: 添加签署者 : 合同编号:{} 签署者:{}", contractNo, signers);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idType", 1);//参数类型：0 合同ID，1 合同自定义编号
        parameters.put("idContent", contractNo);//"idContent": "string",//ID内容
        parameters.put("signers", signers);
        apiJson("contract/signer", parameters);
    }

    /**
     * 签署合同
     *
     * @param signer
     * @param contractNo 合同编号
     * @param signerId   签署者编号
     * @param moulageId  印章编号
     * @throws ParameterException
     * @throws BusinessException
     */
    @Override
    public void signContract(Signer signer, Serializable contractNo, Serializable signerId, Serializable moulageId) throws ParameterException, BusinessException {
        logger.info("云合同 -> 签署合同 : 合同编号:{} 签署者:{} 印章:{}", contractNo, signerId, moulageId);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idType", 0);//参数类型：0 合同ID，1 合同自定义编号
        parameters.put("idContent", contractNo);//"idContent": "string",//ID内容
        parameters.put("signerId", signerId);
        parameters.put("moulageId", moulageId);
        apiJson("contract/sign", parameters);
    }

    //用银行名称换取云合同提供的对应银行编号
    private String toBankCode(String bankName){
        String brankCode = BRANKCODES.get(bankName);
        if(StringUtils.isBlank(brankCode)){
            throw new BusinessException("该操作不支持该银行");
        }
        return brankCode;
    }

    public void setCertifiedUrl(String certifiedUrl) {
        this.certifiedUrl = certifiedUrl;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setBrankCodeFile(String brankCodeFile) {
        this.brankCodeFile = brankCodeFile;
    }

}
