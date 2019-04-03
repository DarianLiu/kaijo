package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.di.component.DaggerCaseSearchComponent;
import com.geek.kaijo.di.module.CaseSearchModule;
import com.geek.kaijo.mvp.contract.CaseSearchContract;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.presenter.CaseSearchPresenter;
import com.geek.kaijo.mvp.ui.adapter.MySpinnerAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 案件查询（自行/非自行）
 */
public class CaseSearchActivity extends BaseActivity<CaseSearchPresenter> implements CaseSearchContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.spinner_case_attribute)
    AppCompatSpinner spinnerCaseAttribute;
    @BindView(R.id.spinner_category_large)
    AppCompatSpinner spinnerCategoryLarge;
    @BindView(R.id.spinner_category_small)
    AppCompatSpinner spinnerCategorySmall;
    @BindView(R.id.spinner_category_sub)
    AppCompatSpinner spinnerCategorySub;
    @BindView(R.id.et_case_num)
    EditText etCaseCode;
    @BindView(R.id.tv_query)
    TextView tvQuery;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private int entry_type;//入口类型（自行：0/非自行：1）

    private CaseAttribute mCaseAttribute;//占位数据
    private List<CaseAttribute> mCategoryLarge;//大类
    private MySpinnerAdapter<CaseAttribute> mCategoryLargeAdapter;//大类

    private MySpinnerAdapter<CaseAttribute> mCategorySmallAdapter;//小类
    private List<CaseAttribute> mCategorySmall;//小类

    private MySpinnerAdapter<CaseAttribute> mCategorySubAdapter;//子类
    private List<CaseAttribute> mCategorySub;//子类

    //相关参数全局变量
    private String mCaseAttributeId, mCasePrimaryCategory, mCaseSecondaryCategory, mCaseChildCategory;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCaseSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .caseSearchModule(new CaseSearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_case_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        entry_type = getIntent().getIntExtra("entry_type", 0);

        tvToolbarTitle.setText("案件查询");

        mCaseAttribute = new CaseAttribute();
        mCaseAttribute.setText("请选择");

        initSpinnerCaseAttribute();
        initSpinnerCategory();
    }

    /**
     * 初始化案件属性Spinner
     */
    private void initSpinnerCaseAttribute() {
        String[] caseAttributeArray = getResources().getStringArray(R.array.array_case_attribute);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_list_item, caseAttributeArray);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_select_singlechoice);
        spinnerCaseAttribute.setAdapter(arrayAdapter);
        spinnerCaseAttribute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Timber.d("=======position" + position);
                mCaseAttributeId = String.valueOf(position);

                spinnerCategoryLarge.setSelection(0);
                mCategoryLarge.clear();
                mCategoryLarge.add(mCaseAttribute);
                mCategoryLargeAdapter.notifyDataSetChanged();

                spinnerCategorySmall.setSelection(0);
                mCategorySmall.clear();
                mCategorySmall.add(mCaseAttribute);
                mCategorySmallAdapter.notifyDataSetChanged();

                spinnerCategorySub.setSelection(0);
                mCategorySub.clear();
                mCategorySub.add(mCaseAttribute);
                mCategorySubAdapter.notifyDataSetChanged();

                if (position != 0 && mPresenter != null) {
                    mPresenter.findCaseCategoryListByAttribute(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化案件属性Spinner（大、小、子类）
     */
    private void initSpinnerCategory() {
        mCategoryLarge = new ArrayList<>();
        mCategoryLarge.add(mCaseAttribute);
        mCategoryLargeAdapter = new MySpinnerAdapter<>(this, mCategoryLarge);
        spinnerCategoryLarge.setAdapter(mCategoryLargeAdapter);
        spinnerCategoryLarge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCategorySmall.setSelection(0);
                mCategorySmall.clear();
                mCategorySmall.add(mCaseAttribute);
                if (position > 0) {
                    mCasePrimaryCategory = mCategoryLarge.get(position).getCategoryId();
                    mCategorySmall.addAll(mCategoryLarge.get(position).getChildList());
                } else {
                    mCasePrimaryCategory = "";
                }
                mCategorySmallAdapter.notifyDataSetChanged();

                spinnerCategorySub.setSelection(0);
                mCategorySub.clear();
                mCategorySub.add(mCaseAttribute);
                mCategorySubAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCategorySmall = new ArrayList<>();
        mCategorySmall.add(mCaseAttribute);
        mCategorySmallAdapter = new MySpinnerAdapter<>(this, mCategorySmall);
        spinnerCategorySmall.setAdapter(mCategorySmallAdapter);
        spinnerCategorySmall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCategorySub.setSelection(0);
                mCategorySub.clear();
                mCategorySub.add(mCaseAttribute);
                if (position > 0) {
                    mCaseSecondaryCategory = mCategorySmall.get(position).getCategoryId();
                    mCategorySub.addAll(mCategorySmall.get(position).getChildList());
                } else {
                    mCaseSecondaryCategory = "";
                }
                mCategorySubAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCategorySub = new ArrayList<>();
        mCategorySub.add(mCaseAttribute);
        mCategorySubAdapter = new MySpinnerAdapter<>(this, mCategorySub);
        spinnerCategorySub.setAdapter(mCategorySubAdapter);
        spinnerCategorySub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mCaseChildCategory = mCategorySub.get(position).getCategoryId();
                } else {
                    mCaseChildCategory = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 设置案件属性列表
     *
     * @param attributeList 案件属性列表
     */
    @Override
    public void setCaseAttributeList(List<CaseAttribute> attributeList) {
        mCategoryLarge.clear();
        mCategoryLarge.add(mCaseAttribute);
        mCategoryLarge.addAll(attributeList);
        mCategoryLargeAdapter.notifyDataSetChanged();

        if (attributeList.size() > 0) {
            spinnerCaseAttribute.setSelection(1, true);
        }
    }

    @Override
    public void setCaseSearchResult(List<CaseInfo> result) {

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

    @OnClick({R.id.tv_query, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_query://查询
                if (checkParams()) {
                    if (mPresenter != null) {
                        String caseCode = etCaseCode.getText().toString();
                        mPresenter.findCaseInfoList(caseCode, mCaseAttributeId, mCasePrimaryCategory,
                                mCaseSecondaryCategory, mCaseChildCategory);
                    }
                }
                break;
            case R.id.tv_cancel://取消
                spinnerCaseAttribute.setSelection(0);
                spinnerCategoryLarge.setSelection(0);
                spinnerCategorySmall.setSelection(0);
                spinnerCategorySub.setSelection(0);
                break;
        }
    }

    private boolean checkParams() {
        if (TextUtils.isEmpty(mCaseAttributeId)) {
            showMessage("请选择案件属性");
            return false;
        } else if (TextUtils.isEmpty(mCasePrimaryCategory)) {
            showMessage("请选择案件大类");
            return false;
        } else if (TextUtils.isEmpty(mCaseSecondaryCategory)) {
            showMessage("请选择案件小类");
            return false;
        } else if (TextUtils.isEmpty(mCaseChildCategory)) {
            showMessage("请选择案件子类");
            return false;
        } else {
            return true;
        }
    }

}
