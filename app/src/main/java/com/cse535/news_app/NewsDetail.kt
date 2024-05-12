package com.cse535.news_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class NewsDetail : ComponentActivity() {
    companion object {
        const val NEWS_DETAIL_TEXT = "news_detail_text"
        const val NEWS_DETAIL_TITLE = "news_detail_title"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val txt = intent.getStringExtra(NEWS_DETAIL_TEXT) ?: ""
        val title = intent.getStringExtra(NEWS_DETAIL_TITLE) ?: ""
        setContent() {
            NewsDetailScreen(title, txt, this)
        }
    }
}

@Composable
fun NewsDetailScreen(title: String, txt: String, context: Context) {
    Column(){
    Text(text = txt)
        Button (onClick = { shareNews(txt, context) }, // Call shareNews function when button is clicked
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        )
        {
            Text(text = "share")
        }
    }
}

@Composable
fun NewsDetailTopBar() {

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