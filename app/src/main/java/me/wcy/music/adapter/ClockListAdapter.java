package me.wcy.music.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
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

import javax.security.auth.callback.Callback;

import me.wcy.music.R;
import me.wcy.music.activity.CallAlarm;
import me.wcy.music.model.Clock;
import me.wcy.music.utils.binding.Bind;
import me.wcy.music.utils.binding.ViewBinder;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class ClockListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Clock> clockArrayList;
    private View.OnClickListener listener;

    public ClockListAdapter(Context context, List<Clock> clockArrayList){
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        this.clockArrayList=clockArrayList;

    }

    public ClockListAdapter(List<Clock> clockArrayList){
        this.clockArrayList = clockArrayList;
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

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder {
        //显示item中时间的两个组件
        public TextView clock_ringtime;
        public Switch clock_switchbutton;
    }

    public void closeclock(){
        Intent intent = new Intent(context, CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,1, intent, 0);
        AlarmManager am;
        am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        Toast.makeText(context,"闹钟时间删除", Toast.LENGTH_SHORT).show();
    }
    public void openclock(int hour,int minute){
        int times=15 *1000;
        Calendar c= Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY,hour);
        c.set(Calendar.MINUTE,minute);
        Intent intent = new Intent(context,
                CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(
                context,1, intent, 0);
        AlarmManager am;
        am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),times,sender);
        //am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
        Toast.makeText(context,"设置闹钟为"+
                        "开始，重复间隔为"+times/1000+"秒",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            //填写ListView的图标和标题等控件的来源，来自于layout;clocklist_item这个布局文件
            //把控件所在的布局文件加载到当前类中
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.clocklist_item,parent,false);
            //生成一个ViewHolder的对象
            holder = new ViewHolder();
            //获取控件对象
            holder.clock_ringtime=convertView.findViewById(R.id.clock_ringtime);
            holder.clock_switchbutton=convertView.findViewById(R.id.clock_statebutton);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Clock clock= clockArrayList.get(position);       //clock列表中的clock
         String pos=position+"";
        Clock clockget=getclock(pos+"");           //位于此处的clock从文件中获取得到的clock对象
        holder.clock_ringtime.setText(clockget.getTime());
        holder.clock_switchbutton.setOnClickListener(listener);
        holder.clock_switchbutton.setTag(position);

        //按钮开关设置
        holder.clock_switchbutton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){

                 openclock(clockget.getHour(),clockget.getMinute());

                  Toast.makeText(context, "open"+clockget.getHour()+" "+clockget.getMinute(), Toast.LENGTH_SHORT).show();
            }else{

                closeclock();
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
//    private static class ViewHolder{
//        @Bind(R.id.clock_ringtime)
//        private TextView clockringtime;
//        @Bind(R.id.clock_statebutton)
//        private Switch clockswitchbutton;
//        public ViewHolder(View view){
//            ViewBinder.bind(this,view);
//        }
//    }


}

