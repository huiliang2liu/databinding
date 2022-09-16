package com.lhl.databinding.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * com.witget
 * 2018/10/29 17:50
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class WebView extends android.webkit.WebView {

    private JsListener js;
    private Map<String, CallBack> backMap;
    private InverseBindingListener mGoBackInverseBindingListener;

    {
        initWebViewSettings();
        initWebViewClient();
        initWebChromeClient();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(true);
        }
        addJavascriptInterface(new JavaScripte(), "javaScript");
        backMap = new HashMap<>();
    }


    public void setJsListener(JsListener js) {
        this.js = js;
    }

    public WebView(Context context) {
        super(context.getApplicationContext());
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context.getApplicationContext(), attrs, defStyleAttr);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context.getApplicationContext(), attrs, defStyleAttr, defStyleRes);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context.getApplicationContext(), attrs, defStyleAttr, privateBrowsing);
    }

    private void initWebViewSettings() {
        WebSettings s = getSettings();
        s.setBuiltInZoomControls(true);
        s.setPluginState(WebSettings.PluginState.ON);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setRenderPriority(WebSettings.RenderPriority.HIGH);
        s.setAppCacheEnabled(false);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(false);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        s.setLoadsImagesAutomatically(true);
        s.setSupportZoom(false);// ql
        s.setBuiltInZoomControls(false);
        s.setGeolocationEnabled(true);
//        s.setGeolocationDatabasePath("http://www.cvbaoli.com/webak/public/showAgreement");
        s.setDomStorageEnabled(true);
        //如果大于5.0设置混合模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            s.setDisplayZoomControls(false);
        }
    }

    private void initWebChromeClient() {
        setWebChromeClient(new DefWebChromeClient());
    }

    @Override
    public void setWebChromeClient(@Nullable WebChromeClient client) {
        super.setWebChromeClient(client);
        if(client instanceof DefWebChromeClient)
            ((DefWebChromeClient) client).setmWebView(this);
    }

    private void initWebViewClient() {
        setWebViewClient(new DefWebViewClient());
    }

    @BindingAdapter("chromeClient")
    public static void setWebChromeClient(WebView webView, WebChromeClient chromeClient) {
        webView.setWebChromeClient(chromeClient);
    }

    @BindingAdapter("viewClient")
    public static void setWebViewClient(WebView webView, WebViewClient viewClient) {
        webView.setWebViewClient(viewClient);
    }

    @BindingAdapter("url")
    public static void loadUrl(WebView webView, String url) {
        webView.loadUrl(url);
    }

    @BindingAdapter({"url", "headers"})
    public static void loadUrl(WebView webView, String url, Map<String, String> headers) {
        webView.loadUrl(url, headers);
    }
    @BindingAdapter("userAgent")
    public static void userAgent(WebView webView,String userAgent){
        webView.getSettings().setUserAgentString(userAgent);
    }

    @InverseBindingAdapter(attribute = "canGoBack")
    public static boolean getCanGoBack(WebView webView){
        return webView.canGoBack();
    }
    @BindingAdapter(value = "canGoBackAttrChanged")
    public static void setCanGoBack(WebView webView,InverseBindingListener inverseBindingListener){
            webView.mGoBackInverseBindingListener = inverseBindingListener;
    }

    public void setCanGoBack(boolean test){
    }

    @BindingAdapter("data")
    public static void loadData(WebView webView, String data) {
        webView.loadData(data);
    }

    public void loadData(String data) {
        loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }

    @BindingAdapter("method")
    public static void js(WebView webView, String method) {
        webView.js(method);
    }

    public void js(String method) {
        loadUrl(String.format("javascript:%s()", method));
    }

    @BindingAdapter({"method", "param"})
    public static void js(WebView webView, String js, String param) {
        webView.js(js, param);
    }

    public void js(String method, @NonNull String param) {
        loadUrl(String.format("javascript:%s('%s')", method, param));
    }

    @BindingAdapter({"method", "params"})
    public static void js(WebView webView, String method, @NonNull List<String> params) {
        webView.js(method, params);
    }

    public void js(String method, @NonNull List<String> params) {
        StringBuffer sb = new StringBuffer("'");
        for (String param : params)
            sb.append(param).append("','");
        loadUrl(String.format("javascript:%s(%s)", method, sb.substring(0, sb.length() - 2)));
    }

    @BindingAdapter({"method", "callBack"})
    public static void js(WebView webView, String method, CallBack back) {
        webView.js(method, back);
    }

    public void js(String method, CallBack back) {
        if (back != null)
            backMap.put(method, back);
        js(method);
    }

    @BindingAdapter({"method", "param", "callBack"})
    public static void js(WebView webView, String method, @NonNull String param, CallBack back) {
        webView.js(method, param, back);
    }

    public void js(String method, @NonNull String param, CallBack back) {
        if (back != null)
            backMap.put(method, back);
        js(method, param);
    }

    @BindingAdapter({"method", "params", "callBack"})
    public static void js(WebView webView, String method, @NonNull List<String> params, CallBack back) {
        webView.js(method, params, back);
    }

    public void js(String method, @NonNull List<String> params, CallBack back) {
        if (back != null)
            backMap.put(method, back);
        js(method, params);
    }


    private void data(final String method, final String... params) {
        if (!isMain()) {
            post(new Runnable() {
                @Override
                public void run() {
                    data(method, params);
                }
            });
            return;
        }
        if (backMap.containsKey(method)) {
            CallBack back = backMap.get(method);
            back.back(params);
            backMap.remove(method);
        }
    }

    private void method(final String method, final String... params) {
        if (!isMain()) {
            post(new Runnable() {
                @Override
                public void run() {
                    method(method, params);
                }
            });
            return;
        }
        if (js != null)
            js.jsLoad(method, params);
    }


    private boolean isMain() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    private class JavaScripte {

        @JavascriptInterface
        public void method(String method, String... params) {
            WebView.this.method(method, params);
        }

        @JavascriptInterface
        public void data(String method, String... params) {
            WebView.this.data(method, params);
        }
    }

    @BindingAdapter("goBack")
    public static void goBack(WebView webView, int index) {
        while (index > 0 && webView.canGoBack()) {
            webView.goBack();
            index--;
        }
    }

    @BindingAdapter("goForward")
    public static void goForward(WebView webView, int index) {
        while (index > 0 && webView.canGoForward()) {
            webView.goForward();
            index--;
        }
    }

    public interface CallBack {
        void back(String... params);
    }

    public interface JsListener {
        void jsLoad(String method, String... params);
    }

    public static class DefWebChromeClient extends WebChromeClient {
        private CustomViewCallback myCallback = null;
        private View myView = null;
        WebView mWebView;

        public void setmWebView(WebView mWebView) {
            this.mWebView = mWebView;
        }

        public DefWebChromeClient() {

        }

        @Nullable
        @Override
        public Bitmap getDefaultVideoPoster() {
            return super.getDefaultVideoPoster();
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {//全屏显示
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null;
                return;
            }
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            parent.removeView(mWebView);
            parent.addView(view);
            myView = view;
            myCallback = callback;
        }

        @Override
        public void onHideCustomView() {
            if (myView != null) {
                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null;
                }
                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                parent.addView(mWebView);
                myView = null;
            }
        }

        @Override
        public boolean onShowFileChooser(android.webkit.WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }

    public static class DefWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedHttpError(android.webkit.WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(android.webkit.WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedError(android.webkit.WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onLoadResource(android.webkit.WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            if(view instanceof  WebView){
                Log.e("======","WebView");
                WebView webView = (WebView) view;
                if(webView.mGoBackInverseBindingListener!=null){
                    Log.e("======","mGoBackInverseBindingListener");
                    webView.mGoBackInverseBindingListener.onChange();
                }
            }
            super.onPageFinished(view, url);
        }
    }

}