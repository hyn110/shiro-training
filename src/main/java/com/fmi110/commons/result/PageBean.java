package com.fmi110.commons.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;

/**
 * 分页实体, easyui 分页 datagrid 用
 * total , rows 两个变量名不能变(easyui 固定死的)
 */
public class PageBean {

    private final static int PAGESIZE = 10; //默认显示的记录数 

    private int total; // 总记录 
    private List<Object> rows; //显示的记录

    @JsonIgnore
    private int from;
    @JsonIgnore
    private int size;
    @JsonIgnore
    private int nowpage; // 当前页 
    @JsonIgnore
    private int pagesize; // 每页显示的记录数
    /**
     * 查询条件,用于 service 分页查找时传参
     */
    @JsonIgnore
    private Map<String, Object> condition; //查询条件

    @JsonIgnore
    private String sort = "seq";// 排序字段
    @JsonIgnore
    private String order = "asc";// asc，desc mybatis Order 关键字

    public PageBean() {}

    /**
     * 根据当前页和页大小,自动计算起始和结束记录
     * @param nowpage
     * @param pagesize
     */
    public PageBean(int nowpage, int pagesize) {
        //计算当前页  
        if (nowpage < 0) {
            this.nowpage = 1;
        } else {
            //当前页
            this.nowpage = nowpage;
        }
        //记录每页显示的记录数  
        if (pagesize < 0) {
            this.pagesize = PAGESIZE;
        } else {
            this.pagesize = pagesize;
        }
        //计算开始的记录和结束的记录  
        this.from = (this.nowpage - 1) * this.pagesize;
        this.size = this.pagesize;
    }

    // 构造方法
    public PageBean(int nowpage, int pagesize, String sort, String order) {
        this(nowpage, pagesize) ;
        // 排序字段，正序还是反序
        this.sort = sort;
        this.order = order;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNowpage() {
        return nowpage;
    }

    public void setNowpage(int nowpage) {
        this.nowpage = nowpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * 升序,降序
     * @return ASC ,
     */
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
