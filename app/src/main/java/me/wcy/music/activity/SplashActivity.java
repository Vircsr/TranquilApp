package me.wcy.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.wcy.music.R;

public class SplashActivity extends AppCompatActivity {
    private static final int WHAT_DELAY = 0x11;// 启动页的延时跳转
    private static final int DELAY_TIME = 3000;// 延时时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getActionBar().hide();
        // 调用handler的sendEmptyMessageDelayed方法
        handler.sendEmptyMessageDelayed(WHAT_DELAY, DELAY_TIME);
    }

    // 创建Handler对象，处理接收的消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case WHAT_DELAY:// 延时3秒跳转
                    goHome();
                    break;
            }
        }
    };
    /**
     * 跳转到主页面
     */
    private void goHome() {
        startActivity(new Intent(SplashActivity.this, MusicActivity.class));
        finish();// 销毁当前活动界面
    }
}
