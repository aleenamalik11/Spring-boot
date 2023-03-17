package com.hazelsoft.springsecurityjpa.dto;

public class PaginationRequest {

    private String page;

    private  Integer size;

    public PaginationRequest(String page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "paginationRequest{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}
