package com.geek.kaijo.mvp.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.Utils.GridSpacingItemDecoration;
import com.geek.kaijo.di.component.DaggerPhotoActivityComponent;
import com.geek.kaijo.di.module.PhotoActivityModule;
import com.geek.kaijo.mvp.contract.PhotoActivityContract;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.presenter.PhotoActivityPresenter;
import com.geek.kaijo.mvp.ui.adapter.PhotoAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PhotoActivityActivity extends BaseActivity<PhotoActivityPresenter> implements PhotoActivityContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tv_toolbar_title_right_text;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<UploadFile> photoList = new ArrayList<>();
    private PhotoAdapter adapter ;
    private List<UploadFile> selectPhotoList= new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPhotoActivityComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .photoActivityModule(new PhotoActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_photo; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initToolbar();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
        getPhotoData();
    }

    private void initToolbar(){
        tvToolbarTitle.setText("图片选择");
        tv_toolbar_title_right_text.setVisibility(View.VISIBLE);
        tv_toolbar_title_right_text.setText("确定");
        tv_toolbar_title_right_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectPhotoList!=null && selectPhotoList.size()>0){
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("selectPhotoList", (ArrayList<? extends Parcelable>) selectPhotoList);
                    setResult(UploadActivity.RESULT_PHOTO,intent);
                }
                finish();
            }
        });
    }

    private void getPhotoData() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_TAKEN + " DESC");
//                MediaStore.Images.Media.DATE_MODIFIED);

//        Cursor mCursor = mContentResolver.query(mImageUri, null,null,null,null);
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    UploadFile uploadPhoto = new UploadFile();
                    uploadPhoto.setFileName(path);
                    photoList.add(uploadPhoto);
                }
                recyclerViewAdapter();
            }
        });

    }

    private void recyclerViewAdapter(){
        if(adapter==null){
            adapter = new PhotoAdapter(this,photoList);
            recyclerView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setOnItemOnClilcklisten(new PhotoAdapter.OnItemOnClicklisten() {
            @Override
            public void onItemClick(View v, int position) {
                if(photoList.get(position).selectStatus==0){
                    photoList.get(position).selectStatus = 1;
                }else {
                    photoList.get(position).selectStatus = 0;
                }
                boolean isAdd = true;
                for(int i=0;i<selectPhotoList.size();i++){
                    if(selectPhotoList.get(i).getFileName().equals(photoList.get(position).getFileName())){ //存在  修改状态
                        selectPhotoList.remove(i);
                        selectPhotoList.add(i,photoList.get(position));
                        isAdd = false;
                        break;
                    }
                }
                if(isAdd){
                    selectPhotoList.add(photoList.get(position));
                }
                adapter.notifyItemChanged(position);

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
}
