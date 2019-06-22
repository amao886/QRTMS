package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.entity.FileEntity;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.extend.FileUploadHelper;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.WaybillException;
import com.ycg.ksh.service.api.ExceptionService;
import com.ycg.ksh.service.api.FileService;
import com.ycg.ksh.service.api.ReceiptService;
import com.ycg.ksh.service.api.WaybillService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 客户操作货物进度
 *
 * @author zcl
 */
@Controller("mobile.trace.controller")
@RequestMapping("/mobile/trace")
public class TraceController extends BaseController {

    @Resource
    WaybillService waybillService;
    @Resource
    ReceiptService receiptService;
    @Resource
    ExceptionService exceptionService;
    @Resource
    FileService fileService;
    /**
     * 确认到货
     *
     * @param 任务单id
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/confirmArrive/{waybillid}")
    @ResponseBody
    public JsonResult confirmArrive(@PathVariable Integer waybillid, HttpServletRequest request) throws Exception {
        User user = RequestUitl.getUserInfo(request);
        logger.info("-----------confirmArrive------------" + waybillid, user.getId());
        Collection<Integer> collection = new ArrayList<Integer>(1);
        collection.add(waybillid);
        waybillService.confirmReceive(user.getId(), collection, Constant.CONFIRM_DELIVERY_WAY_BACK);
        return JsonResult.SUCCESS;
    }

    /**
     * 已送达
     *
     * @param 任务单id
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deliver/{waybillid}")
    @ResponseBody
    public JsonResult deliver(@PathVariable Integer waybillid, HttpServletRequest request) throws Exception {
        logger.info("-----------deliver------------" + waybillid);
        //判断用户是否有操作权限
        User user = RequestUitl.getUserInfo(request);
        Collection<Integer> collection = new ArrayList<Integer>(1);
        collection.add(waybillid);
        waybillService.confirmArrive(user.getId(), collection, Constant.CONFIRM_DELIVERY_WAY_BACK);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/receipt/upload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult upload(HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer waybillKey = body.getInteger("waybillKey");
        Assert.notBlank(waybillKey, "请选择任务单后再上传回单");
        User user = RequestUitl.getUserInfo(request);
        Collection<String> collection = FileUploadHelper.saveImage(request, SystemUtils.directoryUpload(), Directory.UPLOAD_RECEIPT.getDir());
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException("请上传一个回单文件!!!");
        }
        receiptService.saveReceipt(user, waybillKey, collection, false);
        return JsonResult.SUCCESS;
    }

    /**
     * 录入异常信息
     *
     * @param object
     * @param request
     * @return
     * @author wangke
     * @date 2018/3/2 10:18
     */
    @RequestMapping(value = "/exception/upload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult enteringException(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer waybillKey = body.getInteger("waybillKey");
        Assert.notBlank(waybillKey, "请选择任务单后再上报异常");
        String content = body.get("content");
        Assert.notBlank(content, "异常内容不能为空");
        User user = RequestUitl.getUserInfo(request);
        WaybillException waybillException = new WaybillException();
        waybillException.setUserid(user.getId());
        waybillException.setWaybillid(waybillKey);
        waybillException.setContent(content);
        Collection<String> collection = new ArrayList<String>();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            if (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    String suffix = FileUtils.suffix(file.getOriginalFilename());
                    if (StringUtils.isBlank(suffix)) {
                        suffix = "jpeg";
                    }
                    if (!FileUtils.isImage(suffix)) {
                        throw new BusinessException("必须是图片文件,请重新选择文件!!!");
                    }
                    String filePath = FileUtils.path("exception", FileUtils.appendSuffix(StringUtils.UUID(), suffix));
                    file.transferTo(FileUtils.file(FileUtils.path(SystemUtils.directoryUpload(), filePath), true));
                    collection.add("/" + filePath);
                }
            }
        }
        exceptionService.saveWaybillException(waybillException, collection, false);
        return JsonResult.SUCCESS;
    }

    @RequestMapping(value = "/import/excel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult waybillImport(HttpServletRequest request) throws Exception {
        RequestObject body = new RequestObject(request.getParameterMap());
        Integer gKey = body.getInteger("groupid"), sendKey = body.getInteger("senderKey");
        logger.info("waybill import -> groupid: {} senderKey:{}", gKey, sendKey);
        //Assert.notNull(gKey, "请选择项目组");
        if(gKey != null && 17 == gKey){//金发项目组
            Assert.notNull(sendKey, "请选择发货方信息");
        }
        User user = RequestUitl.getUserInfo(request);
        FileEntity fileEntity = null;
        File file = FileUploadHelper.saveExcel(request, SystemUtils.directoryTemp());
        if(file != null){
            fileEntity = new FileEntity();
            fileEntity.setSuffix(FileUtils.suffix(file.getName()));
            fileEntity.setDirectory(SystemUtils.directoryTemp());
            fileEntity.setFileName(file.getName());
            fileEntity.setSize(file.length());
        }
        if (fileEntity == null) {
            throw new BusinessException("请上传一个文件!!!");
        }
        String path = fileEntity.getPath();
        try {
            fileEntity = fileService.saveByFile(user.getId(), gKey, sendKey, fileEntity);
        } finally {
            FileUtils.forceDelete(new File(path));
        }
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isNotBlank(fileEntity.getPath())) {
            jsonResult.modify(false, "存在异常数据</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条</br>请下载处理后上传导入");
            jsonResult.put("file", fileEntity.getPath());
            jsonResult.put("count", fileEntity.getCount());
            jsonResult.put("size", fileEntity.getSize());
            jsonResult.put("fileName", fileEntity.getFileName());
            jsonResult.put("url", FileUtils.mobileDownload(fileEntity.getPath(), FileUtils.appendSuffix("导入异常数据", fileEntity.getSuffix()), false));
        } else {
            jsonResult.message("操作成功</br>成功导入"+ fileEntity.getSuccessCount() +"条,失败"+ fileEntity.getFailureCount() +"条");
        }
        return jsonResult;
    }
}

