/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:04:24
 */
package com.ycg.ksh.service.impl;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.entity.RequestSerial;
import com.ycg.ksh.common.exception.BusinessException;
import com.ycg.ksh.common.exception.ParameterException;
import com.ycg.ksh.common.extend.mybatis.page.CustomPage;
import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.entity.persistent.SysRequestSerial;
import com.ycg.ksh.entity.service.PageScope;
import com.ycg.ksh.service.persistence.SysRequestSerialMapper;
import com.ycg.ksh.service.api.SysRequestSerialService;
import is.tagomor.woothee.Classifier;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户请求数据记录接口
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:04:24
 */
@Service("ksh.core.service.sysRequestSerialService")
public class SysRequestSerialServiceImpl implements SysRequestSerialService {

	private static final Pattern PATH_PATTERN = Pattern.compile("(\\/[a-zA-Z-]+)+");

	private final Logger logger = LoggerFactory.getLogger(SysRequestSerialService.class);
	
	@Resource
	SysRequestSerialMapper requestSerialMapper;
	
	/**
	 * @see com.ycg.ksh.service.api.SysRequestSerialService#save(SysRequestSerial)
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-24 09:04:24
	 */
	@Override
	public void save(SysRequestSerial serial) throws ParameterException, BusinessException {
		try {
			if(serial != null) {
				Map<String, String> agents = Classifier.parse(serial.getAgentString());
				if(agents != null) {
					serial.setOs(agents.get("os") +"-"+ agents.get("os_version"));
					serial.setBrowser(agents.get("name") +"-"+ agents.get("version"));
					//r.get("category")
					// => "pc", "smartphone", "mobilephone", "appliance", "crawler", "misc", "unknown"
				}
				serial.setDateTime(new Date());
				if(StringUtils.isNotBlank(serial.getUri())) {
					Matcher matcher = PATH_PATTERN.matcher(serial.getUri());
					if(matcher.find()) {
						serial.setStandardUri(matcher.group());
					}else {
						serial.setStandardUri(serial.getUri());
					}
				}
				serial.setUriKey(DigestUtils.md5Hex(serial.getStandardUri()).toUpperCase());
				requestSerialMapper.insertSelective(serial);
			}
        }catch (Exception e) {
        	logger.error("保存用户请求数据异常 {}", serial, e);
        }
	}
	/**
	 * 
	 * @see com.ycg.ksh.service.api.SysRequestSerialService#queryPageList(SysRequestSerial, PageScope)
	 * <p>
	 * @developer Create by <a href="mailto:58469@ycgwl.com">dangjunqiang</a> at 2017-12-28 15:03:54
	 */
	@Override
	public CustomPage<SysRequestSerial> queryPageList(RequestSerial serial, PageScope pageScope)
			throws ParameterException, BusinessException {
		try {
			Page<SysRequestSerial> page = requestSerialMapper.queryPage(serial, new RowBounds(pageScope.getPageNum(), pageScope.getPageSize()));
			return new CustomPage<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page);
		} catch (Exception e) {
			logger.debug("queryPageList  serial:{}",serial);
			BusinessException.dbException("用户日志查询异常");
		}
		return null;
	}
}
