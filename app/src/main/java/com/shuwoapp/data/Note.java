package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
//留言
public class Note extends BmobObject {
    private String note_text;//留言内容
    private String note_user;//留言用户id

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public void setNote_user(String note_user) {
        this.note_user = note_user;
    }
}
