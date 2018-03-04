package com.shuwoapp.user;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.data.Join;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

public class UserTipDetail extends Activity {
    String userid;
    TextView tv;
    String[] joinArr;
    int pos;
    ProgressDialog pd;
    List<Join> joinList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_tip_detail);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(UserTipDetail.this, SetLogin.class);
            startActivity(intent1);
            finish();
        }
        pd = new ProgressDialog(UserTipDetail.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.waitforl));
        pd.setIndeterminate(true);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", pos);
        tv = (TextView) findViewById(R.id.tv_tip_detail);
        createDetail(pos);

    }

    private void createDetail(int pos) {
        pd.show();
        String text = "";
        switch (pos) {
            case 0:
                text = "交易提醒";
                getBook();
                break;
            case 1:
                text = "捐出提醒";
                getGive();
                break;
            case 2:
                text = "受助提醒";
                getNeed();
                break;
            case 3:
                text = "活动提醒";
                getCharity();
                break;
        }
        tv.setText(text + "\n\n");
        pd.dismiss();
    }

    private void getCharity() {
        BmobQuery<Join> query = new BmobQuery<>();
        query.addWhereEqualTo("join_over", false);
        query.addWhereEqualTo("join_user", userid);
        query.findObjects(this, new FindListener<Join>() {
            @Override
            public void onSuccess(List<Join> list) {

                if (list != null) {
                    joinList = list;
                    joinArr = new String[joinList.size()];
                    for (int i = 0; i < joinList.size(); i++) {
                        joinArr[i] = joinList.get(i).getJoin_charityid();
                    }
                } else {
                    Toast.makeText(UserTipDetail.this, "无参加活动", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(UserTipDetail.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        String cloudCodeName = "getCharity";
        JSONObject params = new JSONObject();
        try {
            params.put("join", joinArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(UserTipDetail.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                tv.append(result.toString());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserTipDetail.this, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getNeed() {
        String cloudCodeName = "getNeed";
        JSONObject params = new JSONObject();
        try {
            params.put("user", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(UserTipDetail.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                tv.append(result.toString());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserTipDetail.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGive() {
        String cloudCodeName = "getGive";
        JSONObject params = new JSONObject();
        try {
            params.put("user", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(UserTipDetail.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                tv.append(result.toString());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserTipDetail.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBook() {
        String cloudCodeName = "getBook";
        JSONObject params = new JSONObject();
        try {
            params.put("owner", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(UserTipDetail.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                tv.append(result.toString());
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserTipDetail.this, s, Toast.LENGTH_SHORT).show();
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
