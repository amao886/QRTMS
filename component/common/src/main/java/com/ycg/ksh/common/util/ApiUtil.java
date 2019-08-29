package com.ycg.ksh.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.FileUtils;
import com.ycg.ksh.common.util.HttpUtils;
import com.ycg.ksh.common.util.IOUtils;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.common.util.encrypt.MD5;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URLEncoder;

public class ApiUtil {

    public static void main(String[] args) throws IOException {
        //String token = "D749F60CD78AD057CDC87CE27DECB5EC28011A440CC8120B20F64B857B4D9A673A0714D5BE72D16260AA7D0E91F33EEC11FAE723C31B907CB04FB2E864955846FEB05788BFB8918AF540555973080D201E0A750AB083854123594BCC01225F03C22CC7FF141C4D15";
        //HttpClient httpClient = new HttpClient();
        //httpClient.property("token", token);

		/*
		httpClient.setUrlString("http://localhost/mobile/mine/smsCode/15201748774");
		String result = httpClient.json();

		System.out.println(httpClient);
			*/

        //httpClient.setUrlString("http://localhost/mobile/wayBill/group/search");
		/*
		httpClient.parameter("size", 20);
		httpClient.parameter("num", 1);
		Waybill waybill = new Waybill();

	    //private String bindStartTime;// 绑定开始时间 (不映射数据库)
	   // private String bindEndTime; //绑定结束时间(不映射数据库)

		waybill.setBindStartTime("2017-08-01");
		waybill.setBindEndTime("2017-08-31");
		httpClient.parameter("waybill", waybill);
		 */
        //httpClient.json();
        //System.out.println(httpClient);

        //创建公众号菜单
        //prod : 生产环境，test:测试环境，dev:开发环境，其他值为私人测试号
        try {
            ApiUtil.build("prod");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void build(String env) throws Exception {
        String host = null, appid = null, secret = null;
        if("prod".equals(env)) {
            System.out.println("创建生产环境菜单");
            host = "http://www.lehan-tech.com";
            appid = "wxde218922b232741f";
            secret = "41c6a697695a3acc13b88a8056cb9105";
        }else if("test".equals(env)) {
            System.out.println("创建测试环境菜单");
            host = "http://51rong.51vip.biz";
            appid = "wxb7df6f7ff3599fa8";
            secret = "60953f1f9b39dcb3707cef93741f6619";
        }else if("dev".equals(env)) {
            System.out.println("创建开发环境菜单"+env);
            host = "http://www.lehan-tech.com";
            appid = "wxb7df6f7ff3599fa8";
            secret = "60953f1f9b39dcb3707cef93741f6619";
        }else {
            System.out.println("创建其他环境菜单");
            host = "http://www.fty628.top";
            appid = "wxd6789c8b5c73d0b1";
            secret = "7a86338f9d9d587294e0e78c7df59993";
        }

        //System.out.println(Api.getToken(appid, secret));
        ApiUtil.create2(appid, secret, host);
		/*
		SystemUtils.put(SystemKey.SYSTEM_FILE_ROOT, "D:/workspace/backup/temp");
		String token = getToken("wx2823e2b931352af4", "5c2314f736c4f23c158b8c9f64790ed2");
		String[] mids = new String[]{
                "pkD7ZENYoGZ-EkUEbh_QggZPmRUxwtOWKEhnauEBBLAcM7yBIjppglurVL72Ey7p",
                "3WqJOgbFAZvxqjdO6CbBizKaT3c0Dim2S4ouar6UG8rFnLy97fuujov_mWuYTWJ8",
                "ZPPg_nsX4_EtVU8tL-Dr_kHG2hCcv0peYx7ym7JQofu26cPg5GcDlCneFgLdojZB",
                "mmRaurv2ljeyDaTeQWAtrLtgkbjEHCPsPZs0eT7w5oKGIlML4KUD5jhsa1LBGjf2",
                "iqgYb5ZR5dNebEpC1aiW9ZN1jcShfIKqAtqo-544-P0sMiRa5MiKLZAh11kicHPI",
                "pHiYNNVQwZ5fNlVxYMfx2FOa-kl35Qjj9W-bNsFteuNbITMAu8PSCNEUg5p5-DyL",
                "t4scDGpTTGz1mZsYLAMeLCwXuyP6EPC2XG6Z5FAyBBtqR_ywczU2kXyuR16g-dP9",
                "dIhgMu57_o8KEPs6L27umh1D2Vy6T5hGAQHjn5egyXItYMB4wSKuCajbF4Ab75fU",
                "gdKU9x-vgAG9rPPfRSCgc_gPhDflMFWv5FKm6Jg-pDz3b5kVctdzJx_B-s_myCCB",
                "oIv7X3WmyNKMLmv4TmYyADOqIfX9uHWnLYkyUPAenTVS8bVcABm1cgPX6xxwdeUZ",
                "-beMerOmfzEzSL-yIF6dRSBMHiLO8431afQ8p0xJttCeQgXefp_VGuJsc2085N8F",
                "16Ul53EhotsoOOQvKkEWSWu-vV5RNTv1E6R_MNZQ_2mcgNXwaMe9ptcSxAhEduDd",
                "eU28iOnqk28TComR7pozthTwwPi-fW6y5PXXFnW3Lg9p3RqWLxFcV1DXNmollvju",
                "WW0mFIfyoDTtl4DZ2EqMb1F6GdPdcSS9YMAYrayHUPHBlti3WTzrDKTDP9VMuK-h",
                "_jbHaceUDKQZtYG0s9S13tFLLjk0O34R9DXyEooT8uShfhKdeT8Ut2HduOYFN8L0",
                "X5YyGQQUBg2LGzOAfidFGgBwt49NXmQA61dWLd7LBjbTPgCObtMJx0ID5xHm38oV",
                "HWpXmRkwyjLI-0EGIve1oEhD90qE8U7CZRC8tYyrQPWPuiLN0NZOLMoI1y5kk2ga",
                "Z-YyhH0-Qdu5XwCDz_aiuh-L6-d9hO4vF-qUBLnfUA8eQVBcfbHT2mGFdrbKvBhR",
                "QpQsrc7vJn0s4V04btcqgPBQFEih8Blcd5Gr0hoaX8sQCdTrCJyKt48HbcgxAsEL",
                "qz5HO2pvFPKjCB27vSb_vynY-Rj-rtKZssDINxx2n4E0Zs6ea8b7e9E03_cE05aA",
                "j8QnP0VQl11GVkuGCT6dgomENvnDXDj0ougRrecdI-dgs2-XjATvBfSBGcS7TEG7",
                "jDXRDHcmefz3RdCow1VIyX_TLrW7VN3uy3DMVYs2jd0Ae1NasU643NRYJHSYEPc3",
                "w20EjITcaXY_yjJJjraUfKHu1Fhdd6QSsbNNLVSsMI8Ucgnpd0S36CWfV2geJIRj",
                "4eij86jJv6TKNengfclZMJEqbcsgCcid2j9q8BGl4ShyjWeXJUpvOmaT576h6SS1",
                "hYKJmSTbFfZ_S5gjnr0BBYWES83fpZGwuDHBhrJqjXZyF20_7gSWwL19ddl5ecx4",
                "MIbreSqmCAM94E_Dh3LbPLPT6rtt3PZ1wgPTRjViJXt9sdvtXdfncVcC_qiQ6E4t",
                "nKXICgkMm9gWPAnQ1xkx2DbvQ9IL6fbBcIgJolffvWs441l8luPzWzPd9X08wyQG",
                "KsYc9I0SGyIAmIhC4udP6Y84TU6eC_SBLiYf_WFi8MUd1VIvoPAOqYXzfGoFcka1",
                "uIOKItVgZRzCuSzVP3VAY1XixIyFPSiobDpNFxn3JURGhdEda0XK3eAP9WTEql67",
                "2eBTWmReRCfPZpnh6ZzlADo6epEf60U8mFUngTIGuuioDRXKq2_u6lidQXhvpS1-",
                "1237378768e7q8e7r8qwesafdasdfasdfaxss111",
                "7W0seomYmMdbb6sE9M3Z9kZQY9INSRDaVCYNAkp90Y1h9v1ogmppVE6cmhm-i2q-",
                "jV5lSK-TICUDM_wsEZZRZ2FmOh8SuhwlAS4FCL95hY9T9p5XIwZ5Klk-UjwErS8f"
        };
        for (String mid : mids) {
            Api.download(token, mid, "wx", 3);
        }
        */
    }

    public static void create2(String appid, String secret, String host) throws Exception{
        String token = getToken(appid, secret);
        System.out.println(token);
        String callback= host+"/mobile/wechat/auth";
        try {
            callback = URLEncoder.encode(callback, "utf-8");
        } catch (UnsupportedEncodingException e1) { }
        String index = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ appid +"&redirect_uri="+ callback +"&response_type=code&scope=snsapi_base&state=INDEX#wechat_redirect";
        //String index = "http://www.wq5000.xyz/wechat/home.html";
        String center = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ appid +"&redirect_uri="+ callback +"&response_type=code&scope=snsapi_base&state=CENTER#wechat_redirect";
        //String center = "http://www.wq5000.xyz/wechat/view/user/peopleCenter.html";
        String str = "{\"button\": ["
                + "{\"type\":\"view\",\"name\":\"首页\",\"url\":\"" + index + "\"},"
                + "{\"type\":\"scancode_push\",\"name\":\"扫描\",\"key\":\"rselfmenu_0_1\"},"
                + "{\"type\":\"view\",\"name\":\"我的\",\"url\":\"" + center + "\"}"
                + "]}";
        String result = HttpUtils.post("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ token, str);
        System.out.println(result);
    }

    public static void create(String appid, String secret, String host) throws Exception{
        String token = getToken(appid, secret);
        System.out.println(token);
        String consultation= host+"/weixin/weixinAuth.html?type=9";//首页
        String aboutUs = host+"/weixin/mine.html?type=7";//我的
        String str = "{\"button\": ["
                + "{\"type\":\"view\",\"name\":\"首页\",\"url\":\"" + consultation + "\"},"
                + "{\"type\":\"scancode_push\",\"name\":\"扫描\",\"key\":\"rselfmenu_0_1\"},"
                + "{\"type\":\"view\",\"name\":\"我的\",\"url\":\"" + aboutUs + "\"}"
                + "]}";
        String result = HttpUtils.post("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ token, str);
        System.out.println(result);
    }


    private static String getToken(String appid, String secret) throws Exception {

        String result = HttpUtils.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret);
        JSONObject node = JSON.parseObject(result);
        String access_token = node.getString("access_token");
        return access_token;
    }

    private static String url(String baseUrl, String subUrl){
        if(baseUrl.endsWith("/")){
            return baseUrl + subUrl;
        }
        return baseUrl +"/"+ subUrl;
    }

    private static String download(String accessToken, String mediaId, String parent, int maxCount) {
        String filePath = null;
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id=" + mediaId;
        CloseableHttpClient httpclient = null;
        try{
            httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                String suffix = null;
                Header header = response.getLastHeader("Content-Type");
                if(header != null) {
                    suffix = StringUtils.trim(header.getValue().substring(6));//image/gif  image/jpeg  image/png
                }
                if(!FileUtils.isImage(suffix)) {
                    suffix = "jpeg";
                }
                HttpEntity entity = response.getEntity();
                filePath = FileUtils.path(parent, FileUtils.appendSuffix(MD5.encrypt(mediaId), suffix));
                File desc = FileUtils.file(FileUtils.path(SystemUtils.directoryUpload(), filePath), true);
                InputStream is = null;
                OutputStream os = null;
                try{
                    is = entity.getContent();
                    os = new FileOutputStream(desc);
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    os.flush();
                    //StreamUtils.copy(is, os);
                }catch(Throwable e){
                    System.out.println("从微信服务器下载多媒体文件异常:"+ mediaId);
                    throw e;
                }finally {
                    IOUtils.closeQuietly(is, os);
                }
                System.out.println("downimage -> "+ mediaId +" to "+ desc);
            }
        } catch(Throwable e){
            if(maxCount <= 0) {
                throw new BusinessException("从微信服务器下载多媒体文件异常 :" + mediaId, e) ;
            }else {
                return download(accessToken, mediaId, parent, --maxCount);
            }
        }finally {
            if(httpclient != null){
                IOUtils.closeQuietly(httpclient);
            }
        }
        return "/"+ filePath;
    }
}