package com.miaozhen.o2o.entity;

import java.util.Date;

public class ShopCategory {
    private Long shopCategoryId;
    private String shopCategoryName;
    private String getShopCategoryDesc;
    private Integer priority;
    private Date createTime;
    private Date LastEditTime;

    public Long getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Long shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    public String getGetShopCategoryDesc() {
        return getShopCategoryDesc;
    }

    public void setGetShopCategoryDesc(String getShopCategoryDesc) {
        this.getShopCategoryDesc = getShopCategoryDesc;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return LastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        LastEditTime = lastEditTime;
    }

    public ShopCategory getParent() {
        return parent;
    }

    public void setParent(ShopCategory parent) {
        this.parent = parent;
    }

    private ShopCategory parent;
}
