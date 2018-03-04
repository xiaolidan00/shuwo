package com.shuwoapp.love;

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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.data.Give;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class LoveGive extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView lv;
    MyListAdapter adapter;
    List<Give> giveList;
    int limit = 5;
    LinearLayout footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_give);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.lv_give);
        getData();
        footer = (LinearLayout) findViewById(R.id.load_footer);
        footer.setOnClickListener(this);
        lv.setOnItemClickListener(this);
    }
//获取捐赠书籍数据
    private void getData() {
        BmobQuery<Give> query = new BmobQuery<Give>();
        query.setLimit(limit);
        query.addWhereEqualTo("give_over", false);
        query.findObjects(this, new FindListener<Give>() {
            @Override
            public void onSuccess(List<Give> object) {
                // TODO Auto-generated method stub
                giveList = object;
                if (limit == 5) {
                    createList();
                } else {
                    adapter.notifyDataSetChanged();
                }
                setListViewHeightBasedOnChildren(lv);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(LoveGive.this, "fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }
//加载更多数据
    @Override
    public void onClick(View v) {
        limit += 5;
        getData();
    }

    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return giveList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.love_give_item, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_give_name);
                holder.tv_book = (TextView) convertView.findViewById(R.id.tv_give_book);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_give_time);
                holder.tv_school = (TextView) convertView.findViewById(R.id.tv_give_school);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(giveList.get(position).getGive_gname());
            holder.tv_school.setText(giveList.get(position).getGive_gschool());
            holder.tv_time.setText(giveList.get(position).getGive_deadline());
            holder.tv_book.setText(giveList.get(position).getGive_book());
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_title, tv_book, tv_time, tv_school;
    }

    private void createList() {
        adapter = new MyListAdapter(this);
        lv.setAdapter(adapter);
    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(LoveGive.this, LoveWantG.class);
        intent.putExtra("giveid", giveList.get(position).getObjectId());
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
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
