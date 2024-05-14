package com.cse535.news_app

import AppDatabase
import ArticleEntity
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
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsDetail : ComponentActivity() {

    // Accessing the database
    val database = MainActivity.database
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
            NewsDetailScreen(title, txt, imageUrl, url, this,database)
        }
    }
}
fun bookmarkNews(title: String, txt: String, imageUrl: String, url: String, database: AppDatabase) {
    val articleEntity = ArticleEntity(
        title = title,
        txt = txt,
        imageUrl = imageUrl,
        url = url
    )

    CoroutineScope(Dispatchers.IO).launch {
        database.articleDao().insertArticle(articleEntity)
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsDetailScreen(title: String, content: String,imageURL: String, url:String, context: Context,database: AppDatabase) {
    val shareText = "Check out this news article: $title\n$url"
    val showWebView = remember { mutableStateOf(false) }
    Scaffold(
        topBar = { NewsDetailTopBar(shareText, url, context, showWebView,title,content,imageURL,database) },
        content = {
            paddingValues ->
            //Spacer(modifier = Modifier.padding(30.dp))
            Column(modifier = Modifier.padding(paddingValues)){

                if (showWebView.value) {
                    WebViewComposable(
                        url = url,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
                else{
                    Text(title, style = MaterialTheme.typography.bodyMedium,
                        fontWeight= FontWeight.Bold,
                        modifier = Modifier.padding(16.dp))
                    ImageFromUrl(imageUrl = imageURL)
                    Text(content)
                }
            }
        }
    )

}@Composable
fun ImageFromUrl(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        modifier = Modifier.heightIn(200.dp).fillMaxWidth()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailTopBar(shareText:String, url: String, context: Context,showWebView: MutableState<Boolean>,title: String,
                     txt: String,
                     imageUrl: String,
                     database: AppDatabase) {
        Log.d("NewsDetailTopBar", "Open in Browser URL: $url")
        TopAppBar(title = { /*TODO*/
        Row(){
            Text("News Detail", style = MaterialTheme.typography.bodyLarge)
        }},
            actions = {
                IconButton(onClick = {shareNews(shareText, context)}) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                IconButton(onClick = { showWebView.value = !showWebView.value }) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = "Open in Browser")
                }
                IconButton(onClick = { bookmarkNews(title,txt,imageUrl,url,database) }) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = "Bookmark")
                }
        },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
        )
}

//@Composable
//fun WebViewComposable(url: String, modifier: Modifier = Modifier) {
//    val webViewState = rememberWebViewState(url)
//    AndroidView(
//        factory = { context ->
//            WebView(context).apply {
//                webViewClient = WebViewClient()
//                settings.javaScriptEnabled = true
//            }
//        },
//        update = { webView ->
//            webView.loadUrl(webViewState.current)
//        },
//        modifier = modifier
//    )
//}

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
//fun openWebPage(url: String, context: Context) {
//    Log.d("openWebPage", "Opening URL: $url")
//    val webpage = Uri.parse(url)
//    val intent = Intent(Intent.ACTION_VIEW, webpage)
//    if (intent.resolveActivity(context.packageManager) != null) {
//        context.startActivity(intent)
//    }
//}
fun shareNews(news: String, context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, news) // Share the news headline
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}