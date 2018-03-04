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
import com.shuwoapp.data.Need;
import com.shuwoapp.data.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;


public class LoveNeed extends Activity implements AdapterView.OnItemClickListener {
    ListView lv;
    List<Need> needList;
    LinearLayout footer;
    MyListAdapter adapter;
    BmobQuery<User> query = new BmobQuery<>("_User");
    User myuser = new User();
    int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_need);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.lv_need);
        footer = (LinearLayout) findViewById(R.id.load_footer);
        getData();
        lv.setOnItemClickListener(this);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limit += 5;
                getData();
            }
        });
    }

    private void getData() {
        BmobQuery<Need> query = new BmobQuery<Need>();
        query.setLimit(limit);
        query.addWhereEqualTo("need_over", false);
        query.findObjects(this, new FindListener<Need>() {
            @Override
            public void onSuccess(List<Need> object) {
                // TODO Auto-generated method stub
                needList = object;
                if (limit == 10) {
                    createList();
                } else {
                    adapter.notifyDataSetChanged();
                }
                setListViewHeightBasedOnChildren(lv);

            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(LoveNeed.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createList() {
        adapter = new MyListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
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

    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return needList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.love_need_item, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_need_name);
                holder.tv_book = (TextView) convertView.findViewById(R.id.tv_need_book);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_need_time);
                holder.tv_school = (TextView) convertView.findViewById(R.id.tv_need_school);
                holder.tv_reason = (TextView) convertView.findViewById(R.id.tv_need_reason);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_reason.setText(needList.get(position).getNeed_reason());
            query.getObject(LoveNeed.this, needList.get(position).getNeed_needer(), new GetListener<User>() {
                @Override
                public void onSuccess(User user) {
                    myuser = user;
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(LoveNeed.this, s, Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_title.setText(needList.get(position).getNeed_nname());
            holder.tv_school.setText(needList.get(position).getNeed_nschool());
            holder.tv_time.setText(needList.get(position).getNeed_deadline());
            holder.tv_book.setText(needList.get(position).getNeed_book());
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_title, tv_book, tv_time, tv_school, tv_reason;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(LoveNeed.this, LoveWantN.class);
        intent.putExtra("needid", needList.get(position).getObjectId());
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
