package com.miaozhen.o2o.entity;

import java.util.Date;
//微信账号
public class WechatAuth {
    private Long WeChatAuthId;
    private String openId;
    private Date createTime;
    private PersonInfo personInfo;

    public Long getWeChatAuthId() {
        return WeChatAuthId;
    }

    public void setWeChatAuthId(Long weChatAuthId) {
        WeChatAuthId = weChatAuthId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
