package com.geek.kaijo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.di.component.DaggerPlanViewComponent;
import com.geek.kaijo.di.module.PlanViewModule;
import com.geek.kaijo.mvp.contract.PlanViewContract;
import com.geek.kaijo.mvp.model.entity.treebean.BranchNode;
import com.geek.kaijo.mvp.model.entity.treebean.BranchViewBinder;
import com.geek.kaijo.mvp.model.entity.treebean.LeafNode;
import com.geek.kaijo.mvp.model.entity.treebean.LeafViewBinder;
import com.geek.kaijo.mvp.model.entity.treebean.RootNode;
import com.geek.kaijo.mvp.model.entity.treebean.RootViewBinder;
import com.geek.kaijo.mvp.presenter.PlanViewPresenter;
import com.geek.kaijo.mvp.ui.adapter.treeview.LayoutItem;
import com.geek.kaijo.mvp.ui.adapter.treeview.TreeNode;
import com.geek.kaijo.mvp.ui.adapter.treeview.TreeViewAdapter;
import com.geek.kaijo.mvp.ui.adapter.treeview.TreeViewBinder;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 预案查看
 */
public class PlanViewActivity extends BaseActivity<PlanViewPresenter> implements PlanViewContract.View {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_toolbar_title_right)
    ImageView tvToolbarTitleRight;
    @BindView(R.id.tv_toolbar_title_right_text)
    TextView tvToolbarTitleRightText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.plan_txt)
    TextView planTxt;
    @BindView(R.id.plan_et_search)
    EditText planEtSearch;
    @BindView(R.id.plan_iv_search)
    ImageView planIvSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    private List<TreeNode> list;
    private TreeViewAdapter adapter;

    @BindView(R.id.webview)
    WebView webview;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPlanViewComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .planViewModule(new PlanViewModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_plan_view; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitle.setText(R.string.view_plan);
        list = new ArrayList<>();
        list.addAll(initRoot());
//        initAdapter();

        WebSettings webSettings = webview.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("file:////android_asset/html/tree.html");

    }

    private void initAdapter() {
        adapter = new TreeViewAdapter(list, Arrays.asList(new RootViewBinder(), new BranchViewBinder(), new LeafViewBinder())) {

            @Override
            public void toggleClick(TreeViewBinder.ViewHolder viewHolder, View view, boolean isOpen, TreeNode treeNode) {
                adapter.lastToggleClickToggle();
            }

            @Override
            public void toggled(TreeViewBinder.ViewHolder viewHolder, View view, boolean isOpen, TreeNode treeNode) {
                if (treeNode.isExpanded()) {
                    ((ImageView) viewHolder.findViewById(R.id.ivNode)).setImageResource(R.drawable.tree_less);
                } else {
                    ((ImageView) viewHolder.findViewById(R.id.ivNode)).setImageResource(R.drawable.tree_add);
                }

            }

            @Override
            public void checked(TreeViewBinder.ViewHolder viewHolder, View view, boolean checked, TreeNode treeNode) {

            }

            @Override
            public void itemClick(TreeViewBinder.ViewHolder viewHolder, View view, TreeNode treeNode) {

                String name = null;
                LayoutItem item = treeNode.getValue();
                if (item instanceof RootNode) {
                    name = ((RootNode) item).getName();
                } else if (item instanceof BranchNode) {
                    name = ((BranchNode) item).getName();
                } else if (item instanceof LeafNode) {
                    name = ((LeafNode) item).getName();
                }
                showMessage(name + "===" + treeNode.isChecked());

            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    /**
     * 初始化跟
     *
     * @return
     */
    private List<TreeNode> initRoot() {
        List<TreeNode> rootList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TreeNode<RootNode> node = new TreeNode<>(new RootNode("节点1" + i));
            node.setChildNodes(initBranchs(node.getValue().getName()));
            rootList.add(node);
        }
        return rootList;
    }

    /**
     * 初始化枝
     *
     * @param name
     * @return
     */
    private List<TreeNode> initBranchs(String name) {
        List<TreeNode> branchList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TreeNode<BranchNode> node = new TreeNode<>(new BranchNode(name + "节点2" + i));
            node.setChildNodes(initLeaves(node.getValue().getName()));
            branchList.add(node);
        }
        return branchList;
    }

    /**
     * 初始化叶
     *
     * @param name
     * @return
     */
    private List<TreeNode> initLeaves(String name) {
        List<TreeNode> leafList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TreeNode<LeafNode> node = new TreeNode<>(new LeafNode(name + "节点3" + i));
            leafList.add(node);
        }
        return leafList;
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
