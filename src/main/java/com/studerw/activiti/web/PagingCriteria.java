package com.studerw.activiti.web;

/**
 * Simple pojo used for paging result sets.
 *
 * @author studerw
 */
public class PagingCriteria {

    protected Integer start;
    protected Integer limit;
    protected Integer page;

    public PagingCriteria() {
    }

    public PagingCriteria(Integer start, Integer limit, Integer page) {
        this.start = start;
        this.limit = limit;
        this.page = page;
    }

    public Integer getStart() {
        return this.start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.limit == null) ? 0 : this.limit.hashCode());
        result = prime * result + ((this.page == null) ? 0 : this.page.hashCode());
        result = prime * result + ((this.start == null) ? 0 : this.start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PagingCriteria other = (PagingCriteria) obj;
        if (this.limit == null) {
            if (other.limit != null)
                return false;
        } else if (!this.limit.equals(other.limit))
            return false;
        if (this.page == null) {
            if (other.page != null)
                return false;
        } else if (!this.page.equals(other.page))
            return false;
        if (this.start == null) {
            if (other.start != null)
                return false;
        } else if (!this.start.equals(other.start))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PagingCriteria [start=").append(this.start).append(", limit=").append(this.limit)
                .append(", page=").append(this.page).append("]");
        return builder.toString();
    }
}
