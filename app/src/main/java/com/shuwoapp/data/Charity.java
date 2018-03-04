package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;

//公益活动
public class Charity extends BmobObject {
    private String charity_title;//活动名称
    private String charity_time;//活动时间
    private String charity_address;//活动地点
    private String charity_people;//活动对象
    private String charity_form;//多动形式
    private String charity_award;//活动奖励
    private String charity_join;//报名方式
    private String charity_phone;//联系电话

    public String getCharity_title() {
        return charity_title;
    }


    public String getCharity_time() {
        return charity_time;
    }


    public String getCharity_address() {
        return charity_address;
    }


    public String getCharity_people() {
        return charity_people;
    }


    public String getCharity_form() {
        return charity_form;
    }


    public String getCharity_award() {
        return charity_award;
    }


    public String getCharity_join() {
        return charity_join;
    }


    public String getCharity_phone() {
        return charity_phone;
    }

}
