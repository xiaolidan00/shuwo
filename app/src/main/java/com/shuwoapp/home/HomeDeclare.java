package com.shuwoapp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shuwoapp.R;
import com.shuwoapp.ResultActivity;
import com.shuwoapp.data.Book;
import com.shuwoapp.data.Record;
import com.shuwoapp.pic.SelectPictureActivity;
import com.shuwoapp.set.SetLogin;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;


public class HomeDeclare extends Activity implements View.OnClickListener {
    private EditText ed_name, ed_author, ed_publisher, ed_time, ed_price, ed_size, ed_isbn, ed_deal;
    private Button bt_declare;
    ImageView im;
    private Spinner sp_way, sp_school, sp_kind;
    private String name;
    private String author;
    private String publisher;
    private String time;
    private String size;
    private float price;
    private String isbn;
    private String userid;
    private String school;
    private String kind;
    private String way;
    private String deal;
    private String path;
    ProgressDialog pd;
    BmobFile pic;
    Book book = new Book();
    private static final int REQUEST_PICK = 0;
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_declare);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //检查是否登录
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(HomeDeclare.this, SetLogin.class);
            startActivity(intent1);
            finish();
        }

        pd = new ProgressDialog(HomeDeclare.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.waitford));
        pd.setIndeterminate(true);

        ed_name = (EditText) findViewById(R.id.ed_book_name);
        ed_author = (EditText) findViewById(R.id.ed_book_author);
        ed_publisher = (EditText) findViewById(R.id.ed_book_publish);
        ed_time = (EditText) findViewById(R.id.ed_book_time);
        ed_price = (EditText) findViewById(R.id.ed_book_price);
        ed_size = (EditText) findViewById(R.id.ed_book_size);
        ed_isbn = (EditText) findViewById(R.id.ed_book_isbn);
        sp_kind = (Spinner) findViewById(R.id.sp_book_kind);
        sp_school = (Spinner) findViewById(R.id.sp_book_school);
        sp_way = (Spinner) findViewById(R.id.sp_book_way);
        ed_deal = (EditText) findViewById(R.id.ed_deal);
        bt_declare = (Button) findViewById(R.id.bt_declare);
        im = (ImageView) findViewById(R.id.im_declare);
        //调用图片加载器
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(50)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

        bt_declare.setOnClickListener(this);
        //获取书籍分类
        sp_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kind = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(HomeDeclare.this, "请选择分类", Toast.LENGTH_SHORT).show();
            }
        });
        //获取学校
        sp_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                school = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(HomeDeclare.this, "请选择学校", Toast.LENGTH_SHORT).show();
            }
        });
        //获取方式
        sp_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                way = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(HomeDeclare.this, "请选择方式", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //获取数据
    private void getData() {
        name = ed_name.getText().toString();
        author = ed_author.getText().toString();
        publisher = ed_publisher.getText().toString();
        time = ed_time.getText().toString();
        size = ed_size.getText().toString();
        price = Float.parseFloat(ed_price.getText().toString());
        isbn = ed_isbn.getText().toString();
        deal = ed_deal.getText().toString();
    }
    //创建保存书籍信息
    private void saveData() {
        getData();
        book.setBook_way(way);
        book.setBook_deal(deal);
        book.setBook_oschool(school);
        book.setBook_owner(userid);
        book.setBook_title(name);
        book.setBook_author(author);
        book.setBook_publisher(publisher);
        book.setBook_time(time);
        book.setBook_price(price);
        book.setBook_isbn(isbn);
        book.setBook_size(size);
        book.setBook_over(false);
        book.setBook_kind(kind);
        book.setBook_pic(pic);
        book.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                createRecord();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(HomeDeclare.this, s, Toast.LENGTH_SHORT).show();
            }
        });

    }
    //上传选中图片
    public void declarePic(View view) {
        if (selectedPicture != null) {
            for (int i = 0; i < selectedPicture.size(); i++) {
                selectedPicture.remove(i);
            }
        }
        startActivityForResult(new Intent(this, SelectPictureActivity.class),
                REQUEST_PICK);
    }
    //返回选择图片
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            ImageLoader.getInstance().displayImage(
                    "file://" + selectedPicture.get(0), im);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //发布书籍
        switch (id) {
            case R.id.bt_declare:
                createBook();
                break;
        }
    }
    //创建新书籍
    private void createBook() {
        pd.show();
        path = selectedPicture.get(0);
        BTPFileResponse response1 = BmobProFile.getInstance(this).upload(path, new UploadListener() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(HomeDeclare.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {
                pic = bmobFile;
                saveData();
                pd.dismiss();
            }

            @Override
            public void onProgress(int i) {
            }
        });


    }
        //船家操作记录
    private void createRecord() {
        Record record = new Record();
        record.setRecord_id(book.getObjectId());
        record.setRecord_kind("Book");
        record.setRecord_user(userid);
        record.setRecord_text(name);
        record.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(HomeDeclare.this, ResultActivity.class);
                intent.putExtra("result", "ok");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(HomeDeclare.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
