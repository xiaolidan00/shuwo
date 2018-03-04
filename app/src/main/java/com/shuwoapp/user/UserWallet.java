package com.shuwoapp.user;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.shuwoapp.R;
import com.shuwoapp.data.User;
import com.shuwoapp.set.SetLogin;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;


public class UserWallet extends Activity {
    String userid, account;
    float money, profit;
    User myuser = new User();
    TextView tv_account, tv_money, tv_profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_wallet);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        account = sp.getString("account", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(UserWallet.this, SetLogin.class);
            startActivity(intent1);
        }
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(this, userid, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                myuser = user;
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        profit = myuser.getProfit();
        money = myuser.getMoney();
        tv_account = (TextView) findViewById(R.id.tv_wallet_account);
        tv_profit = (TextView) findViewById(R.id.tv_wallet_profit);
        tv_money = (TextView) findViewById(R.id.tv_wallet_money);
        tv_money.setText(String.valueOf(money));
        tv_profit.setText(String.valueOf(profit));
        tv_account.setText(account);
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
