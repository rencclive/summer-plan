package com.summerplan.common.dto;

import com.summerplan.common.constant.Constants;
import lombok.Data;

/**
 * 分页请求DTO
 */
@Data
public class PageRequest {
    
    /**
     * 页码（从1开始）
     */
    private Integer pageNumber = Constants.DEFAULT_PAGE_NUMBER;
    
    /**
     * 页面大小
     */
    private Integer pageSize = Constants.DEFAULT_PAGE_SIZE;
    
    /**
     * 排序字段
     */
    private String sortBy;
    
    /**
     * 排序方向（asc/desc）
     */
    private String sortDirection = "desc";
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber != null && pageNumber > 0 ? pageNumber : Constants.DEFAULT_PAGE_NUMBER;
    }
    
    public void setPageSize(Integer pageSize) {
        if (pageSize != null && pageSize > 0) {
            this.pageSize = Math.min(pageSize, Constants.MAX_PAGE_SIZE);
        } else {
            this.pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
    }
    
    public void setSortDirection(String sortDirection) {
        if ("asc".equalsIgnoreCase(sortDirection)) {
            this.sortDirection = "asc";
        } else {
            this.sortDirection = "desc";
        }
    }
} 