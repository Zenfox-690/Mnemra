package com.example.mnemra.di

import android.content.Context
import com.example.mnemra.data.database.DatabaseProvider
import com.example.mnemra.data.repository.FlashcardRepository
import com.example.mnemra.data.database.MnemraDatabase
import com.example.mnemra.data.repository.MemoryRepository
import com.example.mnemra.data.repository.ReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MnemraDatabase =
        DatabaseProvider.getDatabase(context)

    @Provides
    @Singleton
    fun provideMemoryRepository(
        database: MnemraDatabase
    ): MemoryRepository =
        MemoryRepository(database.memoryDao())

    @Provides
    @Singleton
    fun provideFlashcardRepository(
        database: MnemraDatabase
    ): FlashcardRepository =
        FlashcardRepository(database.flashcardDao())

    @Provides
    @Singleton
    fun provideReviewRepository(
        database: MnemraDatabase
    ): ReviewRepository =
        ReviewRepository(database.reviewDao())
}