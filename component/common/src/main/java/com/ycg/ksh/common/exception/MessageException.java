package com.ycg.ksh.common.exception;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */

/**
 * 不回滚异常消息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/7/13
 */
public class MessageException extends Exception {
    /**
     * 创建一个新的 TMCException实例.
     * <p>
     *
     * @param message
     * @param cause
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:48:53
     */
    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 创建一个新的 TMCException实例.
     * <p>
     *
     * @param message
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:48:53
     */
    public MessageException(String message) {
        super(message);
    }

    /**
     * 创建一个新的 TMCException实例.
     * <p>
     *
     * @param cause
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-11-29 22:48:53
     */
    public MessageException(Throwable cause) {
        super(cause);
    }
}
