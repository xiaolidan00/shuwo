package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

//图片切换器图片
public class Pic extends BmobObject {
    private BmobFile picture;//图片
    private String pic_text;//描述

    public BmobFile getPicture() {
        return picture;
    }
}
