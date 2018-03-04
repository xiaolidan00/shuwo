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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shuwoapp.R;
import com.shuwoapp.ResultActivity;
import com.shuwoapp.data.Give;
import com.shuwoapp.data.Record;
import com.shuwoapp.data.User;
import com.shuwoapp.pic.SelectPictureActivity;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;


public class LoveGiveD extends Activity implements View.OnClickListener {
    String userid, time, book, path, school, phone, nickname;
    EditText ed_time, ed_book;
    Button bt;
    ImageView im;
    BmobFile bf;
    ProgressDialog pd;
    private static final int REQUEST_PICK = 0;
    Give give = new Give();
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.love_give_d);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        school = sp.getString("school", "");
        phone = sp.getString("phone", "");
        nickname = sp.getString("nickName", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(LoveGiveD.this, SetLogin.class);
            startActivity(intent1);
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(50)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);

        pd = new ProgressDialog(LoveGiveD.this);
        pd.setMessage(getResources().getString(R.string.waitford));
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(100);
        pd.setIndeterminate(true);
        ed_book = (EditText) findViewById(R.id.ed_gived_book);
        ed_time = (EditText) findViewById(R.id.ed_gived_time);
        bt = (Button) findViewById(R.id.bt_give_declare);
        bt.setOnClickListener(this);
        im = (ImageView) findViewById(R.id.im_gived);
        im.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            ImageLoader.getInstance().displayImage("file://" + selectedPicture.get(0), im);
        }
    }

    public void givedPic() {
        if (selectedPicture != null) {
            for (int i = 0; i < selectedPicture.size(); i++) {
                selectedPicture.remove(i);
            }
        }
        startActivityForResult(new Intent(this, SelectPictureActivity.class),
                REQUEST_PICK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_gived:
                givedPic();
                break;
            case R.id.bt_give_declare:
                pd.show();
                giveDeclare();
                pd.dismiss();
                break;
        }
    }

    private void giveDeclare() {
        path = selectedPicture.get(0);
        BTPFileResponse response = BmobProFile.getInstance(this).upload(path, new UploadListener() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(LoveGiveD.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {
                bf = bmobFile;
                saveData();
            }

            @Override
            public void onProgress(int i) {
                pd.setProgress(i);
            }
        });

    }

    private void saveData() {
        book = ed_book.getText().toString();
        time = ed_time.getText().toString();
        give.setGive_book(book);
        give.setGive_deadline(time);
        give.setGive_giver(userid);
        give.setGive_gschool(school);
        give.setGive_gphone(phone);
        give.setGive_gname(nickname);
        give.setGive_pic(bf);
        give.setGive_over(false);
        give.save(LoveGiveD.this, new SaveListener() {
            @Override
            public void onSuccess() {
                createRecord();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveGiveD.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
//运用云端代码实现创建操作记录
    private void createRecord() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String cloudCodeName = "createRecord";
        JSONObject params = new JSONObject();
        try {
            params.put("text", book);
            params.put("kind", "Give");
            params.put("user", userid);
            params.put("date", date);
            params.put("id", give.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(LoveGiveD.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {
                Intent intent = new Intent(LoveGiveD.this, ResultActivity.class);
                intent.putExtra("result", "ok");
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoveGiveD.this, s, Toast.LENGTH_SHORT).show();
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
