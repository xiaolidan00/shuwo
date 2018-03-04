package com.shuwoapp.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.shuwoapp.R;
import com.shuwoapp.user.UserTip;

import cn.bmob.push.PushConstants;
//推送接收器
public class PushReceiver extends BroadcastReceiver {
    NotificationManager nm;
    @Override
    public void onReceive(Context context, Intent intent) {
       nm= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Intent intent1=new Intent(context, UserTip.class);
            PendingIntent pi=PendingIntent.getActivities(context,0,new Intent[]{intent1},0);
            Notification notify=new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setTicker("书窝消息")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("你有提醒消息哦")
                    .setContentText("请及时查看")
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pi)
                    .build();
            nm.notify(0,notify);
        }
    }

}
