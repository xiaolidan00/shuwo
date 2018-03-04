package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
//就话题发表评论
public class AddComment extends BmobObject {
    private String add_user;//评论用户id
    private String add_text;//评论内容
    private String add_id;//评论话题id

    public String getAdd_user() {
        return add_user;
    }

    public void setAdd_user(String add_user) {
        this.add_user = add_user;
    }

    public String getAdd_text() {
        return add_text;
    }

    public void setAdd_text(String add_text) {
        this.add_text = add_text;
    }

    public String getAdd_id() {
        return add_id;
    }

    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }
}
