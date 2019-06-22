package com.ycg.ksh.common.extend.mybatis.page;

import com.ycg.ksh.common.entity.BaseEntity;

import java.util.Collection;

/**
 * 分页
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:36:19
 */
public class CustomPage<T> extends BaseEntity {

	private static final long serialVersionUID = -7655364611936344461L;

    private int pageNum;
    private int pageSize;
    private int startRow;
    private int endRow;
    private long total;
    private int pages;
    
	//集合对象
	private Collection<T> collection;
	private Object attachment;
	//实际数量
	private int currSize;
	//前一页
    private int prePage;
    //下一页
    private int nextPage;

    //是否为第一页
    private Boolean isFirstPage = false;
    //是否为最后一页
    private Boolean isLastPage = false;
    //是否有前一页
    private Boolean hasPreviousPage = false;
    //是否有下一页
    private Boolean hasNextPage = false;
    //导航页码数
    private int navigatePages;
    //所有导航页号
    private int[] navigatepageNums;
    //导航条上的第一页
    private int navigateFirstPage;
    //导航条上的最后一页
    private int navigateLastPage;
    
	public CustomPage() {
		super();
	}
	public CustomPage(int pageNum, int pageSize, long total, Collection<T> collection) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.total = total;
		this.collection  = collection;
		this.currSize = collection.size();
		this.pages = (int) (total / pageSize);
		if(total % pageSize != 0) {
			pages = pages + 1;
		}
		if (this.pageSize == 0) {
            this.startRow = 0;
            this.endRow = 0;
        } else {
            this.startRow = Math.max(1, (pageSize - 1) * pageNum);
            this.endRow = startRow + pageSize - 1;
        }
		
		this.navigatePages = 10;
        //计算导航页
        calcNavigatepageNums();
        //计算前后页，第一页，最后一页
        calcPage();
        //判断页面边界
        judgePageBoudary();
	}
	
	 /**
     * 计算导航页
     */
    private void calcNavigatepageNums() {
        //当总页数小于或等于导航页码数时
        if (pages <= navigatePages) {
            navigatepageNums = new int[pages];
            for (int i = 0; i < pages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = pageNum - navigatePages / 2;
            int endNum = pageNum + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > pages) {
                endNum = pages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
    }

    /**
     * 计算前后页，第一页，最后一页
     */
    private void calcPage() {
        if (navigatepageNums != null && navigatepageNums.length > 0) {
            navigateFirstPage = navigatepageNums[0];
            navigateLastPage = navigatepageNums[navigatepageNums.length - 1];
            if (pageNum > 1) {
                prePage = pageNum - 1;
            }
            if (pageNum < pages) {
                nextPage = pageNum + 1;
            }
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = pageNum == 1;
        isLastPage = pageNum == pages;
        hasPreviousPage = pageNum > 1;
        hasNextPage = pageNum < pages;
    }
	
	
	
	/**
	 * getter method for pageNum
	 * @return the pageNum
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * setter method for pageNum
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * getter method for pageSize
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * setter method for pageSize
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * getter method for startRow
	 * @return the startRow
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * setter method for startRow
	 * @param startRow the startRow to set
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * getter method for endRow
	 * @return the endRow
	 */
	public int getEndRow() {
		return endRow;
	}

	/**
	 * setter method for endRow
	 * @param endRow the endRow to set
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	/**
	 * getter method for total
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * setter method for total
	 * @param total the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * getter method for pages
	 * @return the pages
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * setter method for pages
	 * @param pages the pages to set
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * getter method for collection
	 * @return the collection
	 */
	public Collection<T> getCollection() {
		return collection;
	}

	/**
	 * setter method for collection
	 * @param collection the collection to set
	 */
	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}

	/**
	 * getter method for attachment
	 * @return the attachment
	 */
	public Object getAttachment() {
		return attachment;
	}

	/**
	 * setter method for attachment
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	/**
	 * getter method for currSize
	 * @return the currSize
	 */
	public int getCurrSize() {
		return currSize;
	}

	/**
	 * setter method for currSize
	 * @param currSize the currSize to set
	 */
	public void setCurrSize(int currSize) {
		this.currSize = currSize;
	}

	/**
	 * getter method for prePage
	 * @return the prePage
	 */
	public int getPrePage() {
		return prePage;
	}

	/**
	 * setter method for prePage
	 * @param prePage the prePage to set
	 */
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	/**
	 * getter method for nextPage
	 * @return the nextPage
	 */
	public int getNextPage() {
		return nextPage;
	}

	/**
	 * setter method for nextPage
	 * @param nextPage the nextPage to set
	 */
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * getter method for isFirstPage
	 * @return the isFirstPage
	 */
	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	/**
	 * setter method for isFirstPage
	 * @param isFirstPage the isFirstPage to set
	 */
	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	/**
	 * getter method for isLastPage
	 * @return the isLastPage
	 */
	public Boolean getIsLastPage() {
		return isLastPage;
	}

	/**
	 * setter method for isLastPage
	 * @param isLastPage the isLastPage to set
	 */
	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	/**
	 * getter method for hasPreviousPage
	 * @return the hasPreviousPage
	 */
	public Boolean getHasPreviousPage() {
		return hasPreviousPage;
	}

	/**
	 * setter method for hasPreviousPage
	 * @param hasPreviousPage the hasPreviousPage to set
	 */
	public void setHasPreviousPage(Boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	/**
	 * getter method for hasNextPage
	 * @return the hasNextPage
	 */
	public Boolean getHasNextPage() {
		return hasNextPage;
	}

	/**
	 * setter method for hasNextPage
	 * @param hasNextPage the hasNextPage to set
	 */
	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * getter method for navigatePages
	 * @return the navigatePages
	 */
	public int getNavigatePages() {
		return navigatePages;
	}

	/**
	 * setter method for navigatePages
	 * @param navigatePages the navigatePages to set
	 */
	public void setNavigatePages(int navigatePages) {
		this.navigatePages = navigatePages;
	}

	/**
	 * getter method for navigatepageNums
	 * @return the navigatepageNums
	 */
	public int[] getNavigatepageNums() {
		return navigatepageNums;
	}

	/**
	 * setter method for navigatepageNums
	 * @param navigatepageNums the navigatepageNums to set
	 */
	public void setNavigatepageNums(int[] navigatepageNums) {
		this.navigatepageNums = navigatepageNums;
	}

	/**
	 * getter method for navigateFirstPage
	 * @return the navigateFirstPage
	 */
	public int getNavigateFirstPage() {
		return navigateFirstPage;
	}

	/**
	 * setter method for navigateFirstPage
	 * @param navigateFirstPage the navigateFirstPage to set
	 */
	public void setNavigateFirstPage(int navigateFirstPage) {
		this.navigateFirstPage = navigateFirstPage;
	}

	/**
	 * getter method for navigateLastPage
	 * @return the navigateLastPage
	 */
	public int getNavigateLastPage() {
		return navigateLastPage;
	}

	/**
	 * setter method for navigateLastPage
	 * @param navigateLastPage the navigateLastPage to set
	 */
	public void setNavigateLastPage(int navigateLastPage) {
		this.navigateLastPage = navigateLastPage;
	}
}
