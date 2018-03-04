package com.shuwoapp.love;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;


public class LoveNeedD extends Activity implements View.OnClickListener {
    String reason, book, time;
    EditText ed_reason, ed_book, ed_time;
    Button bt;
    String userid, nickname, school, phone;
    ProgressDialog pd;
    Need need = new Need();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_need_d);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        school = sp.getString("school", "");
        phone = sp.getString("phone", "");
        nickname = sp.getString("nickName", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(LoveNeedD.this, SetLogin.class);
            startActivity(intent1);
        }
        pd = new ProgressDialog(LoveNeedD.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.waitford));
        pd.setIndeterminate(true);
        ed_book = (EditText) findViewById(R.id.ed_needd_book);
        ed_reason = (EditText) findViewById(R.id.ed_needd_reason);
        ed_reason.setSingleLine(false);
        ed_reason.setHorizontallyScrolling(false);
        ed_time = (EditText) findViewById(R.id.ed_needd_time);
        bt = (Button) findViewById(R.id.bt_need_declare);
        bt.setOnClickListener(this);
    }

    private void getData() {
        book = ed_book.getText().toString();
        reason = ed_reason.getText().toString();
        time = ed_time.getText().toString();
    }

    @Override
    public void onClick(View v) {
        pd.show();
        getData();
        need.setNeed_book(book);
        need.setNeed_reason(reason);
        need.setNeed_deadline(time);
        need.setNeed_needer(userid);
        need.setNeed_nname(nickname);
        need.setNeed_nphone(phone);
        need.setNeed_nschool(school);
        need.setNeed_over(false);
        need.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                createRecord();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveNeedD.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        pd.dismiss();
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
        cloudCode.callEndpoint(LoveNeedD.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                Intent intent = new Intent(LoveNeedD.this, ResultActivity.class);
                intent.putExtra("result", "ok");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveNeedD.this, s, Toast.LENGTH_SHORT).show();
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
