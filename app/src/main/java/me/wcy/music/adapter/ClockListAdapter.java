package me.wcy.music.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.wcy.music.R;
import me.wcy.music.receiver.CallAlarm;
import me.wcy.music.model.Clock;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class ClockListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Clock> clockArrayList;
    private View.OnClickListener listener;

    public ClockListAdapter(Context context,ArrayList<Clock> clockArrayList){
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        this.clockArrayList=clockArrayList;

    }


    @Override
    public int getCount() {
        return clockArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return clockArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        //显示item中时间的两个组件
        public TextView clock_ringtime;
        public Switch clock_switchbutton;
    }

    public void closeclock(int position){
        Intent intent = new Intent(context, CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,position, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am;
        am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        Toast.makeText(context,"闹钟时间删除", Toast.LENGTH_SHORT).show();
    }
    public void openclock(int hour,int minute,int position,int interval,int ring,boolean[] week){
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()),
                    intervalMillis, sender);
        } else {
            if (flag == 0) {
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);*/
        int times=interval*60 *1000;
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,minute);
        Intent i = new Intent(context, CallAlarm.class);
        String str=ring+"th";
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
        i.putExtra("RING",str);
        PendingIntent sender = PendingIntent.getBroadcast(context,position, i, 0);
        AlarmManager am;
        am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        if(interval==0){
            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
        }
        else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),times,sender);
        }
        Toast.makeText(context,"设置闹钟为"+ hour+":"+minute, Toast.LENGTH_SHORT).show();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder=null;
        if(convertView == null){
            //填写ListView的图标和标题等控件的来源，来自于layout;clocklist_item这个布局文件
            //把控件所在的布局文件加载到当前类中
            convertView = layoutInflater.inflate(R.layout.clocklist_item,null);
            //生成一个ViewHolder的对象
            holder = new ViewHolder();
            //获取控件对象
            holder.clock_ringtime=convertView.findViewById(R.id.clock_ringtime);
            holder.clock_switchbutton=convertView.findViewById(R.id.clock_statebutton);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Clock clock= clockArrayList.get(position);       //clock列表中的clock
        final String pos=position+"";
        final Clock clockget=getclock(pos+"");           //位于此处的clock从文件中获取得到的clock对象
        holder.clock_ringtime.setText(clockget.getTime());
        holder.clock_switchbutton.setOnClickListener(listener);
        holder.clock_switchbutton.setTag(position);

        //按钮开关设置
        holder.clock_switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    openclock(clockget.getHour(),clockget.getMinute(),position,clockget.getIntervaltime(),clockget.getRing(),clockget.getWorkday());
                }else{

                    closeclock(position);
                }
            }
        });
        return convertView;
    }

    private Clock getclock(String i){  //获取json内容，并转换为class对象
        Clock clock=new Clock();
        String key="KEY_CLOCK_"+i+"th";
        SharedPreferences sp = context.getSharedPreferences("SP_CLOCK", MODE_PRIVATE);
        // Toast.makeText(context,sp.toString(),Toast.LENGTH_LONG).show();
        String clockJson = sp.getString(key,""); //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if(clockJson!="")  //防空判断
        {
            Gson gson = new Gson();
            // Toast.makeText(context,clockJson,Toast.LENGTH_LONG).show();
            clock = gson.fromJson(clockJson, Clock.class); //将json字符串转换成 people对象
        }

        return clock;
    }
   /* private static long calMethod(boolean[] week, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
        if (weekflag != 0) {
            Calendar c = Calendar.getInstance();
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (1 == week) {
                week = 7;
            } else if (2 == week) {
                week = 1;
            } else if (3 == week) {
                week = 2;
            } else if (4 == week) {
                week = 3;
            } else if (5 == week) {
                week = 4;
            } else if (6 == week) {
                week = 5;
            } else if (7 == week) {
                week = 6;
            }

            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }


}*/


}


