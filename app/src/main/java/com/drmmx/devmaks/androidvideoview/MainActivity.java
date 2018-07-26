package com.drmmx.devmaks.androidvideoview;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
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

import com.drmmx.devmaks.androidvideoview.database.Content;
import com.drmmx.devmaks.androidvideoview.database.ContentDatabase;

public class MainActivity extends AppCompatActivity {

    static final int VIDEO_REQUEST_BACKGROUND = 300;
    static final int VIDEO_REQUEST_1 = 301;
    static final int VIDEO_REQUEST_2 = 302;

    private Content mContent;
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

    private ContentDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        mVideoView1 = findViewById(R.id.videoView1);
        mVideoView2 = findViewById(R.id.videoView2);
        mVideoViewBackground = findViewById(R.id.videoViewBackground);
        mFrameLayout1 = findViewById(R.id.frame1);
        mFrameLayout2 = findViewById(R.id.frame2);
        mFrameLayoutBackground = findViewById(R.id.frameBackground);
        mBackgroundImage = findViewById(R.id.imageBackground);
        mImageVideo1 = findViewById(R.id.imageVideo1);
        mImageVideo2 = findViewById(R.id.imageVideo2);

        //Db init
        mDatabase = ContentDatabase.getInstance(this);
        mContent = mDatabase.mContentDao().getLastContent();
        if (mContent == null) {
            mContent = new Content(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "background_video_2").toString(),
                    Uri.parse("android.resource://" + getPackageName() + "/raw/" + "firebase_authentication").toString(),
                    Uri.parse("android.resource://" + getPackageName() + "/raw/" + "introducing_firebase").toString());
            mDatabase.mContentDao().insert(mContent);
        } else {
            mVideoUriBackground = Uri.parse(mDatabase.mContentDao().getLastContent().getBackground());
            mVideoUri1 = Uri.parse(mDatabase.mContentDao().getLastContent().getFirstVideo());
            mVideoUri2 = Uri.parse(mDatabase.mContentDao().getLastContent().getSecondVideo());
        }

        //MP settings
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
//                mDatabase.mContentDao().getContent().setBackground(mVideoUriBackground.toString());
            }
        } else if (requestCode == VIDEO_REQUEST_1) {
            if (resultCode == RESULT_OK) {
                mVideoUri1 = data.getData();
                mImageVideo1.setVisibility(View.GONE);
//                mDatabase.mContentDao().getContent().setFirstVideo(mVideoUri1.toString());
            }
        } else if (requestCode == VIDEO_REQUEST_2) {
            if (resultCode == RESULT_OK) {
                mVideoUri2 = data.getData();
                mImageVideo2.setVisibility(View.GONE);
//                mDatabase.mContentDao().getContent().setSecondVideo(mVideoUri2.toString());
            }
        } else {
            Toast.makeText(this, "Error loading video", Toast.LENGTH_SHORT).show();
        }
        mContent = new Content(mVideoUriBackground.toString(), mVideoUri1.toString(), mVideoUri2.toString());
        mDatabase.mContentDao().insert(mContent);
    }

/*    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + mediaName);
    }*/

    private void initializePlayer() {
        if (mVideoUriBackground == null) {
            mVideoViewBackground.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "background_video_2").toString());
            mBackgroundImage.setVisibility(View.GONE);
        } else {
            mVideoViewBackground.setVideoPath(mDatabase.mContentDao().getLastContent().getBackground());
            mBackgroundImage.setVisibility(View.GONE);
        }
        mVideoViewBackground.start();

        if (mVideoUri1 == null) {
            mVideoView1.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "introducing_firebase").toString());
            mImageVideo1.setVisibility(View.GONE);
        } else {
            mVideoView1.setVideoPath(mDatabase.mContentDao().getLastContent().getFirstVideo());
            mImageVideo1.setVisibility(View.GONE);
        }
        mVideoView1.start();

        if (mVideoUri2 == null) {
            mVideoView2.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "firebase_authentication").toString());
            mImageVideo2.setVisibility(View.GONE);
        } else {
            mVideoView2.setVideoPath(mDatabase.mContentDao().getLastContent().getSecondVideo());
            mImageVideo2.setVisibility(View.GONE);
        }
        mVideoView2.start();
    }

    private void releasePlayer() {
        mVideoViewBackground.stopPlayback();
        mVideoView1.stopPlayback();
        mVideoView2.stopPlayback();
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
            mVideoViewBackground.pause();
            mVideoView1.pause();
            mVideoView2.pause();
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
