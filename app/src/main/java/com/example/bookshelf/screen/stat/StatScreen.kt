package com.example.bookshelf.screen.stat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookshelf.model.MBook
import com.example.bookshelf.screen.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {

    val current = FirebaseAuth.getInstance().currentUser
    var listbook = emptyList<MBook>()
    if (!viewModel.data.value.data.isNullOrEmpty() && !viewModel.data.value.loading) {
        listbook = viewModel.data.value.data!!.toList().filter {
            it.userId == current?.uid.toString()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "BookStats",
                    style = MaterialTheme.typography.headlineMedium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }, navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navHostController.popBackStack() },
                    tint = Color.Unspecified
                )
            })
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {


            Card(
                shape = RoundedCornerShape(30.dp),
                elevation = CardDefaults.elevatedCardElevation(30.dp),
                colors = CardDefaults.cardColors(Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp), verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Your Stats", style = MaterialTheme.typography.headlineLarge)
                    HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = "Currently Reading : ${listbook.filter { it.startedReading != null && it.finishedReading == null }.size}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "You Have Completed : ${listbook.filter { it.startedReading != null && it.finishedReading != null }.size}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            }

            LazyColumn {
                items(listbook.filter { it.startedReading != null && it.finishedReading != null }) {
                    BookCard(book = it)
                }
            }
        }
    }
}


@Composable
fun BookCard(book: MBook) {
    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.elevatedCardElevation(30.dp),
        colors = CardDefaults.cardColors(Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(model = book.photoUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(vertical = 5.dp, horizontal = 5.dp)
            )
            Column(
                modifier = Modifier
                    .padding(2.dp)
                    .wrapContentSize(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Title: " + book.title.toString(),
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Authors: " + book.authors.toString(),
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Started on : " + SimpleDateFormat("dd MMM , yy").format(book.startedReading!!.toDate()),
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Finished on: " + SimpleDateFormat("dd MMM , yy").format(book.finishedReading!!.toDate()),
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.titleMedium
                )

            }
            if (book.rating!! >= 4) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(5.dp),
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }

        }
    }
}