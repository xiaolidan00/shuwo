package com.shuwoapp.love;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shuwoapp.R;

public class LoveFragment extends Fragment implements View.OnClickListener {
    private LinearLayout give, need, charity, rank;
    private Button give_d, need_d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_love, container, false);
        give = (LinearLayout) view.findViewById(R.id.l_give);
        need = (LinearLayout) view.findViewById(R.id.l_need);
        charity = (LinearLayout) view.findViewById(R.id.l_charity);
        rank = (LinearLayout) view.findViewById(R.id.l_rank);
        give_d = (Button) view.findViewById(R.id.bt_give_d);
        need_d = (Button) view.findViewById(R.id.bt_need_d);
        need.setOnClickListener(this);
        give.setOnClickListener(this);
        charity.setOnClickListener(this);
        rank.setOnClickListener(this);
        need_d.setOnClickListener(this);
        give_d.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.l_give:
                //捐书公告
                Intent intent1 = new Intent(getActivity(), LoveGive.class);
                startActivity(intent1);
                break;
            //求书公告
            case R.id.l_need:
                Intent intent2 = new Intent(getActivity(), LoveNeed.class);
                startActivity(intent2);
                break;
            //发布求书公告
            case R.id.bt_need_d:
                Intent intent3 = new Intent(getActivity(), LoveNeedD.class);
                startActivity(intent3);
                break;
            //发布捐书公告
            case R.id.bt_give_d:
                Intent intent4 = new Intent(getActivity(), LoveGiveD.class);
                startActivity(intent4);
                break;
            //公益活动公告
            case R.id.l_charity:
                Intent intent5 = new Intent(getActivity(), LoveCharity.class);
                startActivity(intent5);
                break;
            //积分排行榜
            case R.id.l_rank:
                Intent intent6 = new Intent(getActivity(), LoveRank.class);
                startActivity(intent6);
                break;
        }
    }

}
