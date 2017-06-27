package com.cxb.starrysky.model;

/**
 * 个人信息
 */
public class PersonInfo {
    private String token;       //用户令牌
    private String membersId;   //开星号
    private String id;          //主键id
    private String memberName;  //呢称
    private String memberImg;   //用户头像

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMembersId() {
        return membersId;
    }

    public void setMembersId(String membersId) {
        this.membersId = membersId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberImg() {
        return memberImg;
    }

    public void setMemberImg(String memberImg) {
        this.memberImg = memberImg;
    }
}
