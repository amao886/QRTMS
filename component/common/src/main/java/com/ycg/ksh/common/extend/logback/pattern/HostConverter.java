package com.ycg.ksh.common.extend.logback.pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 打印日志时获取当前主机的IP
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:32:09
 */
public class HostConverter extends ClassicConverter {
	
	private String Local_IP;

	@Override
	public String convert(ILoggingEvent event) {
		return getLocalIP();
	}
	
	/**
	 * 判断是windows系统还是linux系统
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:32:47
	 * @return
	 */
	public boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if(StringUtils.isNotBlank(osName)){
			if (osName.toLowerCase().indexOf("windows") > -1) {
				isWindowsOS = true;
			}
		}
		return isWindowsOS;
	}

	/**
	 * 获取本机ip地址
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:33:43
	 * @return 本机IP地址
	 */
	public String getLocalIP() {
		if (Local_IP == null || Local_IP.length() <= 0) {
			synchronized (this) {
				InetAddress address = getInetAddress();
				if(address != null){
					Local_IP = address.getHostAddress();
					if(Local_IP == null || Local_IP.length() <= 0){
						String[] items = address.toString().split("/");
						if(items.length > 1){
							Local_IP = items[1];
						}
					}
				}
			}
		}
		return Local_IP;
	}
	
	/**
	 * 自动区分系统类型，并获取网络地址
	 * <p>
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:34:06
	 * @return 没有获取到则返回null
	 */
	public InetAddress getInetAddress() {
		try {
			if (isWindowsOS()) {// 如果是Windows操作系统
				return InetAddress.getLocalHost();
			} else {// 如果是Linux操作系统
				Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					NetworkInterface ni = netInterfaces.nextElement();
					// 遍历所有ip,特定情况，可以考虑用ni.getName判断
					if (ni.getName().contains("et") || ni.getName().contains("en")) {//ni.getName().contains("eth") || ni.getName().contains("enp")
						Enumeration<InetAddress> ips = ni.getInetAddresses();
						while (ips.hasMoreElements()) {
							InetAddress address = (InetAddress) ips.nextElement();
							if (address.isSiteLocalAddress() && !address.isLoopbackAddress() // 127.开头的都是lookback地址
									&& address.getHostAddress().indexOf(":") == -1) {
								return address;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
