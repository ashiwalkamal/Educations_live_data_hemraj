package com.attors.educations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.R;
import com.attors.educations.databinding.ActivityNewsdetailBinding;

public class Newsdetail extends AppCompatActivity implements View.OnClickListener {

    ActivityNewsdetailBinding newsdetailBinding;
    NewsModal detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsdetailBinding= DataBindingUtil.setContentView(this,R.layout.activity_newsdetail);

        clcik1();
        Webaction();

    }
    public void clcik1(){

        newsdetailBinding.detailback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.detailback){
            onBackPressed();
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void Webaction() {
        if(getIntent()!=null) {
            detail= Utlity.gson.fromJson(getIntent().getStringExtra("detail"),NewsModal.class);
            newsdetailBinding.title.setText(detail.getTitle());
            newsdetailBinding.alldetail.getSettings().setJavaScriptEnabled(true);
            newsdetailBinding.alldetail.getSettings().setAppCacheEnabled(true);

            newsdetailBinding.alldetail.loadDataWithBaseURL("", detail.getDesc(), "text/html", "UTF-8", "");
            newsdetailBinding.alldetail.setWebViewClient(new MyWebViewClient());
        }
        else
        {
            Utlity.show_toast(this,"Error in parsing !");
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            newsdetailBinding.progess.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            newsdetailBinding.progess.setVisibility(View.GONE);
        }
    }

}
