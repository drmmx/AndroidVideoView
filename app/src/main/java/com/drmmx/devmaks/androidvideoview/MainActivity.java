package com.drmmx.devmaks.androidvideoview;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    static final int VIDEO_REQUEST_BACKGROUND = 300;
    static final int VIDEO_REQUEST_1 = 301;
    static final int VIDEO_REQUEST_2 = 302;

    private Uri mVideoUriBackground;
    private Uri mVideoUri1;
    private Uri mVideoUri2;
    private VideoView mVideoView1;
    private VideoView mVideoView2;
    private MyVideoView mVideoViewBackground;
    private FrameLayout mFrameLayout1;
    private FrameLayout mFrameLayout2;
    private FrameLayout mFrameLayoutBackground;
    private ImageView mBackgroundImage;
    private ImageView mImageVideo1;
    private ImageView mImageVideo2;
    private MaterialCardView mCardView1;
    private MaterialCardView mCardView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView1 = findViewById(R.id.videoView1);
        mVideoView2 = findViewById(R.id.videoView2);
        mVideoViewBackground = findViewById(R.id.videoViewBackground);
        mFrameLayout1 = findViewById(R.id.frame1);
        mFrameLayout2 = findViewById(R.id.frame2);
        mFrameLayoutBackground = findViewById(R.id.frameBackground);
        mBackgroundImage = findViewById(R.id.imageBackground);
        mImageVideo1 = findViewById(R.id.imageVideo1);
        mImageVideo2 = findViewById(R.id.imageVideo2);
        mCardView1 = findViewById(R.id.imageCardView1);
        mCardView2 = findViewById(R.id.imageCardView2);

        mVideoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0, 0);
                mp.setLooping(true);
            }
        });

        mVideoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0, 0);
                mp.setLooping(true);
            }
        });

        mVideoViewBackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                mp.setVolume(0, 0);
                mp.setLooping(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST_BACKGROUND) {
            if (resultCode == RESULT_OK) {
                mVideoUriBackground = data.getData();
                mBackgroundImage.setVisibility(View.GONE);
            }
        } else if (requestCode == VIDEO_REQUEST_1) {
            if (resultCode == RESULT_OK) {
                mVideoUri1 = data.getData();
                mImageVideo1.setVisibility(View.GONE);
                mCardView1.setVisibility(View.GONE);
            }
        } else if (requestCode == VIDEO_REQUEST_2) {
            if (resultCode == RESULT_OK) {
                mVideoUri2 = data.getData();
                mImageVideo2.setVisibility(View.GONE);
                mCardView2.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Error loading video", Toast.LENGTH_SHORT).show();
        }
    }

/*    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + mediaName);
    }*/

    private void initializePlayer() {
        mVideoView1.setVideoURI(mVideoUri1);
        mVideoView1.start();
        mVideoView2.setVideoURI(mVideoUri2);
        mVideoView2.start();
        mVideoViewBackground.setVideoURI(mVideoUriBackground);
        mVideoViewBackground.start();
    }

    private void releasePlayer() {
        mVideoView1.stopPlayback();
        mVideoView2.stopPlayback();
        mVideoViewBackground.stopPlayback();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView1.pause();
            mVideoView2.pause();
            mVideoViewBackground.pause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backgroundMenu:
                Intent backgroundIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                backgroundIntent.setType("video/*");
                startActivityForResult(backgroundIntent, VIDEO_REQUEST_BACKGROUND);
                return true;
            case R.id.video1Menu:
                Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent1.setType("video/*");
                startActivityForResult(intent1, VIDEO_REQUEST_1);
                return true;
            case R.id.video2Menu:
                Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent2.setType("video/*");
                startActivityForResult(intent2, VIDEO_REQUEST_2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
