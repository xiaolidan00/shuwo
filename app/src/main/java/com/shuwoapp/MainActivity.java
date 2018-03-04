package com.shuwoapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.shuwoapp.data.Pic;
import com.shuwoapp.home.HomeFragment;
import com.shuwoapp.love.LoveFragment;
import com.shuwoapp.set.SetActivity;
import com.shuwoapp.set.SetLogin;
import com.shuwoapp.set.SetRegister;
import com.shuwoapp.set.SetShare;
import com.shuwoapp.user.UserFragment;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;


public class MainActivity extends Activity implements View.OnClickListener {
    private LinearLayout home, love, user;
    private ImageView im_home, im_love, im_user;
    private TextView tv_home, tv_love, tv_user;
    private ViewFlipper vf;
    private String APPID = "e1b518778cda8fd1e80181c9b14bc9e9";
    private HomeFragment homeFragment;
    private LoveFragment loveFragment;
    private List<Pic> picList;
    private UserFragment userFragment;
    private Fragment fragment;
    int picnum = 5;
    int[] imid = {R.drawable.top1, R.drawable.top2, R.drawable.top3, R.drawable.top4, R.drawable.top5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //接入Bmob云端后台
        Bmob.initialize(this, APPID);
        BmobInstallation.getCurrentInstallation(this).save();
        BmobInstallation installation = BmobInstallation.getCurrentInstallation(this);
        BmobPush.startWork(this, APPID);

        vf = (ViewFlipper) findViewById(R.id.vf_main);
        home = (LinearLayout) findViewById(R.id.home);
        love = (LinearLayout) findViewById(R.id.love);
        user = (LinearLayout) findViewById(R.id.user);
        im_home = (ImageView) findViewById(R.id.im_home);
        im_love = (ImageView) findViewById(R.id.im_love);
        im_user = (ImageView) findViewById(R.id.im_user);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_love = (TextView) findViewById(R.id.tv_love);
        tv_user = (TextView) findViewById(R.id.tv_user);

        home.setOnClickListener(this);
        love.setOnClickListener(this);
        user.setOnClickListener(this);

        createViewFlipper();
        //检查网络情况
        ConnectionDirector cd = new ConnectionDirector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            Toast.makeText(MainActivity.this, "请链接网络", Toast.LENGTH_SHORT).show();
        }
        setDefaultFragment();
    }

    //创建图片切换器
    private void createViewFlipper() {
//        final String[] url=new String[5];
//        BmobQuery<Pic> query=new BmobQuery<Pic>();
//        query.findObjects(this, new FindListener<Pic>() {
//            @Override
//            public void onSuccess(List<Pic> list) {
//                picList=list;
//                for(int i=0;i<url.length;i++) {
//                    url[i]=list.get(i).getPicture().getFileUrl(MainActivity.this);
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
        for (int i = 0; i < picnum; i++) {
            vf.addView(getImage(i));
        }
        vf.setInAnimation(this, android.R.anim.slide_in_left);
        vf.setOutAnimation(this, android.R.anim.slide_out_right);
        vf.startFlipping();
    }

    //图片切换器的图片
    private View getImage(int i) {
        ImageView imageview = new ImageView(this);
//        BmobFile bf=picList.get(i).getPicture();
//        bf.loadImageThumbnail(MainActivity.this,imageview,200,160, 100);
        imageview.setBackgroundResource(imid[i]);
        return imageview;
    }

    //初始化界面
    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.replace(R.id.fragment, homeFragment);
        transaction.commit();
        im_home.setImageResource(R.drawable.ahome);
        im_love.setImageResource(R.drawable.heart);
        im_user.setImageResource(R.drawable.user);
        tv_home.setTextColor(getResources().getColor(R.color.b1));
        tv_love.setTextColor(getResources().getColor(R.color.g500));
        tv_user.setTextColor(getResources().getColor(R.color.g500));
    }

    //菜单栏导航
    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        fragment = getFragmentManager().findFragmentById(R.id.fragment);
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {

            //主页
            case R.id.home:
                homeFragment = new HomeFragment();
                transaction.hide(fragment).replace(R.id.fragment, homeFragment);
                im_home.setImageResource(R.drawable.ahome);
                im_love.setImageResource(R.drawable.heart);
                im_user.setImageResource(R.drawable.user);
                tv_home.setTextColor(getResources().getColor(R.color.b1));
                tv_love.setTextColor(getResources().getColor(R.color.g500));
                tv_user.setTextColor(getResources().getColor(R.color.g500));
                break;

            //爱心
            case R.id.love:
                loveFragment = new LoveFragment();
                transaction.hide(fragment).replace(R.id.fragment, loveFragment);
                im_home.setImageResource(R.drawable.home);
                im_love.setImageResource(R.drawable.aheart);
                im_user.setImageResource(R.drawable.user);
                tv_home.setTextColor(getResources().getColor(R.color.g500));
                tv_love.setTextColor(getResources().getColor(R.color.b1));
                tv_user.setTextColor(getResources().getColor(R.color.g500));
                break;
            //个人
            case R.id.user:
                userFragment = new UserFragment();
                transaction.hide(fragment).replace(R.id.fragment, userFragment);
                im_home.setImageResource(R.drawable.home);
                im_love.setImageResource(R.drawable.heart);
                im_user.setImageResource(R.drawable.auser);
                tv_home.setTextColor(getResources().getColor(R.color.g500));
                tv_love.setTextColor(getResources().getColor(R.color.g500));
                tv_user.setTextColor(getResources().getColor(R.color.b1));
                break;
        }
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            //登录
            case R.id.action_login:
                Intent intent1 = new Intent(MainActivity.this, SetLogin.class);
                startActivity(intent1);
                break;
            //设置
            case R.id.action_set:
                Intent intent2 = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent2);
                break;
            //注册
            case R.id.action_register:
                Intent intent = new Intent(MainActivity.this, SetRegister.class);
                startActivity(intent);
                break;
            //分享
            case R.id.action_share:
                Intent intent3 = new Intent(MainActivity.this, SetShare.class);
                startActivity(intent3);
                break;
            //退出
            case R.id.action_out:
                BmobUser.logOut(this);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
