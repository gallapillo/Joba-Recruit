package com.gallapillo.jobarecruit.domain.use_case.firebase

import com.gallapillo.jobarecruit.domain.model.User
import com.gallapillo.jobarecruit.domain.repository.AuthenticationRepository
import javax.inject.Inject

class FirebaseSignUp @Inject constructor(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(user: User) = repository.firebaseSignUp(user)
}