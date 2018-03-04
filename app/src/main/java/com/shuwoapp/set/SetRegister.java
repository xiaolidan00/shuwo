package com.shuwoapp.set;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import com.shuwoapp.data.User;
import com.shuwoapp.pic.SelectPictureActivity;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;


public class SetRegister extends Activity implements View.OnClickListener {
    private EditText ed_phone, ed_email, ed_pass1, ed_pass2;
    private String phone;
    private String email;
    private String pass1, pass2;
    User myuser;
    String path;
    BmobFile bf;
    ImageView im;
    private static final int REQUEST_PICK = 0;
    private ArrayList<String> selectedPicture = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_register);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ed_phone = (EditText) findViewById(R.id.ed_phoner);
        ed_email = (EditText) findViewById(R.id.ed_emailr);
        ed_pass1 = (EditText) findViewById(R.id.ed_pass1);
        ed_pass2 = (EditText) findViewById(R.id.ed_pass2);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(50)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        im = (ImageView) findViewById(R.id.im_register);

        Button bt_register = (Button) findViewById(R.id.bt_register);
        im.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectedPicture = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            ImageLoader.getInstance().displayImage(
                    "file://" + selectedPicture.get(0), im);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                createUser();
                break;
            case R.id.im_register:
                if (selectedPicture != null) {
                    for (int i = 0; i < selectedPicture.size(); i++) {
                        selectedPicture.remove(i);
                    }
                }
                startActivityForResult(new Intent(this, SelectPictureActivity.class),
                        REQUEST_PICK);
                break;
        }
    }

    private void createUser() {
        path = selectedPicture.get(0);
        BTPFileResponse response = BmobProFile.getInstance(this).upload(path, new UploadListener() {
            @Override
            public void onError(int i, String s) {
                Toast.makeText(SetRegister.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {
                bf = bmobFile;
                saveData();
            }

            @Override
            public void onProgress(int i) {

            }
        });

    }

    private void saveData() {
        phone = ed_phone.getText().toString();
        email = ed_email.getText().toString();
        pass1 = ed_pass1.getText().toString();
        pass2 = ed_pass1.getText().toString();
        myuser = new User();
        myuser.setUsername(email);
        myuser.setEmail(email);
        myuser.setMobilePhoneNumber(phone);
        myuser.setPassword(pass1);
        myuser.setIcon(bf);
        myuser.signUp(SetRegister.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(SetRegister.this, SetLogin.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SetRegister.this, s, Toast.LENGTH_SHORT).show();
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