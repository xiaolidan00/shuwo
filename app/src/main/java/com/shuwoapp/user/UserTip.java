package com.shuwoapp.user;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.shuwoapp.R;
import com.shuwoapp.data.User;
import com.shuwoapp.set.SetLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserTip extends Activity implements AdapterView.OnItemClickListener {
    ListView lv;
    String userid, table;
    User myuser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_tip);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(UserTip.this, SetLogin.class);
            startActivity(intent1);
            finish();
        }

        lv = (ListView) findViewById(R.id.lv_tip);
        creatList();
        lv.setOnItemClickListener(this);
    }


    private void creatList() {

        List<Map<String, Object>> list = new ArrayList<>();
        String[] s = {"交易提醒", "捐出提醒", "受助提醒", "活动提醒"};
        int imid = R.drawable.tip;
        for (int i = 0; i < s.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", imid);
            map.put("title", s[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.user_tip_item,
                new String[]{"image", "title"}, new int[]{R.id.im_tip, R.id.tv_tip});
        lv.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(UserTip.this, UserTipDetail.class);
        intent.putExtra("pos", position);
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
