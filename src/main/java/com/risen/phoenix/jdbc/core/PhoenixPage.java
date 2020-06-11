package com.risen.phoenix.jdbc.core;

public class PhoenixPage {

    private Integer start = 0;
    private Integer limit = null;
    private Integer end;
    private Integer pageNumber;
    private Integer totalSize;
    private Integer totalPages;



    public boolean hasPreviews() {
        return this.start > 0;
    }

    public boolean hasNext() {
        return this.start + this.getLimit() < this.totalSize;
    }

    public Integer getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public Integer getEnd() {
        return this.end;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public Integer getLimit() {
        return this.limit == null ? 15 : this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public boolean hasLimit() {
        return this.limit != null;
    }

    public Integer getStart() {
        return this.start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void calPage() {
        this.totalPages = (int)Math.ceil((double)this.totalSize * 1.0D / (double)this.getLimit());
        this.end = Math.min(this.start + this.getLimit(), this.totalSize);
        this.pageNumber = this.start / this.getLimit() + 1;
    }

    @Override
    public String toString() {
        return "PhoenixPage{" +
                "start=" + start +
                ", limit=" + limit +
                ", end=" + end +
                ", pageNumber=" + pageNumber +
                ", totalSize=" + totalSize +
                ", totalPages=" + totalPages +
                '}';
    }
}
