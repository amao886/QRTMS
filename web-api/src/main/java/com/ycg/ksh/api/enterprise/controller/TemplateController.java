package com.ycg.ksh.api.enterprise.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */

import com.alibaba.fastjson.JSON;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.enterprise.ImportTemplate;
import com.ycg.ksh.entity.persistent.enterprise.TemplateDetail;
import com.ycg.ksh.entity.service.enterprise.TemplateAlliance;
import com.ycg.ksh.entity.service.enterprise.TemplateContext;
import com.ycg.ksh.entity.service.enterprise.TemplateDescribe;
import com.ycg.ksh.service.api.TemplateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * 模板控制层
 * <p>
 *
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/10
 */
@Controller("enterprise.template.controller")
@RequestMapping("/enterprise/template")
public class TemplateController extends BaseController {

    @Resource
    TemplateService templateService;

    @RequestMapping(value = "/manage/{viewkey}")
    public String view(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/template/" + viewkey +"/manage";
    }
    @RequestMapping(value = "/operate/{viewkey}")
    public String operateView(@PathVariable String viewkey, Model model, HttpServletRequest request) throws Exception {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/template/" + viewkey;
    }
    /**
     * 模板管理
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public JsonResult list(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        //RequestObject body = new RequestObject(request.getParameterMap());
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("templates", templateService.listTemplates(loadUserKey(request), requestObject.getInteger("category")));
        return jsonResult;
    }

    /**
     * 新增模板
     *
     * @param requestObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public JsonResult add(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String templateName = requestObject.get("templateName");
        Assert.notBlank(templateName, "模板名称不能为空");
        String jsonString = requestObject.get("templateDetails");
        Assert.notBlank(jsonString, "模板字段配置不能为空");
        List<TemplateDetail> templateDetails = JSON.parseArray(jsonString, TemplateDetail.class);
        Assert.notEmpty(templateDetails, "模板必填字段配置不能为空");

        ImportTemplate template = new ImportTemplate();
        template.setName(templateName);
        template.setMergeType(requestObject.getInteger("mergeType"));
        template.setCategory(requestObject.getInteger("category"));
        Long templateKey = templateService.saveTemplate(loadUserKey(request), template, templateDetails);
        //是否需要导出
        if (requestObject.getBoolean("export")) {
            return buildByTemplate(loadUserKey(request), templateKey);
        }
        return jsonResult;
    }

    /**
     * 编辑模板
     *
     * @param requestObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public JsonResult edit(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Long templateKey = requestObject.getLong("templateKey");
        Assert.notBlank(templateKey, "模板编号不能为空");
        String templateName = requestObject.get("templateName");
        Assert.notBlank(templateName, "模板名称不能为空");
        String jsonString = requestObject.get("templateDetails");
        Assert.notBlank(jsonString, "模板字段配置不能为空");
        List<TemplateDetail> templateDetails = JSON.parseArray(jsonString, TemplateDetail.class);
        Assert.notEmpty(templateDetails, "模板必填字段配置不能为空");

        ImportTemplate template = new ImportTemplate();
        template.setName(templateName);
        template.setKey(templateKey);
        //template.setEmptyColumn();
        template.setMergeType(requestObject.getInteger("mergeType"));

        templateService.updateTemplate(loadUserKey(request), template, templateDetails);
        //是否需要导出
        if (requestObject.getBoolean("export")) {
            return buildByTemplate(loadUserKey(request), templateKey);
        }
        return jsonResult;
    }

    /**
     * 查看模板详情
     *
     * @param requestObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public JsonResult detail(@RequestBody RequestObject requestObject, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Integer category = requestObject.getInteger("category");
        Long templateKey = requestObject.getLong("templateKey");
        if (templateKey != null && templateKey > 0) {
            TemplateAlliance alliance = templateService.allianceTemplate(templateKey);
            if(alliance != null){
                category = alliance.getCategory();
            }
            jsonResult.put("template", alliance);
        }
        //基本配置字段(必选和可选)
        jsonResult.put("bases", templateService.listTemplateProperty(category));
        return jsonResult;
    }

    /**
     * 设置常用和取消常用
     *
     * @param templateKey
     * @param request
     * @return
     */
    @RequestMapping(value = "/setting/{templateKey}")
    @ResponseBody
    public JsonResult setting(@PathVariable Long templateKey, HttpServletRequest request) {
        //设置常用 或者 取消常用
        Assert.notBlank(templateKey, "模板编号不能为空");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("result", templateService.modifyTemplateFettle(loadUserKey(request), templateKey));
        return jsonResult;
    }

    /**
     * 删除模板
     *
     * @param templateKey
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete/{templateKey}")
    @ResponseBody
    public JsonResult delete(@PathVariable Long templateKey, HttpServletRequest request) {
        //删除模板
        templateService.deleteTemplate(loadUserKey(request), templateKey);
        return JsonResult.SUCCESS;
    }


    /**
     * 下载模板
     *
     * @param templateKey
     * @param request
     * @return
     */
    @RequestMapping(value = "/download/{templateKey}")
    @ResponseBody
    public JsonResult download(@PathVariable Long templateKey, HttpServletRequest request) {
        Assert.notBlank(templateKey, "模板编号不能为空");
        return buildByTemplate(loadUserKey(request), templateKey);
    }


    private JsonResult buildByTemplate(Integer uKey, Long templateKey) {
        JsonResult jsonResult = new JsonResult();
        FileEntity fileEntity = templateService.buildByTemplate(uKey, templateKey);
        logger.info("fileEntity -> {}", fileEntity);
        if (fileEntity != null) {
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix(fileEntity.getAliasName(), fileEntity.getSuffix()), true));
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
        } else {
            jsonResult.modify(false, "模板文件生成异常");
        }
        return jsonResult;
    }

    /**
     * 根据模板ID获取模板信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/templateInfo/{templateKey}")
    @ResponseBody
    public JsonResult template(@PathVariable Long templateKey, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        TemplateAlliance alliance = templateService.allianceTemplate(templateKey);
        if (alliance != null) {
            if (CollectionUtils.isNotEmpty(alliance.getDescribes())) {
                Map<String, Object> required = new HashMap<String, Object>();
                Collection<Object[]> optionalList = new ArrayList<Object[]>();
                alliance.getDescribes().forEach((TemplateDescribe templateDescribe) -> {
                    if (templateDescribe.isCustom()) {
                        //自定义字段
                        optionalList.add(new Object[]{templateDescribe.getName(), templateDescribe.getDetailKey()});
                    } else {
                        //必选字段
                        required.put(templateDescribe.getDataKey(), templateDescribe.getDetailKey());
                    }
                });
                jsonResult.put("required", required);
                jsonResult.put("optional", optionalList);
            }
        }

        return jsonResult;
    }


    /**
     * 承运商订单录入
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/entrance/conveyer")
    @ResponseBody
    public JsonResult entranceConveyer(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        entrance(loadUserKey(request), PartnerType.CONVEY ,requestObject);
        return JsonResult.SUCCESS;
    }

    /**
     * 发货方订单录入
     *
     * @param requestObject
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/entrance/shipper")
    @ResponseBody
    public JsonResult entranceShipper(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        entrance(loadUserKey(request), PartnerType.SHIPPER ,requestObject);
        return JsonResult.SUCCESS;
    }

    private void entrance(Integer uKey, PartnerType partner, RequestObject requestObject) throws Exception {
        Assert.notNull(requestObject, Constant.PARAMS_ERROR);
        Long templateKey = requestObject.getLong("templateKey");
        Assert.notBlank(templateKey, "模板ID不能为空");

        TemplateContext context = new TemplateContext(uKey, partner, templateKey);

        String describeString = requestObject.get("describes");
        if(StringUtils.isNotBlank(describeString)){
            Collection<TemplateDescribe> describes = Globallys.toJavaObjects(describeString, TemplateDescribe.class);
            Assert.notEmpty(describes, "模板数据不能为空");

            templateService.saveByTemplateDescribe(context, describes);
        }else{
            Map<? extends Serializable, Object> waybillInfo = (Map<? extends Serializable, Object>) JSON.parse(requestObject.get("waybillInfo"));
            Assert.notEmpty(waybillInfo, "运单信息不能为空");
            List<Map<? extends Serializable, Object>> commodities = (List<Map<? extends Serializable, Object>>) JSON.parse(requestObject.get("commodities"));
            Assert.notEmpty(commodities, "货物信息不能为空");
            templateService.saveWaybillEntry(context, waybillInfo, commodities);
        }
    }
}
