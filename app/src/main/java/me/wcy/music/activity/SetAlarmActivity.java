package me.wcy.music.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.util.Calendar;

import me.wcy.music.R;
import me.wcy.music.model.Clock;
public class SetAlarmActivity extends AppCompatActivity implements View.OnClickListener{
private int hour;
private int minute;
public  TextView cancel;
public  TextView sure;
public  TextView day;
public  TextView ring;
public  TextView remindway;
public  TextView ringname;
public CheckBox Mon;
public CheckBox Tue;
public CheckBox Wed;
public CheckBox Thu;
public CheckBox Fri;
public CheckBox Sat;
public CheckBox Sun;
public TimePicker timePicker;
public String clockJson;
//当前clock对象
public Clock clocknow;
//用于显示的读取来的clock对象
public Clock clockregister=new Clock();
public Calendar c;
public boolean[] workday={false,false,false,false,false,false,false};
Intent intent=getIntent();
String clockid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        timePicker=(TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        init();//实例化按钮

        //获取闹钟
        if(getclock(clockid)==null){
            clocknow=new Clock();
        }else{
            clocknow=getclock(clockid);
            hour=clocknow.getHour();
            minute=clocknow.getMinute();
        }
        clockDisplay(clocknow);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    SetAlarmActivity.this.hour=hourOfDay;
                    SetAlarmActivity.this.minute=minute;
                    clocknow.setTime(SetAlarmActivity.this.hour,SetAlarmActivity.this.minute);
            }
        });
    }
    public void init(){
        clockid=getIntent().getStringExtra("itemid");
        //Toast.makeText(getApplicationContext(), "item"+clockid, Toast.LENGTH_LONG).show();
        c=Calendar.getInstance();
        cancel= (TextView) findViewById(R.id.cancel_text);
        cancel.setOnClickListener(this);
        sure=(TextView) findViewById(R.id.sure_text);
        sure.setOnClickListener(this);
        day=(TextView) findViewById(R.id.day_text);
        ring=(TextView) findViewById(R.id.ring_text);
        ring.setOnClickListener(this);
        ringname=(TextView) findViewById(R.id.ringname_text);
        ringname.setOnClickListener(this);
        remindway=(TextView) findViewById(R.id.remindway_text);
        remindway.setOnClickListener(this);
        Mon=(CheckBox) findViewById(R.id.Mon);
        Mon.setOnClickListener(this);
        Tue=(CheckBox) findViewById(R.id.Tue);
        Tue.setOnClickListener(this);
        Wed=(CheckBox) findViewById(R.id.Wed);
        Wed.setOnClickListener(this);
        Thu=(CheckBox) findViewById(R.id.Thu);
        Thu.setOnClickListener(this);
        Fri=(CheckBox) findViewById(R.id.Fri);
        Fri.setOnClickListener(this);
        Sat=(CheckBox) findViewById(R.id.Sat);
        Sat.setOnClickListener(this);
        Sun=(CheckBox) findViewById(R.id.Sun);
        Sun.setOnClickListener(this);


    }
    @Override
    public void onClick( View view){
        switch (view.getId()){
            case R.id.Mon: case R.id.Tue:case R.id.Wed:case R.id.Thu:case R.id.Fri:case R.id.Sat:case R.id.Sun:
                getday();
                break;
            case R.id.cancel_text:
                finish();
                break;
            case R.id.sure_text:
                saveclock(clockid);
                //getclock(clockid);
                finish();
                break;
            case R.id.ring_text:
                startActivity(new Intent(SetAlarmActivity.this, RingActivity.class));
                break;
            case R.id.ringname_text:
                startActivity(new Intent(SetAlarmActivity.this, RingActivity.class));
                break;
            case R.id.remindway_text:
                startActivity(new Intent(SetAlarmActivity.this, RemindWayActivity.class));
                break;
        }

    }



    public void getday(){ //展示周一到周日的字符串，并且记录周一到周日的选中状态
        String r="";
        if(Mon.isChecked()) {
            r=r+"周一、" ;
        }
        if(Tue.isChecked()){
            r=r+"周二、" ;
        }
        if(Wed.isChecked()) {
            r=r+"周三、" ;
        }
        if(Thu.isChecked()) {
            r=r+"周四、" ;
        }
        if(Fri.isChecked()) {
            r=r+"周五、" ;
        }
        if(Sat.isChecked()) {
            r=r+"周六、" ;
        }
        if(Sun.isChecked()) {
            r=r+"周日、" ;
        }
        clocknow.setWorkday(Mon.isChecked(),Tue.isChecked(),Wed.isChecked(),Thu.isChecked(),Fri.isChecked(),Sat.isChecked(),Sun.isChecked());
        day.setText(r);
    }
    private void saveclock(String i){   //保存闹钟当前设置内容
        String key="KEY_CLOCK_"+i;
        clocknow.setTime(hour,minute);
        clocknow.setRingname("铃声名");
        clocknow.setRemindway("callme");
        clocknow.setWorkday(workday);
        clocknow.setWorkday(Mon.isChecked(),Tue.isChecked(),Wed.isChecked(),Thu.isChecked(),Fri.isChecked(),Sat.isChecked(),Sun.isChecked());
        SharedPreferences sp = getSharedPreferences("SP_CLOCK",MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出，否则就创建一个此key的sp对象
        Gson gson=new Gson();
        String jsonStr=gson.toJson(clocknow);
        SharedPreferences.Editor edit=sp.edit();
        edit.putString(key, jsonStr) ;
        //Toast.makeText(getApplicationContext(),jsonStr,Toast.LENGTH_LONG).show();
        edit.commit();
    }
    private Clock getclock(String i){  //获取json内容，并转换为class对象

        Clock clock=new Clock();
        String key="KEY_CLOCK_"+i;
        SharedPreferences sp = getSharedPreferences("SP_CLOCK",MODE_PRIVATE);                     //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        clockJson = sp.getString(key,"");                        //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值

        if(clockJson!="")  //防空判断
        {
            Gson gson = new Gson();
           // Toast.makeText(getApplicationContext(),clockJson,Toast.LENGTH_LONG).show();
            clock = gson.fromJson(clockJson, Clock.class);
            //Toast.makeText(getApplicationContext(),i+"##########"+clock.getTime(),Toast.LENGTH_LONG).show();//将json字符串转换成 people对象
        }

        return clock;
    }
    private void clockDisplay(Clock clockitem){  //展示闹钟的设置内容

        timePicker.setCurrentHour(clockitem.getHour());
        timePicker.setCurrentMinute(clockitem.getMinute());
        remindway.setText(clockitem.getRemindway());
        ringname.setText(clockitem.getRingname());
        boolean workday[]=clockitem.getWorkday();
        Mon.setChecked(workday[0]);
        Tue.setChecked(workday[1]);
        Wed.setChecked(workday[2]);
        Thu.setChecked(workday[3]);
        Fri.setChecked(workday[4]);
        Sat.setChecked(workday[5]);
        Sun.setChecked(workday[6]);
        getday();

    }





}

