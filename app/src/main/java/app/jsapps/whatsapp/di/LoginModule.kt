package app.jsapps.whatsapp.di

import app.jsapps.whatsapp.model.repositories.network.LoginImpl
import app.jsapps.whatsapp.model.repositories.network.LoginInterface
import app.jsapps.whatsapp.model.repositories.network.RealtimeInterface
import app.jsapps.whatsapp.model.repositories.network.RealtimeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthLogin(
        auth: FirebaseAuth,
        @RealtimeModule.UsersReference userRef:DatabaseReference
    ): LoginInterface {
        return LoginImpl(
            auth,
            userRef
        )
    }


    @Provides
    @Singleton
    fun provideDbToRepo(
        @RealtimeModule.UsersReference userRef:DatabaseReference
    ): RealtimeInterface {
        return RealtimeRepository(
            userRef
        )
    }
}