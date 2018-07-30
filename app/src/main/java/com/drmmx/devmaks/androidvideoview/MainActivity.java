package com.drmmx.devmaks.androidvideoview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.drmmx.devmaks.androidvideoview.database.Content;
import com.drmmx.devmaks.androidvideoview.database.ContentDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int VIDEO_REQUEST_BACKGROUND = 300;
    static final int VIDEO_REQUEST_1 = 301;
    static final int VIDEO_REQUEST_2 = 302;

    private Content mContent;
    private Uri mVideoUriBackground;
    private Uri mVideoUri1;
    private Uri mVideoUri2;
    private Uri mVideoUriPreview1;
    private Uri mVideoUriPreview2;
    private VideoView mVideoView1;
    private VideoView mVideoView2;
    private MyVideoView mVideoViewBackground;
    private ImageView mBackgroundImage;
    private ImageView mImageVideo1;
    private ImageView mImageVideo2;
    private int backgroundProgress;
//    private int video1Progress;
//    private int video2Progress;

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
                    Uri.parse("android.resource://" + getPackageName() + "/raw/" + "introducing_firebase").toString(),
                    Uri.parse("android.resource://" + getPackageName() + "/raw/" + "firebase_authentication").toString(),
                    Uri.parse("android.resource://" + getPackageName() + "/raw/" + "introducing_firebase").toString());
            mDatabase.mContentDao().insert(mContent);
            mVideoUriBackground = Uri.parse(mDatabase.mContentDao().getLastContent().getBackground());
            mVideoUri1 = Uri.parse(mDatabase.mContentDao().getLastContent().getFirstVideo());
            mVideoUri2 = Uri.parse(mDatabase.mContentDao().getLastContent().getSecondVideo());
            mVideoUriPreview1 = Uri.parse(mDatabase.mContentDao().getLastContent().getFirstPreview());
            mVideoUriPreview2 = Uri.parse(mDatabase.mContentDao().getLastContent().getSecondPreview());
        } else {
            mVideoUriBackground = Uri.parse(mDatabase.mContentDao().getLastContent().getBackground());
            mVideoUri1 = Uri.parse(mDatabase.mContentDao().getLastContent().getFirstVideo());
            mVideoUri2 = Uri.parse(mDatabase.mContentDao().getLastContent().getSecondVideo());
            mVideoUriPreview1 = Uri.parse(mDatabase.mContentDao().getLastContent().getFirstPreview());
            mVideoUriPreview2 = Uri.parse(mDatabase.mContentDao().getLastContent().getSecondPreview());
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
                mp.setVolume(100, 100);
                mp.setLooping(true);
            }
        });

        frameLayout1.setOnClickListener(this);
        frameLayout2.setOnClickListener(this);
        imagePopupMenu.setOnClickListener(this);
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
        backgroundProgress = mVideoViewBackground.getCurrentPosition();
//        video1Progress = mVideoView1.getCurrentPosition();
//        video2Progress = mVideoView2.getCurrentPosition();
        mVideoViewBackground.pause();
        mVideoView1.pause();
        mVideoView2.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoViewBackground.seekTo(backgroundProgress);
//        mVideoView1.seekTo(video1Progress);
//        mVideoView2.seekTo(video2Progress);
        mVideoViewBackground.resume();
        mVideoView1.resume();
        mVideoView2.resume();
    }

    private void initializePlayer() {

        if (mVideoUriBackground == null) {
            mBackgroundImage.setVisibility(View.VISIBLE);
        } else {
            if (isImageType(MediaFilePath.getPath(this, mVideoUriBackground)).equals("image")) {
                Picasso.get().load(mVideoUriBackground.toString()).into(mBackgroundImage);
                mBackgroundImage.setVisibility(View.VISIBLE);
            } else {
                mVideoViewBackground.setVideoPath(mVideoUriBackground.toString());
                mBackgroundImage.setVisibility(View.GONE);
                mVideoViewBackground.start();
            }
        }

        if (mVideoUriPreview1 == null) {
            mImageVideo1.setVisibility(View.VISIBLE);
        } else {
            if (isImageType(MediaFilePath.getPath(this, mVideoUriPreview1)).equals("image")) {
                Picasso.get().load(mVideoUriPreview1.toString()).into(mImageVideo1);
                mImageVideo1.setVisibility(View.VISIBLE);
            } else {
                mVideoView1.setVideoPath(mVideoUriPreview1.toString());
                mImageVideo1.setVisibility(View.GONE);
                mVideoView1.start();
            }
        }

        if (mVideoUriPreview2 == null) {
            mImageVideo2.setVisibility(View.VISIBLE);
        } else {
            if (isImageType(MediaFilePath.getPath(this, mVideoUriPreview2)).equals("image")) {
                Picasso.get().load(mVideoUriPreview2.toString()).into(mImageVideo2);
                mImageVideo2.setVisibility(View.VISIBLE);
            } else {
                mVideoView2.setVideoPath(mVideoUriPreview2.toString());
                mImageVideo2.setVisibility(View.GONE);
                mVideoView2.start();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame1:
                if (!isImageType(MediaFilePath.getPath(this, mVideoUriPreview1)).equals("image")) {
                    Intent firstIntent = new Intent(Intent.ACTION_VIEW);
                    firstIntent.setPackage("com.mxtech.videoplayer.ad");
                    firstIntent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
                    firstIntent.setData(Uri.parse(mContent.getFirstVideo()));
                    if (!isPackageInstalled(this,"com.mxtech.videoplayer.ad")) {
                        Toast.makeText(this, "Please install MX Player", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(firstIntent);
                    }
                } else {
                    Toast.makeText(this, "Please select Video file", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.frame2:
                if (!isImageType(MediaFilePath.getPath(this, mVideoUriPreview2)).equals("image")) {
                    Intent secondIntent = new Intent(Intent.ACTION_VIEW);
                    secondIntent.setPackage("com.mxtech.videoplayer.ad");
                    secondIntent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
                    secondIntent.setData(Uri.parse(mContent.getSecondVideo()));
                    if (!isPackageInstalled(this,"com.mxtech.videoplayer.ad")) {
                        Toast.makeText(this, "Please install MX Player", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(secondIntent);
                    }
                } else {
                    Toast.makeText(this, "Please select Video file", Toast.LENGTH_SHORT).show();
                }
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
                mContent.setBackground(mVideoUriBackground.toString());
                mDatabase.mContentDao().update(mContent);
            }
        } else if (requestCode == VIDEO_REQUEST_1) {
            if (resultCode == RESULT_OK && data != null) {
                mVideoUri1 = data.getData();
                mVideoUriPreview1 = data.getData();
                mImageVideo1.setVisibility(View.GONE);
                mContent.setFirstVideo(MediaFilePath.getPath(this, mVideoUri1));
                mContent.setFirstPreview(mVideoUriPreview1.toString());
                mDatabase.mContentDao().update(mContent);
            }
        } else if (requestCode == VIDEO_REQUEST_2) {
            if (resultCode == RESULT_OK && data != null) {
                mVideoUri2 = data.getData();
                mVideoUriPreview2 = data.getData();
                mImageVideo2.setVisibility(View.GONE);
                mContent.setSecondVideo(MediaFilePath.getPath(this, mVideoUri2));
                mContent.setSecondPreview(mVideoUriPreview2.toString());
                mDatabase.mContentDao().update(mContent);
            }
        }
    }

    private String isImageType(String filepath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filepath);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (type != null) {
            if (type.equals("image/jpeg") || type.equals("image/jpg") || type.equals("image/png") || type.equals("image/bmp")) {
                type = "image";
                return type;
            } else {
                return type;
            }
        } else return "video";
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}