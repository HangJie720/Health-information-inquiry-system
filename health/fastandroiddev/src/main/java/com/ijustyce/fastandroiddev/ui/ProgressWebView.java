package com.ijustyce.fastandroiddev.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ijustyce.fastandroiddev.net.DownManager;

/**
 * Created by yangchun on 16/4/13.  带进度条的WebView，
 * 如果你调用了  setWebChromeClient  请务必在 onProgressChanged 里调用这个类的onProgressChanged
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private Context mContext;

    public ProgressWebView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mContext = context;
        progressbar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 10, 0, 0));
        addView(progressbar);
        getSettings().setSupportZoom(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        getSettings().setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient());
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownManager downManager = new DownManager(mContext, url, null);
                downManager.startDown();
            }
        });
    }

    @Override
    public void destroy() {
        super.destroy();
        setDownloadListener(null);
        mContext = null;
    }

    public void setProgressDrawable(Drawable drawable) {

        progressbar.setProgressDrawable(drawable);
    }

    //  重写这个方法可以设置
    public void setProgressbarHeight(int height) {

        if (height > 5) {
            progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, height, 0, 0));
        }
    }

    //  如果你调用了 setWebChromeClient ，请务必调用这个方法！！！
    public void onProgressChanged(WebView view, int newProgress) {

        if (newProgress == 100) {
            progressbar.setVisibility(GONE);
        } else {
            if (progressbar.getVisibility() == GONE)
                progressbar.setVisibility(VISIBLE);
            progressbar.setProgress(newProgress);
        }
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            ProgressWebView.this.onProgressChanged(view, newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
