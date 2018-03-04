package com.shuwoapp.set;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.MainActivity;
import com.shuwoapp.R;
import com.shuwoapp.data.User;

import cn.bmob.v3.listener.SaveListener;


public class SetLogin extends Activity implements View.OnClickListener {
    private TextView forget, register;
    private Button land;
    private CheckBox remember;
    User myuser;
    private EditText username, password;
    private String loginname, loginpass, loginid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_login);
        username = (EditText) findViewById(R.id.ed_login_user);
        password = (EditText) findViewById(R.id.ed_password);
        remember = (CheckBox) findViewById(R.id.cb_remember);
        forget = (TextView) findViewById(R.id.tv_forget);
        register = (TextView) findViewById(R.id.tv_register);
        land = (Button) findViewById(R.id.bt_land);
        forget.setOnClickListener(this);
        register.setOnClickListener(this);
        land.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        username.setText(sp.getString("email", ""));
        password.setText(sp.getString("password", ""));
        remember.setChecked(sp.getBoolean("remember", false));
    }

    @SuppressLint("CommitPrefEdits")
    private void createRemember() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", loginid);
        if (remember.isChecked()) {
            editor.putString("email", loginname);
            editor.putString("password", loginpass);
            editor.putBoolean("remember", true);
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                Intent intent1 = new Intent(SetLogin.this, SetForget.class);
                startActivity(intent1);
                break;
            case R.id.tv_register:
                Intent intent2 = new Intent(SetLogin.this, SetRegister.class);
                startActivity(intent2);
                break;
            case R.id.bt_land:
                loginUser();
                break;
        }
        finish();
    }

    private void loginUser() {
        myuser = new User();
        loginname = username.getText().toString();
        loginpass = password.getText().toString();
        myuser.setUsername(loginname);
        myuser.setPassword(loginpass);
        myuser.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Intent intent;
                String s = getResources().getString(R.string.login_ok);
                Toast.makeText(SetLogin.this, s, Toast.LENGTH_LONG).show();
                loginid = myuser.getObjectId();
                createRemember();
                if (myuser.getEmailVerified()) {
                    if (myuser.getSchool() == null || myuser.getNickName() == null || myuser.getRealName() == null ||
                            myuser.getCollege() == null || myuser.getStuId() == null) {
                        intent = new Intent(SetLogin.this, SetActivity.class);
                    } else {
                        intent = new Intent(SetLogin.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SetLogin.this, "请邮箱验证~", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SetLogin.this, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
