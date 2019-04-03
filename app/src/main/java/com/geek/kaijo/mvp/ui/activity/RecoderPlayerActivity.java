package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.di.component.DaggerRecoderPlayerComponent;
import com.geek.kaijo.di.module.RecoderPlayerModule;
import com.geek.kaijo.mvp.contract.RecoderPlayerContract;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.presenter.RecoderPlayerPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RecoderPlayerActivity extends BaseActivity<RecoderPlayerPresenter> implements RecoderPlayerContract.View {

    private static final String INTENT_VIDEO_PATH = "video_path";

    private String mVideoPath;

    private VideoView mVideoView;
    private CircleProgressBar mCircleProgressBar;
    private RxPermissions rxPermissions;
    @BindView(R.id.btn_upload)
    public Button btn_upload;
    @BindView(R.id.btn_back)
    public Button btn_back;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRecoderPlayerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .recoderPlayerModule(new RecoderPlayerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_recoder_player; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mVideoPath = getIntent().getStringExtra(INTENT_VIDEO_PATH);

        //video view
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setVisibility(View.GONE);

        //loading circle
        mCircleProgressBar = (CircleProgressBar) findViewById(R.id.circleProgressBar);
        mCircleProgressBar.setVisibility(View.GONE);
        mCircleProgressBar.setColorSchemeResources(android.R.color.holo_blue_light);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        rxPermissions = new RxPermissions(this);
        //同时申请多个权限
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {           // All requested permissions are granted
                playLocalVideo();
            } else {
                // At least one permission is denied
                AlertDialog.Builder builder = new AlertDialog.Builder(RecoderPlayerActivity.this);
                builder.setTitle("权限提醒");
                builder.setMessage("需要读取本地文件权限");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PermissionUtils.permissionSkipSetting(RecoderPlayerActivity.this);
                    }
                });
                builder.show();
            }
        });


    }

    //播放本地视频
    private void playLocalVideo() {
        //获取屏幕宽度
        DisplayMetrics dm = getResources().getDisplayMetrics();

        //获取视频宽高
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mVideoPath);
        float mediaWidth = dm.widthPixels;
        float mediaHeight = dm.heightPixels;
        try {
            int rotation = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));

            if(rotation == 90 || rotation == 270) {
                mediaWidth = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                mediaHeight = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            } else {
                mediaWidth = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                mediaHeight = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            }
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "视频播放错误，请重试", Toast.LENGTH_SHORT).show();
            File file = new File(mVideoPath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            finish();
        }

        //根据比例占全屏
        int width = 0;
        int height = 0;
        if(mediaWidth/mediaHeight > dm.widthPixels/dm.heightPixels) {
            width = dm.widthPixels;
            height = (int) (mediaHeight / (mediaWidth/dm.widthPixels));
        } else {
            height = dm.heightPixels;
            width = (int) (mediaWidth / (mediaHeight/dm.heightPixels));
        }
        ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        mVideoView.setLayoutParams(layoutParams);

        //视频路径
        mVideoView.setVideoPath(mVideoPath);

        //点击退出
//        mVideoView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                        finish();
//                        break;
//
//                }
//                return true;
//            }
//        });

        //循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.seekTo(0);
                mVideoView.start();
            }
        });

        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.start();
    }

    /**
     * 打开视频
     * @param context
     * @param videoPath
     * @return
     */
    public static Intent createIntent(Context context, String videoPath) {
        Intent intent = new Intent(context, RecoderPlayerActivity.class);
        intent.putExtra(INTENT_VIDEO_PATH, videoPath);
        return intent;
    }

    @Override
    public void uploadSuccess(UploadFile uploadFile) {
        if(uploadFile.getIsSuccess()==1){
            setResult(1,new Intent().putExtra("UploadFile",uploadFile));
            finish();
        }
    }

    @OnClick({R.id.btn_upload,R.id.btn_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_upload:
                mPresenter.uploadFile(this,mVideoPath);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
