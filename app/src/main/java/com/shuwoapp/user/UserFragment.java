package com.shuwoapp.user;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class UserFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout l_tip, l_record, l_wallet, l_score, footer;
    EditText ed;
    Button bt;
    ListView lv;
    String userid;
    Comment comment = new Comment();
    MyListAdapter adapter;
    List<Comment> commentList;
    int limit = 5;
    BmobFile bf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        userid = sp.getString("id", "");
        if (userid.equals("")) {
            Intent intent1 = new Intent(getActivity(), SetLogin.class);
            startActivity(intent1);
        }

        lv = (ListView) view.findViewById(R.id.lv_comment);
        footer = (LinearLayout) view.findViewById(R.id.load_footer);
        ed = (EditText) view.findViewById(R.id.ed_comment);
        bt = (Button) view.findViewById(R.id.bt_comment);
        l_tip = (LinearLayout) view.findViewById(R.id.l_tip);
        l_record = (LinearLayout) view.findViewById(R.id.l_record);
        l_wallet = (LinearLayout) view.findViewById(R.id.l_wallet);
        l_score = (LinearLayout) view.findViewById(R.id.l_score);
        l_tip.setOnClickListener(this);
        l_record.setOnClickListener(this);
        l_wallet.setOnClickListener(this);
        l_score.setOnClickListener(this);
        lv.setOnItemClickListener(this);
        bt.setOnClickListener(this);
        footer.setOnClickListener(this);
        getData();
        return view;
    }

    private void createComment() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());

        comment.setComment_text(ed.getText().toString());
        comment.setComment_user(userid);
        comment.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                getData();
                createRecord();
                ed.setText("");
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getData() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.setLimit(limit);
        query.findObjects(getActivity(), new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> list) {
                commentList = list;
                if (list != null) {
                    if (limit == 5) {
                        createList();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    setListViewHeightBasedOnChildren(lv);
                } else {
                    Toast.makeText(getActivity(), "无评论~", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), UserAddComment.class);
        intent.putExtra("comment", commentList.get(position).getObjectId());
        startActivity(intent);
    }

    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return commentList.size();
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
            query.getObject(getActivity(), commentList.get(position).getComment_user(), new GetListener<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user.getIcon() == null) {
                        holder.im.setImageResource(R.drawable.icon);
                    } else {
                        bf = user.getIcon();
                        bf.loadImageThumbnail(getActivity(), holder.im, 32, 32, 100);
                    }
                    holder.tv_name.setText(user.getNickName());
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
            holder.tv_text.setText(commentList.get(position).getComment_text());
            holder.tv_time.setText(commentList.get(position).getCreatedAt());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView im;
        TextView tv_name, tv_text, tv_time;
    }

    private void createList() {
        adapter = new MyListAdapter(getActivity());
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.l_tip:
                Intent intent1 = new Intent(getActivity(), UserTip.class);
                startActivity(intent1);
                break;
            case R.id.l_record:
                Intent intent2 = new Intent(getActivity(), UserRecord.class);
                startActivity(intent2);
                break;
            case R.id.l_wallet:
                Intent intent3 = new Intent(getActivity(), UserWallet.class);
                startActivity(intent3);
                break;
            case R.id.l_score:
                Intent intent4 = new Intent(getActivity(), UserScore.class);
                startActivity(intent4);
                break;
            case R.id.bt_comment:
                limit += 1;
                createComment();
                break;
            case R.id.load_footer:
                limit += 5;
                getData();
                break;
        }
    }

    private void createRecord() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String cloudCodeName = "createRecord";
        JSONObject params = new JSONObject();
        try {
            params.put("text", comment.getComment_text());
            params.put("kind", "Comment");
            params.put("user", userid);
            params.put("date", date);
            params.put("id", comment.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        cloudCode.callEndpoint(getActivity(), cloudCodeName, params, new CloudCodeListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
