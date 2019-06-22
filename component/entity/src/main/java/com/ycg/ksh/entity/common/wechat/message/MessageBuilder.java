package com.ycg.ksh.entity.common.wechat.message;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.util.XmlUtils;
import com.ycg.ksh.entity.common.wechat.WeChatConstant;
import com.ycg.ksh.entity.common.wechat.message.common.*;
import com.ycg.ksh.entity.common.wechat.message.event.CommonEvent;
import com.ycg.ksh.entity.common.wechat.message.event.LocationReportEvent;
import com.ycg.ksh.entity.common.wechat.message.event.OrdinaryEvent;
import com.ycg.ksh.entity.common.wechat.message.event.SubscribeEvent;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MessageBuilder {
	
	private enum EnumObjectMessageBuilder{  
        
    	SINGLETON_OBJECT;  
          
        private MessageBuilder instance;  
          
        private EnumObjectMessageBuilder(){
            instance = new MessageBuilder();  
        }  
        public MessageBuilder getInstance(){  
        	return instance;  
        }
    }
	
	/**
	 * 创建一个新的 私有的 MessageBuilder实例. 
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-09-01 10:18:25
	 */
	private MessageBuilder() {
		super();
	}

	public static MessageBuilder build() {
		return EnumObjectMessageBuilder.SINGLETON_OBJECT.getInstance();
	}
	
	public Message create(String xmlString) throws BusinessException{
		Element element = XmlUtils.root(xmlString);
		try {
			Message message = null;
			String msgType = XmlUtils.childText(element, "MsgType");
			if(StringUtils.equals(WeChatConstant.MSGTYPE_EVENT, msgType)){
				message = buildCommonEvent(element);
			}else{
				message = buildCommonMessage(element);
			}
			String createTime = XmlUtils.childText(element, "CreateTime");
			if(StringUtils.isNotBlank(createTime)){
				message.setCreateTime(Long.parseLong(createTime));
			}
			message.setFromUserName(XmlUtils.childText(element, "FromUserName"));
			message.setToUserName(XmlUtils.childText(element, "ToUserName"));
			message.setMsgType(msgType);
			message.setMsgId(XmlUtils.childText(element, "MsgId"));
			return message;
		} catch (Exception e) {
			throw new BusinessException("微信消息解析异常, " + xmlString);
		}
	}
	
	private CommonEvent buildCommonEvent(Element element) {
		CommonEvent eventmsg = null;
		String event = XmlUtils.childText(element, "Event");
		if(StringUtils.equals(WeChatConstant.MSGEVENT_LOCATION, event)) {
			eventmsg = buildLocationReportEvent(element);
		}else if(StringUtils.equals(WeChatConstant.MSGEVENT_CLICK, event) ||
				StringUtils.equals(WeChatConstant.MSGEVENT_VIEW, event)){
			eventmsg = buildOrdinaryEvent(element);
		}else {
			eventmsg = buildSubscribeEvent(element);
		}
		eventmsg.setEvent(XmlUtils.childText(element, "Event"));
		return eventmsg;
	}
	
	private LocationReportEvent buildLocationReportEvent(Element element) {
		LocationReportEvent event = new LocationReportEvent();
		event.setLatitude(XmlUtils.childText(element, "Latitude"));
		event.setLongitude(XmlUtils.childText(element, "Longitude"));
		event.setPrecision(XmlUtils.childText(element, "Precision"));
		return event;
	}
	private OrdinaryEvent buildOrdinaryEvent(Element element) {
		OrdinaryEvent event = new OrdinaryEvent();
		event.setEventKey(XmlUtils.childText(element, "EventKey"));
		return event;
	}
	private SubscribeEvent buildSubscribeEvent(Element element) {
		SubscribeEvent event = new SubscribeEvent();
		String eventKey = XmlUtils.childText(element, "EventKey");
		int spilt_index = eventKey.indexOf("_");
		if(spilt_index >= 0){
			event.setEventKey(eventKey.substring(0, spilt_index));
			event.setParameter(eventKey.substring(spilt_index + 1));
		}else {
			event.setEventKey(eventKey);
		}
		event.setTicket(XmlUtils.childText(element, "Ticket"));
		return event;
	}
	
	private CommonMessage buildCommonMessage(Element element) {
		CommonMessage commonmsg = null;
		String msgType = XmlUtils.childText(element, "MsgType");
		if(StringUtils.equals(WeChatConstant.MSGTYPE_IMAGE, msgType)){
			commonmsg = buildImageMessage(element);
		}else if(StringUtils.equals(WeChatConstant.MSGTYPE_VOICE, msgType)){
			commonmsg = buildVoiceMessage(element);
		}else if(StringUtils.equals(WeChatConstant.MSGTYPE_VIDEO, msgType) || 
				StringUtils.equals(WeChatConstant.MSGTYPE_SHORTVIDEO, msgType)){
			commonmsg = buildVideoMessage(element);
		}else if(StringUtils.equals(WeChatConstant.MSGTYPE_LINK, msgType)){
			commonmsg = buildLinkMessage(element);
		}else if(StringUtils.equals(WeChatConstant.MSGTYPE_LOCATION, msgType)){
			commonmsg = buildLocationMessage(element);
		}else {
			commonmsg = buildTextMessage(element);
		}
		commonmsg.setMsgId(XmlUtils.childText(element, "MsgId"));
		return commonmsg;
	}
	
	
	private CommonMessage buildTextMessage(Element element) {
		return new TextMessage(XmlUtils.childText(element, "Content"));
	}
	private CommonMessage buildImageMessage(Element element) {
		ImageMessage message = new ImageMessage();
		message.setPicUrl(XmlUtils.childText(element, "PicUrl"));
		message.setMediaId(XmlUtils.childText(element, "MediaId"));
		return message;
	}
	private CommonMessage buildVoiceMessage(Element element) {
		VoiceMessage message = new VoiceMessage();
		message.setMediaId(XmlUtils.childText(element, "MediaId"));
		message.setFormat(XmlUtils.childText(element, "Format"));
		message.setRecognition(XmlUtils.childText(element, "Recognition"));
		return message;
	}
	private CommonMessage buildVideoMessage(Element element) {
		VideoMessage message = new VideoMessage();
		message.setMediaId(XmlUtils.childText(element, "MediaId"));
		message.setThumbMediaId(XmlUtils.childText(element, "ThumbMediaId"));
		return message;
	}
	private CommonMessage buildLocationMessage(Element element) {
		LocationMessage message = new LocationMessage();
		message.setX(XmlUtils.childText(element, "Location_X"));
		message.setY(XmlUtils.childText(element, "Location_Y"));
		message.setScale(XmlUtils.childText(element, "Scale"));
		message.setLabel(XmlUtils.childText(element, "Label"));
		return message;
	}
	private CommonMessage buildLinkMessage(Element element) {
		LinkMessage message = new LinkMessage();
		message.setTitle(XmlUtils.childText(element, "Title"));
		message.setDescription(XmlUtils.childText(element, "Description"));
		message.setUrl(XmlUtils.childText(element, "Url"));
		return message;
	}
	
	public String textMessage(String openid, String content){
		return "{\"touser\":\""+ openid + "\", \"msgtype\":\""+ WeChatConstant.MSGTYPE_TEXT +"\",\"text\":{\"content\":\""+ content + "\"}}";
	}
	
	public String createGroup(String groupName){
		return "{\"group\":{\"name\":\""+ groupName + "\"}}";
	}
	
	public String modifyGroupMember(String openid, Long to_groupid){
		return "{\"openid\":\""+ openid+ "\",\"to_groupid\":"+ to_groupid+ "}";
	}
	
	/**
	 * 解析推送XML
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public String xmlMessage(InputStream in) throws BusinessException{
		ByteArrayOutputStream os = null;  
	    try {
	    	os = new ByteArrayOutputStream();
	    	byte[] buffer = new byte[1024];  
		    int len = -1;  
		    while ((len = in.read(buffer)) != -1) {  
		    	os.write(buffer, 0, len);  
		    }  
		    return os.toString();
		}catch (Exception e) {
			throw new BusinessException("微信消息读取异常", e);
		}finally{
			try {
				if(null != os){ os.close(); os = null; }
			} catch (Exception e2) { }
		}
	}
	
	
	
	public static void main(String[] args) {
		String xml ="<xml><ToUserName><![CDATA[gh_ac64a04ed9b8]]></ToUserName>\r\n" + 
				"<FromUserName><![CDATA[oyzJjv3YHnqG_XZ5rdyMENcoiUJc]]></FromUserName>\r\n" + 
				"<CreateTime>1506572635</CreateTime>\r\n" + 
				"<MsgType><![CDATA[text]]></MsgType>\r\n" + 
				"<Content><![CDATA[@kk]]></Content>\r\n" + 
				"<MsgId>6470680196803132811</MsgId>\r\n" + 
				"</xml>";
		
		Message message = MessageBuilder.build().create(xml);
		System.out.println(message);
	}
	
}
