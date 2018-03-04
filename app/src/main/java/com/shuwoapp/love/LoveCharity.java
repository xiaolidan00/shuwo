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
import com.shuwoapp.data.Charity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class LoveCharity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView lv;
    List<Charity> charity;
    LinearLayout footer;
    int limit = 5;
    MyListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_charity);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.lv_charity);
        footer = (LinearLayout) findViewById(R.id.load_footer);
        getData();
        lv.setOnItemClickListener(this);
        footer.setOnClickListener(this);
    }

    //获取公益活动信息
    private void getData() {
        BmobQuery<Charity> query = new BmobQuery<Charity>();
        query.setLimit(limit);
        query.findObjects(this, new FindListener<Charity>() {
            @Override
            public void onSuccess(List<Charity> object) {
                // TODO Auto-generated method stub
                charity = object;
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
                Toast.makeText(LoveCharity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createList() {
        adapter = new MyListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return charity.size();
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
            int i = position;
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.love_charity_item, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_charity_title);
                holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_charity_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String title, detail;

            title = charity.get(i).getCharity_title();
            detail = "活动时间：" + charity.get(i).getCharity_time() + "\n"
                    + "活动地点：" + charity.get(i).getCharity_address() + "\n"
                    + "活动对象：" + charity.get(i).getCharity_people() + "\n"
                    + "活动形式：" + charity.get(i).getCharity_form() + "\n"
                    + "活动奖励：" + charity.get(i).getCharity_award() + "\n"
                    + "报名方式：" + charity.get(i).getCharity_join() + "\n"
                    + "联系方式：" + charity.get(i).getCharity_phone() + "\n";

            holder.tv_title.setText(title);
            holder.tv_detail.setText(detail);
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_title, tv_detail;
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

    //进入公益活动详细信息
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(LoveCharity.this, LoveJoin.class);
        String charityid = charity.get(position).getObjectId();
        intent.putExtra("charityid", charityid);
        intent.putExtra("title", charity.get(position).getCharity_title());
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
            //返回
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
