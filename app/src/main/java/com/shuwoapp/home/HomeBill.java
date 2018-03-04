package com.shuwoapp.home;

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
import android.widget.TextView;

import com.shuwoapp.MainActivity;
import com.shuwoapp.R;
import com.shuwoapp.set.SetLogin;

    //下订单
public class HomeBill extends Activity implements View.OnClickListener {
    private Button back, pay;
    private EditText receiver, adress, phone, note;
    private SharedPreferences sp;
    private String userid;
    private String[] bookid;
    private TextView tv;
    private float total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_bill);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //检查是否登录
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(HomeBill.this, SetLogin.class);
            startActivity(intent1);
            finish();
        }

        //获取订单的书籍信息
        Intent intent = getIntent();
        bookid = intent.getStringArrayExtra("bookid");
        total=intent.getFloatExtra("total",0);
        back = (Button) findViewById(R.id.bt_bill_back);
        pay = (Button) findViewById(R.id.bt_pay);
        receiver = (EditText) findViewById(R.id.ed_receiver);
        adress = (EditText) findViewById(R.id.ed_adress);
        phone = (EditText) findViewById(R.id.ed_phone);
        note = (EditText) findViewById(R.id.ed_note);
        tv = (TextView) findViewById(R.id.tv_bill_total);
        tv.setText(String.valueOf(total));
        back.setOnClickListener(this);
        pay.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        String re = sp.getString("receiver", "");
        String ph = sp.getString("phone", "");
        String ad = sp.getString("adress", "");
        String no = sp.getString("note", "");
        receiver.setText(re);
        phone.setText(ph);
        adress.setText(ad);
        note.setText(no);

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();

    }
    //保存订单信息
    private void saveData() {
        String re = receiver.getText().toString();
        String ph = phone.getText().toString();
        String ad = adress.getText().toString();
        String no = note.getText().toString();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("receiver", re);
        editor.putString("phone", ph);
        editor.putString("adress", ad);
        editor.putString("note", no);
        editor.commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回购物车
            case R.id.bt_bill_back:
                Intent intent1 = new Intent(HomeBill.this, HomeCart.class);
                startActivity(intent1);
                break;
            //付款
            case R.id.bt_pay:
                saveData();
                Intent intent2 = new Intent(HomeBill.this, HomePay.class);
                intent2.putExtra("total", total);
                intent2.putExtra("bookid", bookid);
                startActivity(intent2);
                break;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
