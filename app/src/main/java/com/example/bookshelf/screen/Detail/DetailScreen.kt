package com.example.bookshelf.screen.Detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookshelf.model.MBook
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

@Composable
fun DetailScreen(
    navHostController: NavHostController,
    id: String,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) { viewModel.getBookInfo(id) }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (viewModel.data.value.loading) {
                CircularProgressIndicator()
            } else {
                Image(
                    painter = rememberAsyncImagePainter(model = viewModel.data.value.data?.volumeInfo?.imageLinks?.thumbnail),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = viewModel.data.value.data?.volumeInfo?.title ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Author : " + viewModel.data.value.data?.volumeInfo?.authors ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = ("Categories : " + viewModel.data.value.data?.volumeInfo?.categories)
                        ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = (("Published : " + (viewModel.data.value.data?.volumeInfo?.publishedDate
                        ?: ""))
                        ?: "") ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(5.dp),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = HtmlCompat.fromHtml(
                        viewModel.data.value.data?.volumeInfo?.description.toString(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    ).toString().trim(),
                    readOnly = true,
                    textStyle = MaterialTheme.typography.titleMedium,
                    onValueChange = {},
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(.5f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,

                    ) {
                    Button(
                        onClick = {
                            val Book = MBook(title = viewModel.data.value.data?.volumeInfo?.title,
                                authors = viewModel.data.value.data?.volumeInfo?.authors.toString(),
                                description = HtmlCompat.fromHtml(
                                    viewModel.data.value.data?.volumeInfo?.description.toString(),
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                ).toString().trim(),
                                categories = viewModel.data.value.data?.volumeInfo?.categories.toString(),
                                notes = "",
                                photoUrl = viewModel.data.value.data?.volumeInfo?.imageLinks?.thumbnail,
                                publishedate = viewModel.data.value.data?.volumeInfo?.publishedDate,
                                pageCount = viewModel.data.value.data?.volumeInfo?.pageCount.toString(),
                                rating = 0.0,
                                googleBookld = id,
                                userId = Firebase.auth.currentUser?.uid.toString()
                            )

                            val db = FirebaseFirestore.getInstance()
                            db.collection("Books").add(Book).addOnSuccessListener {
                                db.collection("Books").document(it.id).update(hashMapOf("id" to it.id) as Map<String,Any>)
                            }.addOnCompleteListener {
                                Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show()
                                navHostController.popBackStack()
                            }

                        },
                        shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(text = "Save", style = MaterialTheme.typography.titleLarge)
                    }
                    Button(
                        onClick = { navHostController.popBackStack() },
                        shape = RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(text = "Cancel", style = MaterialTheme.typography.titleLarge)

                    }
                }

            }
        }
    }

}