package com.example.bookshelf.screen.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookshelf.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHostController: NavHostController) {

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 1000, delayMillis = 100))
        delay(800L)
        if(FirebaseAuth.getInstance().currentUser!=null){
            navHostController.navigate(Screen.HomeScreen.route){
                popUpTo(0)
            }
        }
        else{
            navHostController.navigate(Screen.LogInScreen.route){
                popUpTo(0)
            }
        }

    }

    Surface(modifier = Modifier.fillMaxSize(),) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier.size(330.dp).scale(scale.value),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                elevation = CardDefaults.cardElevation(30.dp), border = BorderStroke(2.dp,Color.Gray)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BookShelf",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Red, modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Keep Track of Your Books",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        }


    }

}