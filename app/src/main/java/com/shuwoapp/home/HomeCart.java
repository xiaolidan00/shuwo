package com.shuwoapp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.MainActivity;
import com.shuwoapp.R;
import com.shuwoapp.data.Book;
import com.shuwoapp.set.SetLogin;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

//购物车
public class HomeCart extends Activity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private ListView lv;
    private MyListAdapter adapter;
    private Button order;
    private float total;
    private Book mybook = new Book();
    private List<Book> bookList;
    private BmobFile bf;
    private String[] billbook;
    private Boolean over = false;
    private String userid, objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_cart);

        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.lv_cart);
        order = (Button) findViewById(R.id.bt_order);
        //检查是否登录
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(HomeCart.this, SetLogin.class);
            startActivity(intent1);
            finish();
        }
        //获取添加书籍id
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");

        //加载购物车中图书
        if (objectId != null) {
            BmobQuery<Book> query = new BmobQuery<>("Book");
            query.getObject(this, objectId, new GetListener<Book>() {
                @Override
                public void onSuccess(Book book) {
                    mybook = book;
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(HomeCart.this, s, Toast.LENGTH_SHORT).show();
                }
            });
            //更新书籍信息
            if (mybook.getBook_buyer() == null) {
                Book book = new Book();
                book.setBook_buyer(userid);
                book.update(this, objectId, new UpdateListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(HomeCart.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        getData();
        order.setOnClickListener(this);
        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);

    }
    //创建购物清单列表
    private void createList() {
        adapter = new MyListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        setListViewHeightBasedOnChildren(lv);
    }
    //测量列表高度
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View v = adapter.getView(i, null, listView);
            v.measure(0, 0);
            height += v.getMeasuredHeight();
        }
        ViewGroup.LayoutParams ps = listView.getLayoutParams();
        ps.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) ps).setMargins(15, 15, 15, 15);
        listView.setLayoutParams(ps);
    }
    //获取购物清单
    private void getData() {
        BmobQuery<Book> query = new BmobQuery<Book>();
        query.addWhereEqualTo("book_over", false);
        query.addWhereEqualTo("book_buyer", userid);
        query.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                // TODO Auto-generated method stub
                bookList = object;
                if (bookList != null) {
                    createList();
                } else {
                    Toast.makeText(HomeCart.this, "你的购物车中没书", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(HomeCart.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return bookList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.home_cart_item, null);
                holder = new ViewHolder();
                holder.im = (ImageView) convertView.findViewById(R.id.im_cart);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_cart_name);
                holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_cart_way);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (bookList.get(position).getBook_pic() == null) {
                holder.im.setImageResource(R.drawable.book1);
            } else {
                bf = bookList.get(position).getBook_pic();
                bf.loadImageThumbnail(HomeCart.this, holder.im, 64, 64, 100);
            }

            holder.tv_title.setText(bookList.get(position).getBook_title());
            holder.tv_detail.setText(bookList.get(position).getBook_way());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView im;
        TextView tv_title, tv_detail;
    }
    //计算价格并下订单
    @Override
    public void onClick(View v) {
        String[] bookid = new String[bookList.size()];
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getBook_way().equals("买卖") ||
                    bookList.get(i).getBook_way().equals("租借")) {
                total =total+ Float.parseFloat(bookList.get(i).getBook_deal());
            }
            bookid[i] = bookList.get(i).getObjectId();
        }

        Intent intent = new Intent(HomeCart.this, HomeBill.class);
        intent.putExtra("bookid", bookid);
        intent.putExtra("total",total);
        startActivity(intent);

    }

    //删除选中图书
    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        String delete = getResources().getString(R.string.delete);
        String no = getResources().getString(R.string.no);
        String yes = getResources().getString(R.string.yes);
        final AlertDialog alert = new AlertDialog.Builder(HomeCart.this).create();
        alert.setMessage(delete);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String s = bookList.get(position).getBook_title();
                final String d = getResources().getString(R.string.deleted);
                String removeid = bookList.get(position).getObjectId();
                bookList.remove(position);
                Book book = new Book();
                book.setObjectId(removeid);
                book.remove("book_buyer");
                book.update(HomeCart.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(HomeCart.this, d + s, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(HomeCart.this, s, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.show();
        return true;
    }
    //了解书籍详细信息
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(HomeCart.this, HomeBook.class);
        intent.putExtra("objectId", bookList.get(position).getObjectId());
        startActivity(intent);
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
