package com.shuwoapp.user;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuwoapp.R;
import com.shuwoapp.data.AddComment;
import com.shuwoapp.data.Comment;
import com.shuwoapp.data.User;
import com.shuwoapp.set.SetLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;


public class UserAddComment extends Activity implements View.OnClickListener {
    String userid, commentid;
    LinearLayout footer;
    EditText ed;
    List<AddComment> addComments;
    AddComment comment = new AddComment();
    Button bt;
    MyListAdapter adapter;
    ListView lv;
    int limit = 5;
    TextView tv_name, tv_text, tv_time;
    ImageView im;
    BmobFile bf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_add);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(UserAddComment.this, SetLogin.class);
            startActivity(intent1);
        }

        lv = (ListView) findViewById(R.id.lv_comment);
        footer = (LinearLayout) findViewById(R.id.load_footer);
        ed = (EditText) findViewById(R.id.ed_comment);
        bt = (Button) findViewById(R.id.bt_comment);
        bt.setOnClickListener(this);
        footer.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_addcommentuser);
        tv_text = (TextView) findViewById(R.id.tv_addcommenttext);
        tv_time = (TextView) findViewById(R.id.tv_addcommenttime);
        im = (ImageView) findViewById(R.id.im_addcomment);
        Intent intent = getIntent();
        commentid = intent.getStringExtra("comment");
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.getObject(this, commentid, new GetListener<Comment>() {
            @Override
            public void onSuccess(Comment comment) {
                tv_time.setText(comment.getCreatedAt());
                tv_text.setText(comment.getComment_text());

                BmobQuery<User> query = new BmobQuery<User>();
                query.getObject(UserAddComment.this, comment.getComment_user(), new GetListener<User>() {
                    @Override
                    public void onSuccess(User user) {

                        if (user.getIcon() == null) {
                            im.setImageResource(R.drawable.auser);
                        } else {
                            bf = user.getIcon();
                            bf.loadImageThumbnail(UserAddComment.this, im, 32, 32, 100);
                        }
                        tv_name.setText(user.getNickName());
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(UserAddComment.this, s, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserAddComment.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_comment:
                createComment();
                limit += 1;
                getData();
                break;
            case R.id.load_footer:
                getData();
                break;
        }

    }

    private void createComment() {
        comment.setAdd_text(ed.getText().toString());
        comment.setAdd_id(commentid);
        comment.setAdd_user(userid);
        comment.save(UserAddComment.this, new SaveListener() {
            @Override
            public void onSuccess() {
                getData();
                ed.setText("");
                createRecord();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserAddComment.this, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getData() {
        BmobQuery<AddComment> query = new BmobQuery<AddComment>();
        query.setLimit(limit);
        query.addWhereEqualTo("add_id", commentid);
        query.findObjects(this, new FindListener<AddComment>() {

            @Override
            public void onSuccess(List<AddComment> list) {
                addComments = list;
                if (list != null) {
                    if (limit == 5) {
                        createList();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    setListViewHeightBasedOnChildren(lv);
                } else {
                    Toast.makeText(UserAddComment.this, "no comment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(UserAddComment.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }


    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return addComments.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
                holder = new ViewHolder();
                holder.im = (ImageView) convertView.findViewById(R.id.im_comment);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_commentuser);
                holder.tv_text = (TextView) convertView.findViewById(R.id.tv_commenttext);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_commenttime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BmobQuery<User> query = new BmobQuery<User>();
            query.getObject(UserAddComment.this, addComments.get(position).getAdd_user(), new GetListener<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user.getIcon() == null) {
                        holder.im.setImageResource(R.drawable.auser);
                    } else {
                        bf = user.getIcon();
                        bf.loadImageThumbnail(UserAddComment.this, holder.im, 32, 32, 100);
                    }
                    holder.tv_name.setText(user.getNickName());

                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(UserAddComment.this, s, Toast.LENGTH_SHORT).show();
                }
            });

            holder.tv_text.setText(addComments.get(position).getAdd_text());
            holder.tv_time.setText(addComments.get(position).getCreatedAt());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView im;
        TextView tv_name, tv_text, tv_time;
    }

    private void createList() {
        adapter = new MyListAdapter(this);
        lv.setAdapter(adapter);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View v = adapter.getView(i, null, listView);
            v.measure(0, 0);
            height += v.getMeasuredHeight();
        }
        ViewGroup.LayoutParams ps = listView.getLayoutParams();
        ps.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) ps).setMargins(15, 15, 15, 15);
        listView.setLayoutParams(ps);
    }

    private void createRecord() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String cloudCodeName = "createRecord";
        JSONObject params = new JSONObject();
        try {
            params.put("text", comment.getAdd_text());
            params.put("kind", "AddComment");
            params.put("user", userid);
            params.put("date", date);
            params.put("id", comment.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(UserAddComment.this, cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserAddComment.this, s, Toast.LENGTH_SHORT).show();
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
