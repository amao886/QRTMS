/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 20:48:51
 */
package com.ycg.ksh.service.impl;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.rabbitmq.MediaMessage;
import com.ycg.ksh.common.system.FrontUtils;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.system.SecurityTokenUtil;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.common.wechat.WeChatConstant;
import com.ycg.ksh.entity.common.wechat.message.Message;
import com.ycg.ksh.entity.common.wechat.message.MessageBuilder;
import com.ycg.ksh.entity.common.wechat.message.common.TextMessage;
import com.ycg.ksh.entity.common.wechat.message.event.LocationReportEvent;
import com.ycg.ksh.entity.common.wechat.message.event.SubscribeEvent;
import com.ycg.ksh.entity.adapter.wechat.TemplateDataValue;
import com.ycg.ksh.entity.adapter.wechat.TemplateMesssage;
import com.ycg.ksh.entity.adapter.wechat.TemplateType;
import com.ycg.ksh.adapter.api.MessageQueueService;
import com.ycg.ksh.adapter.api.WeChatApiService;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.MapGroup;
import com.ycg.ksh.entity.persistent.MapGroupMember;
import com.ycg.ksh.entity.persistent.User;
import com.ycg.ksh.entity.persistent.UserTrack;
import com.ycg.ksh.entity.service.AuthorizeUser;
import com.ycg.ksh.entity.service.user.UserContext;
import com.ycg.ksh.service.persistence.MapGroupMapper;
import com.ycg.ksh.service.persistence.UserMapper;
import com.ycg.ksh.service.persistence.UserTrackMapper;
import com.ycg.ksh.service.api.WechatService;
import com.ycg.ksh.service.observer.UserObserverAdapter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * 微信相关逻辑
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 20:48:51
 */
@Service("ksh.core.service.wechatService")
public class WechatServiceImpl implements WechatService, UserObserverAdapter {

	private static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");

	private static final String KEY_SPLIT = "#";
	private static final String KEY_CREATE_GROUP = "建组";
	private static final String KEY_ENTER_GROUP = "进组";
	private static final String KEY_LOOK_GROUP = "查组";
	
	@Resource
	UserMapper userMapper;
	
	@Resource
	UserTrackMapper userTrackMapper;
	
	@Resource
	MapGroupMapper mapGroupMapper;

	@Resource
	WeChatApiService apiService;
	@Resource
	MessageQueueService queueService;
	/**
	 * @see com.ycg.ksh.service.api.WechatService#receive(Message)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-12 20:48:51
	 * @param message  微信推送消息
	 * @return  要发送给用户的信息，为空表示不发送任何信息
	 * @throws ParameterException  参数错误时
	 * @throws BusinessException   业务逻辑处理异常时
	 */
	@Override
	public Message receive(Message message) throws ParameterException, BusinessException {
		if(message instanceof TextMessage) {
			return handTextMessage((TextMessage) message);
		} else if(message instanceof LocationReportEvent) {
			return handLocationReportEvent((LocationReportEvent) message);
		}else if(message instanceof SubscribeEvent) {
			return handSubscribeEvent((SubscribeEvent) message);
		}
		return null;
	}
	
	
	private Message handTextMessage(TextMessage text) {
		if(StringUtils.isBlank(text.getContent())) {
			throw new ParameterException("消息内容不能为空!");
		}
		String[] keys = text.getContent().split(KEY_SPLIT);
		if(keys == null || keys.length <= 0) {
			throw new ParameterException("不知道你在说什么-_-");
		}
		User user = userMapper.loadUserByOpenId(text.getFromUserName());
		if(user == null) {
			throw new BusinessException("用户信息尚未初始化!");
		}
		if(KEY_CREATE_GROUP.equals(keys[0])) {
			//新建组
			if(keys.length < 2 || StringUtils.isBlank(keys[1])) {
				throw new BusinessException("请输入一个特别的个性化的组名称\n指令格式为  进组#个性化名称");
			}
			MapGroup group = new MapGroup(keys[1], new Date());
			if(mapGroupMapper.insert(group) > 0) {
				mapGroupMapper.insertMember(new MapGroupMember(group.getId(), user.getId(), 1));
				text.setContent("新建组[ "+ group.getGroupName() +" ]编号为 [ "+ group.getId() +" ]\n 发送 进组#"+group.getId()+" 即可加入该组");
				return text;
			}else {
				throw new BusinessException("建组失败!");
			}
		}else if(KEY_ENTER_GROUP.equals(keys[0])) {
			//进入组
			if(keys.length < 2 || StringUtils.isBlank(keys[1])) {
				throw new BusinessException("请输入一个组编号\n指令格式为  进组#组编号");
			}
			Integer groupId = Integer.parseInt(keys[1]);
			if(mapGroupMapper.getMember(groupId, user.getId()) != null) {
				throw new BusinessException("你已经加入改组了,无需重复加入!");
			}
			MapGroup group = mapGroupMapper.selectByPrimaryKey(groupId);
			if(group != null) {
				mapGroupMapper.insertMember(new MapGroupMember(group.getId(), user.getId(), 2));
				text.setContent("加入组[ "+ group.getGroupName() +" ]编号为 [ "+ group.getId() +" ]\n 与该组成员共享所有地址");
				return text;
			}else {
				throw new BusinessException("组编号错误,确认后重新进组!");
			}
		}else if(KEY_LOOK_GROUP.equals(keys[0])) {
			//进入组
			Collection<MapGroup> collection = mapGroupMapper.listByUserKey(user.getId());
			if(collection != null && !collection.isEmpty()) {
				StringBuilder builder = new StringBuilder("你已加入的组:\n");
				collection.forEach(g -> {
					builder.append("名称:").append(g.getGroupName()).append(" 编号:").append(g.getId()).append("\n");
				});
				text.setContent(builder.toString());
			}else {
				text.setContent("你还没有加入组!");
			}
			return text;
		}else {
			throw new BusinessException("你还有999件包裹没送,还有时间闲聊啊^_^,真替你着急!");
		}
	}
	
	private Message handLocationReportEvent(LocationReportEvent location) {
		if(StringUtils.isNotBlank(location.getLatitude()) && StringUtils.isNotBlank(location.getLongitude())) {
			try {
				User user = userMapper.loadUserByOpenId(location.getFromUserName());
				if(user != null) {
					userTrackMapper.insert(new UserTrack(user.getId(), location.getLatitude(), location.getLongitude(), new Date()));
				}
			} catch (Exception e) {
				logger.error("handLocationReportEvent -> location:{}", location, e);
			}
		}
		return null;
	}
	
	private Message handSubscribeEvent(SubscribeEvent subscribe) {
		try {
			String type = subscribe.subscribe() ? WeChatConstant.SUBSCRIBE_OK : WeChatConstant.SUBSCRIBE_UN;
			userMapper.modifySubscribe(subscribe.getFromUserName(), type);
		} catch (Exception e) {
			logger.error("handSubscribeEvent -> subscribe:{}", subscribe, e);
		}
		return null;
	}

	@Override
	public void receive(String xmlString) throws ParameterException, BusinessException {
		try {
			Message message = MessageBuilder.build().create(xmlString);
			logger.debug("解析后的消息:{}", message);
			if(message instanceof TextMessage){
				queueService.sendNetMessage(new MediaMessage(message.getMsgId(), message));
			}else{
				queueService.sendNetMessage(new MediaMessage(message.getMsgId(), "暂不支持该类消息哦"));
			}
		} catch (Exception e) {
			logger.error("微信推送消息解析处理异常", e);
		}
	}

	/**
	 * 用户事件通知
	 * <p>
	 *
	 * @param type          事件类型
	 * @throws BusinessException
	 */
	@Override
	public void notifyUserChange(AuthorizeUser user, Integer type, UserContext context) throws BusinessException {
		if(StringUtils.isNotBlank(user.getUsername())){
			try{
                MessageBuilder builder = MessageBuilder.build();
                String openId = user.getUsername();
                if (CoreConstants.USER_REGISTER - type == 0) {//用户注册
					/*
					{{first.DATA}}
					用户：{{keyword1.DATA}}
					时间：{{keyword2.DATA}}
					{{remark.DATA}}
					*/
					TemplateMesssage messsage = new TemplateMesssage(user.getUsername(), TemplateType.REGISTER);
					messsage.addData("first", new TemplateDataValue("当前微信号注册成为合同物流管理平台平台用户", "#173177"));
					messsage.addData("keyword1", new TemplateDataValue(user.getUnamezn(), "#173177"));
					messsage.addData("keyword2", new TemplateDataValue(LocalDateTime.now().format(DATETIMEFORMATTER), "#173177"));
					messsage.addData("remark", new TemplateDataValue("感谢使用合同物流管理平台平台", "#173177"));
					messsage.setUrl(FrontUtils.personalAuth(SecurityTokenUtil.createToken(user.getId().toString()), user.getId()));

					queueService.sendNetMessage(new MediaMessage(Globallys.UUID(), messsage));
                } else if (CoreConstants.USER_BINDMOBILEPHONE - type == 0) {//用户绑定手机号
                    /*
					{{first.DATA}}
					姓名：{{keyword1.DATA}}
					手机号：{{keyword2.DATA}}
					时间：{{keyword3.DATA}}
					{{remark.DATA}}
					*/
					TemplateMesssage messsage = new TemplateMesssage(user.getUsername(), TemplateType.BINDMOBILE);
					messsage.addData("first", new TemplateDataValue("您刚刚使用手机号绑定了合同物流管理平台平台用户", "#173177"));
					messsage.addData("keyword1", new TemplateDataValue(user.getUnamezn(), "#173177"));
					messsage.addData("keyword2", new TemplateDataValue(user.getMobilephone(), "#173177"));
					messsage.addData("keyword3", new TemplateDataValue(LocalDateTime.now().format(DATETIMEFORMATTER), "#173177"));
					messsage.addData("remark", new TemplateDataValue("感谢使用合同物流管理平台平台", "#173177"));
					messsage.setUrl(FrontUtils.index(user.getToken()));

					queueService.sendNetMessage(new MediaMessage(Globallys.UUID(), messsage));
                } else if (CoreConstants.USER_LOGIN - type == 0) {//用户登陆(PC端登陆)
                    if(context.getClientType() != null && CoreConstants.LOGIN_CLIENT_WX - context.getClientType() != 0){
                    /*
					{{first.DATA}}
					登录用户：{{keyword1.DATA}}
					登录平台：{{keyword2.DATA}}
					登录网址：{{keyword3.DATA}}
					登录IP：{{keyword4.DATA}}
					登录时间：{{keyword5.DATA}}
					{{remark.DATA}}
					*/
                        String ltype =StringUtils.EMPTY;
                        switch (context.getLoginType()){
                            case CoreConstants.LOGIN_TYPE_SMS:
                                ltype = "使用短信";
                                break;
                            case CoreConstants.LOGIN_TYPE_ACCOUNT:
                                ltype = "使用账号密码";
                                break;
                            case CoreConstants.LOGIN_TYPE_SCAN:
                                ltype = "使用微信扫码";
                                break;
                            case CoreConstants.LOGIN_TYPE_WX:
                                ltype = "使用微信授权";
                                break;
                            case CoreConstants.LOGIN_TYPE_DEBUG:
                                ltype = "调试";
                                break;
                        }
						String name, host ;
                        if(StringUtils.equalsIgnoreCase(context.getEnv(), "prod")){
							name = "平台";
							host = "www.wq5000.xyz";
						}else  if(StringUtils.equalsIgnoreCase(context.getEnv(), "uat")){
							name = "测试环境";
							host = "ksh.wq5000.com";
						}else{
							name = "开发环境";
							host = "51rong.51vip.biz";
						}
						TemplateMesssage messsage = new TemplateMesssage(user.getUsername(), TemplateType.LOGIN);
                        messsage.addData("first", new TemplateDataValue("您刚刚"+ ltype +"登陆了合同物流管理平台"+ name, "#173177"));
                        messsage.addData("keyword1", new TemplateDataValue(user.getUnamezn(), "#173177"));
                        if(CoreConstants.LOGIN_CLIENT_WX - context.getClientType() == 0){
                            messsage.addData("keyword2", new TemplateDataValue("微信端", "#173177"));
                        }else if(CoreConstants.LOGIN_CLIENT_SMS - context.getClientType() == 0){
                            messsage.addData("keyword2", new TemplateDataValue("PDA手持设备", "#173177"));
                        }else{
                            messsage.addData("keyword2", new TemplateDataValue("电脑端", "#173177"));
                        }
                        messsage.addData("keyword3", new TemplateDataValue(host, "#173177"));
                        messsage.addData("keyword4", new TemplateDataValue(Optional.ofNullable(context.getClientHost()).orElse(StringUtils.EMPTY), "#173177"));//登录IP
                        messsage.addData("keyword5", new TemplateDataValue(LocalDateTime.now().format(DATETIMEFORMATTER), "#173177"));
                        messsage.addData("remark", new TemplateDataValue("感谢使用合同物流管理平台平台", "#173177"));
                        messsage.setUrl(FrontUtils.index(user.getToken()));

                        queueService.sendNetMessage(new MediaMessage(Globallys.UUID(), messsage));
                    }
				}
            }catch (Exception e){
                logger.error("用户行为发送微信消息异常 -> {}", e.getMessage(), e);
            }
		}
	}
}

