package com.geek.kaijo.mvp.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Case;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ProcessActivity extends AppCompatActivity {

    private TextView tvToolbarTitle;
    private Toolbar toolbar;
    private WebView webWiew;
    private Case aCase;

    static boolean flag =false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_process);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbar = findViewById(R.id.toolbar);

        tvToolbarTitle.setText("查看处理过程");

        webWiew = findViewById(R.id.webWiew);

        aCase = (Case) getIntent().getParcelableExtra("Case");

        WebSettings webSettings = webWiew.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webWiew.setWebChromeClient(new WebChromeClient()
                {
                    public void onProgressChanged(WebView view, int progress)
                    {
                        //当进度走到100的时候做自己的操作，我这边是弹出dialog
                        if(progress == 100 &&!flag){
                            flag = true;
                            webWiew.loadUrl("javascript:passParam("+aCase.getCaseId()+","+aCase.getCaseAttribute()+","+aCase.getProcessId()+")");
                        }
                    }
                });

        if(aCase!=null){
//            Map<String,String> map=new HashMap<String,String>();
//            map.put("caseId",aCase.getCaseId());
//            map.put("caseAttribute",aCase.getCaseAttribute());
//            map.put("processId","2");
//            webWiew.loadUrl("file:////android_asset/process.html",map);
            webWiew.loadUrl("file:////android_asset/process.html");
        }else {
            webWiew.loadUrl("file:////android_asset/process.html");
        }
//
//        String a = "";
////        String method ="javascript:test(\""+a+"\")" ;
//        String method ="javascript:passParamaa(rtretre)" ;
//        webWiew.loadUrl(method);





//        webWiew.evaluateJavascript(method, new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String value) {
//                DyToastUtils.showShort(PropertyPaymentActivity.this, value);
//            }
//        });
    }


}
