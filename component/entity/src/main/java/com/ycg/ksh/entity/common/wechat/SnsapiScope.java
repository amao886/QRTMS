/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-26 09:09:04
 */
package com.ycg.ksh.entity.common.wechat;

/**
 * 微信授权作用域
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-26 09:09:04
 */
public enum SnsapiScope {
    
    BASE("snsapi_base", "CACHE_KEY_WXSS_TOKEN", "基础授权"),
    USERINFO("snsapi_userinfo", "CACHE_KEY_WXSS_TOKEN", "授权用户信息"),
    LOGIN("snsapi_login", "CACHE_KEY_WXSS_LOGINTOKEN", "扫码登陆");
    
    
    private String key;
    private String cacheKey;
    private String description;
    
    private SnsapiScope(String key, String cacheKey, String description) {
        this.key = key;
        this.cacheKey = cacheKey;
        this.description = description;
    }
    
    public static SnsapiScope parse(String key) {
        for (SnsapiScope scope : values()) {
            if(key.equals(scope.getKey())) {
                return scope;
            }
        }
        return null;
    }
    
    /**
     * @see java.lang.Enum#toString()
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 12:57:31
     */
    @Override
    public String toString() {
        return description +"["+ key +"]";
    }

    /**
     * getter method for key
     * @return the key
     */
    public String getKey() {
        return key;
    }
    /**
     * getter method for description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter method for cacheKey
     * @return the cacheKey
     */
    public String getCacheKey() {
        return cacheKey;
    }
}
