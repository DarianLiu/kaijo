package com.geek.kaijo.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.PermissionUtils;
import com.geek.kaijo.di.component.DaggerVideoRecordComponent;
import com.geek.kaijo.di.module.VideoRecordModule;
import com.geek.kaijo.mvp.contract.VideoRecordContract;
import com.geek.kaijo.mvp.presenter.VideoRecordPresenter;
import com.geek.kaijo.view.RecorderCircleView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tsy.sdk.video.CameraHelper;
import com.tsy.sdk.video.CameraPreviewView;
import com.tsy.sdk.video.MyOrientationDetector;
import com.tsy.sdk.video.Recorder;

import org.bytedeco.javacv.FrameFilter;
import org.bytedeco.javacv.FrameRecorder;

import java.lang.ref.WeakReference;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class VideoRecordActivity extends BaseActivity<VideoRecordPresenter> implements VideoRecordContract.View,View.OnTouchListener,View.OnClickListener {

    private Camera mCamera;
    private int mCameraFace = Camera.CameraInfo.CAMERA_FACING_BACK;     //相机默认后置摄像头
    private Recorder mRecorder;
    private CameraPreviewView mCameraPreviewView;       //预览view
    private RecorderCircleView mRecorderCircleView;     //录像按钮
    private ImageView mImgCamera;       //翻转摄像头
    private MyHandler mMyHandler;
    private RxPermissions rxPermissions;

    private MyOrientationDetector mMyOrientationDetector;

    private int RECORDER_WIDTH;     //视频录制宽度
    private int RECORDER_HEIGHT;    //视频录制高度

    private static final int MIN_RECORDER_TIME = 1000;       //最小录制时长 单位ms
    private static final int MAX_RECORDER_TIME = 10000;      //最大录制时长 单位ms

    private static final int MSG_STOP = 1;      //录制结束

    private static class MyHandler extends Handler {
        private String TAG = "MyHandler";
        private WeakReference<VideoRecordActivity> mWeakRecorderActivity;
        private VideoRecordActivity mRecorderActivity;

        MyHandler(VideoRecordActivity recorderActivity) {
            mWeakRecorderActivity = new WeakReference<VideoRecordActivity>(recorderActivity);
            mRecorderActivity = mWeakRecorderActivity.get();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STOP:
                    mRecorderActivity.stopRecorder();
                    break;
            }
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoRecordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .videoRecordModule(new VideoRecordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_video_record; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRecorderCircleView = (RecorderCircleView) findViewById(R.id.recorder_circle_view);
        mRecorderCircleView.setTotalTime(MAX_RECORDER_TIME);
        mImgCamera = (ImageView) findViewById(R.id.img_camera);
        mMyHandler = new MyHandler(this);

        //录制尺寸为全屏比例的一半
        DisplayMetrics dm = getResources().getDisplayMetrics();
        RECORDER_WIDTH = dm.widthPixels / 2;
        RECORDER_HEIGHT = dm.heightPixels / 2;

        mMyOrientationDetector = new MyOrientationDetector(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyOrientationDetector.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyOrientationDetector.disable();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onStart() {
        super.onStart();
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        //同时申请多个权限
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {           // All requested permissions are granted
                initRecoder();
            } else {
                // At least one permission is denied
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoRecordActivity.this);
                            builder.setTitle("权限提醒");
                            builder.setMessage("录制小视频需要开启摄像头、录音、文件权限");
                            builder.setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PermissionUtils.permissionSkipSetting(VideoRecordActivity.this);
                                }
                            });
                            builder.show();
            }
        });
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

    //初始化
    private void initRecoder() {
        //检查sd卡
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "SD卡不可用！", Toast.LENGTH_SHORT).show();
            return ;
        }

        //初始化摄像头
        mCamera = CameraHelper.getCamera(this, mCameraFace);       //默认打开后置摄像头
        String path = Environment.getExternalStorageDirectory() + "/a.com.tsy.videodemo/" + System.currentTimeMillis() + ".mp4";  //存储路径

        //初始化摄像预览界面
        mCameraPreviewView = (CameraPreviewView) findViewById(R.id.camera_preview);
        mCameraPreviewView.init(mCamera, RECORDER_WIDTH, RECORDER_HEIGHT);

        //初始化recorder
        mRecorder = new Recorder.Builder()
                .context(getApplicationContext())
                .camera(mCamera)
                .cameraFace(mCameraFace)
                .outputSize(RECORDER_WIDTH, RECORDER_HEIGHT)
                .outputFilePath(path)
                .orientationDetector(mMyOrientationDetector)
                .build();

        mRecorderCircleView.setOnTouchListener(this);
        mImgCamera.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        releaseHandler();
        releaseRecoder();
        releaseCamera();
        super.onStop();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startRecorder();
                break;

            case MotionEvent.ACTION_UP:
                stopRecorder();
                break;
        }

        return true;
    }


    //开始录制
    private void startRecorder() {
        if(mRecorder == null || mRecorder.isRecording()) {
            return;
        }

        try {
            mRecorder.start();
            mRecorderCircleView.start();
            mMyHandler.sendEmptyMessageDelayed(MSG_STOP, MAX_RECORDER_TIME);        //MAX_RECORDER_TIME后自动停止
        } catch (FrameRecorder.Exception|FrameFilter.Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "录制失败", Toast.LENGTH_SHORT).show();
        }
    }

    //结束录制
    private void stopRecorder() {
        if(mRecorder == null || !mRecorder.isRecording()) {
            return;
        }

        if(mMyHandler.hasMessages(MSG_STOP)) {
            mMyHandler.removeMessages(MSG_STOP);
        }

        try {
            mRecorderCircleView.stop();
            long recordingTime = mRecorder.getRecordingTime();
            mRecorder.stop();
            if(recordingTime < MIN_RECORDER_TIME) {         //少于最少录制时间
                Toast.makeText(getApplicationContext(), "录制时间至少" + MIN_RECORDER_TIME / 1000 + "s", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getApplicationContext(), "录制完成", Toast.LENGTH_SHORT).show();
                startActivityForResult(RecoderPlayerActivity.createIntent(this, mRecorder.getOutputPath()),1);
//                finish();
            }
        } catch (FrameRecorder.Exception|FrameFilter.Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_camera:
                if(mRecorder.isRecording()) {       //录制的时候禁止旋转摄像头
                    return;
                }
                switchCamera();
                break;
        }
    }

    //切换前后摄像头
    private void switchCamera() {
        if(mCameraFace == Camera.CameraInfo.CAMERA_FACING_BACK) {       //切换前置
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                Toast.makeText(getApplicationContext(), "不支持前置摄像头", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        mImgCamera.setOnClickListener(null);

        releaseCamera();

        if(mCameraFace == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mCameraFace = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCameraFace = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        mCamera = CameraHelper.getCamera(this, mCameraFace);
        mCameraPreviewView.switchCamera(mCamera);
        mRecorder.switchCamera(mCamera, mCameraFace);

        mImgCamera.setOnClickListener(this);
    }

    //释放handler
    private void releaseHandler() {
        if(mMyHandler != null) {
            if(mMyHandler.hasMessages(MSG_STOP)) {
                mMyHandler.removeMessages(MSG_STOP);
            }
        }
    }

    //释放recorder
    private void releaseRecoder() {
        if(mRecorder != null) {
            if(mRecorder.isRecording()) {
                try {
                    mRecorder.stop();
                } catch (FrameRecorder.Exception|FrameFilter.Exception e) {
                    e.printStackTrace();
                }
            }
            mRecorder = null;
        }
    }

    //释放camera
    private void releaseCamera() {
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    //检查Android M系统以下的国产定制系统权限处理 （模拟开启 捕获异常来检查权限）
    private static boolean checkPermissionBelowM() {
        boolean canUse = true;

        Camera camera = null;
        AudioRecord audioRecord = null;
        try {
            camera = Camera.open();
            Camera.Parameters mParameters = camera.getParameters();
            camera.setParameters(mParameters);

            int bufferSize = AudioRecord.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            audioRecord.startRecording();
            if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                throw new Exception();
            }
            audioRecord.stop();
        } catch (Exception e) {
            canUse = false;
        } finally {
            if(camera != null) {
                camera.release();
            }

            if(audioRecord != null) {
                audioRecord.release();
            }
        }

        return canUse;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode==1 && resultCode==1){
//                String mVideoPath = data.getStringExtra("mVideoPath");
                setResult(1,data);
                finish();
            }
        }
    }
}
