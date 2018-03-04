package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;
//操作记录信息
public class Record extends BmobObject {
    private String record_text;//记录内容
    private String record_kind;//记录分类
    private String record_date;//记录日期
    private String record_user;//记录用户id
    private String record_id;//记录的操作id

    public String getRecord_text() {
        return record_text;
    }

    public void setRecord_text(String record_text) {
        this.record_text = record_text;
    }

    public String getRecord_kind() {
        return record_kind;
    }

    public void setRecord_kind(String record_kind) {
        this.record_kind = record_kind;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getRecord_user() {
        return record_user;
    }

    public void setRecord_user(String record_user) {
        this.record_user = record_user;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }
}
