package com.ycg.ksh.api.enterprise.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.depot.OutboundDetail;
import com.ycg.ksh.entity.persistent.depot.OutboundOrder;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.entity.service.depot.DepotSearch;
import com.ycg.ksh.service.api.DepotOutboundService;
import com.ycg.ksh.service.api.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Collection;

/**
 * 仓库出入库管理控制类
 *
 * @Auther: wangke
 * @Date: 2018/9/4 16:21
 * @Description:
 */
@Controller("enterprise.depot.controller")
@RequestMapping("/enterprise/depot")
public class DepotOutboundController extends BaseController {

    @Resource
    protected DepotOutboundService depotOutboundService;

    @Resource
    protected FileService fileService;

    /**
     * 仓库管理入口
     *
     * @return
     */
    @RequestMapping(value = "/view/{viewkey}")
    public String depotMange(@PathVariable String viewkey, Model model, HttpServletRequest request) {
        model.addAllAttributes(RequestUitl.getRequestParameters(request));
        return "/enterprise/depot/" + viewkey;
    }

    /**
     * 功能描述:入库单导入
     *
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/5 8:41
     */
    @RequestMapping(value = "/inboun/import", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult inbounImport(HttpServletRequest request) throws Exception {
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        Assert.notNull(file, "请上传一个文件!!!");
        FileEntity fileEntity = new FileEntity(file);
        String path = fileEntity.getPath();
        try {
            fileEntity = fileService.saveInboundOrder(loadUserKey(request), fileEntity);
        } finally {
            FileUtils.forceDelete(new File(path));
        }
        JsonResult jsonResult = new JsonResult();
        if (fileEntity != null && fileEntity.getFailureCount() != null && fileEntity.getFailureCount() > 0) {
            jsonResult.modify(false, "存在异常数据</br>成功导入" + fileEntity.getSuccessCount() + "条,失败" + fileEntity.getFailureCount() + "条</br>请下载处理后上传导入");
            fileEntity.setUrl(FileUtils.buildDownload(fileEntity.getPath(), FileUtils.appendSuffix(fileEntity.getAliasName(), fileEntity.getSuffix())));
            jsonResult.put("result", fileEntity);
        } else {
            jsonResult.message("操作成功</br>成功导入" + fileEntity.getSuccessCount() + "条");
        }
        return jsonResult;
    }


    /**
     * 功能描述: 入库单查询
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/6 13:57
     */
    @RequestMapping(value = "/batch/number/{batchNumber}")
    @ResponseBody
    public JsonResult batchNumber(@PathVariable String batchNumber, HttpServletRequest request) throws Exception {

        JsonResult jsonResult = new JsonResult();

        jsonResult.put("order", depotOutboundService.getByBatchNumber(batchNumber));

        return jsonResult;
    }

    /**
     * 功能描述: 入库单查询
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/6 13:57
     */
    @RequestMapping(value = "/inboun/search")
    @ResponseBody
    public JsonResult inbounManage(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        DepotSearch search = new DepotSearch();
        search.setLikefirst(object.get("likefirst"));
        search.setLikesecond(object.get("likesecond"));
        search.setFirstTime(DateUtils.minOfDay(object.getDate("firstTime")));
        search.setSecondTime(DateUtils.maxOfDay(object.getDate("secondTime")));

        AuthorizeUser user = RequestUitl.getUserInfo(request);
        if (user.getEmployee() != null) {
            search.setCompanyKey(user.getEmployee().getCompanyId());
        }

        JsonResult jsonResult = new JsonResult();

        jsonResult.put("page", depotOutboundService.pageInboundOrder(user.getId(), search, new PageScope(object.getInteger("num"), object.getInteger("size"))));

        return jsonResult;
    }

    /**
     * 功能描述: 批次查询
     *
     * @param object
     * @param request
     * @return
     * @auther: wangke
     * @date: 2018/9/6 13:57
     */
    @RequestMapping(value = "/batch/search")
    @ResponseBody
    public JsonResult batchManage(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        DepotSearch search = new DepotSearch();
        search.setLikefirst(object.get("likefirst"));
        search.setLikesecond(object.get("likesecond"));
        search.setFirstTime(DateUtils.minOfDay(object.getDate("firstTime")));
        search.setSecondTime(DateUtils.maxOfDay(object.getDate("secondTime")));

        AuthorizeUser user = RequestUitl.getUserInfo(request);
        if (user.getEmployee() != null) {
            search.setCompanyKey(user.getEmployee().getCompanyId());
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("page", depotOutboundService.pageBatchSomething(user.getId(), search, new PageScope(object.getInteger("num"), object.getInteger("size"))));
        return jsonResult;
    }

    /**
     * 功能描述: 出库记录
     *
     * @param object
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/takeout")
    @ResponseBody
    public JsonResult takeout(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception {
        OutboundOrder order = requestObject.toJavaBean(OutboundOrder.class);
        Collection<OutboundDetail> details = Globallys.toJavaObjects(requestObject.get("details"), OutboundDetail.class);
        depotOutboundService.takeOutDepot(loadUserKey(request), order, details);
        return JsonResult.SUCCESS;
    }


    /**
     * 删除入库单
     *
     * @param object
     * @return
     */
    @RequestMapping(value = "/delete/inbound")
    @ResponseBody
    public JsonResult deleteInboun(@RequestBody RequestObject object) {
        Collection<Long> inbounIds = StringUtils.longCollection(object.get("inbounIds"));
        Assert.notEmpty(inbounIds, "请至少选择一条需要删除的数据");
        depotOutboundService.deleteInboun(inbounIds);
        return JsonResult.SUCCESS;
    }

    /**
     * 出库导出
     *
     * @param object
     * @param request
     * @return
     */
    @RequestMapping(value = "/export/outbound")
    @ResponseBody
    public JsonResult outBoundExport(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        Collection<Long> outboundIds = StringUtils.longCollection(object.get("outboundIds"));
        FileEntity fileEntity = depotOutboundService.listExportOrders(outboundIds);
        JsonResult jsonResult = new JsonResult();
        if (null != fileEntity && StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.put("file", fileEntity.getPath());
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
            String fileName = FileUtils.appendSuffix("出货单", fileEntity.getSuffix());
            jsonResult.put("fileName", fileName);
            jsonResult.put("url", FileUtils.buildDownload(fileEntity.getPath(), fileName));
        } else {
            jsonResult.modify(false, "没有找到相应数据文件");
        }
        return jsonResult;
    }


    /**
     * 查询打印数据
     *
     * @param object
     * @param request
     * @return
     */
    @RequestMapping(value = "/print/outbound")
    @ResponseBody
    public JsonResult outBoundPrint(@RequestBody RequestObject object, HttpServletRequest request) {
        Collection<Long> outboundIds = StringUtils.longCollection(object.get("outboundIds"));
        Assert.notEmpty(outboundIds, "请至少选择一项需要打印的数据");
        JsonResult jsonResult = new JsonResult();
        jsonResult.put("results", depotOutboundService.listPrintGroup(outboundIds));
        return jsonResult;
    }
}
