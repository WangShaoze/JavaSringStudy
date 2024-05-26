package com.show.words.utils;

import com.show.words.enums.PageSize;

public class SimplePage {
    private Integer pageNo;  // 页码
    private Integer pageSize;  // 每页数据量
    private Integer total;  // 总数据条数
    private Integer pageTotal;  // 共有多少页
    private Integer start;   // 开始位置
    private Integer offset;  // 偏移量

    public void action(){
        if (this.pageSize<=0){
            this.pageSize = PageSize.SIZE20.getSize();  // 默认每页20条数据
        }

        // 计算共有几页
        if (this.total>0){
            this.pageTotal = this.total % this.pageSize == 0?this.total/this.pageSize:this.total/this.pageSize+1;
        }else {
            pageTotal = 1;
        }

        // 最小页不能小于第一页
        if (pageNo<=1){
            pageNo=1;
        }

        // 最大页不能大于最后一页
        if (pageNo>pageTotal){
            pageNo =pageTotal;
        }
        this.start = (pageNo-1)*this.pageSize;  // 开始位置
        this.offset = this.pageSize;  // 偏移量
    }


    public SimplePage() {
    }

    public SimplePage(Integer pageNo, Integer pageSize, Integer total) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        action();
    }

    public SimplePage(Integer start, Integer offset){
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
        // 更新起始位置当每页的数据量发生变化的时候
        action();
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
        // 重新计算总的页数当总数据量发生变化的时候
        action();
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
