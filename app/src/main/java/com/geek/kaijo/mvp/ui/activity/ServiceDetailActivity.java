package com.geek.kaijo.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.geek.kaijo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        ButterKnife.bind(this);

        int entry_type = getIntent().getIntExtra("entry_type", 0);
        initTitle(entry_type);

        String title = getIntent().getStringExtra("title");
        tvTitle.setText(title);

        String content = getIntent().getStringExtra("content");
        tvContent.setText(Html.fromHtml(content));
    }

    /**
     * 初始化标题
     *
     * @param entry_type 入口
     */
    private void initTitle(int entry_type) {
        switch (entry_type) {
            case 1://计划生育
                tvToolbarTitle.setText("计划生育详情");
                break;
            case 2://结婚登记
                tvToolbarTitle.setText("结婚登记详情");
                break;
            case 3://医疗卫生
                tvToolbarTitle.setText("医疗卫生详情");
                break;
            case 4://社会救助
                tvToolbarTitle.setText("社会救助详情");
                break;
            case 5://社会保障
                tvToolbarTitle.setText("社会保障详情");
                break;
            case 6://死亡殡葬
                tvToolbarTitle.setText("死亡殡葬详情");
                break;
            case 7://养老服务
                tvToolbarTitle.setText("养老服务详情");
                break;
            case 8://兵役
                tvToolbarTitle.setText("兵役详情");
                break;
            case 9://土地房产
                tvToolbarTitle.setText("土地房产详情");
                break;
            case 11://法律公益
                tvToolbarTitle.setText(R.string.business_legal_service);
                break;
            case 12://办事指南
                tvToolbarTitle.setText(R.string.business_guide);
                break;
            case 13://扶持政策
                tvToolbarTitle.setText("政策详情");
                break;
        }
    }
}
