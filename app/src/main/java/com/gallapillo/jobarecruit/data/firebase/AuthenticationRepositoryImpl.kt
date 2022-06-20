package com.gallapillo.jobarecruit.data.firebase

import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.domain.model.User
import com.gallapillo.jobarecruit.domain.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticationRepository {
    var operationSuccessful : Boolean = false

    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    // @ExperimentalCoroutinesApi
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override fun firebaseSignIn(email: String, password: String): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            emit(Response.Success(operationSuccessful))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Uncepted error"))
        }
    }

    override fun firebaseSignOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Uncepted error"))
        }
    }

    override fun firebaseSignUp(
        user: User
    ): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            emit(Response.Loading)
            auth.createUserWithEmailAndPassword(user.email, user.password).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            if (operationSuccessful) {
                val userId = auth.currentUser?.uid!!
                user.userId = userId
                firestore.collection("Users").document(userId).set(user).addOnSuccessListener {

                }.await()
                emit(Response.Success(operationSuccessful))
            } else {
                Response.Success(operationSuccessful)
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Uncepted error"))
        }
    }
}