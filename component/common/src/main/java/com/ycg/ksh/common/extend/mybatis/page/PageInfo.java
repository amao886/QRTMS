package com.ycg.ksh.common.extend.mybatis.page;

import com.github.pagehelper.Page;
import com.ycg.ksh.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 分页信息
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-06-06 12:35:45
 */
public class PageInfo<T> extends BaseEntity {

	private static final long serialVersionUID = -5020717917793904186L;
	
	private int num;
	private int size;
	private int startRow;
	private int endRow;
	private long total;
	private int pages;
	//集合对象
	private Collection<T> collection;
	private Map<String,List<T>> map;
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

	
	
	public PageInfo() {
		super();
	}
	public PageInfo(Collection<? extends T> collection) {
		super();
		if (collection instanceof Page) {
			Page<?> page = ((Page<?>) collection);
			this.num = page.getPageNum();
			this.size = page.getPageSize();
			this.startRow = page.getStartRow();
			this.endRow = page.getEndRow();
			this.total = page.getTotal();
			this.pages = page.getPages();
			this.collection  = new ArrayList<T>(collection);
			this.currSize = collection.size();

			//由于结果是>startRow的，所以实际的需要+1
			if (this.size == 0) {
	            this.startRow = 0;
	            this.endRow = 0;
	        } else {
	            this.startRow = page.getStartRow() + 1;
	            //计算实际的endRow（最后一页的时候特殊）
	            this.endRow = this.startRow - 1 + this.size;
	        }
		}else if(collection instanceof Collection){
			this.num = 1;
            this.size = collection.size();

            this.pages = 1;
            this.collection = new ArrayList<T>(collection);
            this.currSize = collection.size();
            this.total = collection.size();
            this.startRow = 0;
            this.endRow = currSize > 0 ? currSize - 1 : 0;
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
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

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
            if (num > 1) {
                prePage = num - 1;
            }
            if (num < pages) {
                nextPage = num + 1;
            }
        }
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = num == 1;
        isLastPage = num == pages;
        hasPreviousPage = num > 1;
        hasNextPage = num < pages;
    }
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public Collection<T> getCollection() {
		return collection;
	}
	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}
	public int getCurrSize() {
		return currSize;
	}
	public void setCurrSize(int currSize) {
		this.currSize = currSize;
	}
	public int getPrePage() {
		return prePage;
	}
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public Boolean getIsFirstPage() {
		return isFirstPage;
	}
	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}
	public Boolean getIsLastPage() {
		return isLastPage;
	}
	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}
	public Boolean getHasPreviousPage() {
		return hasPreviousPage;
	}
	public void setHasPreviousPage(Boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}
	public Boolean getHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public int getNavigatePages() {
		return navigatePages;
	}
	public void setNavigatePages(int navigatePages) {
		this.navigatePages = navigatePages;
	}
	public int[] getNavigatepageNums() {
		return navigatepageNums;
	}
	public void setNavigatepageNums(int[] navigatepageNums) {
		this.navigatepageNums = navigatepageNums;
	}
	public int getNavigateFirstPage() {
		return navigateFirstPage;
	}
	public void setNavigateFirstPage(int navigateFirstPage) {
		this.navigateFirstPage = navigateFirstPage;
	}
	public int getNavigateLastPage() {
		return navigateLastPage;
	}
	public void setNavigateLastPage(int navigateLastPage) {
		this.navigateLastPage = navigateLastPage;
	}
	public Map<String, List<T>> getMap() {
		return map;
	}
	public void setMap(Map<String, List<T>> map) {
		this.map = map;
	}
	
}
