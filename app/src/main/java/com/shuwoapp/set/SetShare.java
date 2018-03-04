package com.shuwoapp.set;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shuwoapp.R;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.utils.HttpUtils;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

public class SetShare extends Activity {
    String APP_ID = "1105291498";
    String icon = "http://file.bmob.cn/M03/02/CF/oYYBAFb5QsaAf2D4AAFhKrtssxg130.png";
    Tencent mTencent;
    LinearLayout qq, wechat;
    ImageView im;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_share);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        qq = (LinearLayout) findViewById(R.id.shareqq);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAppShare();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
        // Tencent.onActivityResultData(requestCode,resultCode,data,listener);
    }

    private void onClickAppShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "二手书的暖窝窝");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://535460548484.bmob.cn/");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, icon);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "书尽其用，人尽其心");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "书窝");
        mTencent.shareToQQ(SetShare.this, params, new BaseUiListener());
    }

    class BaseUiListener implements IUiListener {


        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onComplete(Object response) {
            doComplete((JSONObject) response);
        }

        @Override
        public void onError(UiError e) {
            String s = "code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail;
            Toast.makeText(SetShare.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            String s = "cancel";
            Toast.makeText(SetShare.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    class BaseApiListener implements IRequestListener {

        @Override
        public void onComplete(JSONObject jsonObject) {

        }

        @Override
        public void onIOException(IOException e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMalformedURLException(MalformedURLException e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onJSONException(JSONException e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUnknowException(Exception e) {
            Toast.makeText(SetShare.this, e.toString(), Toast.LENGTH_SHORT).show();
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