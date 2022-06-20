package com.gallapillo.jobarecruit.presentation.screens.auth_screen

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.common.Screen
import com.gallapillo.jobarecruit.presentation.theme.fontFamily

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel
) {
    Box(modifier= Modifier.fillMaxSize()) {

        val context = LocalContext.current

        val emailState = remember {
            mutableStateOf("")
        }
        val passwordState = remember {
            mutableStateOf("")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вход в учетную запись",
                modifier = Modifier.padding(10.dp),
                fontSize = 30.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.ExtraBold
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                },
                modifier = Modifier.padding(10.dp),
                label = {
                    Text(
                        text = "Email",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                },
                modifier = Modifier.padding(10.dp),
                label = {
                    Text(
                        text = "Пароль",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal
                    )
                },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    if (emailState.value.isBlank()) {
                        makeText(context, "Введите почту", Toast.LENGTH_LONG).show()
                    } else {
                        if (passwordState.value.isBlank()) {
                            makeText(context, "Введите пароль", Toast.LENGTH_LONG).show()
                        } else {
                            viewModel.signIn(
                                email = emailState.value,
                                password = passwordState.value
                            )
                        }
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Войти",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal
                )
                when (val response = viewModel.signInState.value) {
                    is Response.Loading -> {

                    }
                    is Response.Error -> {
                        makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                    is Response.Success -> {
                        if (response.data) {
                            // TODO(gallapillo): need fix this transition maybe set hello screen
                            navController.navigate(Screen.HelloScreen.route) {
                                popUpTo(Screen.LoginScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}