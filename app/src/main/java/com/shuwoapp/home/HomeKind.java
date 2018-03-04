package com.shuwoapp.home;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import com.shuwoapp.R;


public class HomeKind extends Activity implements View.OnClickListener {
    Spinner sp_price, sp_kind, sp_way, sp_school;
    CheckBox cb_price, cb_kind, cb_way, cb_school;
    float price = 10;
    String way, kind, school;
    ImageView imSearch;
    boolean[] select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_classify);
        //返回
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        select = new boolean[4];

        cb_school= (CheckBox) findViewById(R.id.cb_school);
        cb_way = (CheckBox) findViewById(R.id.cb_way);
        cb_kind= (CheckBox) findViewById(R.id.cb_kind);
        cb_price= (CheckBox) findViewById(R.id.cb_price);
        sp_price = (Spinner) findViewById(R.id.sp_chooseprice);
        sp_kind = (Spinner) findViewById(R.id.sp_choosekind);
        sp_way = (Spinner) findViewById(R.id.sp_chooseway);
        sp_school = (Spinner) findViewById(R.id.sp_chooseschool);
        imSearch = (ImageView) findViewById(R.id.search_kind);

        imSearch.setOnClickListener(this);
        //获取分类
        sp_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kind = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //获取方式
        sp_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                way = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //获取价格范围
        sp_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        price = 10;
                        break;
                    case 1:
                        price = 20;
                        break;
                    case 2:
                        price = 30;
                        break;
                    case 3:
                        price = 40;
                        break;
                    case 4:
                        price = 50;
                        break;
                    case 5:
                        price = 200;
                        break;
                    default:
                        price = 10;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //获取学校
        sp_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                school = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //获取分类选择项并跳转到查询结果
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HomeKind.this, HomeKindReasult.class);

        if (cb_kind.isChecked()) {
            select[0] = true;
            intent.putExtra("kind", kind);
        }
        if (cb_price.isChecked()) {
            select[1] = true;
            intent.putExtra("price", price);
        }
        if (cb_way.isChecked()) {
            select[2] = true;
            intent.putExtra("way", way);
        }
        if (cb_school.isChecked()) {
            select[3] = true;
            intent.putExtra("school", school);
        }
        intent.putExtra("select", select);
        startActivity(intent);
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
