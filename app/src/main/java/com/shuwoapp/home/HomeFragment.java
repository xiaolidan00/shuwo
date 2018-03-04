package com.shuwoapp.home;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shuwoapp.R;
import com.shuwoapp.data.Book;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

public class HomeFragment extends Fragment implements OnClickListener, AdapterView.OnItemClickListener {
    private TextView cart, bill, declare;
    private ImageView search;
    private LinearLayout classify;
    private ListView lv;
    private LinearLayout footer;
    private List<Book> booklist;
    private int limit = 5;
    private BmobFile bf;
    private MyListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bill = (TextView) view.findViewById(R.id.tv_bill);
        cart = (TextView) view.findViewById(R.id.tv_cart);
        search = (ImageView) view.findViewById(R.id.im_search);
        declare = (TextView) view.findViewById(R.id.tv_declare);
        classify = (LinearLayout) view.findViewById(R.id.classity);
        lv = (ListView) view.findViewById(R.id.lv_home);
        footer = (LinearLayout) view.findViewById(R.id.load_footer);

        footer.setOnClickListener(this);
        classify.setOnClickListener(this);
        bill.setOnClickListener(this);
        declare.setOnClickListener(this);
        cart.setOnClickListener(this);
        search.setOnClickListener(this);
        getData();
        return view;
    }
    //获取书籍数据
    private void getData() {
        BmobQuery<Book> query = new BmobQuery<Book>();
        query.setLimit(limit);
        query.addWhereEqualTo("book_over", false);
        query.order("UpdateAt");
        query.findObjects(getActivity(), new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> object) {
                booklist = object;
                if (limit == 5) {
                    createList();
                } else if (limit == 100) {
                    footer.setVisibility(View.GONE);
                } else {
                    adapter.notifyDataSetChanged();
                }
                setListViewHeightBasedOnChildren(lv);
            }

            @Override
            public void onError(int code, String msg) {

            }
        });
    }
    //创建书籍列表
    private void createList() {
        adapter = new MyListAdapter(getActivity());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }


    class MyListAdapter extends BaseAdapter {
        Context context;

        public MyListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return booklist.size();
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.home_search_item, null);
                holder = new ViewHolder();
                holder.im = (ImageView) convertView.findViewById(R.id.im_search);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_search_title);
                holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_search_detail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (booklist.get(position).getBook_pic() == null) {
                holder.im.setImageResource(R.drawable.book1);
            } else {
                bf = booklist.get(position).getBook_pic();
                bf.loadImageThumbnail(getActivity(), holder.im, 64, 64, 100);
            }
            holder.tv_title.setText(booklist.get(position).getBook_title());
            holder.tv_detail.setText("学校：" + booklist.get(position).getBook_oschool());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView im;
        TextView tv_title, tv_detail;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //搜索
            case R.id.im_search:
                Intent intent1 = new Intent(getActivity(), HomeSearch.class);
                startActivity(intent1);
                break;
            //购物车
            case R.id.tv_cart:
                Intent intent2 = new Intent(getActivity(), HomeCart.class);
                startActivity(intent2);
                break;
            //订单
            case R.id.tv_bill:
                Intent intent3 = new Intent(getActivity(), HomeBill.class);
                startActivity(intent3);
                break;
            //发布
            case R.id.tv_declare:
                Intent intent4 = new Intent(getActivity(), HomeDeclare.class);
                startActivity(intent4);
                break;
            //分类
            case R.id.classity:
                Intent intent5 = new Intent(getActivity(), HomeKind.class);
                startActivity(intent5);
                break;
            //加载更多
            case R.id.load_footer:
                limit += 5;
                getData();
                break;
        }
    }
    //测量列表高度
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
    //进入书籍详细信息
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String objectId = booklist.get(position).getObjectId();
        Intent intent = new Intent(getActivity(), HomeBook.class);
        intent.putExtra("objectId", objectId);
        startActivity(intent);
    }

}