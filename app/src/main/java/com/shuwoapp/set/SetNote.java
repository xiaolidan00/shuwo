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
import com.shuwoapp.data.Note;

import cn.bmob.v3.listener.SaveListener;

public class SetNote extends Activity {
    String userid, text;
    Button bt;
    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_note);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(SetNote.this, SetLogin.class);
            startActivity(intent1);
        }
        bt = (Button) findViewById(R.id.bt_notebox);
        ed = (EditText) findViewById(R.id.ed_notebox);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = ed.getText().toString();
                ed.setText("");
                Note note = new Note();
                note.setNote_text(text);
                note.setNote_user(userid);
                note.save(SetNote.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SetNote.this, "已经收到您的意见", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(SetNote.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
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
