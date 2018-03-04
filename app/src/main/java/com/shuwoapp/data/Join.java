package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;

//参加公益活动报名者
public class Join extends BmobObject {
    private String join_userid;//用户id
    private String join_name;//用户姓名
    private String join_school;//用户所在学校
    private String join_phone;//用户电话
    private String join_charityid;//参加活动id
    private boolean join_over;//是否活动已结束

    public String getJoin_userid() {
        return join_userid;
    }

    public void setJoin_userid(String join_userid) {
        this.join_userid = join_userid;
    }

    public String getJoin_name() {
        return join_name;
    }

    public void setJoin_name(String join_name) {
        this.join_name = join_name;
    }

    public String getJoin_school() {
        return join_school;
    }

    public void setJoin_school(String join_school) {
        this.join_school = join_school;
    }

    public String getJoin_phone() {
        return join_phone;
    }

    public void setJoin_phone(String join_phone) {
        this.join_phone = join_phone;
    }

    public String getJoin_charityid() {
        return join_charityid;
    }

    public void setJoin_charityid(String join_charityid) {
        this.join_charityid = join_charityid;
    }

    public boolean isJoin_over() {
        return join_over;
    }

    public void setJoin_over(boolean join_over) {
        this.join_over = join_over;
    }
}
