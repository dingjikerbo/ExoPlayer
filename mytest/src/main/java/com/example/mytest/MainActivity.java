package com.example.mytest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.io.File;

public class MainActivity extends AppCompatActivity {

  private PlayerView mPlayerView;
  private SimpleExoPlayer mExoPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test_exo_player);

    requestExternalStoragePermission();

    String path = getIntent().getStringExtra("path");
    mPlayerView = findViewById(R.id.exo_play_view);
    play();
  }

  private void requestExternalStoragePermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, /* requestCode= */ 0);
      }
    }
  }

  private void play() {
    mExoPlayer = ExoPlayerFactory.newSimpleInstance(
        this,
        new DefaultRenderersFactory(this),
        new DefaultTrackSelector(),
        new DefaultLoadControl());

    // 生成数据媒体实例，通过该实例加载媒体数据
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util
        .getUserAgent(this, "exoplayerdemo"));
    // 创建资源
    MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
        .createMediaSource(Uri.fromFile(new File("/sdcard/DCIM/Camera/1994.mp4")));

    // 将播放器附加到view
    mPlayerView.setPlayer(mExoPlayer);
    // 准备播放
    mExoPlayer.prepare(mediaSource);
    // 准备好了之后自动播放，如果已经准备好了，调用该方法实现暂停、开始功能
    mExoPlayer.setPlayWhenReady(true);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mExoPlayer.play();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mExoPlayer.pause();
  }

  @Override
  protected void onDestroy() {
    if (mExoPlayer != null) {
      mExoPlayer.stop();
      mExoPlayer.release();
    }
    super.onDestroy();
  }
}
