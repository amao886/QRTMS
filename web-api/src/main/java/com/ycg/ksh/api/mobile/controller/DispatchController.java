package com.ycg.ksh.api.mobile.controller;

import com.ycg.ksh.api.RequestUitl;
import com.ycg.ksh.api.common.controller.BaseController;
import com.ycg.ksh.common.constant.Constant;
import com.ycg.ksh.common.entity.JsonResult;
import com.ycg.ksh.common.entity.RequestObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.util.Assert;
import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.adapter.AutoMapLocation;
import com.ycg.ksh.entity.adapter.baidu.WordInfo;
import com.ycg.ksh.entity.adapter.baidu.WordResult;
import com.ycg.ksh.adapter.api.AutoMapService;
import com.ycg.ksh.adapter.api.BaiduService;
import com.ycg.ksh.entity.persistent.MapAddress;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.service.api.MapAddressService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller("mobile.dispatch.controller")
@RequestMapping("/mobile/dispatch")
public class DispatchController extends BaseController {
	private static final String FORMATTER = "yyyy年MM月dd日";
	private static final Pattern PATTERN = Pattern.compile("^\\d+");
	private static final Pattern PATIPHONE = Pattern.compile("^[\u4e00-\u9fa5a-zA-Z]*+\\d{8,30}");
	private static final Pattern PATCN = Pattern.compile("[\u4e00-\u9fa5]+");
	@Resource
	MapAddressService addressService;
	@Resource
	BaiduService baiduService;
	@Resource
	AutoMapService autoMapService;
	
	Map<String, Collection<String>> CACHE = new ConcurrentHashMap<String, Collection<String>>();
	
	private String cacheKey(Integer userId, Integer addressType) {
		if(addressType == null || addressType <= 0) {
			addressType = Constant.ADDRESS_TYPE_COMMON;
		}
		return userId +"#"+ addressType;
	}
	
	@RequestMapping(value="/recognize/address/{addressType}")
	@ResponseBody
	public JsonResult getAddress(@PathVariable Integer addressType, HttpServletRequest request) throws Exception{
		User user = RequestUitl.getUserInfo(request);
		JsonResult jsonResult = new JsonResult();
		Collection<String> collection = CACHE.get(cacheKey(user.getId(), addressType));
		if(CollectionUtils.isNotEmpty(collection)) {
			jsonResult.put("collection", collection);
		}else {
			jsonResult.modify(false, "请先上传图片识别");
		}
		return jsonResult;
	}
	
	
	@RequestMapping(value="/recognize/image/{addressType}")
	@ResponseBody
	public JsonResult uploadImage(@PathVariable Integer addressType, HttpServletRequest request) throws Exception{
		User user = RequestUitl.getUserInfo(request);
		JsonResult jsonResult = new JsonResult();
		if(request instanceof DefaultMultipartHttpServletRequest) {
			DefaultMultipartHttpServletRequest multipartHttpServletRequest = (DefaultMultipartHttpServletRequest) request;
			Collection<String> collection = new ArrayList<String>();
			for (Map.Entry<String, MultipartFile> entry : multipartHttpServletRequest.getFileMap().entrySet()) {
				MultipartFile multipartFile = entry.getValue();
	            if (FileUtils.isImageByPath(multipartFile.getOriginalFilename())) {// 判断文件类型是否为空
	            	collection(multipartFile.getBytes(), collection);
	            }
			}
			CACHE.put(cacheKey(user.getId(), addressType), collection);
		}else {
			jsonResult.modify(false, "文件类型有误,请重新选择上传!");
		}
		return jsonResult;
	}
	
	@SuppressWarnings("restriction")
	@RequestMapping(value="/base64/image/{addressType}")
	@ResponseBody
	public JsonResult base64(@RequestBody String base64Data, @PathVariable Integer addressType, HttpServletRequest request) throws Exception{
		User user = RequestUitl.getUserInfo(request);
		JsonResult jsonResult = new JsonResult();
		if(StringUtils.isBlank(base64Data)) {
			throw new ParameterException("文件数据解析有误,请重新选择上传!");
		}
		logger.debug("=================微信本地图片base64参数=============================" + base64Data.substring(0, 100));
		base64Data.replaceAll(" ", "+");
		if(base64Data.startsWith("data:image")) {
			int sindex = base64Data.indexOf("/"), eindex = base64Data.indexOf(";"), dindex = base64Data.indexOf(",");
			String suffix = null;
			if(eindex >= 0) {
				suffix = base64Data.substring(sindex + 1, eindex);
			}
			if(!FileUtils.isImage(suffix)) {
				throw new ParameterException(suffix, "文件类型有误,请重新选择上传!");
			}
			base64Data = base64Data.substring(dindex + 1);
		}
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			Collection<String> collection = collection(decoder.decodeBuffer(base64Data), new ArrayList<String>());
			CACHE.put(cacheKey(user.getId(), addressType), collection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	private Collection<String> collection(byte[] bytes, Collection<String> collection){
		try {
			logger.debug("=================={}", bytes.length);
			WordResult wordResult = baiduService.accurateGeneral(bytes);
			logger.debug("识别数量：{}", wordResult.getCount());
			if(wordResult.getCount() == null || wordResult.getCount() <= 0) {
				throw new BusinessException("图片识别失败,请重新上传识别");
			}
			addressData(wordResult.getWordInfos(), collection);
			logger.debug("识别结果：{}", collection);
		} catch (Exception e) {
			logger.debug("图片数据识别异常 size:{}", bytes.length);
			logger.error("图片数据识别异常 ", e);
			throw new BusinessException("图片识别异常", e);
		}
    	return collection;
	}
	
	//组装地址数据
	private void addressData(List<WordInfo> wordInfos, Collection<String> collection){
		logger.debug("识别数据==========：{}", wordInfos.toString());
		for (int i = 0; i < wordInfos.size(); i++) {
		    WordInfo wordInfo = wordInfos.get(i);
            String wordString = wordInfo.getWords();
            if((wordString = filter(wordString)) == null ||filterIphone(wordString)) {
                continue;
            }
            //根据"收"或"件"判断是否是面单
            if(wordString.contains("\u6536") || wordString.contains("\u4EF6")){
                //过滤"收"、"件"
                wordString = wordString.replaceAll("\u6536", "").replaceAll("\u4EF6", "");
                int index =wordString.indexOf("\u8BA1");//截取"计费重量"
                if(index>-1){
                    wordString = wordString.substring(0, index);
                }
                if(wordInfos.size() >= i + 1){
                    for(int j = i + 1; j < wordInfos.size(); j++){
                        String wordstr = wordInfos.get(j).getWords();
                        logger.debug("=======识别数据2==========：{}", wordstr);
                        if(wordstr.indexOf("(") > -1){
                            wordString=wordString+wordstr;
                            break;
                        }
                    }
                }
                collection.add(wordString);
                break;
            }else{
                collection.add(wordString);
            }
        }
	}
	
	//过滤空数据、小于6位数据、不包括中文数据
	private String filter(String wordString) {
		if(StringUtils.isBlank(wordString) || !PATCN.matcher(wordString).find() || StringUtils.length(wordString) < 6) {
			return null;
		}
		wordString = wordString.replaceAll("(null|nul1|nu11|nu|ull|u11|ul1|il|n1|1l|db)", "");
		Matcher m = PATTERN.matcher(wordString);
		if(m.find()) {
			String sss = m.group(0);
			wordString = wordString.substring(sss.length());
		}
		return StringUtils.isBlank(wordString)?null:wordString.trim();
	}
	//判断当前是否包含8-30位数字
	private boolean filterIphone(String wordString) {
		Matcher m = PATIPHONE.matcher(wordString);
		return m.matches();
	}

	@RequestMapping(value="/update/single")
	@ResponseBody
    public JsonResult updateAddress(@RequestBody MapAddress address, HttpServletRequest request) throws Exception {
    	Assert.notNull(address, "编辑对象不能为空地址");
    	User user = RequestUitl.getUserInfo(request);
    	JsonResult result = new JsonResult();
    	if(StringUtils.isNotBlank(address.getAddress())) {
			AutoMapLocation location = autoMapService.coordinate(address.getAddress());
    		if(location != null) {
    	    	convert(location, address);
    			address.setUserId(user.getId());
    	    	result.put("address", addressService.update(address));
    		}else {
    			throw new ParameterException(address, "地址输入有误,无法获取经纬度!");
    		}
    	}else {
	    	result.put("address", addressService.update(address));
    	}
        return result;
    }
	
	@RequestMapping(value="/save/single")
	@ResponseBody
    public JsonResult saveAddress(@RequestBody MapAddress address, HttpServletRequest request) throws Exception {
    	Assert.notNull(address, "地址不能为空地址");
    	Assert.notNull(address.getAddress(), "编辑地址不能为空地址");
    	User user = RequestUitl.getUserInfo(request);
		AutoMapLocation location = autoMapService.coordinate(address.getAddress());
		if(location != null) {
	    	convert(location, address);
	    	address.setUserId(user.getId());
	    	addressService.save(address);
		}else {
			throw new ParameterException(address, "地址输入有误,无法获取经纬度!");
		}
        return JsonResult.SUCCESS;
    }
	
	
	private void convert(AutoMapLocation location, MapAddress mapAddress) {
		mapAddress.setFormatAddress(location.getFormatAddress());
		//mapAddress.setAdcode(location.getAdcode());
		//mapAddress.setProvince(location.getProvince());
		//mapAddress.setCity(location.getCity());
		//mapAddress.setDistrict(location.getDistrict());
		//mapAddress.setStreet(location.getStreet());
		//mapAddress.setNumber(location.getNumber());
		mapAddress.setLatitude(location.getLatitude());
    	mapAddress.setLongitude(location.getLongitude());
	}
	
	@RequestMapping(value="/save/address")
	@ResponseBody
    public JsonResult saveAddress(@RequestBody List<MapAddress> collection, HttpServletRequest request) throws Exception {
    	Assert.notEmpty(collection, "至少包含一个地址");
    	User user = RequestUitl.getUserInfo(request);
    	Collection<MapAddress> mapAddress = new ArrayList<MapAddress>();
    	collection.forEach(address -> {
			AutoMapLocation location = autoMapService.coordinate(address.getAddress());
    		if(location != null) {
    	    	convert(location, address);
    			address.setUserId(user.getId());
    			mapAddress.add(address);
    		}
    	});
    	addressService.save(mapAddress);
    	CACHE.remove(cacheKey(user.getId(), Constant.ADDRESS_TYPE_COMMON));
    	CACHE.remove(cacheKey(user.getId(), Constant.ADDRESS_TYPE_OFTEN));
        return JsonResult.SUCCESS;
    }
	
	@RequestMapping(value="/list")
	@ResponseBody
    public JsonResult listAll(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
    	JsonResult jsonResult = new JsonResult();
    	Integer type = object.getInteger("type");
    	String search = object.get("bodyData");
    	if(StringUtils.isBlank(search)) {
    		search = null;
    	}
    	User user = RequestUitl.getUserInfo(request);
    	Collection<MapAddress> collection = addressService.listByUserKey(user.getId(), type, search);
    	if(CollectionUtils.isNotEmpty(collection)) {
    		Map<String, List<MapAddress>> map = new LinkedHashMap<String, List<MapAddress>>();
    		collection.forEach(address -> {
    			String dateKey = DateUtils.formatDate(address.getModifyTime(), FORMATTER);
				List<MapAddress> list = map.get(dateKey);
				if(list == null) {
					list = new ArrayList<MapAddress>();
					map.put(dateKey, list);
				}
				list.add(address);
    		});
    		jsonResult.put("address", map);
    	}else {
    		jsonResult.put("address", Collections.emptyMap());
    	}
        return jsonResult;
    }
	
	@RequestMapping(value="/planning/{addressType}")
	@ResponseBody
    public JsonResult planning(@RequestBody(required=false) List<Integer> collection, @PathVariable Integer addressType, HttpServletRequest request) throws Exception {
    	logger.debug("==========planning============={}", collection);
		JsonResult jsonResult = new JsonResult();
    	User user = RequestUitl.getUserInfo(request);
    	if(CollectionUtils.isNotEmpty(collection)) {
    		jsonResult.put("address", addressService.listByKeys(user.getId(), collection));
    	}else {
    		jsonResult.put("address", addressService.listByUserKey(user.getId(), addressType, null));
    	}
        return jsonResult;
    }
	
	@RequestMapping(value="/delete")
	@ResponseBody
    public JsonResult delete(@RequestBody List<Integer> collection, HttpServletRequest request) throws Exception {
    	addressService.delete(collection);
        return JsonResult.SUCCESS;
    }
	
	@RequestMapping(value="/package/count")
	@ResponseBody
    public JsonResult packageCount(@RequestBody RequestObject object, HttpServletRequest request) throws Exception {
		addressService.modifyPackageCount(object.getInteger("id"), object.getInteger("count"));
        return JsonResult.SUCCESS;
    }
}
