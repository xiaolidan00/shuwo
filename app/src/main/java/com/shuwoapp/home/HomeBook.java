package com.shuwoapp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.MainActivity;
import com.shuwoapp.R;
import com.shuwoapp.data.Book;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;

//了解书籍详细信息
public class HomeBook extends Activity {
    private ImageView im;
    private Book book;
    private String objectId;
    private BmobFile bf;
    private float price;
    private TextView tv_author, tv_title, tv_time, tv_code, tv_publisher,
            tv_price, tv_way, tv_deal, tv_size, tv_school;
    private String author, title, ptime, code, publisher, exchange, bsize, bschool, way, deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_book);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_author = (TextView) findViewById(R.id.tv_auther);
        tv_title = (TextView) findViewById(R.id.tv_book_name);
        tv_time = (TextView) findViewById(R.id.tv_booktime);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_publisher = (TextView) findViewById(R.id.tv_publisher);
        tv_size = (TextView) findViewById(R.id.tv_booksize);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_way = (TextView) findViewById(R.id.tv_book_way);
        tv_deal = (TextView) findViewById(R.id.tv_book_deal);
        tv_school = (TextView) findViewById(R.id.tv_school);
        im = (ImageView) findViewById(R.id.im_book);
        //获取书籍id
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        book = new Book();
        getBookData();

    }
//    显示书籍信息
    private void createInfo() {
        author = book.getBook_author();
        title = book.getBook_title();
        ptime = book.getBook_time();
        code = book.getBook_isbn();
        publisher = book.getBook_publisher();
        price = book.getBook_price();
        way = book.getBook_way();
        deal = book.getBook_deal();
        bsize = book.getBook_size();
        bschool = book.getBook_oschool();
        tv_author.setText(author);
        tv_title.setText(title);
        tv_time.setText(ptime);
        tv_code.setText(code);
        tv_publisher.setText(publisher);
        tv_price.setText(String.valueOf(price));
        tv_way.setText(way);
        tv_deal.setText(deal);
        tv_size.setText(bsize);
        tv_school.setText(bschool);
        if (book.getBook_pic() == null) {
            im.setImageResource(R.drawable.book);
        } else {
            bf = book.getBook_pic();
            bf.loadImageThumbnail(HomeBook.this, im, 250, 200, 100);
        }

    }
    //获取书籍信息
    private void getBookData() {
        BmobQuery<Book> query = new BmobQuery<Book>();
        query.getObject(this, objectId, new GetListener<Book>() {
            @Override
            public void onSuccess(Book object) {
                // TODO Auto-generated method stub
                book = object;
                if (book != null) {
                    createInfo();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(HomeBook.this, "加载失败~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            //返回
            case android.R.id.home:
                break;
            //加入购物车
            case R.id.action_cart:
                Intent intent1 = new Intent(HomeBook.this, HomeCart.class);
                intent1.putExtra("objectId", objectId);
                startActivity(intent1);
                break;
            //订单
            case R.id.action_bill:
                Intent intent2 = new Intent(HomeBook.this, HomeBill.class);
                startActivity(intent2);
                break;
        }
        finish();
        return super.onOptionsItemSelected(item);
    }
}
