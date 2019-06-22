package com.ycg.ksh.api.adventive.controller;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */

import com.google.common.collect.Lists;
import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.common.constant.PartnerType;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.entity.persistent.adventive.Adventive;
import com.ycg.ksh.entity.persistent.adventive.AdventivePull;
import com.ycg.ksh.service.core.entity.service.adventive.AdventiveOrder;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.service.api.CompanyService;
import com.ycg.ksh.service.api.LocationTrackService;
import com.ycg.ksh.service.api.ReceiptService;
import com.ycg.ksh.service.api.adventive.AdventiveService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对外接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/22
 */
@Controller("adventive.api.controller")
@RequestMapping("/adventive")
public class AdventiveController extends BaseController {

    private static final ThreadLocal<SimpleDateFormat> format = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Resource
    LocationTrackService trackService;
    @Resource
    ReceiptService receiptService;
    @Resource
    CompanyService companyService;
    @Resource
    AdventiveService adventiveService;

    private Long accessKey(HttpServletRequest request){
        return Optional.ofNullable(RequestUitl.getValue(request, "qlm-access-key")).map(k -> Long.parseLong(k.toString())).orElse(0L);
    }

    @RequestMapping(value="/admin/list")
    @ResponseBody
    public JsonResult searchs(HttpServletRequest request) throws Exception{
       JsonResult result = new JsonResult();
       result.put("results", adventiveService.list());
        return result;
    }

    @RequestMapping(value="/admin/key/{key}")
    @ResponseBody
    public JsonResult loadByKey(@PathVariable Long key, HttpServletRequest request) throws Exception{
        JsonResult result = new JsonResult();
        result.put("result", adventiveService.loadAdventiveAlliance(key));
        return result;
    }

    @RequestMapping(value="/admin/modify")
    @ResponseBody
    public JsonResult modify(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception{
        JsonResult result = new JsonResult();
        String adventiveString = requestObject.get("adventive");
        Assert.notBlank(adventiveString, "提交信息不能为空");
        Adventive adventive = Globallys.toJavaObject(adventiveString, Adventive.class);

        Assert.notNull(adventive, "配置信息不能为空");

        result.put("result", adventiveService.saveOrUpdate(adventive));
        return result;
    }

    @RequestMapping(value="/admin/pull")
    @ResponseBody
    public JsonResult modifyPull(@RequestBody RequestObject requestObject, HttpServletRequest request) throws Exception{
        JsonResult result = new JsonResult();
        String pullString = requestObject.get("pull");
        Assert.notBlank(pullString, "提交信息不能为空");
        AdventivePull pull = Globallys.toJavaObject(pullString, AdventivePull.class);

        Assert.notNull(pull, "配置信息不能为空");

        result.put("result", adventiveService.saveOrUpdate(pull));
        return result;
    }

    @RequestMapping(value="/order/transport")
    @ResponseBody
    public JsonResult ordertransport(HttpServletRequest request) throws Exception{
        RequestObject requestObject = new RequestObject(request.getParameterMap());
        Collection<AdventiveOrder> collection = Globallys.toJavaObjects(requestObject.get("orderString"), AdventiveOrder.class);
        Assert.notEmpty(collection, "发货单信息不能为空");
        adventiveService.acceptOrders(accessKey(request), collection, PartnerType.SHIPPER);
        return JsonResult.SUCCESS;
    }


    @RequestMapping(value="/order/keys")
    @ResponseBody
    public JsonResult loadOrderByKey(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        String keyString = object.get("keys");
        Assert.notBlank(keyString, "订单编号不能为空");
        Collection<Long> keys = Globallys.toJavaObjects(keyString, Long.class);
        Assert.notEmpty(keys, "订单编号不能为空");
        JsonResult result = new JsonResult();
        result.put("results", adventiveService.listOrderByKeys(accessKey(request), keys));
        return result;
    }

    @RequestMapping(value="/order/deliverys")
    @ResponseBody
    public JsonResult loadOrderByDelivery(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        String deliveryString = object.get("deliverys");
        Assert.notBlank(deliveryString, "发货单编号不能为空");
        Collection<String> deliverys = Globallys.toJavaObjects(deliveryString, String.class);
        Assert.notEmpty(deliverys, "发货单编号不能为空");
        JsonResult result = new JsonResult();
        result.put("results", adventiveService.listOrderDeliveryNos(accessKey(request), deliverys));
        return result;
    }

    @RequestMapping(value="/order/fettle")
    @ResponseBody
    public JsonResult fettle(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        JsonResult result = new JsonResult();
        Long orderKey = object.getLong("orderKey");
        if(orderKey != null && orderKey > 0){
            Map<Long, Integer> fettles = adventiveService.listOrderFettleByKeys(accessKey(request), Lists.newArrayList(orderKey));
            if(fettles != null && !fettles.isEmpty()){
                result.put("results", fettles.get(orderKey));
            }
        }else{
            String deliveryNo = object.get("deliveryNo");
            if(deliveryNo != null && deliveryNo.length() > 0){
                Map<String, Integer> fettles = adventiveService.listOrderFettleByDeliveryNos(accessKey(request), Lists.newArrayList(deliveryNo));
                if(fettles != null && !fettles.isEmpty()){
                    result.put("results", fettles.get(deliveryNo));
                }
            }
        }
        return result;
    }

    @RequestMapping(value="/order/fettles")
    @ResponseBody
    public JsonResult fettles(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        JsonResult result = new JsonResult();
        Collection<Long> keys = Globallys.toJavaObjects(object.get("keys"), Long.class);
        if(keys != null && keys.size() > 0){
            result.put("results", adventiveService.listOrderFettleByKeys(accessKey(request), keys));
        }else{
            Collection<String> deliverys = Globallys.toJavaObjects(object.get("deliverys"), String.class);
            if(deliverys != null && deliverys.size() > 0){
                result.put("results", adventiveService.listOrderFettleByDeliveryNos(accessKey(request), deliverys));
            }
        }
        return result;
    }


    @RequestMapping(value="/order/receipt")
    @ResponseBody
    public JsonResult receipt(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        JsonResult result = new JsonResult();
        Long accessKey = accessKey(request);
        Long orderKey = object.getLong("orderKey");
        String deliveryNo = object.get("deliveryNo");
        String orderNo = object.get("orderNo");
        if(orderKey != null && orderKey > 0){
            result.put("results", adventiveService.listReceiptByKey(accessKey, orderKey));
        }else if(StringUtils.isNotBlank(deliveryNo)){
            result.put("results", adventiveService.listReceiptByDeliveryNo(accessKey, deliveryNo));
        }else if(StringUtils.isNotBlank(orderNo)){
            Map<String, Object> results = new HashMap<String, Object>(2);
            results.put("orderNo", orderNo);
            Collection<Map<String, Object>> _result = new ArrayList<Map<String, Object>>();
            Map<Long, String> map = adventiveService.listDeliveryNoByOrderNo(accessKey, orderNo);
            for (Map.Entry<Long, String> entry : map.entrySet()) {
                Map<String, Object> item = new HashMap<String, Object>(2);
                item.put("deliveryNo", entry.getValue());
                item.put("detail", adventiveService.listReceiptByKey(accessKey, entry.getKey()));
                _result.add(item);
            }
            results.put("result",  _result);
            result.put("results",  results);
        }
        return result;
    }

    @RequestMapping(value="/order/receipts")
    @ResponseBody
    public JsonResult receipts(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        JsonResult result = new JsonResult();
        Collection<Long> keys = Globallys.toJavaObjects(object.get("keys"), Long.class);
        if(keys != null && keys.size() > 0){
            result.put("results", adventiveService.mapReceipts(accessKey(request), keys, true));
        }else{
            Collection<String> deliverys = Globallys.toJavaObjects(object.get("deliverys"), String.class);
            if(deliverys != null && deliverys.size() > 0){
                result.put("results", adventiveService.mapReceipts(accessKey(request), deliverys, false));
            }
        }
        return result;
    }


	/**
	 * 根据订单编号获取订单轨迹信息
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2018-07-19 17:31:55
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value="/order/track")
    @ResponseBody
    public JsonResult track(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        JsonResult result = new JsonResult();
        Long accessKey = accessKey(request);
        Long orderKey = object.getLong("orderKey");
        String deliveryNo = object.get("deliveryNo");
        String orderNo = object.get("orderNo");
        if(orderKey != null && orderKey > 0){
            result.put("results", adventiveService.listTrackByKey(accessKey, orderKey));
        }else if(StringUtils.isNotBlank(deliveryNo)){
            result.put("results", adventiveService.listTrackByDeliveryNo(accessKey, deliveryNo));
        }else if(StringUtils.isNotBlank(orderNo)){
            Map<String, Object> results = new HashMap<String, Object>(2);
            results.put("orderNo", orderNo);
            Collection<Map<String, Object>> _result = new ArrayList<Map<String, Object>>();
            Map<Long, String> map = adventiveService.listDeliveryNoByOrderNo(accessKey, orderNo);
            for (Map.Entry<Long, String> entry : map.entrySet()) {
                Map<String, Object> item = new HashMap<String, Object>(2);
                item.put("deliveryNo", entry.getValue());
                item.put("detail", adventiveService.listTrackByKey(accessKey, entry.getKey()));
                _result.add(item);
            }
            results.put("result",  _result);
            result.put("results",  results);
        }
        return result;
    }

    @RequestMapping(value="/order/tracks")
    @ResponseBody
    public JsonResult tracks(HttpServletRequest request)throws Exception{
        RequestObject object = new RequestObject(request.getParameterMap());
        JsonResult result = new JsonResult();
        Collection<Long> keys = Globallys.toJavaObjects(object.get("keys"), Long.class);
        if(keys != null && keys.size() > 0){
            result.put("results", adventiveService.mapTracks(accessKey(request), keys, true));
        }else{
            Collection<String> deliverys = Globallys.toJavaObjects(object.get("deliverys"), String.class);
            if(deliverys != null && deliverys.size() > 0){
                result.put("results", adventiveService.mapTracks(accessKey(request), deliverys, false));
            }
        }
        return result;
    }


    @RequestMapping(value="/special/sync/note/{key}")
    @ResponseBody
    public JsonResult syncNote(@PathVariable Long key, HttpServletRequest request)throws Exception{
        Assert.notBlank(key, "要推送的记录编号不能为空");
        adventiveService.enforcePushNote(key);
        return JsonResult.SUCCESS;
    }

}
