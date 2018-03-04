package com.shuwoapp.love;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.data.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class LoveRank extends Activity {
    String name[], rank[], score[];
    int LEN = 10;
    List<User> userList;
    ListView lv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_rank);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.lv_rank);
        getData();
    }
//获取排名信息
    private void getData() {
        BmobQuery<User> query = new BmobQuery<User>();
        query.setLimit(10);
        query.order("-score");
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> object) {
                // TODO Auto-generated method stub
                userList = object;
                if (userList != null) {
                    createList();
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(LoveRank.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createList() {
        LEN = userList.size();
        name = new String[LEN];
        rank = new String[LEN];
        score = new String[LEN];
        for (int i = 0; i < LEN; i++) {
            rank[i] = "第" + String.valueOf(i + 1) + "名";
            score[i] = "积分：" + String.valueOf(userList.get(i).getScore());
            name[i] = userList.get(i).getNickName();
        }
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < LEN; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", name[i]);
            item.put("rank", rank[i]);
            item.put("score", score[i]);
            listItem.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listItem, R.layout.love_rank_item,
                new String[]{"name", "rank", "score"},
                new int[]{R.id.tv_rank_name, R.id.tv_rank_num, R.id.tv_rank_score});
        lv.setAdapter(adapter);

        setListViewHeightBasedOnChildren(lv);
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
