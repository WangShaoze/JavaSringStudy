package com.show.words.utils;

import java.util.ArrayList;
import java.util.List;

public class PaginationResultVO<T> {
    private Integer pageNo;  // 页码
    private Integer pageSize;  // 每页数据量
    private Integer total;  // 总数据条数
    private Integer pageTotal;  // 共有多少页
    private List<T> dataList = new ArrayList<T>();  // 记录列表

    public PaginationResultVO(Integer pageNo, Integer pageSize, Integer total, List<T> dataList) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.dataList = dataList;
    }

    public PaginationResultVO(Integer pageNo, Integer pageSize, Integer total, Integer pageTotal, List<T> dataList) {
        if (pageNo==0){
            pageNo = 1;
        }
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.pageTotal = pageTotal;
        this.dataList = dataList;
    }

    public PaginationResultVO(List<T> list){
        this.dataList = list;
    }
    public PaginationResultVO(){}

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
