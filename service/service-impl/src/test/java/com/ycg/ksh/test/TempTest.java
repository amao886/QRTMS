/**
 * TODO Add description
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-07 15:13:55
 */
package com.ycg.ksh.test;


import com.ycg.ksh.common.extend.http.HttpClient;
import com.ycg.ksh.common.system.Globallys;
import com.ycg.ksh.common.util.RandomUtils;
import com.ycg.ksh.common.util.encrypt.RSA;
import com.ycg.ksh.common.util.encrypt.SignUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class TempTest {

	public static void main(String[] args) throws Exception {
        //请求方标识
        String api_access_key = "406908778850304";
	    //对应的私钥
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM7w+AR5+mCnjUavOO3z8AGwcSnR8TjZOHT+LJAqexso6bhKbRD47x3Z2QbDcrdeiRgSBILqieHsSJ/6ajvQQikl3G1uUnBxkjpmMEsXwTkMp9XNu7t70GXAf1q3VYrM7LWd89tTpdp319+pIlM7AonsUhSJ8Hgm8Pwml73nhhXRAgMBAAECgYAWbbmDyrCfTymZsp4J9DlcBYKOVMm/LpPW4kF0MpZ7IBkMt0xQ3ZoU8yp5eC9zvlQ/fpxsj/z3toM8i4h+CRlSaFcfZOLfRuLIUmCyzpRF9rgHr0ue2DsooKORSlV5c87yWEHKLqAFWpp5R42fvtImJEEXB2RmpNA2UwG7ysajwQJBAPf5g6x0fgEO24aOfyk30/JUHeqsw7L4Z1ykQH9tXyCgXCsdvdx6GqIvGo6gOepe9/0lsinyHwTb0dHWbGLBlAkCQQDVo32GiKHAju6r/AA3qUWRMNodQPzoYSvYXqeu7FKyA69c/ZmvK+TvN/cp5rcDzZhoOiRLeH19qxdmz0YKCDWJAkBmJz3dL2gxG2PH/K/Z1QvVCl4ArQc5lSdPfMKtO+syT51ojaoHR63tHiZ8lvkE2fajLIJG0V2B7OR04zZnQIOxAkEAwzlTS80vgrcagy8VQ8f7OmJZ7sGkWvXxCxf0Q3TRXLrhVSipd9m0qqWJx1B9jYwi6Zu37v98LYpbKCfg6On3gQJACXgaPsq7x8GQLRnpsX2TkJylLq7SziJY1hMGqBLMYlrjgT3hNHAjv/xk7r+wmJzv2TF830sg5t6tvSOWTd+LsQ==";

		//标识:279435982718976
		// 随机字符:1533546654747
		// 时间戳:1533546727
		// 签名串:K2Uiag3HZGgGe5dtCMxgMUq3pBN2JrYcr0vJ932qqwhB6CnZbecmJmUuo/RASNFkxxH9bGlkfFnEufdbP5OALrdFKAxa8QnS2vwosuxIqQTee14HXK0CwUMmCfeG+N4jKM/CpPQoIr1IF8fJx0IoefotSFYy1DA+G3zrp9tutuI=

		Long api_timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());//1539584774L;//
		String api_nonce = RandomUtils.string(5);//"NHJNB";//

		//设置校验参数
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("qlm-access-key", api_access_key);
		parameters.put("qlm-timestamp", api_timestamp);
		parameters.put("qlm-nonce", api_nonce);


        //接口测试
		HttpClient client = new HttpClient("http://localhost:8080/adventive/order/receipt", HttpClient.Type.POST);
		/*
		372219260299264	1111111111111
353130276611072	kkjjhhf8768688
353136014238720	kkjjhhf87686666
		*/
		parameters.put("orderNo", "XSDD1807310060");
		//parameters.put("keys", Globallys.toJsonString(new Long[]{334749966050304L, 335258419437568L}));
		//parameters.put("deliverys", Globallys.toJsonString(new String[]{"0031103794","0031103797"}));
		//parameters.put("orderString", "[{\"deliveryNo\":\"FHTZD1806120028\",\"orderNo\":\"\",\"receiveName\":\"杨香琳（山东省茌平县）\",\"receiverName\":\"杨香琳\",\"receiverContact\":\"15806953682\",\"receiveAddress\":\"茌平县隅东路115号\",\"conveyName\":\"远成物流成都分公司\",\"conveyerName\":\"\",\"conveyerContact\":\"\",\"driverName\":\"\",\"driverContact\":\"\",\"careNo\":\"\",\"distributeAddress\":\"四川省射洪县沱牌镇沱牌大道999号\",\"deliveryTime\":\"2018-06-12\",\"arrivalTime\":\"\",\"remark\":\"请用13米以下车送到客户库，大车进不了！！！！\",\"income\":0,\"expenditure\":0,\"originStation\":\"\",\"arrivalStation\":\"\",\"startStation\":\"\",\"endStation\":\"\",\"commodities\":[{\"volume\":0,\"quantity\":10.0000000000,\"boxCount\":0,\"weight\":0,\"commodityUnit\":\"件\",\"remark\":\"78元/瓶\",\"commodityNo\":\"03.04.05.01.001.38.500.06.00\",\"commodityName\":\"生态·沱牌天曲38%vol 500ml×6\"},{\"volume\":0,\"quantity\":2.0000000000,\"boxCount\":0,\"weight\":0,\"commodityUnit\":\"件\",\"remark\":\"86元/瓶\",\"commodityNo\":\"03.04.05.01.001.38.500.06.00\",\"commodityName\":\"生态·沱牌天曲38%vol 500ml×6\"},{\"volume\":0,\"quantity\":116.0000000000,\"boxCount\":0,\"weight\":0,\"commodityUnit\":\"件\",\"remark\":\"110元/瓶\",\"commodityNo\":\"03.04.05.01.001.38.500.06.00\",\"commodityName\":\"生态·沱牌天曲38%vol 500ml×6\"}],\"others\":{}}]");

		String signContext = SignUtil.createLinkString(parameters);

		System.out.println(signContext);
        //生成签名
		String signVal = RSA.sign(signContext, privateKey);

		System.out.println(signVal);
		parameters.put("qlm-sign-val", signVal);

		//parameters.put("orderKey", "242763490009081");

		client.parameter(parameters);

		//请求并打印结果
		System.out.println(client.request());

	}

}
