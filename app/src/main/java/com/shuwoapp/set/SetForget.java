package com.shuwoapp.set;

import android.app.ActionBar;
import android.app.Activity;
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

public class SetForget extends Activity implements View.OnClickListener {
    EditText ed_school, ed_stuId, ed_college, ed_realName, ed_nickName, ed_account, ed_phone, ed_email;
    String school, stuId, college, realName, nickName, phone, email, account;
    String school0, stuId0, college0, realName0, nickName0, phone0, email0, account0;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_forget);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        phone0 = sp.getString("phone", "");
        school0 = sp.getString("school", "");
        stuId0 = sp.getString("stuId", "");
        college0 = sp.getString("college", "");
        realName0 = sp.getString("realName", "");
        nickName0 = sp.getString("nickNanme", "");
        email0 = sp.getString("email", "");
        account0 = sp.getString("account", "");
        ed_account = (EditText) findViewById(R.id.ed_account);
        ed_school = (EditText) findViewById(R.id.ed_school);
        ed_stuId = (EditText) findViewById(R.id.ed_id);
        ed_college = (EditText) findViewById(R.id.ed_college);
        ed_realName = (EditText) findViewById(R.id.ed_name);
        ed_nickName = (EditText) findViewById(R.id.ed_nickname);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_email = (EditText) findViewById(R.id.ed_email);
        bt = (Button) findViewById(R.id.bt_forget_find);
        bt.setOnClickListener(this);
    }

    private void getAllValue() {
        school = ed_school.getText().toString();
        stuId = ed_stuId.getText().toString();
        college = ed_college.getText().toString();
        realName = ed_realName.getText().toString();
        nickName = ed_nickName.getText().toString();
        phone = ed_phone.getText().toString();
        email = ed_email.getText().toString();
        account = ed_account.getText().toString();
    }


    @Override
    public void onClick(View v) {
        getAllValue();
        if (college.equals(college0) || school.equals(school0) || stuId.equals(stuId0) &&
                realName.equals(realName0) || nickName.equals(nickName0) ||
                email.equals(email0)) {
            Intent intent = new Intent(SetForget.this, SetResetPass.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SetForget.this, "信息匹配错误，请检查清楚~", Toast.LENGTH_SHORT);
        }
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
