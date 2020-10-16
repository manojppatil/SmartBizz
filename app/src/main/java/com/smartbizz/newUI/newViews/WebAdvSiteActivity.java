package com.smartbizz.newUI.newViews;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.smartbizz.R;
import com.smartbizz.Util.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebAdvSiteActivity extends BaseActivity {

    SwipeRefreshLayout mySwipeRefreshLayout;
    private WebView webView;
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_smsservice_website);
        webView = findViewById(R.id.webview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setTitle(getString(R.string.rewards));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString(Constants.Extras.URL);
        }

//        progressBar = findViewById(R.id.web_progress);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        // webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setSupportMultipleWindows(true); // This forces ChromeClient enabled.
        // webView.setScrollbarFadingEnabled(true);
        webView.getSettings().setGeolocationDatabasePath(this.getFilesDir().getPath());
        //webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);

        try {
            //Log.d(TAG, "Enabling HTML5-Features");
            Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", Boolean.TYPE);
            m1.invoke(webView.getSettings(), Boolean.TRUE);

            Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", Boolean.TYPE);
            m2.invoke(webView.getSettings(), Boolean.TRUE);

            Method m3 = WebSettings.class.getMethod("setDatabasePath", String.class);
            m3.invoke(webView.getSettings(), "/data/data/" + getPackageName() + "/databases/");

            Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", Long.TYPE);
            m4.invoke(webView.getSettings(), 1024 * 1024 * 8);

            Method m5 = WebSettings.class.getMethod("setAppCachePath", String.class);
            m5.invoke(webView.getSettings(), "/data/data/" + getPackageName() + "/cache/");

            Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", Boolean.TYPE);
            m6.invoke(webView.getSettings(), Boolean.TRUE);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e("error", "Reflection fail", e);
        };

    }

//    private class MyChromeClient extends WebChromeClient {
//
//        //Handle javascript alerts:
//        @Override
//        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
//            result.confirm();
//            return true;
//        }
//
//        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//            callback.invoke(origin, true, false);
//        }
//
//        //handle new tab event
//        @Override
//        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//
//            WebView newWebView = new WebView(WebViewActivity.this);
//            view.addView(newWebView);
//            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//            transport.setWebView(newWebView);
//            resultMsg.sendToTarget();
//
//            newWebView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    //TODO change if you want to laod new url in application webview
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                    browserIntent.setData(Uri.parse(url));
//                    startActivity(browserIntent);
//                    return true;
//                }
//            });
//            return true;
//        }
//    }

    private class MyChromeClient extends WebChromeClient {

        //Handle javascript alerts:
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
            result.confirm();
            return true;
        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }

        //handle new tab event
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

            WebView newWebView = new WebView(WebAdvSiteActivity.this);
            view.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();

            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //TODO change if you want to laod new url in application webview
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(url));
                    startActivity(browserIntent);

                    return true;
                }
            });
            return true;
        }
    }

    private class MyWebViewClient extends WebViewClient {

        boolean loadingFinished = true;
        boolean redirect = false;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadingFinished = false;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            String url = request.getUrl().toString();

            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;

            view.loadUrl(url);
            return true;
        }

        @Override
        // show the web page in webview but not in web browser
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                //HIDE LOADING IT HAS FINISHED
//                progressBar.setVisibility(View.GONE);
            } else {
                redirect = false;
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
            Log.e("SSL error", error.toString());
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.e("Normal error", error.toString());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
