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
import com.shuwoapp.data.Charity;
import com.shuwoapp.data.Join;
import com.shuwoapp.data.Record;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;

public class LoveJoin extends Activity implements View.OnClickListener {
    Button bt;
    TextView tv;
    Join join=new Join();

    String userid,charityid,phone,school,realname,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_join);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp=getSharedPreferences("user", MODE_PRIVATE);
        userid=sp.getString("id","");
        phone=sp.getString("school","");
        school=sp.getString("phone","");
        realname=sp.getString("realname","");
        if(userid.equals("")){
            Intent intent1=new Intent(LoveJoin.this, SetLogin.class);
            startActivity(intent1);
        }

        Intent intent=getIntent();
        charityid=intent.getStringExtra("charityid");
        title=intent.getStringExtra("title");
        bt= (Button) findViewById(R.id.bt_join);
        tv= (TextView) findViewById(R.id.tv_join);
        getData();
        bt.setOnClickListener(this);
    }
    private void getData() {
        String cloudCodeName = "getCharity";
        JSONObject params = new JSONObject();
        try {
            params.put("charity", charityid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(LoveJoin.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                tv.setText(result.toString());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveJoin.this, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        join.setJoin_charityid(charityid);
        join.setJoin_userid(userid);
        join.setJoin_phone(phone);
        join.setJoin_name(realname);
        join.setJoin_school(school);
        join.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                createRecord();
            }

            @Override
            public void onFailure(int i, String s) {
                Intent intent = new Intent(LoveJoin.this, ResultActivity.class);
                intent.putExtra("result", "no");
                startActivity(intent);
            }
        });
        finish();
    }

    private void createRecord() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String cloudCodeName = "createRecord";
        JSONObject params = new JSONObject();
        try {
            params.put("text",title);
            params.put("kind", "Join");
            params.put("user", userid);
            params.put("date", date);
            params.put("id", join.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(LoveJoin.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                Intent intent = new Intent(LoveJoin.this, ResultActivity.class);
                intent.putExtra("result", "ok");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveJoin.this, s, Toast.LENGTH_SHORT).show();
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
