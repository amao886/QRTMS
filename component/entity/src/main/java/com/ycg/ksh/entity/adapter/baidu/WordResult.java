/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:16:45
 */
package com.ycg.ksh.entity.adapter.baidu;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.List;

/**
 * 图片识别文字，识别结果
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:16:45
 */
public class WordResult extends BaseEntity {

    private static final long serialVersionUID = -685575112226934663L;
    
    private Long id;
    private Integer count;
    private List<WordInfo> wordInfos;
    
    
    
    /**
     * 创建一个新的 WordResult实例. 
     * <p>
     * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-27 15:40:37
     */
    public WordResult() {
        super();
    }
    public WordResult(Long id, Integer count) {
        super();
        this.id = id;
        this.count = count;
    }
    /**
     * getter method for id
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * setter method for id
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * getter method for count
     * @return the count
     */
    public Integer getCount() {
        return count;
    }
    /**
     * setter method for count
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }
    /**
     * getter method for wordInfos
     * @return the wordInfos
     */
    public List<WordInfo> getWordInfos() {
        return wordInfos;
    }
    /**
     * setter method for wordInfos
     * @param wordInfos the wordInfos to set
     */
    public void setWordInfos(List<WordInfo> wordInfos) {
        this.wordInfos = wordInfos;
    }
}
