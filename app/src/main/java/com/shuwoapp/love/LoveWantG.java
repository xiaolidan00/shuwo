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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.ResultActivity;
import com.shuwoapp.data.Give;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;


public class LoveWantG extends Activity implements View.OnClickListener {
    Button bt;
    ImageView im;
    BmobFile bf;
    ProgressDialog pd;
    Give give = new Give();
    String userid, giveid, school, name, phone;
    TextView tv_detail, tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_want_g);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        name = sp.getString("realName", "");
        school = sp.getString("school", "");
        phone = sp.getString("phone", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(LoveWantG.this, SetLogin.class);
            startActivity(intent1);
        }

        pd = new ProgressDialog(LoveWantG.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.waitforl));
        pd.setIndeterminate(true);
        Intent intent = getIntent();
        giveid = intent.getStringExtra("giveid");
        bt = (Button) findViewById(R.id.bt_wantg);
        tv_title = (TextView) findViewById(R.id.tv_charity_title);
        tv_detail = (TextView) findViewById(R.id.tv_charity_detail);
        im = (ImageView) findViewById(R.id.im_wantg);
        getData();

        bt.setOnClickListener(this);
    }

    private void getData() {
        pd.show();
        BmobQuery<Give> query = new BmobQuery<Give>();
        query.addWhereEqualTo("give_over", false);
        query.getObject(this, giveid, new GetListener<Give>() {
            @Override
            public void onSuccess(Give object) {
                // TODO Auto-generated method stub
                give = object;
                String s = "捐赠者:" + give.getGive_gname() + "\n"
                        + "所在学校:" + give.getGive_gschool() + "\n"
                        + "截止时间:" + give.getGive_deadline();

                tv_detail.setText(s);
                tv_title.setText(give.getGive_book());
                if (give.getGive_pic() == null) {
                    im.setImageResource(R.drawable.book1);
                } else {
                    bf = give.getGive_pic();
                    bf.loadImageThumbnail(LoveWantG.this, im, 120, 200, 100);
                }

            }

            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(LoveWantG.this, arg0, Toast.LENGTH_SHORT).show();
            }
        });
        pd.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (give.getGive_needer().isEmpty()) {
            give.setObjectId(giveid);
            give.setGive_needer(userid);
            give.setGive_nname(name);
            give.setGive_nschool(school);
            give.setGive_nphone(phone);
            give.setGive_over(true);
            give.update(this, new UpdateListener() {
                @Override
                public void onSuccess() {
                    createRecord();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(LoveWantG.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoveWantG.this, getResources().getString(R.string.slow), Toast.LENGTH_SHORT).show();
        }

    }

    private void createRecord() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String cloudCodeName = "createRecord";
        JSONObject params = new JSONObject();
        try {
            params.put("text", give.getGive_book());
            params.put("kind", "Give");
            params.put("user", userid);
            params.put("date", date);
            params.put("id", give.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(LoveWantG.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                Intent intent = new Intent(LoveWantG.this, ResultActivity.class);
                intent.putExtra("result", "ok");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveWantG.this, s, Toast.LENGTH_SHORT).show();
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
