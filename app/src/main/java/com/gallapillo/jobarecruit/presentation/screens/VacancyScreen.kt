package com.gallapillo.jobarecruit.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.common.Screen
import com.gallapillo.jobarecruit.presentation.screens.auth_screen.AuthenticationViewModel
import com.gallapillo.jobarecruit.presentation.screens.profile.UserViewModel


@Composable
fun VacancyScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    authViewModel: AuthenticationViewModel
) {

    val context = LocalContext.current
    userViewModel.getUserInfo()

    when (val response = userViewModel.getUserData.value) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Success -> {
            if (response.data != null) {
                val user = response.data
                if (user.userRole != "recruit") {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "VACANCY")
                        }
                        // BottomNavigationMenu(selectedItem = BottomNavigationItem.HOME, navController = navController)
                    }
                } else {
                    authViewModel.signOut()
                    when(val response = authViewModel.signOutState.value) {
                        is Response.Loading -> {

                        }
                        is Response.Error -> {
                            Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                        }
                        is Response.Success -> {
                            if (response.data) {
                                Toast.makeText(context, "Запрещено входить соискателю, скачайте, другое приложение", Toast.LENGTH_LONG).show()
                                navController.navigate(Screen.HelloScreen.route) {
                                    popUpTo(Screen.ProfileScreen.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        is Response.Error -> {

        }
    }
}