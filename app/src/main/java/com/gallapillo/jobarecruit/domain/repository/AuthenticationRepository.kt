package com.gallapillo.jobarecruit.domain.repository

import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun isUserAuthenticatedInFirebase() : Boolean

    fun getFirebaseAuthState() : Flow<Boolean>

    fun firebaseSignIn(email: String, password: String): Flow<Response<Boolean>>

    fun firebaseSignOut(): Flow<Response<Boolean>>

    fun firebaseSignUp(user: User): Flow<Response<Boolean>>
}