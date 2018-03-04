package com.shuwoapp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.data.Book;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;


public class HomeKindReasult extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    List<Book> booklist;
    ListView lv;
    float price;
    int limit = 10;
    boolean[] select;
    LinearLayout footer;
    MyListAdapter adapter;
    String kind, way, school;
    BmobFile bf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_kind);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        footer = (LinearLayout) findViewById(R.id.load_footer);
        lv = (ListView) findViewById(R.id.lv_kind);
        footer.setOnClickListener(this);
        //获取分类内容
        Intent intent = getIntent();
        select = intent.getBooleanArrayExtra("select");
        if (select[0]) {
            kind = intent.getStringExtra("kind");
        }
        if (select[1]) {
            price = intent.getFloatExtra("price", 10);
        }
        if (select[2]) {
            way = intent.getStringExtra("way");
        }
        if (select[3]) {
            school = intent.getStringExtra("school");
        }
        getData();
        lv.setOnItemClickListener(this);
    }
    //获取符合要求的书籍信息
    private void getData() {
        BmobQuery<Book> query = new BmobQuery<Book>();
        if (select[0]) {
            query.addWhereEqualTo("book_kind", kind);
        }
        if (select[1]) {
            if (way.equals("交易")) {

            } else {
                query.addWhereEqualTo("book_deal", price);
            }
        }
        if (select[2]) {
            query.addWhereEqualTo("book_way", way);
        }
        if (select[3]) {
            query.addWhereEqualTo("book_school", school);
        }
        query.addWhereEqualTo("book_over", false);
        query.setLimit(limit);
        query.order("updateAt");
        query.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                // TODO Auto-generated method stub
                booklist = object;
                if (booklist != null) {
                    if (limit == 10) {
                        createList();
                    } else if (limit == 100) {
                        footer.setVisibility(View.GONE);
                        Toast.makeText(HomeKindReasult.this, "no more book", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    setListViewHeightBasedOnChildren(lv);
                } else {
                    Toast.makeText(HomeKindReasult.this, "no book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(HomeKindReasult.this, "fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //创建书籍列表
    private void createList() {
        adapter = new MyListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
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

    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return booklist.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.home_search_item, null);
                holder = new ViewHolder();
                holder.im = (ImageView) convertView.findViewById(R.id.im_search);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_search_title);
                holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_search_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (booklist.get(position).getBook_pic() == null) {
                holder.im.setImageResource(R.drawable.book1);
            } else {
                bf = booklist.get(position).getBook_pic();
                bf.loadImageThumbnail(HomeKindReasult.this, holder.im, 64, 64, 100);
            }

            holder.tv_title.setText(booklist.get(position).getBook_title());
            holder.tv_detail.setText(booklist.get(position).getBook_oschool());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView im;
        TextView tv_title, tv_detail;
    }
    //进入书籍详细信息
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String objectId = booklist.get(position).getObjectId();
        Intent intent = new Intent(HomeKindReasult.this, HomeBook.class);
        intent.putExtra("objectId", objectId);
        startActivity(intent);
    }
    //加载更多
    @Override
    public void onClick(View v) {
        limit += 5;
        getData();
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
//            返回
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
