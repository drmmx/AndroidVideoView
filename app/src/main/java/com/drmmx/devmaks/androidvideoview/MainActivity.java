package com.drmmx.devmaks.androidvideoview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.drmmx.devmaks.androidvideoview.database.Content;
import com.drmmx.devmaks.androidvideoview.database.ContentDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
        FrameLayout frameLayout1 = findViewById(R.id.frame1);
        FrameLayout frameLayout2 = findViewById(R.id.frame2);
        mBackgroundImage = findViewById(R.id.imageBackground);
        mImageVideo1 = findViewById(R.id.imageVideo1);
        mImageVideo2 = findViewById(R.id.imageVideo2);
        ImageView imagePopupMenu = findViewById(R.id.popupImageView);

        //Db init
        mDatabase = ContentDatabase.getInstance(this);
        mContent = mDatabase.mContentDao().getLastContent();
        if (mContent == null) {
            mContent = new Content(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "background_video_2").toString(),
                    Uri.parse("android.resource://" + getPackageName() + "/raw/" + "firebase_authentication").toString(),
                    Uri.parse("android.resource://" + getPackageName() + "/raw/" + "introducing_firebase").toString());
            mDatabase.mContentDao().insert(mContent);
            mVideoUriBackground = Uri.parse(mDatabase.mContentDao().getLastContent().getBackground());
            mVideoUri1 = Uri.parse(mDatabase.mContentDao().getLastContent().getFirstVideo());
            mVideoUri2 = Uri.parse(mDatabase.mContentDao().getLastContent().getSecondVideo());
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

        frameLayout1.setOnClickListener(this);
        frameLayout2.setOnClickListener(this);
        imagePopupMenu.setOnClickListener(this);
    }

    private void initializePlayer() {
        if (mVideoUriBackground == null) {
//            mVideoViewBackground.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "background_video_2").toString());
            mBackgroundImage.setVisibility(View.GONE);
        } else {
            mVideoViewBackground.setVideoPath(mDatabase.mContentDao().getLastContent().getBackground());
            mBackgroundImage.setVisibility(View.GONE);
        }
        mVideoViewBackground.start();

        if (mVideoUri1 == null) {
//            mVideoView1.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "introducing_firebase").toString());
            mImageVideo1.setVisibility(View.GONE);
        } else {
            mVideoView1.setVideoPath(mDatabase.mContentDao().getLastContent().getFirstVideo());
            mImageVideo1.setVisibility(View.GONE);
        }
        mVideoView1.start();

        if (mVideoUri2 == null) {
//            mVideoView2.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/raw/" + "firebase_authentication").toString());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame1:
                Intent firstIntent = new Intent(Intent.ACTION_VIEW);
                firstIntent.setPackage("com.mxtech.videoplayer.ad");
                firstIntent.setClassName( "com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen" );
                firstIntent.setData(Uri.parse(mContent.getFirstVideo()));
                startActivity(firstIntent);
                Log.d("MainActivity_", "onClick: " + mContent.getFirstVideo());
                break;
            case R.id.frame2:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.mxtech.videoplayer.ad");
                intent.setClassName( "com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen" );
                intent.setData(Uri.parse(mContent.getSecondVideo()));
                Log.d("MainActivity_", "onClick: " + mContent.getSecondVideo());
                startActivity(intent);
                break;
            case R.id.popupImageView:
                showPopupMenu(view);
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View v) {

        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.inflate(R.menu.popup_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.backgroundMenu:
                        Intent backgroundIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        backgroundIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        backgroundIntent.setType("*/*");
                        startActivityForResult(backgroundIntent, VIDEO_REQUEST_BACKGROUND);
                        break;
                    case R.id.video1Menu:
                        Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent1.addCategory(Intent.CATEGORY_OPENABLE);
                        intent1.setType("*/*");
                        startActivityForResult(intent1, VIDEO_REQUEST_1);
                        break;
                    case R.id.video2Menu:
                        Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent2.addCategory(Intent.CATEGORY_OPENABLE);
                        intent2.setType("*/*");
                        startActivityForResult(intent2, VIDEO_REQUEST_2);
                        break;
                }
                return true;
            }
        });
        @SuppressLint("RestrictedApi")
        MenuPopupHelper menuHelper = new MenuPopupHelper(v.getContext(), (MenuBuilder) menu.getMenu(), v);
        menuHelper.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST_BACKGROUND) {
            if (resultCode == RESULT_OK && data != null) {
                mVideoUriBackground = data.getData();
                mBackgroundImage.setVisibility(View.GONE);
                mContent.setBackground(ImageFilePath.getPath(this, mVideoUriBackground));
                mDatabase.mContentDao().update(mContent);
            }
        } else if (requestCode == VIDEO_REQUEST_1) {
            if (resultCode == RESULT_OK && data != null) {
                mVideoUri1 = data.getData();
                mImageVideo1.setVisibility(View.GONE);
                mContent.setFirstVideo(ImageFilePath.getPath(this, mVideoUri1));
                mDatabase.mContentDao().update(mContent);
            }
        } else if (requestCode == VIDEO_REQUEST_2) {
            if (resultCode == RESULT_OK && data != null) {
                mVideoUri2 = data.getData();
                mImageVideo2.setVisibility(View.GONE);
                mContent.setSecondVideo(ImageFilePath.getPath(this, mVideoUri2));
                mDatabase.mContentDao().update(mContent);
            }
        }
    }
}