package com.example.recipes.di

import android.content.Context
import androidx.room.Room
import com.example.recipes.data.database.RecipeDatabase
import com.example.recipes.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
            @ApplicationContext context: Context
    ) = Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: RecipeDatabase) = database.recipeDAO()
}