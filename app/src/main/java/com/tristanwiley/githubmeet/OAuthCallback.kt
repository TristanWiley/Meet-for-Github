package com.tristanwiley.githubmeet

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient


class OAuthCallback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth_callback)
        val webView = findViewById(R.id.oauthWebview) as WebView
        webView.settings.javaScriptEnabled = true;
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if(url.contains("oauthcallback://")){
                    val returnIntent = Intent()
                    val uri = Uri.parse(url)
                    Log.wtf("URL M8", url)
                    returnIntent.putExtra("result", uri.getQueryParameter("code"))
                    setResult(1, returnIntent)
                    finish()
                }
            }
        })

        webView.loadUrl("http://github.com/login/oauth/authorize?client_id=e229a63b9e96281e1023")
    }
}
