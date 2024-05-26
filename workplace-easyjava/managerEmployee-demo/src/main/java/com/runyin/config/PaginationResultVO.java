package com.runyin.config;

import java.util.ArrayList;
import java.util.List;

public class PaginationResultVO<T> {
    private Integer total;
    private Integer pageSize;
    private Integer pageNo;
    private Integer pageTotal;
    private List<T> list = new ArrayList<T>();

    public PaginationResultVO(){}

    public PaginationResultVO(List<T> list){
        this.list = list;
    }

    public PaginationResultVO(Integer pageNo, Integer pageSize, Integer total, List<T> list){
        if (pageNo == 0) {
            pageNo = 1;
        }
        this.pageNo =pageNo;
        this.pageSize = pageSize;
        this.total= total;
        this.list = list;
    }
    public PaginationResultVO(Integer pageNo, Integer pageSize, Integer total, Integer pageTotal, List<T> list){
        if (pageNo == 0) {
            pageNo = 1;
        }
        this.pageNo =pageNo;
        this.pageSize = pageSize;
        this.total= total;
        this.pageTotal = pageTotal;
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
