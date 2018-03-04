package com.shuwoapp.home;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;

import com.shuwoapp.R;
import com.shuwoapp.data.Record;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;


public class HomePay extends Activity  {
    String APPID = "e1b518778cda8fd1e80181c9b14bc9e9";
    EditText name, price, body;
    Button go;
    String orderid,userid,school,phone,realname,note;
    float total;
    String[] bookid;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_pay);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //获取相关用户信息，检查是否登录
        SharedPreferences sp=getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        realname=sp.getString("realName","");
        school=sp.getString("school","");
        phone=sp.getString("phone","");
        note=sp.getString("note","");
        if(userid.equals("")){
            Intent intent1=new Intent(HomePay.this, SetLogin.class);
            startActivity(intent1);
            finish();
        }
        //获取购买的价格和书籍id
        Intent intent=getIntent();
        total=intent.getFloatExtra("total", 0);
        bookid=intent.getStringArrayExtra("bookid");

        // 初始化BmobPay
        BP.init(this, APPID);

        name = (EditText) findViewById(R.id.name);
        price = (EditText) findViewById(R.id.price);
        body = (EditText) findViewById(R.id.body);
        go = (Button) findViewById(R.id.go);
        name.setText(realname);
        price.setText(String.valueOf(total));
        body.setText(school+"__"+phone);
        go.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                payByAli();
            }
        });

    }
    //创建操作记录
        private  void createRecord(){
            Record record=new Record();
            record.setRecord_kind("Book");
            record.setRecord_user(userid);
            record.setRecord_id(orderid);
            record.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(HomePay.this, "ok",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(HomePay.this, s,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    // 调用支付宝支付
    void payByAli() {
        showDialog("正在获取订单...");
        final String name = getName();

        BP.pay(this, name, getBody(), getPrice(), true, new PListener() {

            @Override
            public void unknow() {
                Toast.makeText(HomePay.this, "支付结果未知,请稍后手动查询",
                        Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            @Override
            public void succeed() {
                Toast.makeText(HomePay.this, "支付成功!", Toast.LENGTH_SHORT)
                        .show();
                createRecord();
                for(int i=0;i<bookid.length;i++){
                    createBill(bookid[i]);
                }
                hideDialog();
            }

            @Override
            public void orderId(String orderId) {
                orderid = orderId;
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(HomePay.this, reason, Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }
        });
    }
    //创建订单并修改购买书籍数据
    private void createBill(String bookid) {
        String cloudCodeName = "createBill";
        JSONObject params = new JSONObject();
        try {
            params.put("bookid",bookid);
            params.put("school",school);
            params.put("name",realname);
            params.put("phone",phone);
            params.put("note",note);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(HomePay.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(HomePay.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取付款数额
    double getPrice() {
        double price = 0.02;// 默认为0.02
        try {
            price = Double.parseDouble(this.price.getText().toString());
        } catch (NumberFormatException e) {
        }
        return price;
    }

    String getName() {
        return this.name.getText().toString();
    }

    String getBody() {
        return this.body.getText().toString();
    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            Toast.makeText(HomePay.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(HomePay.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
            //返回
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
