package com.simpl.pay.sample.zc_s2s;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private final String TAG = WebViewActivity.class.getSimpleName();



    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface", "LongLogTag"})
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        webView = new WebView(WebViewActivity.this);
        setContentView(webView);

        webView.setVerticalScrollBarEnabled(true);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(TAG, "WebChromeClient: " + consoleMessage.message());
                return true;
            }

            @Override
            public void onCloseWindow(WebView window){
                super.onCloseWindow(window);
                Log.v(TAG, "On ");
            }
        });

        webView.addJavascriptInterface(new WebAppInterface(this), "Simpl");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted " + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished " + url);
            }

            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail){
                Log.v(TAG, "OnRenderProcessGone");
                return false;
            }

        });

        webView.loadUrl(getIntent().getStringExtra("redirection_url") + "&src=android");
   }

    class WebAppInterface {
        Context context;

        WebAppInterface(Context c) {
            context = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();

        }

        @JavascriptInterface
        public void close(){
            Log.v(TAG, "close");
            runOnUiThread(WebViewActivity.this::onBackPressed);
        }

        @JavascriptInterface
        public void onFailure(){
            Log.v(TAG, "onFailure");
        }

        @JavascriptInterface
        public void onSuccess(){
            Log.v(TAG, "onSuccess");
        }
    }
}
