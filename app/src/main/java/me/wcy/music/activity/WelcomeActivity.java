package me.wcy.music.activity;

/**
 *欢迎引导页
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import me.wcy.music.R;

public class WelcomeActivity extends AppCompatActivity {
    public static final String IS_FIRST="is_first";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();//隐藏顶部栏
        //线程休眠
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                    startActivity(new Intent(WelcomeActivity.this,MusicActivity.class));
                return true;
            }
        }).sendEmptyMessageDelayed(0,3000);
    }
}

