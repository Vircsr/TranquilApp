package me.wcy.music.executor;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import me.wcy.music.R;
import me.wcy.music.activity.MusicActivity;
import me.wcy.music.service.QuitTimer;
import me.wcy.music.storage.preference.Preferences;
import me.wcy.music.utils.ToastUtils;

/**
 * 导航菜单执行器
 *
 */
public class NaviMenuExecutor {
    private MusicActivity activity;

    public NaviMenuExecutor(MusicActivity activity) {
        this.activity = activity;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_night:
                nightMode();
                break;
            case R.id.action_timer:
                timerDialog();
                return true;
            case R.id.action_sleep:
                sleepModel();
                return true;
//            case R.id.action_clock:
//                //startActivity();
//                return true;
        }
        return false;
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    private void nightMode() {
        Preferences.saveNightMode(!Preferences.isNightMode());
        activity.recreate();
    }
    private void timerDialog() {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.menu_timer)
                .setItems(activity.getResources().getStringArray(R.array.timer_text), (dialog, which) -> {
                    int[] times = activity.getResources().getIntArray(R.array.timer_int);
                    startTimer(times[which]);
                })
                .show();
    }
    private void startTimer(int minute) {
        if(minute == 1){
            QuitTimer.get().start(minute * 6 * 1000);
        }else QuitTimer.get().start(minute * 60 * 1000);
        if (minute > 0) {
            if(minute ==1){
                ToastUtils.show("设置成功，将于6秒后关闭");
            }else
            ToastUtils.show(activity.getString(R.string.timer_set, String.valueOf(minute)));
        } else {
            ToastUtils.show(R.string.timer_cancel);
        }
    }

    private void sleepModel(){
        QuitTimer.get().start(30*60*1000);
        if(!Preferences.isNightMode()){
            nightMode();
        }
        ToastUtils.show("睡眠模式开启");
    }
}
