package com.shuwoapp.set;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import com.shuwoapp.data.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class SetResetPass extends Activity {
    EditText ed, ed1;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.set_reset_pass);
        ed = (EditText) findViewById(R.id.ed_reset);
        ed1 = (EditText) findViewById(R.id.ed_reset1);
        bt = (Button) findViewById(R.id.bt_reset);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = ed.getText().toString();
                String pass1 = ed1.getText().toString();
                if (pass.equals(pass1)) {
                    SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                    String p = sp.getString("password", "");
                    User myuser = (User) BmobUser.getCurrentUser(SetResetPass.this);
                    myuser.updateCurrentUserPassword(SetResetPass.this, p, pass, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SetResetPass.this, ResultActivity.class);
                            intent.putExtra("result", "ok");
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(SetResetPass.this, s, Toast.LENGTH_SHORT).show();
                        }

                    });
                }
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
