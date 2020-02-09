package com.singularitycoder.folkdatabase.helper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.singularitycoder.folkdatabase.R;

public class HelperWebViewActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar mProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String longUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HelperGeneral().setStatuBarColor(this, R.color.colorPrimaryDark);
        setContentView(R.layout.activity_helper_web_view);

        mProgressBar = findViewById(R.id.load_progress);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar = new ProgressBar(HelperWebViewActivity.this);

        swipeRefreshLayout = findViewById(R.id.swipe_layout_browser);
        swipeRefreshLayout.setOnRefreshListener(this::showWebView);

        Intent intent = getIntent();
        longUrl = intent.getStringExtra("url");

        showWebView();
    }

    private void showWebView() {
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new HelperWebViewActivity.webViewCallBack());
        webView.loadUrl(longUrl);
        webView.getProgress();
        System.out.println("progressss: " + webView.getProgress());

//        if (webView.isLaidOut()) {
//            if (mProgressBar != null) {
//                mProgressBar.setVisibility(View.GONE);
//                mProgressBar = null;
//            }
//        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            mProgressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }, 4000);

        swipeRefreshLayout.setRefreshing(false);
    }

    public void done(View view) {
        finish();
    }


    private class webViewCallBack extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // If webpage loaded completely

        }
    }
}
