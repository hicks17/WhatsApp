package app.jsapps.whatsapp.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealtimeModule {

    @Provides
    @Singleton
    fun provideDb(): FirebaseDatabase {
        return Firebase.database
    }

    @UsersReference
    @Singleton
    @Provides
    fun provideRealtimeDbUsers(
        db: FirebaseDatabase
    ): DatabaseReference {
        return db
            .getReference("users")
    }

    /*@Provides
    @Singleton
    fun provideRankRepo(
        @RankingReference dbReference: DatabaseReference,
        rankingDao: RankingDao
    ): RankingRepository {
        return RankingRepositoryImpl(
            dbReference, rankingDao
        )
    }*/

    /*@RankingReference
    @Singleton
    @Provides
    fun provideRealtimeDb(
        db:FirebaseDatabase
    ):DatabaseReference{
        return db
            .getReference("ranking")
    }*/

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UsersReference

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RankingReference

}