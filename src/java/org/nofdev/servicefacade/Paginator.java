package org.nofdev.servicefacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类是当查询歌曲时用于分页展示的页面导航器
 *
 * @author Li Hongzhen
 */
public class Paginator {
    /**
     * 默认条目数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 默认第一页的页码
     */
    public static final int DEFAULT_FIRST_PAGE = 1;
    /**
     * 每页显示多少条记录
     */
    private int pageSize = Paginator.DEFAULT_PAGE_SIZE;
    /**
     * 当前页码
     */
    private long page = Paginator.DEFAULT_FIRST_PAGE;


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 1 : pageSize;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getOffset() {
        return (page - 1) * pageSize;
    }


    public static Paginator page(long page) {
        Paginator paginator = new Paginator();
        paginator.setPage(page);
        return paginator;
    }

    public static Paginator page(long page, int pageSize) {
        Paginator paginator = new Paginator();
        paginator.setPage(page);
        paginator.setPageSize(pageSize);
        return paginator;
    }

    public static Paginator pageContains(long index, int pageSize) {
        return Paginator.page(index / pageSize + 1, pageSize);
    }

    public void turnToPageContains(long index) {
        this.page = pageContains(index, pageSize).page;
    }

    public Map<String, Object> getGormParams() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", getOffset());
        map.put("max", getPageSize());
        return map;
    }

    public <T> List<T> filter(List<T> list) {
        return list.subList((int) getOffset(),
                Math.max(
                        (int) getOffset(),
                        Math.min(
                                (int) getOffset() + getPageSize(),
                                list.size()
                        )
                )
        );
    }
}
