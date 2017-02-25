package me.shenj.lyric.sample;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import me.shenj.lyric.LyricHelper;
import me.shenj.lyric.LyricView;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int SELECT_MUSIC_REQUEST_CODE = 1;
    private static final int SELECT_LRC_REQUEST_CODE = 2;
    private String mMusicPath = "";
    private String mLrcPath = "";

    private Handler mHandler = new Handler();
    private LyricView mLyricView;
    private ImageView mPlayBtn, mReplayBtn;
    private Button mMusicSelectorBtn, mLrcSelectorBtn;
    private TextView mMusicPathText, mLrcPathText;
    private SeekBar mSeekBar;

    private MediaPlayer mPlayer;

    private Intent mSelector;
    private LyricHelper mLyricHelper = new LyricHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayBtn = (ImageView) findViewById(R.id.play);
        mReplayBtn = (ImageView) findViewById(R.id.replay);
        mSeekBar = (SeekBar) findViewById(R.id.bar);
        mMusicSelectorBtn = (Button) findViewById(R.id.MusicSelector);
        mLrcSelectorBtn = (Button) findViewById(R.id.LrcSelector);
        mMusicPathText = (TextView) findViewById(R.id.musicPath);
        mLrcPathText = (TextView) findViewById(R.id.lrcPath);
        mLyricView = (LyricView) findViewById(R.id.lyricView);

        mPlayer = new MediaPlayer();

        mPlayBtn.setOnClickListener(this);
        mReplayBtn.setOnClickListener(this);
        mMusicSelectorBtn.setOnClickListener(this);
        mLrcSelectorBtn.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int position = (int) (mPlayer.getDuration() * (float) progress / 1000);
                mPlayer.seekTo(position);
                mLyricView.setCurrentTime(position, false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
        mSelector = new Intent(Intent.ACTION_GET_CONTENT);
        mSelector.setType("*/*");
        mSelector.addCategory(Intent.CATEGORY_OPENABLE);
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            if (!mPlayer.isPlaying()) {
                mPlayBtn.setImageResource(R.drawable.ic_action_play);
                mHandler.removeCallbacks(run);
                return;
            }
            long time = mPlayer.getCurrentPosition();
            long duration = mPlayer.getDuration();
            int progress = (int) ((float) time / (float) duration * 1000);
            mSeekBar.setProgress(progress);
            mLyricView.setCurrentTime(time, true);
            mHandler.postDelayed(run, 500);
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(run);
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mPlayBtn) {
            if (mPlayer.isPlaying()) {
                pause();
            } else {
                play();
            }
        } else if (v == mReplayBtn) {
            mPlayer.seekTo(0);
            mLyricView.setCurrentTime(0, false);
        } else if (v == mMusicSelectorBtn) {
            startActivityForResult(Intent.createChooser(mSelector, getString(R.string.select_music)),
                    SELECT_MUSIC_REQUEST_CODE);
        } else if (v == mLrcSelectorBtn) {
            startActivityForResult(Intent.createChooser(mSelector, getString(R.string.select_lrc)),
                    SELECT_LRC_REQUEST_CODE);
        }
    }

    private void pause() {
        mPlayer.pause();
        mPlayBtn.setImageResource(R.drawable.ic_action_play);
        mHandler.removeCallbacks(run);
    }

    private void play() {
        if (mMusicPath == null || mMusicPath.length() == 0) {
            return;
        }
        mPlayer.start();
        mPlayBtn.setImageResource(R.drawable.ic_action_pause);
        mHandler.post(run);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri uri = data.getData();
        String path = uri.getPath();
        if (path == null) {
            return;
        }
        if (requestCode == SELECT_MUSIC_REQUEST_CODE) {
            mMusicPath = path;
            mMusicPathText.setText(mMusicPath);
            try {
                mPlayer.setDataSource(mMusicPath);
                mPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_LRC_REQUEST_CODE) {
            mLrcPath = path;
            mLrcPathText.setText(mLrcPath);
            mLyricHelper.setLrcPath(mLrcPath);
            mLyricView.setLyricContent(mLyricHelper.getLyrics());
        }
    }
}
