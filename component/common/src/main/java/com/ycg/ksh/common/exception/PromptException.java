package com.ycg.ksh.common.exception;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/14
 */

/**
 * 提示异常
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/6/14
 */
public class PromptException extends TMCException {

    private String promptMessage;
    private String promptUrl;

    public PromptException(String promptMessage, String promptUrl) {
        super(promptMessage);
        this.promptMessage = promptMessage;
        this.promptUrl = promptUrl;
    }

    public PromptException(String promptMessage) {
        super(promptMessage);
        this.promptMessage = promptMessage;
    }

    public String getPromptMessage() {
        return promptMessage;
    }

    public void setPromptMessage(String promptMessage) {
        this.promptMessage = promptMessage;
    }

    public String getPromptUrl() {
        return promptUrl;
    }

    public void setPromptUrl(String promptUrl) {
        this.promptUrl = promptUrl;
    }
}
