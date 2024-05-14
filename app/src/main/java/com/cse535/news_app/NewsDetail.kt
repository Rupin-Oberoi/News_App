package com.cse535.news_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.compose.ui.text.font.FontWeight
import android.net.Uri
import androidx.compose.material3.Card
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter

class NewsDetail : ComponentActivity() {
    companion object {
        const val NEWS_DETAIL_TEXT = "news_detail_text"
        const val NEWS_DETAIL_TITLE = "news_detail_title"
        const val NEWS_DETAIL_URL = "news_detail_url"
        const val NEWS_DETAIL_IMAGE_URL = "news_detail_image"
        const val NEWS_DETAIL_SOURCE = "news_detail_source"
        const val NEWS_DETAIL_TIME = "news_detail_time"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val txt = intent.getStringExtra(NEWS_DETAIL_TEXT) ?: ""
        val title = intent.getStringExtra(NEWS_DETAIL_TITLE) ?: ""
        val imageUrl = intent.getStringExtra(NEWS_DETAIL_IMAGE_URL) ?: ""
        val url = intent.getStringExtra(NEWS_DETAIL_URL) ?: ""
        setContent() {
            NewsDetailScreen(title, txt, imageUrl, url, this)
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsDetailScreen(title: String, content: String,imageURL: String, url:String, context: Context) {
    val shareText = "Check out this news article: $title\n$url"
    val showWebView = remember { mutableStateOf(false) }
    Scaffold(
        topBar = { NewsDetailTopBar(shareText, url, context, showWebView) },
        content = {
            paddingValues ->
            //Spacer(modifier = Modifier.padding(30.dp))
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()){

                if (showWebView.value) {
                    WebViewComposable(
                        url = url,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
                else{
                    NewsContentView(title, content, imageURL)
                }
            }
        }
    )

}
@Composable
fun NewsContentView(title: String, content: String, imageURL: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ImageFromUrl(imageUrl = imageURL)
        }
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun ImageFromUrl(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        modifier = Modifier.heightIn(200.dp).fillMaxWidth().padding(16.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailTopBar(shareText:String, url: String, context: Context,showWebView: MutableState<Boolean>) {
        Log.d("NewsDetailTopBar", "Open in Browser URL: $url")
        TopAppBar(title = { /*TODO*/
        Row(){
            Text("News Detail",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }},
            actions = {
                IconButton(onClick = { shareNews(shareText, context) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                IconButton(onClick = { showWebView.value = !showWebView.value }) {
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = "Open in Browser",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
        },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
        )
}

@Composable
fun WebViewComposable(url: String, modifier: Modifier = Modifier) {
    val webViewState = remember { mutableStateOf(url) }
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                webViewState.value.let { url ->
                    loadUrl(url)
                }
            }
        },
        update = { webView ->
            webViewState.value.let { url ->
                if (url != webView.url) {
                    webView.loadUrl(url)
                }
            }
        },
        modifier = modifier
    )
}

fun shareNews(news: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, news) // Share the news headline
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}