package com.cse535.news_app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationProvider
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.cse535.news_app.ui.theme.News_appTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import android.content.res.Configuration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val isDarkMode = remember { mutableStateOf(isSystemInDarkTheme(context)) }

            News_appTheme(isDarkMode.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(this, isDarkMode.value) {
                        isDarkMode.value = !isDarkMode.value}
                    val fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
//                    var lat = 0.0
//                    var long = 0.0
//                    getLatLong(fusedLocationClient, this, { pair ->
//                        lat = pair.first
//                        long = pair.second
//                        getCurrentCity(this, lat, long)
//                    })
                    //Log.d("City", getCurrentCity(this))
                    //val a = getCurrentCity(this, lat, long)
                    //Log.d("City", a)
                }
            }
        }
    }
}

fun isSystemInDarkTheme(context: Context): Boolean {
    return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(context: Context, isDarkMode: Boolean, toggleDarkMode: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { drawerContent(drawerState, coroutineScope) },
        content = {
            Scaffold(
                topBar = { MainTopBar(drawerState, coroutineScope, toggleDarkMode) },
                content = { paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Spacer(modifier = Modifier.padding(60.dp))
                        NewsList(headlines = getDummyHeadlines(40), context)
                    }
                }
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(drawerState: DrawerState, coroutineScope: CoroutineScope, toggleDarkMode: () -> Unit) {
    Column (modifier =  Modifier.padding(vertical = 2.dp)){
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.open()
            }}){
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.Black
                )}
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "News App",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        },

        actions = {
            IconButton(
                onClick = { /* Handle search icon click */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            }
            IconButton(
                onClick = toggleDarkMode
            ) {
                val icon = if (isSystemInDarkTheme()) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                Icon(
                    imageVector = icon,
                    contentDescription = "Toggle Dark Mode",
                    tint = Color.Black
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )

    )
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 5.dp)
        .horizontalScroll(rememberScrollState())){
        Button (onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        {
            Text(text = "General")
        }
        Button (onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        {
            Text(text = "Technology")
        }
        Button (onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        {
            Text(text = "Sports")
        }
        Button (onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
            {
            Text(text = "Near Me")
            }
        }
    }
}

@Composable
fun NewsList(headlines: List<String>, context:Context) {
    // Add LazyColumn here
    LazyColumn (modifier = Modifier.fillMaxSize()){
        items(headlines.size) { index ->
            SingleNews(headlines[index], context)
        }
    }

}

@Composable
fun SingleNews(headline: String, context: Context){
    Button (onClick = {
        val intent = Intent(context, NewsDetail::class.java).apply{
            putExtra(NewsDetail.NEWS_DETAIL_TEXT, headline)
            putExtra(NewsDetail.NEWS_DETAIL_TITLE, headline)
        }
        context.startActivity(intent)
    },
    modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        )
         {
        Text(text = headline)
    }
    NewsText(text = "hi this is a accessibility service")
}



fun getDummyHeadlines(num: Int): List<String> {
    val headlines = mutableListOf<String>()
    for (i in 1..num) {
        headlines.add("Headline $i")
    }
    return headlines
}

fun getCurrentCity(context: Context, lat: Double, long: Double): String {
    val geocoder =  Geocoder(context, Locale.getDefault());

    val addr = geocoder.getFromLocation(lat, long, 1, )
    Log.d("City", addr.toString())
    Log.d("City", addr?.get(0)?.locality.toString())
    return addr?.get(0)?.locality ?: "Delhi"
}


fun getLatLong(locationClient: FusedLocationProviderClient, context: Context, callback: (Pair<Double, Double>) -> Unit) {
    try {
        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(Pair(lat, long))
            } else {
                // Handle case where location is null
                callback(Pair(0.0, 0.0))
            }
        }
    } catch (e: SecurityException) {
        // Handle security exception
        callback(Pair(0.0, 0.0))
    }
}

@Composable
fun DrawerHeader() {
    Box (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = "News App", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun DrawerBody(drawerState: DrawerState, coroutineScope: CoroutineScope){
    Column(modifier = Modifier.padding(vertical = 8.dp)){
        IconButton(onClick = {
            coroutineScope.launch {
                drawerState.close()
            }
        }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }
}
@Composable
fun drawerContent (drawerState: DrawerState, coroutineScope: CoroutineScope){
    Column {
        DrawerHeader()
        DrawerBody(drawerState, coroutineScope)
    }
}

@Composable
fun NewsTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    onImeAction: () -> Unit = {}
) {
    val context = LocalContext.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .semantics { contentDescription = label }, // Set content description for accessibility
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
        })
    )
}
@Composable
fun NewsText(
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String? =  "Breaking News Text"
) {
    BasicTextField(
        value = text,
        onValueChange = { /* Handle value change if needed */ },
        modifier = modifier.semantics {
            this.contentDescription = contentDescription ?: text
        }
    )
}

