package com.gallapillo.jobarecruit.domain.use_case.auth

import com.gallapillo.jobarecruit.domain.use_case.firebase.*

data class AuthenticateUseCases (
    val isUserAuthenticated: IsUserAuthenticated,
    val firebaseAuthState: FirebaseAuthState,
    val firebaseSignIn: FirebaseSignIn,
    val firebaseSignOut: FirebaseSignOut,
    val firebaseSignUp: FirebaseSignUp
) {
}