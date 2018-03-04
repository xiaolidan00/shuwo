package com.shuwoapp.user;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.data.User;
import com.shuwoapp.set.SetLogin;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;


public class UserScore extends Activity implements View.OnClickListener {

    ImageView im_sign;
    TextView tv_sign, sign_day, rank, score;
    User myuser = new User();
    String userid, date;
    FrameLayout f_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_score);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(UserScore.this, SetLogin.class);
            startActivity(intent1);
        }

        im_sign = (ImageView) findViewById(R.id.im_sign);
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        sign_day = (TextView) findViewById(R.id.tv_sign_day);
        rank = (TextView) findViewById(R.id.tv_score_rank);
        score = (TextView) findViewById(R.id.tv_sign_score);
        f_sign = (FrameLayout) findViewById(R.id.f_sign);
        tv_sign.setText(getResources().getString(R.string.user_tip_sign));
        im_sign.setImageResource(android.R.drawable.btn_star_big_off);
        f_sign.setOnClickListener(this);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(new Date());
        BmobQuery<User> query = new BmobQuery<>("_User");
        query.getObject(this, userid, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                myuser = user;
                if (myuser.getSigndate() != null) {
                    if (myuser.getSigndate().equals(date)) {
                        tv_sign.setText(getResources().getString(R.string.user_tip_signed));
                        im_sign.setImageResource(android.R.drawable.btn_star_big_on);
                    }
                }
                sign_day.setText(String.valueOf(myuser.getSignday()));
                score.setText(String.valueOf(myuser.getScore()));
                rank.setText(String.valueOf(myuser.getRank()));
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserScore.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createSign() {
        myuser.setScore(myuser.getScore() + 1);
        myuser.setSignday(myuser.getSignday() + 1);
        myuser.setSigndate(date);
        myuser.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                tv_sign.setText(getResources().getString(R.string.user_tip_signed));
                im_sign.setImageResource(android.R.drawable.btn_star_big_on);
                sign_day.setText(String.valueOf(myuser.getSignday()));
                rank.setText(String.valueOf(myuser.getRank()));
                score.setText(String.valueOf(myuser.getScore()));
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserScore.this, s, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (myuser.getSigndate() != null) {
            if (myuser.getSigndate().equals(date)) {
                Toast.makeText(UserScore.this, getResources().getString(R.string.user_tip_signed), Toast.LENGTH_SHORT).show();
            } else {
                createSign();
            }
        } else {
            createSign();
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
