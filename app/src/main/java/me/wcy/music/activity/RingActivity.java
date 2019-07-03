package me.wcy.music.activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import me.wcy.music.R;

public class RingActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView ring_1;
    public TextView ring_2;
    public TextView ring_3;
    public TextView ring_4;
    public TextView ring_5;
    public TextView ring_6;
    public TextView ring_7;
    public TextView ring_8;
    public TextView ring_9;
    public TextView ring_sure;
    public MediaPlayer mp;
    public  String i="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);



        ring_1=findViewById(R.id.ring_1);
        ring_2=findViewById(R.id.ring_2);
        ring_3=findViewById(R.id.ring_3);
        ring_4=findViewById(R.id.ring_4);
        ring_5=findViewById(R.id.ring_5);
        ring_6=findViewById(R.id.ring_6);
        ring_7=findViewById(R.id.ring_7);
        ring_8=findViewById(R.id.ring_8);
        ring_9=findViewById(R.id.ring_9);
        ring_sure=findViewById(R.id.ring_sure);
        ring_1.setOnClickListener(this);
        ring_2.setOnClickListener(this);
        ring_3.setOnClickListener(this);
        ring_4.setOnClickListener(this);
        ring_5.setOnClickListener(this);
        ring_6.setOnClickListener(this);
        ring_7.setOnClickListener(this);
        ring_8.setOnClickListener(this);
        ring_9.setOnClickListener(this);
        ring_sure.setOnClickListener(this);
        mp=new MediaPlayer();


    }

    public void onClick( View view){
        switch (view.getId()) {
            case R.id.ring_1:
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_1 );
                mp.start();
                i="1";
                break;
            case R.id.ring_2:
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_2 );
                mp.start();
                i="2";
                break;
            case R.id.ring_3:
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_3 );
                mp.start();
                i="3";
                break;
            case R.id.ring_4:
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_4 );
                mp.start();
                i="4";
                break;
            case R.id.ring_5:
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_5 );
                mp.start();
                i="5";
                break;
            case R.id.ring_6:
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_6 );
                mp.start();
                i="6";
                break;
            case R.id.ring_7:
                i="7";
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_7 );
                mp.start();

                break;
            case R.id.ring_8:
                i="8";
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_8 );
                mp.start();
                break;
            case R.id.ring_9:
                if(mp.isPlaying()){
                    mp.release();
                }
                mp = MediaPlayer.create(this, R.raw.music_9 );
                mp.start();
                i="9";
                break;
            case R.id.ring_sure:
                if(mp.isPlaying()){
                    mp.release();
                }
                SharedPreferences sp = getSharedPreferences("SP_RING",MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出，否则就创建一个此key的sp对象
                SharedPreferences.Editor edit=sp.edit();
                edit.putString("NUM", i) ;
                edit.commit();
                finish();
                break;
        }
    }
}
