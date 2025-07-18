package com.summerplan.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页响应DTO
 */
@Data
public class PageResponse<T> {
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页码
     */
    private Integer pageNumber;
    
    /**
     * 页面大小
     */
    private Integer pageSize;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    public PageResponse() {}
    
    public PageResponse(List<T> records, Long total, Integer pageNumber, Integer pageSize) {
        this.records = records;
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.calculatePages();
    }
    
    /**
     * 计算分页信息
     */
    private void calculatePages() {
        if (total != null && pageSize != null && pageSize > 0) {
            this.totalPages = (int) Math.ceil((double) total / pageSize);
            this.hasNext = pageNumber < totalPages;
            this.hasPrevious = pageNumber > 1;
        } else {
            this.totalPages = 0;
            this.hasNext = false;
            this.hasPrevious = false;
        }
    }
    
    /**
     * 创建分页响应
     */
    public static <T> PageResponse<T> of(List<T> records, Long total, Integer pageNumber, Integer pageSize) {
        return new PageResponse<>(records, total, pageNumber, pageSize);
    }
    
    /**
     * 创建空分页响应
     */
    public static <T> PageResponse<T> empty(Integer pageNumber, Integer pageSize) {
        return new PageResponse<>(List.of(), 0L, pageNumber, pageSize);
    }
} 