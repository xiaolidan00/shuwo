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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shuwoapp.MainActivity;
import com.shuwoapp.R;
import com.shuwoapp.ResultActivity;
import com.shuwoapp.data.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;


public class SetActivity extends Activity implements View.OnClickListener {
    LinearLayout agree, note;
    User myuser = new User();
    Button out, save;
    BmobFile bf;
    String userid;
    ImageView im;
    EditText ed_school, ed_stuId, ed_college, ed_realName, ed_nickName, ed_phone, ed_email, ed_account;
    String school, stuId, college, realName, nickName, phone, email, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_person);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        phone = sp.getString("phone", "");
        school = sp.getString("school", "");
        stuId = sp.getString("stuId", "");
        college = sp.getString("college", "");
        realName = sp.getString("realName", "");
        nickName = sp.getString("nickNanme", "");
        account = sp.getString("account", "");
        email = sp.getString("email", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(SetActivity.this, SetLogin.class);
            startActivity(intent1);
            finish();
        }
        BmobQuery<User> query = new BmobQuery<>("_User");
        query.getObject(this, userid, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                myuser = user;
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SetActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        im = (ImageView) findViewById(R.id.im_set);
        if (myuser.getIcon() == null) {
            im.setImageResource(R.drawable.icon);
        } else {
            bf = myuser.getIcon();
            bf.loadImageThumbnail(this, im, 48, 48, 100);
        }
        agree = (LinearLayout) findViewById(R.id.set_agree);
        note = (LinearLayout) findViewById(R.id.set_notebox);
        out = (Button) findViewById(R.id.bt_set_out);
        save = (Button) findViewById(R.id.bt_set_save);
        ed_account = (EditText) findViewById(R.id.ed_account);
        ed_school = (EditText) findViewById(R.id.ed_school);
        ed_stuId = (EditText) findViewById(R.id.ed_id);
        ed_college = (EditText) findViewById(R.id.ed_college);
        ed_realName = (EditText) findViewById(R.id.ed_name);
        ed_nickName = (EditText) findViewById(R.id.ed_nickname);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_email = (EditText) findViewById(R.id.ed_email);
        out.setOnClickListener(this);
        save.setOnClickListener(this);
        agree.setOnClickListener(this);
        note.setOnClickListener(this);
    }

    private void getInfo() {
        school = ed_school.getText().toString();
        stuId = ed_stuId.getText().toString();
        college = ed_college.getText().toString();
        realName = ed_realName.getText().toString();
        nickName = ed_nickName.getText().toString();
        phone = ed_phone.getText().toString();
        email = ed_email.getText().toString();
        account = ed_account.getText().toString();
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("school", school);
        editor.putString("college", college);
        editor.putString("stuId", stuId);
        editor.putString("realName", realName);
        editor.putString("nickName", nickName);
        editor.putString("phone", phone);
        editor.putString("account", account);
        editor.putString("email", email);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ed_school.setText(school);
        ed_college.setText(college);
        ed_stuId.setText(stuId);
        ed_email.setText(email);
        ed_nickName.setText(nickName);
        ed_realName.setText(realName);
        ed_phone.setText(phone);
        ed_account.setText(account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_agree:
                Intent intent = new Intent(SetActivity.this, SetAgreement.class);
                startActivity(intent);
                break;
            case R.id.set_notebox:
                Intent intent2 = new Intent(SetActivity.this, SetNote.class);
                startActivity(intent2);
                break;
            case R.id.bt_set_out:
                BmobUser.logOut(this);
                Intent intent1 = new Intent(SetActivity.this, SetLogin.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.bt_set_save:
                getInfo();
                saveData();
                finish();
                break;
        }

    }

    private void saveData() {
        myuser.setRealName(realName);
        myuser.setNickName(nickName);
        myuser.setSchool(school);
        myuser.setCollege(college);
        myuser.setStuId(stuId);
        myuser.setEmail(email);
        myuser.setAccount(account);
        myuser.setMobilePhoneNumber(phone);
        if (realName == null || nickName == null || phone == null ||
                college == null || stuId == null || email == null) {
            Toast.makeText(SetActivity.this, "请填写完整！", Toast.LENGTH_SHORT).show();
        } else {
            myuser.setEmailVerified(true);
        }
        myuser.update(this, userid, new UpdateListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(SetActivity.this, ResultActivity.class);
                intent.putExtra("result", "ok");
                startActivity(intent);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SetActivity.this, s, Toast.LENGTH_SHORT).show();
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


