package com.example.bookshelf.screen.authentication

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookshelf.navigation.Screen

@Composable
fun LogInScreen(
    navHostController: NavHostController,
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val load = remember{ mutableStateOf(false)}

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BookShelf",
                color = Color.Red,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 60.dp, bottom = 80.dp)
            )

            OutlinedTextField(value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                label = { Text(text = "Email", style = MaterialTheme.typography.titleMedium) })

            OutlinedTextField(value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = { Text(text = "Password", style = MaterialTheme.typography.titleMedium) })
            Button(
                onClick = {
                    load.value = true
                    viewModel.loginUser(email, password, onDone = {
                        navHostController.navigate(Screen.HomeScreen.route) {
                            popUpTo(0)
                        }
                    }, onFail = {

                        Toast.makeText(context, "Login Error", Toast.LENGTH_LONG).show()
                    })
                },
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .size(55.dp),
                elevation = ButtonDefaults.buttonElevation(10.dp),
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.LightGray, disabledContentColor = Color.DarkGray
                )
            ) {

                    Text(text = "LogIn", style = MaterialTheme.typography.titleLarge)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "New User? ", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Red,
                    modifier = Modifier.clickable { navHostController.navigate(Screen.SignUpScreen.route) }
                )
            }

            if(viewModel.loading.value){
                CircularProgressIndicator()
            }

        }

    }

}