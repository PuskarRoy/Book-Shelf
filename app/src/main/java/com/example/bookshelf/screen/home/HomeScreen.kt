package com.example.bookshelf.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookshelf.R
import com.example.bookshelf.model.MBook
import com.example.bookshelf.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()
) {

    val name = Firebase.auth.currentUser?.email?.split('@')?.get(0) ?: "N/A"
    val current = FirebaseAuth.getInstance().currentUser
    var listbook = emptyList<MBook>()

    if (!viewModel.data.value.data.isNullOrEmpty() && !viewModel.data.value.loading) {
        listbook = viewModel.data.value.data!!.toList().filter {
            it.userId == current?.uid.toString()
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "BookShelf",
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic
            )
        }, navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.books),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color.Unspecified
            )
        }, actions = {
            IconButton(onClick = {
                Firebase.auth.signOut().run {
                    navHostController.navigate(Screen.LogInScreen.route) {
                        popUpTo(0)
                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .shadow(elevation = 50.dp)
                        .size(30.dp)
                )
            }
        })

    }, floatingActionButton = {
        IconButton(
            onClick = { navHostController.navigate(Screen.SearchScreen.route) },
            modifier = Modifier
                .size(50.dp)
                .shadow(elevation = 20.dp, shape = CircleShape),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.LightGray)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }

    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            HorizontalDivider(thickness = 2.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Reading \nActivity Right Now",
                    style = MaterialTheme.typography.titleLarge,
                    fontStyle = FontStyle.Italic
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { navHostController.navigate(Screen.StatScreen.route) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .shadow(elevation = 10.dp),
                            tint = Color.DarkGray,

                            )
                    }
                    Text(text = name)
                }


            }

            Spacer(modifier = Modifier.height(30.dp))

            if (listbook.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                ) {
                    items(listbook.filter { it.startedReading != null && it.finishedReading == null }) {
                        BookCard(it, "Reading"){
                            navHostController.navigate(Screen.UpdateScreen.route + "/$it")
                        }
                    }
                }
            } else {
                Text(
                    text = "No Books Found , Add Books", style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Reading List",
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(15.dp))

            if (listbook.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    items(listbook.filter {
                        it.startedReading == null && it.finishedReading == null
                    }) {
                        BookCard(it,"Not started") {
                            navHostController.navigate(Screen.UpdateScreen.route + "/$it")
                        }
                    }
                }
            } else {
                Text(
                    text = "No Books Found , Add Books", style = MaterialTheme.typography.titleLarge
                )
            }

        }

    }

}


@Composable
fun BookCard(book: MBook, text: String, onClick: (String) -> Unit) {
    Card(shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .height(250.dp)
            .width(200.dp)
            .padding(horizontal = 7.dp)
            .clickable { onClick(book.id ?: "") }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = book.photoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(140.dp),
                    contentScale = ContentScale.FillBounds
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Gray),
                    shape = RoundedCornerShape(30.dp),
                    elevation = CardDefaults.elevatedCardElevation(20.dp),
                    modifier = Modifier.align(Alignment.Top).padding(top = 10.dp),

                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
                        Text(text = book.rating.toString(),color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = book.title ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1
                )
                Text(
                    text = book.authors ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Card(
                    shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Cyan),
                    modifier = Modifier.wrapContentSize()
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(7.dp)
                        )
                    }
                }
            }
        }
    }
}