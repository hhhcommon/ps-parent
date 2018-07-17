package com.adpanshi.cashloan.common.http.erp;

import java.io.Serializable;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class ERPResponse<T>
        implements Serializable
{
    private static final long serialVersionUID = 5596721451819059278L;
    private Integer code;
    private String message;
    private T object;
    private ERPResponse<T>.Page page;

    public Integer getCode()
    {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return this.object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public ERPResponse<T>.Page getPage() {
        return this.page;
    }

    public void setPage(ERPResponse<T>.Page page) {
        this.page = page; }
    public class Page { private Integer currentPage;
        private Integer pageRecords;
        private Integer totalPages;
        private Integer totalRecords;
        private Integer startRecord;
        private Integer nextPage;
        private Integer previousPage;
        private Boolean hasNextPage;
        private Boolean hasPreviousPage;

        public Page() {  }
        public Integer getCurrentPage() { return this.currentPage; }

        public void setCurrentPage(Integer currentPage)
        {
            this.currentPage = currentPage;
        }

        public Integer getPageRecords() {
            return this.pageRecords;
        }

        public void setPageRecords(Integer pageRecords) {
            this.pageRecords = pageRecords;
        }

        public Integer getTotalPages() {
            return this.totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Integer getTotalRecords() {
            return this.totalRecords;
        }

        public void setTotalRecords(Integer totalRecords) {
            this.totalRecords = totalRecords;
        }

        public Integer getStartRecord() {
            return this.startRecord;
        }

        public void setStartRecord(Integer startRecord) {
            this.startRecord = startRecord;
        }

        public Integer getNextPage() {
            return this.nextPage;
        }

        public void setNextPage(Integer nextPage) {
            this.nextPage = nextPage;
        }

        public Integer getPreviousPage() {
            return this.previousPage;
        }

        public void setPreviousPage(Integer previousPage) {
            this.previousPage = previousPage;
        }

        public Boolean getHasNextPage() {
            return this.hasNextPage;
        }

        public void setHasNextPage(Boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public Boolean getHasPreviousPage() {
            return this.hasPreviousPage;
        }

        public void setHasPreviousPage(Boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }
    }
}