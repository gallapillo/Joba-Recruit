package com.gallapillo.jobarecruit.data.firebase

import com.gallapillo.jobarecruit.common.Constants.USERS_COLLECTION
import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.domain.model.User
import com.gallapillo.jobarecruit.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor (
    private val firebaseFirestore: FirebaseFirestore
) : UserRepository {

    private var operationSuccessful = false

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserById(userId: String): Flow<Response<User>> = callbackFlow {
        Response.Loading
        val snapshotListener = firebaseFirestore.collection(USERS_COLLECTION)
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                val response = if (snapshot != null) {
                    val userInfo = snapshot.toObject(User::class.java)
                    Response.Success<User>(userInfo!!)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getAllUsers(userId: String): Flow<List<User>>  = callbackFlow {

    }

    override fun saveUserChanges(user: User): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            val userObj = mutableMapOf<String, Any>()
            userObj["name"] = user.name
            userObj["surName"] = user.surName
            userObj["wannaFindJob"] = user.wannaFindJob
            firebaseFirestore.collection(USERS_COLLECTION).document(user.userId).update(userObj as Map<String, Any>)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if (operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Edit error"))
            }
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "Unexcepted error ocurred")
        }
    }
}