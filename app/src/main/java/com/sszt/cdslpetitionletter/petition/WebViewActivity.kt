package com.sszt.cdslpetitionletter.petition

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityWebViewBinding
import com.sszt.resources.IRoute

/**
 * h5
 */
@Route(path = IRoute.letter_web_view)
class WebViewActivity : BaseActivity<PublicViewModel, ActivityWebViewBinding>() {


    override fun layoutId() = R.layout.activity_web_view

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
// 启用javascript

        var url = intent.getStringExtra("url");
        var name = intent.getStringExtra("name")

        bind.titleLayout.titleTitle.text = name
        bind.titleLayout.setOnBackClick { finish() }

        // 启用javascript
        bind.webView.settings.javaScriptEnabled = true


// 设置可以支持缩放
        bind.webView.settings.setSupportZoom(true)
// 设置出现缩放工具
        // 设置出现缩放工具
        bind.webView.settings.builtInZoomControls = true
//扩大比例的缩放
        //扩大比例的缩放
        bind.webView.settings.useWideViewPort = true
//自适应屏幕
        //自适应屏幕
        bind.webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        bind.webView.settings.loadWithOverviewMode = true
// 从assets目录下面的加载html
        //binding.webView.loadUrl(url);
        // 从assets目录下面的加载html
        //binding.webView.loadUrl(url);
        bind.webView.addJavascriptInterface(this, "android")
//系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        bind.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {


                //使用WebView加载显示url
                view.loadUrl(url)
                //返回true
                return true
            }
        }

        bind.webView.loadData(
            url ?: "",
            "text/html",
            "utf-8"
        )


    }

}