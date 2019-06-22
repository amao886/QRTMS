package com.ycg.ksh.test;

import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.service.api.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)  
@ActiveProfiles("local")                                     
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestSomething {

	@Resource
	private CustomerService customerService;
	@Test
	public void dotest() throws ParameterException, BusinessException, Exception {
		/*
		{{first.DATA}}
		运单号：{{keyword1.DATA}}
		签收人：{{keyword2.DATA}}
		签收时间：{{keyword3.DATA}}
		{{remark.DATA}}

		TemplateMesssage messsage = new TemplateMesssage("oyzJjv3YHnqG_XZ5rdyMENcoiUJc");
		messsage.addData("first", new TemplateDataValue("配送单132214325aweqw已经签收,请悉知!", "#173177"));
		messsage.addData("keyword1", new TemplateDataValue("32432432432432432", "#173177"));
		messsage.addData("keyword2", new TemplateDataValue("远成集团有限公司杭州分公司", "#173177"));
		messsage.addData("keyword3", new TemplateDataValue(DateUtils.formatDate(new Date(), "yyyy年MM月dd日 HH点mm分"), "#173177"));
		messsage.addData("keyword4", new TemplateDataValue("异常签收", "#FF0000"));
		messsage.addData("remark", new TemplateDataValue("点击详情查看具体信息,感谢使用合同物流管理平台平台", "#173177"));

		messsage.setUrl(FrontUtils.eleReceipt(SecurityTokenUtil.createToken(String.valueOf(2748)), 153935418384384L));

		//messsage.addData("remark", new TemplateDataValue("点击详情可以查看具体信息", "#173177"));
		weChatApiService.sendTemplateMessage(messsage, TemplateType.YCSIGN);
		*/

		customerService.associateCompany(302106014000128L, 287197782008832L);

	}
}
