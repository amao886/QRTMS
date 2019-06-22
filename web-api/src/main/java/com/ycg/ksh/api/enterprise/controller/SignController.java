package com.ycg.ksh.api.enterprise.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.OrderSignature;
import com.ycg.ksh.entity.persistent.SignedRelation;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.SignRecordSearch;
import com.ycg.ksh.entity.service.enterprise.OrderAlliance;
import com.ycg.ksh.service.api.OrderService;
import com.ycg.ksh.service.api.ReceiptService;
import com.ycg.ksh.service.api.SignRelationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("enterprise.sign.controller")
@RequestMapping("/enterprise/sign")
public class SignController extends BaseController {

    @Resource
    private SignRelationService signRelationService;
    @Resource
    private ReceiptService receiptService;
    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/view/{viewkey}")
    public String manage(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/sign/"+ viewkey;
    }
    /**
     *
     * TODO 新增签署方
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 15:55:09
     * @param object
     * @param request
     * @return
     */
    @RequestMapping(value="add/sign/relation")
    @ResponseBody
    public JsonResult addSignRelation(@RequestBody RequestObject object,HttpServletRequest request) throws Exception{
    	Assert.notNull(object, "添加信息不能空");
    	if(logger.isDebugEnabled())
    		logger.debug("addSignRelation===>object:{}",object);
    	SignedRelation signedRelation = object.toJavaBean(SignedRelation.class);
    	signRelationService.saveSignRelation(signedRelation);
    	return JsonResult.SUCCESS;
    }
    /**
     *
     * TODO 修改签署方
     * <p>
     * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-06-13 16:17:22
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="eidt/sign/relation")
    @ResponseBody
    public JsonResult editSignRelation(@RequestBody RequestObject object, HttpServletRequest request)
    		throws Exception{
    	Assert.notNull(object, "添加信息不能空");
    	if(logger.isDebugEnabled())
    		logger.debug("editSignRelation===>object:{}",object);
    	SignedRelation signedRelation = object.toJavaBean(SignedRelation.class);
    	signRelationService.updateSignRelation(signedRelation);
    	return JsonResult.SUCCESS;
    }

    @RequestMapping(value="del/sign/relation/{key}")
    @ResponseBody
    public JsonResult delSignRelation(@PathVariable Long key,HttpServletRequest request)
    		throws Exception{
    	if(logger.isDebugEnabled())
    		logger.debug("delSignRelation===>object:{}",key);
    	signRelationService.deleteSignRelation(key);
    	return JsonResult.SUCCESS;
    }


    /**
     * 电子签收短信验证码发送
     * @param request
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/send/code")
    @ResponseBody
    public JsonResult sendSignatureCode(HttpServletRequest request,@RequestBody(required=false) RequestObject body) throws Exception {
        receiptService.sendSignatureCode(loadUserKey(request), StringUtils.longCollection(body.get("orderKey")));
        return JsonResult.SUCCESS;
    }

    /**
     * 签署校验
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/signature/validate")
    @ResponseBody
    public JsonResult signatureValidate(HttpServletRequest request) throws Exception {
        receiptService.signatureValidate(loadUserKey(request));
        return JsonResult.SUCCESS;
    }
    /**
     * 订单签署
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping(value = "/signature/single")
    @ResponseBody
    public JsonResult signatureSingle(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("signature {}", body);
        Assert.notNull(body, Constant.PARAMS_ERROR);
        User user = loadUser(request);
        OrderSignature orderSignature = body.toJavaBean(OrderSignature.class);
        String exceptionContent = null;
        Integer flag = body.getInteger("exception");
        if (flag != null && flag == 1) {
            exceptionContent = body.get("content");
            Assert.notBlank(exceptionContent, "异常内容不能为空");
        }
        String code = body.get("code");
        Assert.notBlank(code, "验证码不能为空");
        orderSignature.setUserId(user.getId());
        receiptService.signature(code, orderSignature, exceptionContent);
        return JsonResult.SUCCESS;
    }

    /**
     * 根据选择的订单编号查询电子回单信息
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping(value = "/receipt/selects")
    @ResponseBody
    public JsonResult receiptSelects(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("signature {}", body);
        Assert.notNull(body, Constant.PARAMS_ERROR);
        JsonResult jsonResult = new JsonResult();
        String orderKeyString = body.get("orderKeys");
        Assert.notBlank(orderKeyString, "至少选择一个要签署的订单");
        Collection<Long> orderKeys = StringUtils.longCollection(orderKeyString);
        Assert.notEmpty(orderKeys, "至少选择一个要签署的订单");
        Collection<OrderAlliance> alliances = orderService.electoralOrderList(loadUserKey(request), orderKeys);
        if(CollectionUtils.isNotEmpty(alliances)){
            jsonResult.put("keys", alliances.parallelStream().map(a -> a.getId()).collect(Collectors.toList()));
        }
        jsonResult.put("results", orderService.electoralOrderList(loadUserKey(request), orderKeys));
        return jsonResult;
    }
    /**
     * 订单签署
     *
     * @param body
     * @param request
     * @return
     */
    @RequestMapping(value = "/signature/multiple")
    @ResponseBody
    public JsonResult signatureMultiple(@RequestBody RequestObject body, HttpServletRequest request) throws Exception {
        logger.info("signature {}", body);
        Assert.notNull(body, Constant.PARAMS_ERROR);

        JsonResult jsonResult = new JsonResult();
        String code = body.get("code");
        Assert.notBlank(code, "验证码不能为空");

        String orderKeyString = body.get("keys");
        Assert.notBlank(orderKeyString, "至少选择一个要签署的订单");
        Collection<Long> orderKeys = StringUtils.longCollection(orderKeyString);
        Assert.notEmpty(orderKeys, "至少选择一个要签署的订单");

        //Long psealKey = body.getLong("psealKey");
        //Assert.notBlank(psealKey, "请选择个人印章");

        Long csealKey = body.getLong("csealKey");
        Assert.notBlank(csealKey, "请选择企业印章");

        OrderSignature orderSignature = new OrderSignature();
        //orderSignature.setPersonalSeal(psealKey);
        orderSignature.setSealId(csealKey);
        String exceptionContent = null;
        Integer flag = body.getInteger("exception");
        if (flag != null && flag == 1) {
            exceptionContent = body.get("content");
            Assert.notBlank(exceptionContent, "异常内容不能为空");
        }
        orderSignature.setUserId(loadUserKey(request));
        int count = receiptService.signature(code, orderSignature, orderKeys, exceptionContent);
        if(count > 0){
            jsonResult.modify(count - orderKeys.size() == 0, "成功签署["+ count +"]份回单");
        }else{
            jsonResult.modify(false, "签署异常");
        }
        return jsonResult;
    }




    @RequestMapping(value = "/search/manage")
    @ResponseBody
    public JsonResult searchSignManage(HttpServletRequest request ,@RequestBody(required=false) RequestObject body) throws Exception {
        logger.debug("searchsignManage-->{}" ,body);

        Integer size = Optional.ofNullable(body.getInteger("pageSize")).orElse(10);
        Integer num = Optional.ofNullable(body.getInteger("pageNum")).orElse(1);
        PageScope scope = new PageScope(num,size);

        SignRecordSearch signRecordSearch = body.toJavaBean(SignRecordSearch.class);
        signRecordSearch.setUserId(loadUserKey(request));

        JsonResult jsonResult = new JsonResult();
        jsonResult.put("page", signRelationService.listSignRecord(signRecordSearch, scope));
        return jsonResult;
    }
}
