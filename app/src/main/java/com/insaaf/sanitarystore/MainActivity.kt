package com.insaaf.sanitarystore

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private const val STORE_URL = "https://ais-dev-wlbj57muu3tgc374kofopw-910450070364.asia-northeast1.run.app/"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var storeWebView by remember { mutableStateOf<WebView?>(null) }

            BackHandler(enabled = storeWebView?.canGoBack() == true) {
                storeWebView?.goBack()
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadsImagesAutomatically = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        storeWebView = this
                        loadUrl(STORE_URL)
                    }
                },
                update = { webView ->
                    if (webView.url == null) webView.loadUrl(STORE_URL)
                    storeWebView = webView
                }
            )

            DisposableEffect(Unit) {
                onDispose {
                    storeWebView?.apply {
                        stopLoading()
                        destroy()
                    }
                    storeWebView = null
                }
            }
        }
    }
}
