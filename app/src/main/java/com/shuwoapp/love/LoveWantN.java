package com.shuwoapp.love;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.ResultActivity;
import com.shuwoapp.data.Need;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;


public class LoveWantN extends Activity implements View.OnClickListener {
    Button bt;
    TextView tv_detail, tv_title;
    Need need = new Need();

    String userid, needid, school, phone, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_want_n);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        name = sp.getString("realName", "");
        school = sp.getString("school", "");
        phone = sp.getString("phone", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(LoveWantN.this, SetLogin.class);
            startActivity(intent1);
        }

        Intent intent = getIntent();
        needid = intent.getStringExtra("needid");
        bt = (Button) findViewById(R.id.bt_wantn);
        tv_title = (TextView) findViewById(R.id.tv_charity_title);
        tv_detail = (TextView) findViewById(R.id.tv_charity_detail);
        getData();
        bt.setOnClickListener(this);
    }

    private void getData() {
        BmobQuery<Need> query = new BmobQuery<Need>();
        query.addWhereEqualTo("need_over", false);
        query.getObject(this, needid, new GetListener<Need>() {
            @Override
            public void onSuccess(Need object) {
                // TODO Auto-generated method stub
                need = object;
                String s = "求书者：" + need.getNeed_nname() + "\n"
                        + "所在学校：" + need.getNeed_nschool() + "\n"
                        + "理由：" + need.getNeed_reason() + "\n"
                        + "截止时间：" + need.getNeed_deadline();

                tv_detail.setText(s);
                tv_title.setText(need.getNeed_book());
            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(LoveWantN.this, arg0, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
//        if(need.getNeed_giver().isEmpty()) {
        need.setObjectId(needid);
        need.setNeed_giver(userid);
        need.setNeed_gname(name);
        need.setNeed_gphone(phone);
        need.setNeed_gschool(school);
        need.setNeed_over(true);
        need.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                createRecord();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveWantN.this, s, Toast.LENGTH_SHORT).show();
            }
        });
//        }else {
//            Toast.makeText(LoveWantN.this,"感谢您的爱心，求书者已获得帮助了！", Toast.LENGTH_SHORT).show();
//        }
    }

    private void createRecord() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String cloudCodeName = "createRecord";
        JSONObject params = new JSONObject();
        try {
            params.put("text", need.getNeed_book());
            params.put("kind", "Need");
            params.put("user", userid);
            params.put("date", date);
            params.put("id", need.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(LoveWantN.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                Intent intent = new Intent(LoveWantN.this, ResultActivity.class);
                intent.putExtra("result", "ok");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveWantN.this, s, Toast.LENGTH_SHORT).show();
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
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
