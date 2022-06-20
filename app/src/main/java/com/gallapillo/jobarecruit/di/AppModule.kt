package com.gallapillo.jobarecruit.di

import com.gallapillo.jobarecruit.domain.use_case.firebase.FirebaseSignUp
import com.gallapillo.jobarecruit.data.firebase.AuthenticationRepositoryImpl
import com.gallapillo.jobarecruit.data.firebase.UserRepositoryImpl
import com.gallapillo.jobarecruit.domain.repository.AuthenticationRepository
import com.gallapillo.jobarecruit.domain.repository.UserRepository
import com.gallapillo.jobarecruit.domain.use_case.auth.AuthenticateUseCases
import com.gallapillo.jobarecruit.domain.use_case.firebase.FirebaseAuthState
import com.gallapillo.jobarecruit.domain.use_case.firebase.FirebaseSignIn
import com.gallapillo.jobarecruit.domain.use_case.firebase.FirebaseSignOut
import com.gallapillo.jobarecruit.domain.use_case.firebase.IsUserAuthenticated
import com.gallapillo.jobarecruit.domain.use_case.user.GetUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFireBaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFireBaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthenticationRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthenticationRepository {
        return AuthenticationRepositoryImpl(auth = auth, firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideUserRepository(firebaseFirestore: FirebaseFirestore) : UserRepository {
        return UserRepositoryImpl(firebaseFirestore = firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideUserUseCases(repository: UserRepositoryImpl) : GetUser {
        return GetUser(repository = repository)
    }

    @Singleton
    @Provides
    fun provideAuthenticateUseCases(repository: AuthenticationRepositoryImpl) : AuthenticateUseCases {
        return AuthenticateUseCases(
            isUserAuthenticated = IsUserAuthenticated(repository = repository),
            firebaseAuthState = FirebaseAuthState(repository = repository),
            firebaseSignOut = FirebaseSignOut(repository),
            firebaseSignIn = FirebaseSignIn(repository),
            firebaseSignUp = FirebaseSignUp(repository)
        )
    }
}