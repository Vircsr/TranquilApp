package me.wcy.music.fragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.io.File;

import me.wcy.music.R;
import me.wcy.music.activity.MusicInfoActivity;
import me.wcy.music.adapter.OnMoreClickListener;
import me.wcy.music.adapter.PlaylistAdapter;
import me.wcy.music.application.AppCache;
import me.wcy.music.constants.Keys;
import me.wcy.music.constants.RequestCode;
import me.wcy.music.constants.RxBusTags;
import me.wcy.music.loader.MusicLoaderCallback;
import me.wcy.music.model.Music;
import me.wcy.music.service.AudioPlayer;
import me.wcy.music.utils.PermissionReq;
import me.wcy.music.utils.ToastUtils;
import me.wcy.music.utils.binding.Bind;

import static me.wcy.music.constants.Keys.LOCAL_MUSIC_POSITION;

/**
 * 本地音乐列表
 *
 */
public class LocalMusicFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnMoreClickListener {
    @Bind(R.id.lv_local_music)
    private ListView lvLocalMusic;
    @Bind(R.id.v_searching)
    private TextView vSearching;
//    private View view;
    private Loader<Cursor> loader;
    private PlaylistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
//    if(view !=null){
//        ViewGroup parent = (ViewGroup) getView().getParent();
//        if(parent != null){
//            parent.removeView(view);
//        }
//        return view;
//    }
//    view = inflater.inflate(R.layout.fragment_local_music, container, false);
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new PlaylistAdapter(AppCache.get().getLocalMusicList());
        adapter.setOnMoreClickListener(this);
        lvLocalMusic.setAdapter(adapter);
        loadMusic();
    }

    private void loadMusic() {
        lvLocalMusic.setVisibility(View.GONE);
        vSearching.setVisibility(View.VISIBLE);
        PermissionReq.with(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .result(new PermissionReq.Result() {
                    @Override
                    public void onGranted() {
                        initLoader();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.show(R.string.no_permission_storage);
                        lvLocalMusic.setVisibility(View.VISIBLE);
                        vSearching.setVisibility(View.GONE);
                    }
                })
                .request();
    }

    private void initLoader() {
        loader = getActivity().getLoaderManager().initLoader(0, null, new MusicLoaderCallback(getContext(), value -> {
//            ToastUtils.show("本地播放列表初始化");
            AppCache.get().getLocalMusicList().clear();
            AppCache.get().getLocalMusicList().addAll(value);
            lvLocalMusic.setVisibility(View.VISIBLE);
            vSearching.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }));
    }

    @Subscribe(tags = { @Tag(RxBusTags.SCAN_MUSIC) })
    public void scanMusic(Object object) {
        ToastUtils.show("接收消息");
        if (loader != null) {
            loader.forceLoad();
        }
    }

    @Override
    protected void setListener() {
        lvLocalMusic.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Music music = AppCache.get().getLocalMusicList().get(position);
        AudioPlayer.get().addAndPlay(music);
        ToastUtils.show("已添加到播放列表");
    }

    @Override
    public void onMoreClick(final int position) {
        Music music = AppCache.get().getLocalMusicList().get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(music.getTitle());
        dialog.setItems(R.array.local_music_dialog, (dialog1, which) -> {
            switch (which) {
                case 0:// 查看歌曲信息
                    MusicInfoActivity.start(getContext(), music);
                    break;
                case 1:// 删除
                    deleteMusic(music);
                    break;
            }
        });
        dialog.show();
    }

    private void deleteMusic(final Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        String title = music.getTitle();
        String msg = getString(R.string.delete_music, title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(R.string.delete, (dialog1, which) -> {
            File file = new File(music.getPath());
            if (file.delete()) {
                // 刷新媒体库
                Intent intent =
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://".concat(music.getPath())));
                getContext().sendBroadcast(intent);
            }
        });
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.REQUEST_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(getContext())) {
                ToastUtils.show(R.string.grant_permission_setting);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int position = lvLocalMusic.getFirstVisiblePosition();
        int offset = (lvLocalMusic.getChildAt(0) == null) ? 0 : lvLocalMusic.getChildAt(0).getTop();
        outState.putInt(LOCAL_MUSIC_POSITION, position);
        outState.putInt(Keys.LOCAL_MUSIC_OFFSET, offset);
    }

    public void onRestoreInstanceState(final Bundle savedInstanceState) {
        lvLocalMusic.post(() -> {
            int position = savedInstanceState.getInt(LOCAL_MUSIC_POSITION);
            int offset = savedInstanceState.getInt(Keys.LOCAL_MUSIC_OFFSET);
            lvLocalMusic.setSelectionFromTop(position, offset);
        });
    }

}
