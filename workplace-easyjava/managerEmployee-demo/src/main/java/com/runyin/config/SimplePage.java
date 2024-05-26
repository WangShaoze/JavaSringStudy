package com.runyin.config;

import com.runyin.enums.PageSize;

public class SimplePage {
    private Integer pageNo;
    private Integer pageSize;
    private Integer total;
    private Integer pageTotal;
    private Integer start;
    private Integer offset;

    public void action(){
        if (this.pageSize<=0){
            this.pageSize = PageSize.SIZE20.getSize();
        }
        if (this.total>0){
            this.pageTotal = this.total%this.pageSize==0?this.total/this.pageSize:this.total/this.pageSize+1;
        }else {
            this.pageTotal = 1;
        }

        if (this.pageNo<=1){
            this.pageNo=1;
        }
        if (this.pageNo>this.pageTotal){
            this.pageNo= this.pageTotal;
        }

        this.start = (pageNo-1)*this.pageSize;
        this.offset = this.pageSize;

    }

    public SimplePage() {
    }

    public SimplePage(Integer pageNo, Integer pageSize, Integer total) {
        if (pageNo==null){
            pageNo=0;
        }
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        action();
    }

    public SimplePage(Integer start, Integer offset) {
        this.start = start;
        this.offset = offset;
    }

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
        action();  // Update start and offset when page size changes
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
        action(); // Recalculate page total when total count changes
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
