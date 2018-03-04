package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
//捐赠书籍信息
public class Give extends BmobObject {
    private String give_book;//捐赠书籍
    private String give_deadline;//截止日期
    private BmobFile give_pic;//书籍图片
    private String give_giver;//捐书者id
    private String give_gname;//捐书者姓名
    private String give_gschool;//捐书者所在学校
    private String give_gphone;//捐书者电话
    private String give_needer;//求书者id
    private String give_nname;//求书者姓名
    private String give_nschool;//求书者所在学校
    private String give_nphone;//求书者电话
    private boolean give_over;//是否已经捐出

    public String getGive_book() {
        return give_book;
    }

    public void setGive_book(String give_book) {
        this.give_book = give_book;
    }

    public String getGive_deadline() {
        return give_deadline;
    }

    public void setGive_deadline(String give_deadline) {
        this.give_deadline = give_deadline;
    }

    public BmobFile getGive_pic() {
        return give_pic;
    }

    public void setGive_pic(BmobFile give_pic) {
        this.give_pic = give_pic;
    }

    public String getGive_giver() {
        return give_giver;
    }

    public void setGive_giver(String give_giver) {
        this.give_giver = give_giver;
    }

    public String getGive_gname() {
        return give_gname;
    }

    public void setGive_gname(String give_gname) {
        this.give_gname = give_gname;
    }

    public String getGive_gschool() {
        return give_gschool;
    }

    public void setGive_gschool(String give_gschool) {
        this.give_gschool = give_gschool;
    }

    public String getGive_gphone() {
        return give_gphone;
    }

    public void setGive_gphone(String give_gphone) {
        this.give_gphone = give_gphone;
    }

    public String getGive_needer() {
        return give_needer;
    }

    public void setGive_needer(String give_needer) {
        this.give_needer = give_needer;
    }

    public String getGive_nname() {
        return give_nname;
    }

    public void setGive_nname(String give_nname) {
        this.give_nname = give_nname;
    }

    public String getGive_nschool() {
        return give_nschool;
    }

    public void setGive_nschool(String give_nschool) {
        this.give_nschool = give_nschool;
    }

    public String getGive_nphone() {
        return give_nphone;
    }

    public void setGive_nphone(String give_nphone) {
        this.give_nphone = give_nphone;
    }

    public boolean isGive_over() {
        return give_over;
    }

    public void setGive_over(boolean give_over) {
        this.give_over = give_over;
    }
}
