package com.geek.kaijo.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.geek.kaijo.R;
import com.geek.kaijo.mvp.model.entity.Case;

import java.util.HashMap;
import java.util.Map;

public class ProcessActivity extends Activity {

    private WebView webWiew;
    private Case aCase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_process);

        webWiew = findViewById(R.id.webWiew);

        aCase = (Case) getIntent().getParcelableExtra("Case");

        WebSettings webSettings = webWiew.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webWiew.setWebChromeClient(new WebChromeClient());
        if(aCase!=null){
            Map<String,String> map=new HashMap<String,String>();
            map.put("caseId",aCase.getCaseId());
            map.put("caseAttribute",aCase.getCaseAttribute());
            webWiew.loadUrl("file:////android_asset/process.html",map);
        }else {
            webWiew.loadUrl("file:////android_asset/process.html");
        }
    }


}
