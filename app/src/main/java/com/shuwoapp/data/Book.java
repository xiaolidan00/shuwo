package com.shuwoapp.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
//交易书籍信息
public class Book extends BmobObject {

    private String book_title;//书名
    private String book_author;//作者
    private String book_publisher;//出版社
    private String book_time;//出版时间
    private float book_price;//原定价
    private String book_size;//开本
    private String book_isbn;//ISBN编码
    private String book_way;//交易方式
    private String book_kind;//书籍分类
    private String book_deal;//交易价格或互换书籍
    private String book_owner;//书籍主人
    private String book_oschool;//主人所在学校
    private String book_buyer;//买家id
    private String book_bname;//买家名字
    private String book_bschool;//买家所在学校
    private String book_bphone;//买家电话
    private String book_bnote;//买家备注
    private BmobFile book_pic;//书籍相片
    private boolean book_over;//是否已交易

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getBook_publisher() {
        return book_publisher;
    }

    public void setBook_publisher(String book_publisher) {
        this.book_publisher = book_publisher;
    }

    public String getBook_time() {
        return book_time;
    }

    public void setBook_time(String book_time) {
        this.book_time = book_time;
    }

    public float getBook_price() {
        return book_price;
    }

    public void setBook_price(float book_price) {
        this.book_price = book_price;
    }

    public String getBook_size() {
        return book_size;
    }

    public void setBook_size(String book_size) {
        this.book_size = book_size;
    }

    public String getBook_isbn() {
        return book_isbn;
    }

    public void setBook_isbn(String book_isbn) {
        this.book_isbn = book_isbn;
    }

    public String getBook_way() {
        return book_way;
    }

    public void setBook_way(String book_way) {
        this.book_way = book_way;
    }

    public String getBook_kind() {
        return book_kind;
    }

    public void setBook_kind(String book_kind) {
        this.book_kind = book_kind;
    }

    public String getBook_deal() {
        return book_deal;
    }

    public void setBook_deal(String book_deal) {
        this.book_deal = book_deal;
    }

    public String getBook_owner() {
        return book_owner;
    }

    public void setBook_owner(String book_owner) {
        this.book_owner = book_owner;
    }

    public String getBook_oschool() {
        return book_oschool;
    }

    public void setBook_oschool(String book_oschool) {
        this.book_oschool = book_oschool;
    }

    public String getBook_buyer() {
        return book_buyer;
    }

    public void setBook_buyer(String book_buyer) {
        this.book_buyer = book_buyer;
    }

    public String getBook_bname() {
        return book_bname;
    }

    public void setBook_bname(String book_bname) {
        this.book_bname = book_bname;
    }

    public String getBook_bschool() {
        return book_bschool;
    }

    public void setBook_bschool(String book_bschool) {
        this.book_bschool = book_bschool;
    }

    public String getBook_bphone() {
        return book_bphone;
    }

    public void setBook_bphone(String book_bphone) {
        this.book_bphone = book_bphone;
    }

    public BmobFile getBook_pic() {
        return book_pic;
    }

    public void setBook_pic(BmobFile book_pic) {
        this.book_pic = book_pic;
    }

    public boolean isBook_over() {
        return book_over;
    }

    public void setBook_over(boolean book_over) {
        this.book_over = book_over;
    }

    public String getBook_bnote() {
        return book_bnote;
    }

    public void setBook_bnote(String book_bnote) {
        this.book_bnote = book_bnote;
    }
}
