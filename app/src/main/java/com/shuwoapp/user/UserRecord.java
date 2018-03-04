package com.shuwoapp.user;

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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.data.Record;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;


public class UserRecord extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    ListView lv;
    List<Record> recordList;
    String userid;
    MyListAdapter adapter;
    LinearLayout footer;
    int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_record);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.lv_record);
        footer = (LinearLayout) findViewById(R.id.load_footer);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(UserRecord.this, SetLogin.class);
            startActivity(intent1);
        }
        getData();
        footer.setOnClickListener(this);
    }

    private void getData() {
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.addWhereEqualTo("record_user", userid);
        query.setLimit(limit);
        query.findObjects(this, new FindListener<Record>() {
            @Override
            public void onSuccess(List<Record> object) {
                recordList = object;
                if (recordList != null) {
                    if (limit == 10) {
                        createList();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    setListViewHeightBasedOnChildren(lv);
                } else {
                    Toast.makeText(UserRecord.this, "无记录", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(UserRecord.this, "加载失败~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        limit += 5;
        getData();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        String delete = getResources().getString(R.string.delete);
        String no = getResources().getString(R.string.no);
        String yes = getResources().getString(R.string.yes);
        final AlertDialog alert = new AlertDialog.Builder(UserRecord.this).create();
        alert.setMessage(delete);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cloudCodeName = "deleteData";
                JSONObject params = new JSONObject();
                try {
                    params.put("classname", recordList.get(position).getRecord_kind());
                    params.put("dataid", recordList.get(position).getRecord_id());
                    params.put("record", recordList.get(position).getObjectId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
                cloudCode.callEndpoint(UserRecord.this, cloudCodeName, params, new CloudCodeListener() {
                    @Override
                    public void onSuccess(Object result) {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(UserRecord.this, s, Toast.LENGTH_SHORT).show();
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

    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return recordList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.user_record_item, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_record_text);
                holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_record_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(recordList.get(position).getRecord_text());
            holder.tv_detail.setText(recordList.get(position).getUpdatedAt());
            return convertView;
        }
    }

    class ViewHolder {
        TextView tv_title, tv_detail;
    }

    private void createList() {
        adapter = new MyListAdapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
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
