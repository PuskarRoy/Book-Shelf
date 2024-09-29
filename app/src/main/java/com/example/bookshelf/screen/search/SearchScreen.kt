package com.example.bookshelf.screen.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookshelf.model.Item
import com.example.bookshelf.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navHostController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val search = remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    val List:List<Item> = if(viewModel.listOfBook.value.data!=null) viewModel.listOfBook.value.data!! else emptyList<Item>()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search Books",
                        style = MaterialTheme.typography.headlineMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(start = 10.dp),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .shadow(elevation = 20.dp)
                        )
                    }
                },
            )
        }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            OutlinedTextField(value = search.value,
                onValueChange = { search.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search

                ), keyboardActions = KeyboardActions(onSearch = {
                    keyboard?.hide()
                    viewModel.searchBooks(search = search.value)
                    search.value = ""

                }),
                label = { Text(text = "Search", style = MaterialTheme.typography.titleMedium) })

            Box(modifier = Modifier.fillMaxSize()) {

                if (viewModel.listOfBook.value.loading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(10.dp)
                    ) {

                        items(List) {
                            SearchList(it){
                                navHostController.navigate(Screen.DetailScreen.route+"/${it.id}")
                            }
                        }
                    }
                }
            }


        }

    }

}


@Composable
fun SearchList(item: Item,onClick:()->Unit) {

    val url =
        item.volumeInfo.imageLinks.thumbnail.ifEmpty { "https://books.google.com/books/content?id=VnxuQgAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp).clickable { onClick() },
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)

    ) {

        Row {
            Image(
                painter = rememberAsyncImagePainter(model = url),
                contentDescription = null,
                modifier = Modifier.size(150.dp).align(Alignment.CenterVertically),
                contentScale = ContentScale.FillBounds,


            )
            Spacer(modifier = Modifier.width(15.dp))

            Column {
                Text(
                    text = item.volumeInfo.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontStyle = FontStyle.Italic,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Author : ${item.volumeInfo.authors}",
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Date : ${item.volumeInfo.publishedDate}",
                    style = MaterialTheme.typography.labelLarge,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${item.volumeInfo.categories?:""}",
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic
                )
            }


        }

    }
}