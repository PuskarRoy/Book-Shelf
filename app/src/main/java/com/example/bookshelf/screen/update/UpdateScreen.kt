package com.example.bookshelf.screen.update

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookshelf.screen.home.HomeViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    navHostController: NavHostController,
    id: String,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val book = viewModel.data.value.data?.filter {
        it.id == id
    }?.get(0)
    val alert = remember { mutableStateOf(false) }
    val thoughts = remember { mutableStateOf("") }

    val startread = remember { mutableStateOf(false) }
    val finishread = remember { mutableStateOf(false) }
    val rate = remember { mutableIntStateOf(0) }

    if (book?.notes != null) {
        thoughts.value = book.notes!!
    }

    if (book?.rating != null) {
        rate.value = book.rating!!.toInt()
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(
                text = "Update Book",
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic
            )
        }, navigationIcon = {
            IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
            }
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (book != null) {
                Card(
                    shape = RoundedCornerShape(100.dp),
                    elevation = CardDefaults.elevatedCardElevation(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(200.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = book.photoUrl),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp), verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = book.title.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                            Text(
                                text = book.authors.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                fontStyle = FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = book.publishedate.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                overflow = TextOverflow.Ellipsis,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }


                OutlinedTextField(
                    value = thoughts.value,
                    onValueChange = { thoughts.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .height(120.dp),
                    label = {
                        Text(
                            text = "Enter Your Thoughts",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.titleLarge,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(
                        onClick = { startread.value = true },
                        enabled = book.startedReading == null
                    ) {
                        if (book.startedReading == null) {
                            if (!startread.value) {
                                Text(
                                    text = "Start Reading",
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            } else {
                                Text(
                                    text = "Started Reading",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.alpha(0.5f),
                                    maxLines = 1
                                )
                            }

                        } else {
                            Text(
                                text = "Started on " + SimpleDateFormat("dd MMM , yy").format(book.startedReading!!.toDate()),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                        }
                    }

                    TextButton(
                        onClick = { finishread.value = true },
                        enabled = book.finishedReading == null
                    ) {
                        if (book.finishedReading == null) {
                            if (!finishread.value) {
                                Text(
                                    text = "Mark as Read",
                                    style = MaterialTheme.typography.titleMedium, maxLines = 1
                                )
                            } else {
                                Text(
                                    text = "Finished Reading",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.alpha(0.5f), maxLines = 1
                                )
                            }

                        } else {
                            Text(
                                text = "Finished on " + SimpleDateFormat("dd MMM , yy").format(book.finishedReading!!.toDate()),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1
                            )
                        }
                    }
                }

                Text(
                    text = "Rating",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.absolutePadding(5.dp)
                )
                Row(modifier = Modifier.padding(5.dp)) {
                    for (i in 1..5) {
                        IconButton(onClick = { rate.value = i }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (rate.value >= i) Color.Yellow else Color.Gray,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {

                        val start = if (startread.value) Timestamp.now() else book.startedReading
                        val finished =
                            if (finishread.value) Timestamp.now() else book.finishedReading

                        val bookupdate = hashMapOf(
                            "notes" to thoughts.value,
                            "rating" to rate.value,
                            "startedReading" to start,
                            "finishedReading" to finished
                        ).toMap()
                        FirebaseFirestore.getInstance().collection("Books").document(book.id!!)
                            .update(bookupdate).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(context, "Record Updated", Toast.LENGTH_LONG)
                                        .show().run {
                                            navHostController.popBackStack()
                                        }
                                }
                            }

                    }) {
                        Text(text = "Update", style = MaterialTheme.typography.titleLarge)
                    }
                    Button(onClick = { alert.value = !alert.value }) {
                        Text(text = "Delete", style = MaterialTheme.typography.titleLarge)
                    }
                }

                if (alert.value) {
                    AlertDialog(
                        onDismissRequest = { alert.value = false },
                        title = {Text(text = "Are you sure you want to delete this item")},
                        dismissButton = {Button(onClick = {alert.value = false}) {
                            Text(text = "No")
                        }},
                        confirmButton = { Button(onClick = {

                            book.id?.let { it1 ->
                                FirebaseFirestore.getInstance().collection("Books").document(
                                    it1
                                ).delete().addOnSuccessListener {
                                    Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show()
                                    navHostController.popBackStack()
                                }
                            }

                        }) {
                            Text(text = "Yes")
                        } }

                    )

                    
                }

            }
        }
    }

}