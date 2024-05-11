package com.cse535.news_app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.cse535.news_app.ui.theme.News_appTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            News_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Scaffold(topBar = {MainTopBar()} ,
        content = {Column (
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer (modifier = Modifier.padding(60.dp))
            NewsList(headlines = getDummyHeadlines(40))
        } })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    Column (modifier =  Modifier.padding(vertical = 2.dp)){
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.clickable { /* Handle menu icon click */ }
                )
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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )

    )
    Row (modifier = Modifier.fillMaxWidth()
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
fun NewsList(headlines: List<String>) {
    // Add LazyColumn here
    LazyColumn (modifier = Modifier.fillMaxSize()){
        items(headlines.size) { index ->
            SingleNews(headlines[index])
        }
    }

}

@Composable
fun SingleNews(headline: String){
    Button (onClick = { /*TODO*/ },
    modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        )
         {
        Text(text = headline)
    }
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
