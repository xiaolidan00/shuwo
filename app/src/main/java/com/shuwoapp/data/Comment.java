package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
//评论话题
public class Comment extends BmobObject {
    private String comment_text;//发表话题
    private String comment_user;//发表用户id

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_user() {
        return comment_user;
    }

    public void setComment_user(String comment_user) {
        this.comment_user = comment_user;
    }
}
