package com.shuwoapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*反馈操作结果*/
public class ResultActivity extends Activity {
    Button back;
    String result;
    ImageView im;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        result = intent.getStringExtra("result");
        im = (ImageView) findViewById(R.id.im_result);
        tv = (TextView) findViewById(R.id.tv_result);

        if (result.equals("ok")) {
            im.setImageResource(R.drawable.ok);
            tv.setText(getResources().getString(R.string.result_ok));
        } else {
            im.setImageResource(R.drawable.no);
            tv.setText(getResources().getString(R.string.result_no));
        }
        back = (Button) findViewById(R.id.bt_result_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
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
