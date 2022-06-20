package com.gallapillo.jobarecruit.presentation.screens.auth_screen


import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gallapillo.jobarecruit.common.Constants
import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.common.Screen
import com.gallapillo.jobarecruit.common.isAdult
import com.gallapillo.jobarecruit.domain.model.User
import com.gallapillo.jobarecruit.presentation.theme.fontFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalMaterialApi
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel
) {
    Box(modifier= Modifier.fillMaxSize()) {

        val context = LocalContext.current

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val days = calendar.get(Calendar.DAY_OF_MONTH)

        val nameState = remember {
            mutableStateOf("")
        }
        val surNameState = remember {
            mutableStateOf("")
        }
        val emailState = remember {
            mutableStateOf("")
        }
        val passwordState = remember {
            mutableStateOf("")
        }
        val confirmPasswordState = remember {
            mutableStateOf("")
        }
        val birthDayState = remember {
            mutableStateOf("")
        }
        val expanded = remember {
            mutableStateOf(false)
        }
        val selectedOptionText = remember {
            mutableStateOf(Constants.GENDERS_LIST[0])
        }

        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                if (mDayOfMonth < 10) {
                    birthDayState.value = "0$mDayOfMonth-${mMonth+1}-$mYear"
                } else {
                    birthDayState.value = "$mDayOfMonth-${mMonth+1}-$mYear"
                }
                if (mMonth < 10) {
                    birthDayState.value = "$mDayOfMonth-${mMonth+1}-$mYear"
                } else {
                    birthDayState.value = "$mDayOfMonth-0${mMonth+1}-$mYear"
                }
                if (mDayOfMonth < 10 && mMonth < 10) {
                    birthDayState.value = "0$mDayOfMonth-0${mMonth+1}-$mYear"
                }
            }, year, month, days
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация пользователя",
                modifier = Modifier.padding(10.dp),
                fontSize = 30.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.ExtraBold
            )
            OutlinedTextField(
                value = nameState.value,
                onValueChange = {
                    nameState.value = it
                },
                modifier = Modifier.padding(10.dp),
                label = {
                    Text(
                        text = "Имя",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            )
            OutlinedTextField(
                value = surNameState.value,
                onValueChange = {
                    surNameState.value = it
                },
                modifier = Modifier.padding(10.dp),
                label = {
                    Text(
                        text = "Фамилия",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            )
            // TODO:(gallapillo) maybe upgrade a birth day update registration form
            Text(
                text = "Дата рождения: ${birthDayState.value}",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal
            )
            Button(
                onClick = {
                    datePickerDialog.show()
                }
            ) {
                Text(
                    text = "Выбрать дату рождения",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal
                )
            }
            // GENDER CHOSE
            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = {
                    expanded.value = !expanded.value
                }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedOptionText.value,
                    onValueChange = { },
                    label = { Text(
                        "Ваш пол",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal
                    ) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded.value
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = {
                        expanded.value = false
                    }
                ) {
                    Constants.GENDERS_LIST.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText.value = selectionOption
                                expanded.value = false
                            }
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }
            //
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
            OutlinedTextField(
                value = confirmPasswordState.value,
                onValueChange = {
                    confirmPasswordState.value = it
                },
                modifier = Modifier.padding(10.dp),
                label = {
                    Text(
                        text = "Повторите пароль",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal
                    )
                },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    // Check for validate forms
                    if (nameState.value.isBlank()) {
                        makeText(context, "Введите имя", Toast.LENGTH_LONG).show()
                    } else {
                        if (surNameState.value.isBlank()) {
                            makeText(context, "Введите фамилию", Toast.LENGTH_LONG).show()
                        } else {
                            if (passwordState.value != confirmPasswordState.value) {
                                makeText(context, "Пароли не совподают", Toast.LENGTH_LONG).show()
                            } else {
                                if (!birthDayState.value.contains("[0-9]".toRegex())) {
                                    makeText(context, "Введите свою дату рождения", Toast.LENGTH_LONG).show()
                                } else {
                                    // for new test
                                    val dateFormatInput = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                    val date = LocalDate.parse(birthDayState.value, dateFormatInput)
                                    val currentDate = LocalDate.now()
                                    if (!isAdult(date, currentDate)) {
                                        makeText(context, "Вы не совершеннолетний!", Toast.LENGTH_LONG).show()
                                    } else {
                                        if (emailState.value.isBlank()) {
                                            makeText(context, "Введите почту", Toast.LENGTH_LONG).show()
                                        } else {
                                            if (passwordState.value.isBlank()) {
                                                makeText(context, "Введите пароль", Toast.LENGTH_LONG).show()
                                            } else {
                                                viewModel.signUp(
                                                    User(
                                                        email = emailState.value,
                                                        password = passwordState.value,
                                                        name = nameState.value,
                                                        surName = surNameState.value,
                                                        gender = selectedOptionText.value,
                                                        birthDay = birthDayState.value,
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Зарегистрироватся",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal
                )
                when (val response = viewModel.signUpState.value) {
                    is Response.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is Response.Error -> {
                        makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                    is Response.Success -> {
                        if (response.data) {
                            // TODO(gallapillo): need fix this transition maybe set hello screen
                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo(Screen.RegisterScreen.route) {
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
