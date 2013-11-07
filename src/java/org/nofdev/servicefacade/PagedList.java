package org.nofdev.servicefacade;


import java.util.ArrayList;
import java.util.Collection;

/**
 * 支持分页的结果集，内含总条目数，当前页码，每页条目数等信息
 *
 * @author Richard  Zhang
 */
public class PagedList<T> {

	/**
	 * 总页数
	 */
	private long totalPage;
	/**
	 * 信息总数
	 */
	private long totalCount;
	/**
	 * 当前页数
	 */
	private long currentPage;
	/**
	 * 每页的信息条数
	 */
	private int pageSize;
	/**
	 * 信息列表
	 */
	private ArrayList<T> list;

	public PagedList() {
		this.list = new ArrayList<T>();
	}

	public PagedList(long totalCount, Paginator paginator) {
		this();
		this.totalCount = totalCount;
		this.currentPage = paginator.getPage();
		this.pageSize = paginator.getPageSize();
	}

	public PagedList(long totalCount, Paginator paginator, Collection<T> list) {
		this.list = new ArrayList<T>(list);
		this.totalCount = totalCount;
		this.currentPage = paginator.getPage();
		this.pageSize = paginator.getPageSize();
	}

	public long getTotalPage() {
		if (totalPage == 0) {
            if (this.totalCount <= 0) {
                totalPage = 1;
            } else {
			totalPage = this.totalCount % this.pageSize > 0 ? this.totalCount / this.pageSize + 1 : this.totalCount / this.pageSize;
		}
        }
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public ArrayList<T> getList() {
		return list;
	}

	public void setList(ArrayList<T> list) {
		this.list = list;
	}


    public static <E> PagedList<E> wrap(Collection<E> collection) {
        return new PagedList<E>(collection.size(), Paginator.page(1, collection.size()), collection);
    }
}
