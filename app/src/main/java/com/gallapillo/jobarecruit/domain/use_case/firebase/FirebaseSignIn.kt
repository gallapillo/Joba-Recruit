package com.gallapillo.jobarecruit.domain.use_case.firebase


import com.gallapillo.jobarecruit.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseSignIn @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(email: String, password: String) = repository.firebaseSignIn(email, password)
}