package me.wcy.music.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;



import me.wcy.music.R;
import me.wcy.music.adapter.FragmentAdapter;
import me.wcy.music.constants.Extras;
import me.wcy.music.constants.Keys;
import me.wcy.music.executor.ControlPanel;
import me.wcy.music.executor.NaviMenuExecutor;
import me.wcy.music.fragment.ClockFragment;
import me.wcy.music.fragment.LocalMusicFragment;
import me.wcy.music.fragment.PlayFragment;
import me.wcy.music.fragment.SheetListFragment;
import me.wcy.music.service.AudioPlayer;
import me.wcy.music.service.QuitTimer;
import me.wcy.music.utils.SystemUtils;
import me.wcy.music.utils.binding.Bind;
//音乐列表界面
public class MusicActivity extends BaseActivity implements View.OnClickListener, QuitTimer.OnTimerListener,
        NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;//抽屉布局
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;//侧边栏
    @Bind(R.id.iv_menu)
    private ImageView ivMenu;//侧边栏按钮（左上角）
    @Bind(R.id.iv_search)
    private ImageView ivSearch;//查询按钮
    @Bind(R.id.tv_local_music)
    private TextView tvLocalMusic;//本地
    @Bind(R.id.tv_online_music)
    private TextView tvOnlineMusic;//在线
    @Bind(R.id.tv_more)
    private TextView tvClock;//闹钟
    @Bind(R.id.viewpager)
    private ViewPager mViewPager;//左右滑动
    @Bind(R.id.fl_play_bar)
    private FrameLayout flPlayBar;//快捷播放

    private LocalMusicFragment mLocalMusicFragment;
    private SheetListFragment mSheetListFragment;//在线
    private ClockFragment clockFragment;
    private PlayFragment mPlayFragment;
    private ControlPanel controlPanel;
    private NaviMenuExecutor naviMenuExecutor;
    private MenuItem timerItem;
    private boolean isPlayFragmentShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);//主页面
    }

    @Override
    protected void onServiceBound() {
        setupView();//s设置界面
        controlPanel = new ControlPanel(flPlayBar);//播放控制面板
        naviMenuExecutor = new NaviMenuExecutor(this);//导航面板（侧边）
        AudioPlayer.get().addOnPlayEventListener(controlPanel);
        parseIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        parseIntent();
    }

    private void setupView() {
        // add navigation header
//        vNavigationHeader = LayoutInflater.from(this).inflate(R.layout.navigation_header, navigationView, false);//天气
//        navigationView.addHeaderView(vNavigationHeader);

        // setup view pager
        mLocalMusicFragment = new LocalMusicFragment();
        mSheetListFragment = new SheetListFragment();
        clockFragment = new ClockFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(mLocalMusicFragment);
        adapter.addFragment(mSheetListFragment);
        adapter.addFragment(clockFragment);
        mViewPager.setAdapter(adapter);
        tvLocalMusic.setSelected(true);

        ivMenu.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvLocalMusic.setOnClickListener(this);
        tvOnlineMusic.setOnClickListener(this);
        tvClock.setOnClickListener(this);
        flPlayBar.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Extras.EXTRA_NOTIFICATION)) {
            showPlayingFragment();
            setIntent(new Intent());
        }
    }


    @Override
    public void onTimer(long remain) {
        if (timerItem == null) {
            timerItem = navigationView.getMenu().findItem(R.id.action_timer);
        }
        String title = getString(R.string.menu_timer);
        timerItem.setTitle(remain == 0 ? title : SystemUtils.formatTime(title + "(mm:ss)", remain));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_search:
                startActivity(new Intent(this, SearchMusicActivity.class));
                break;
            case R.id.tv_local_music:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_online_music:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv_more:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.fl_play_bar:
                showPlayingFragment();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        handler.postDelayed(() -> item.setChecked(false), 500);
        return naviMenuExecutor.onNavigationItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tvLocalMusic.setSelected(true);
                tvOnlineMusic.setSelected(false);
                tvClock.setSelected(false);
                break;
            case 1:
                tvOnlineMusic.setSelected(true);
                tvLocalMusic.setSelected(false);
                tvClock.setSelected(false);
                break;
            case 2:
                tvClock.setSelected(true);
                tvOnlineMusic.setSelected(false);
                tvLocalMusic.setSelected(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    //转到播放界面
    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new PlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }
//隐藏播放页面
    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    @Override//主页面退出
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        super.onBackPressed();
    }
//意外退出、中途退出
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {//保存
        outState.putInt(Keys.VIEW_PAGER_INDEX, mViewPager.getCurrentItem());
        mLocalMusicFragment.onSaveInstanceState(outState);
        mSheetListFragment.onSaveInstanceState(outState);
        clockFragment.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {//恢复
        mViewPager.post(() -> {
            mViewPager.setCurrentItem(savedInstanceState.getInt(Keys.VIEW_PAGER_INDEX), false);
            mLocalMusicFragment.onRestoreInstanceState(savedInstanceState);
            mSheetListFragment.onRestoreInstanceState(savedInstanceState);
            clockFragment.onRestoreInstanceState(savedInstanceState);
        });
    }

    @Override
    protected void onDestroy() {
        AudioPlayer.get().removeOnPlayEventListener(controlPanel);
        QuitTimer.get().setOnTimerListener(null);
        super.onDestroy();
    }
}
