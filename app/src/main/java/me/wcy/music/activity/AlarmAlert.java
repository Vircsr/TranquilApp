package me.wcy.music.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import me.wcy.music.R;

public class AlarmAlert extends Activity
{
    private MediaPlayer mediaPlayer;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this, R.raw.clockmusic2 );
        mediaPlayer.start();
        if(alertDialog==null){
            alertDialog =new AlertDialog.Builder(AlarmAlert.this)
                    .setTitle("闹钟响了!!")
                    .setMessage("起来吧!!!")
                    .setPositiveButton("关掉它",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {
                                    AlarmAlert.this.finish();
                                    mediaPlayer.stop();
                                }
                            })
                    .show();
        }else{
            alertDialog.show();
        }

    }
}

