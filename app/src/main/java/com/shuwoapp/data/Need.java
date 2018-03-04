package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
//求书信息
public class Need extends BmobObject {
    private String need_book;//需求书籍
    private String need_deadline;//截止日期
    private String need_reason;//求书理由
    private String need_needer;//求书者id
    private String need_nname;//求书者姓名
    private String need_nschool;//求书者所在学校
    private String need_nphone;//求书者电话
    private String need_giver;//捐书者id
    private String need_gschool;//捐书者所在学校
    private String need_gphone;//捐书者电话
    private String need_gname;//捐书者姓名
    private boolean need_over;//是否已经受赠

    public String getNeed_book() {
        return need_book;
    }

    public void setNeed_book(String need_book) {
        this.need_book = need_book;
    }

    public String getNeed_deadline() {
        return need_deadline;
    }

    public void setNeed_deadline(String need_deadline) {
        this.need_deadline = need_deadline;
    }

    public String getNeed_reason() {
        return need_reason;
    }

    public void setNeed_reason(String need_reason) {
        this.need_reason = need_reason;
    }

    public String getNeed_needer() {
        return need_needer;
    }

    public void setNeed_needer(String need_needer) {
        this.need_needer = need_needer;
    }

    public String getNeed_nname() {
        return need_nname;
    }

    public void setNeed_nname(String need_nname) {
        this.need_nname = need_nname;
    }

    public String getNeed_nschool() {
        return need_nschool;
    }

    public void setNeed_nschool(String need_nschool) {
        this.need_nschool = need_nschool;
    }

    public String getNeed_nphone() {
        return need_nphone;
    }

    public void setNeed_nphone(String need_nphone) {
        this.need_nphone = need_nphone;
    }

    public String getNeed_giver() {
        return need_giver;
    }

    public void setNeed_giver(String need_giver) {
        this.need_giver = need_giver;
    }

    public String getNeed_gschool() {
        return need_gschool;
    }

    public void setNeed_gschool(String need_gschool) {
        this.need_gschool = need_gschool;
    }

    public String getNeed_gphone() {
        return need_gphone;
    }

    public void setNeed_gphone(String need_gphone) {
        this.need_gphone = need_gphone;
    }

    public String getNeed_gname() {
        return need_gname;
    }

    public void setNeed_gname(String need_gname) {
        this.need_gname = need_gname;
    }

    public boolean isNeed_over() {
        return need_over;
    }

    public void setNeed_over(boolean need_over) {
        this.need_over = need_over;
    }
}
