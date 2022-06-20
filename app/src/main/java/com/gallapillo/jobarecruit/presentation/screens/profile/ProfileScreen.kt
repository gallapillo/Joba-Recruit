package com.gallapillo.jobarecruit.presentation.screens.profile

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.common.Screen
import com.gallapillo.jobarecruit.presentation.screens.auth_screen.AuthenticationViewModel
import com.gallapillo.jobarecruit.presentation.theme.fontFamily
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    authenticationViewModel: AuthenticationViewModel
) {
    val context = LocalContext.current
    userViewModel.getUserInfo()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    when (val response = userViewModel.getUserData.value) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Success -> {
            if (response.data != null) {
                val user = response.data

                val radioOptions = listOf("В активном поиске работы >", "Открыт к предложениям >", "Не ищю работу >")
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[user.wannaFindJob]) }
                var wannaJob = remember {
                    mutableStateOf(radioOptions[user.wannaFindJob])
                }
                ModalBottomSheetLayout(
                    sheetContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 12.dp)
                            ) {
                                Text(
                                    text = "Ваш статус поиска работы",
                                    modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Normal
                                )
                                radioOptions.forEachIndexed { index, text ->
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        RadioButton(
                                            selected = (text == selectedOption),modifier = Modifier.padding(all = Dp(value = 8F)),
                                            onClick = {
                                                onOptionSelected(text)
                                                wannaJob.value = text
                                                user.wannaFindJob = index
                                                userViewModel.setUserInfo(user)
                                                when (val response = userViewModel.setUserData.value) {
                                                    is Response.Loading -> {
                                                        makeText(
                                                            context,
                                                            "Пожалуйста подождите обновление данных",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                    is Response.Error -> {
                                                        makeText(
                                                            context,
                                                            response.message,
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                    is Response.Success -> {
                                                        if (response.data) {
                                                            makeText(
                                                                context,
                                                                "Данные обновлены успешно!",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }
                                                }
                                            }
                                        )
                                        Text(
                                            text = text,
                                            modifier = Modifier.padding(all = Dp(value = 8F)),
                                            fontFamily = fontFamily,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    },
                    sheetState = sheetState
                ) {
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                    Row(horizontalArrangement = Arrangement.End) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Sign out button",
                            modifier = Modifier.clickable {
                                authenticationViewModel.signOut()
                                when(val response = authenticationViewModel.signOutState.value) {
                                    is Response.Loading -> {

                                    }
                                    is Response.Error -> {
                                        makeText(context, response.message, Toast.LENGTH_LONG).show()
                                    }
                                    is Response.Success -> {
                                        if (response.data) {
                                            navController.navigate(Screen.HelloScreen.route) {
                                                popUpTo(Screen.ProfileScreen.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }

                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp)
                    ) {

                        Spacer(modifier = Modifier.padding(top = 32.dp))
                        Text(text = user.name, fontSize = 48.sp)
                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        Text(
                            text = wannaJob.value,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.clickable {
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.padding(top = 48.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(onClick = {  }) {
                                Text(
                                    "Создать новое резюме",
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
        is Response.Error -> {
            makeText(context, "ERROR WITH RESPONSE", Toast.LENGTH_LONG).show()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column (modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "ERRPR WITH PROFILE")
                }
            }
        }
    }
}

